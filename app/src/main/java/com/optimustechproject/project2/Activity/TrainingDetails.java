package com.optimustechproject.project2.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.Streams;
import com.optimustechproject.project2.Fragments.dialog_enquiry;
import com.optimustechproject.project2.Interface.EnquiriesRequest;
import com.optimustechproject.project2.Models.EnquiriesPOJO;
import com.optimustechproject.project2.Models.TrainingsPOJO;
import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.ColoredSnackbar;
import com.optimustechproject.project2.app.DbHandler;
import com.optimustechproject.project2.app.NetworkCheck;
import com.optimustechproject.project2.app.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;

public class TrainingDetails extends AppCompatActivity {

    TextView key_learning,desc,availability,duration,date,timings,venue;
    TrainingsPOJO data;
    static Button enquire,register;
    Gson gson=new Gson();
    int index,training_id;
    private ColoredSnackbar coloredSnackbar;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_details);

        data=gson.fromJson(DbHandler.getString(TrainingDetails.this,"training_details","{}"), TrainingsPOJO.class);
        index=getIntent().getExtras().getInt("index");

        Toolbar toolbar;
        toolbar=(Toolbar)findViewById(R.id.anim_toolbar);
        toolbar.setTitle(data.getTitle().get(index));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        enquire=(Button)findViewById(R.id.enquire);
        register=(Button)findViewById(R.id.register);

        key_learning=(TextView)findViewById(R.id.key_learnings);
        desc=(TextView)findViewById(R.id.desc);
        availability=(TextView)findViewById(R.id.availability);
        date=(TextView)findViewById(R.id.date);
        timings=(TextView)findViewById(R.id.timings);
        duration=(TextView)findViewById(R.id.duration);
        venue=(TextView)findViewById(R.id.venue);

        key_learning.setText("1. "+getIntent().getExtras().getString("key_learning1")+"\n2. "+getIntent().getExtras().getString("key_learning2")+"\n3. "+getIntent().getExtras().getString("key_learning3"));
        desc.setText(getIntent().getExtras().getString("desc"));
        availability.setText(getIntent().getExtras().getString("availability"));
        date.setText(getIntent().getExtras().getString("date"));
        timings.setText(getIntent().getExtras().getString("timings"));
        duration.setText(getIntent().getExtras().getString("duration"));
        venue.setText(getIntent().getExtras().getString("venue"));

        if(getIntent().getExtras().getString("enquiry_status").equals("1")){
            enquire.setText("Already enquired");
            enquire.setClickable(false);
        }

        enquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog_enquiry dialog= dialog_enquiry.instance(getIntent().getExtras().getString("training_id"));
//                dialog.show(getSupportFragmentManager(),"Enquiry");
                if(NetworkCheck.isNetworkAvailable(TrainingDetails.this)) {

                    dialog = new ProgressDialog(TrainingDetails.this);
                    dialog.setMessage("Please wait !!");
                    dialog.setCancelable(false);
                    dialog.show();

                    EnquiriesRequest enquiriesRequest = ServiceGenerator.createService(EnquiriesRequest.class, DbHandler.getString(TrainingDetails.this, "bearer", ""));
                    Call<EnquiriesPOJO> call = enquiriesRequest.requestResponse(getIntent().getExtras().getString("training_id"), "");
                    call.enqueue(new Callback<EnquiriesPOJO>() {
                        @Override
                        public void onResponse(Call<EnquiriesPOJO> call, retrofit2.Response<EnquiriesPOJO> response) {
                            dialog.dismiss();
                            if (response.code() == 200) {
                                if (!response.body().getError()) {
                                    //send_enquiry(s);
                                    enquire.setText("Enquired");
                                    enquire.setClickable(false);
                                    Toast.makeText(TrainingDetails.this, "Enquiry send!!", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(TrainingDetails.this, NavigationActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(TrainingDetails.this,"Session Expired",Toast.LENGTH_LONG).show();
                                    DbHandler.unsetSession(TrainingDetails.this,"isForcedLoggedOut");

                                }
                            } else {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_LONG);
                                coloredSnackbar.warning(snackbar).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<EnquiriesPOJO> call, Throwable t) {
                            dialog.dismiss();
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_LONG);
                            coloredSnackbar.warning(snackbar).show();
                        }
                    });
                }
                else{
                    Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"No internet connection",Snackbar.LENGTH_LONG);
                    coloredSnackbar.alert(snackbar).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public static void change(){
        enquire.setText("Enquire Sent");
        enquire.setClickable(false);
    }
}
