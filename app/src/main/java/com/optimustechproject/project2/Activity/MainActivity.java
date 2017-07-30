package com.optimustechproject.project2.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.optimustechproject.project2.Interface.RegistrationRequest;
import com.optimustechproject.project2.Models.RegDataPOJO;
import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.ColoredSnackbar;
import com.optimustechproject.project2.app.DbHandler;
import com.optimustechproject.project2.app.NetworkCheck;
import com.optimustechproject.project2.app.ServiceGenerator;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText first_name,last_name,dob,mobile,email,password,location;
    AppCompatButton submit;
    LatLng l=null;
    TextView pwd_txt;
    ImageView img,male,female;
    String gender="J";
    ProgressDialog progressDialog;
    private ColoredSnackbar coloredSnackbar;
    Calendar myCalendar = Calendar.getInstance();
    Gson gson=new Gson();

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        first_name=(EditText)findViewById(R.id.first_name);
        last_name=(EditText)findViewById(R.id.last_name);
        dob=(EditText)findViewById(R.id.dob);
        mobile=(EditText)findViewById(R.id.mobile);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        location=(EditText)findViewById(R.id.location);
         pwd_txt=(TextView)findViewById(R.id.pwd_txt);

        img=(ImageView)findViewById(R.id.img);
        male=(ImageView)findViewById(R.id.male);
        female=(ImageView)findViewById(R.id.female);

        submit=(AppCompatButton)findViewById(R.id.register);

         if(!getIntent().getExtras().getString("regType").equals("normal")){
             String[] arr=getIntent().getExtras().getString("name").split(" ");
             if(arr.length>1) {
                 first_name.setText(arr[0]);
                 last_name.setText(arr[1]);
             }
             else{
                 first_name.setText(arr[0]);
                 last_name.setText(" ");

             }

             email.setText(getIntent().getExtras().getString("email"));
             password.setVisibility(View.GONE);
             pwd_txt.setVisibility(View.GONE);
         }

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setColorFilter(getResources().getColor(R.color.colorAccent));
                gender="M";
                female.setColorFilter(null);
                img.setImageDrawable(getResources().getDrawable(R.drawable.male_account));
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female.setColorFilter(getResources().getColor(R.color.colorAccent));
                gender="F";
                male.setColorFilter(null);
                img.setImageDrawable(getResources().getDrawable(R.drawable.female_account));
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy";

                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                dob.setText(sdf.format(myCalendar.getTime()));

            }

        };


        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus) {
                    try {
                        Intent intent =
                                new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                        .build(MainActivity.this);
                        startActivityForResult(intent, 1);
                    } catch (GooglePlayServicesRepairableException e) {
                        // TODO: Handle the error.
                    } catch (GooglePlayServicesNotAvailableException e) {
                        // TODO: Handle the error.
                    }
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(first_name.getText().toString().equals("")){
                    first_name.setError("First name required");
                }

                if(last_name.getText().toString().equals("")){
                    last_name.setError("Last name required");
                }

                if(dob.getText().toString().equals("")){
                    dob.setError("DOB required");
                }

                if(mobile.getText().toString().equals("")){
                    mobile.setError("Mobile number required");
                }

                if(email.getText().toString().equals("")){
                    email.setError("Email required");
                }

                if(getIntent().getExtras().getString("regType").equals("normal") && password.getText().toString().equals("")){
                    password.setError("Password required");
                }

                if(l==null || location.getText().toString().equals("")){
                    location.setError("Location required");
                }

                if(gender.equals("J")){
                    Toast.makeText(MainActivity.this,"Select your gender",Toast.LENGTH_LONG).show();
                }

                if(getIntent().getExtras().getString("regType").equals("normal")) {
                    if (!first_name.getText().toString().equals("") && !last_name.getText().toString().equals("") && !dob.getText().toString().equals("") && !mobile.getText().toString().equals("") && !email.getText().toString().equals("") && !password.getText().toString().equals("") && !location.getText().toString().equals("") && l != null && !gender.equals("J")) {
                        register(password.getText().toString());
                    }
                }
                else{
                    if (!first_name.getText().toString().equals("") && !last_name.getText().toString().equals("") && !dob.getText().toString().equals("") && !mobile.getText().toString().equals("") && !email.getText().toString().equals("")  && !location.getText().toString().equals("") && l != null && !gender.equals("J")) {
                        register("");
                    }

                }

            }
        });

    }


    public void register(String pwd){
        if(NetworkCheck.isNetworkAvailable(MainActivity.this)){
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final RegistrationRequest registrationRequest= ServiceGenerator.createService(RegistrationRequest.class);
            Call<RegDataPOJO> call=registrationRequest.requestResponse(first_name.getText().toString(),last_name.getText().toString(),dob.getText().toString(),gender,mobile.getText().toString(),String.valueOf(l.latitude),String.valueOf(l.longitude),email.getText().toString(),pwd, FirebaseInstanceId.getInstance().getToken());
            Log.e("det",first_name.getText().toString()+last_name.getText().toString()+dob.getText().toString()+gender+mobile.getText().toString()+String.valueOf(l.latitude)+String.valueOf(l.longitude)+email.getText().toString()+password.getText().toString()+ FirebaseInstanceId.getInstance().getToken());
            call.enqueue(new Callback<RegDataPOJO>() {
                @Override
                public void onResponse(Call<RegDataPOJO> call, Response<RegDataPOJO> response) {
                    progressDialog.dismiss();
                    if(response.code()==200){
                        if(!response.body().getError()){

                            Log.e("Error",String.valueOf(response.body()));
                            Toast.makeText(MainActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                            DbHandler.setSession(MainActivity.this,gson.toJson(response.body()),response.body().getData().getToken());
                            DbHandler.putString(MainActivity.this,"training_details",gson.toJson(response.body().getData().getTrainings()));
                            Intent intent = new Intent(MainActivity.this,NavigationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage(response.body().getMessage())
                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // onBackPressed();
                                        }
                                    });

                        }
                    }
                    else{

                        Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"Error connecting to server",Snackbar.LENGTH_LONG);
                        coloredSnackbar.warning(snackbar).show();
                    }
                }

                @Override
                public void onFailure(Call<RegDataPOJO> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("regerror",String.valueOf(t));
                    Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"Error connecting to server",Snackbar.LENGTH_LONG);
                    coloredSnackbar.warning(snackbar).show();
                }
            });
        }
        else{
            Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"No internet connection",Snackbar.LENGTH_LONG);
            coloredSnackbar.alert(snackbar).show();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(MainActivity.this, data);
                //Toast.makeText(MainActivity.this,String.valueOf(place.getName())+"\n"+String.valueOf(place.getLatLng().latitude)+" , "+String.valueOf(place.getLatLng().longitude), Toast.LENGTH_LONG).show();
                l=place.getLatLng();

                location.setText(place.getName());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(MainActivity.this, data);
                // TODO: Handle the error

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }


}
