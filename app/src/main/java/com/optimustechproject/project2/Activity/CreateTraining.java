package com.optimustechproject.project2.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.optimustechproject.project2.Interface.CategoriesRequest;
import com.optimustechproject.project2.Interface.CreateTrainingRequest;
import com.optimustechproject.project2.Interface.TrainingDetailsRequest;
import com.optimustechproject.project2.Models.CategoriesPOJO;
import com.optimustechproject.project2.Models.CreateTrainingPOJO;
import com.optimustechproject.project2.Models.CreatedTrainingsPOJO;
import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.ColoredSnackbar;
import com.optimustechproject.project2.app.DbHandler;
import com.optimustechproject.project2.app.NetworkCheck;
import com.optimustechproject.project2.app.ServiceGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTraining extends AppCompatActivity {

    ProgressDialog progressDialog;
    Spinner categories,availability;
    Calendar myCalendar = Calendar.getInstance();
    EditText name,location,price,date_et,kl1,kl2,kl3,desc,duration,fTime,tTime;
    LatLng l=null;
    List<String> avail=new ArrayList<String>();
    Button submit;
    List<String> cat_id=new ArrayList<String>();
    Gson gson=new Gson();
    private ColoredSnackbar coloredSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_training);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Training");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        categories=(Spinner)findViewById(R.id.category1);
        name=(EditText)findViewById(R.id.name1);
        location=(EditText)findViewById(R.id.location1);
        price=(EditText)findViewById(R.id.price1);
        date_et=(EditText)findViewById(R.id.date_1);
        kl3=(EditText)findViewById(R.id.kl3_1);
        kl1=(EditText)findViewById(R.id.kl1_1);
        kl2=(EditText)findViewById(R.id.kl2_1);
        desc=(EditText)findViewById(R.id.description1);
        duration=(EditText)findViewById(R.id.duration1);
        fTime=(EditText)findViewById(R.id.fTime_1);
        tTime=(EditText)findViewById(R.id.tTime_1);
        availability=(Spinner)findViewById(R.id.availability1);
        submit=(Button)findViewById(R.id.rgstr);

        avail.add("Weekdays");
        avail.add("Weekends");
        avail.add("Daily");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CreateTraining.this,
                android.R.layout.simple_spinner_item, avail);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        availability.setAdapter(dataAdapter);

        if(NetworkCheck.isNetworkAvailable(this)){

            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            CategoriesRequest categoriesRequest= ServiceGenerator.createService(CategoriesRequest.class);
            Call<CategoriesPOJO> call=categoriesRequest.requestResponse();
            call.enqueue(new Callback<CategoriesPOJO>() {
                @Override
                public void onResponse(Call<CategoriesPOJO> call, Response<CategoriesPOJO> response) {
                    progressDialog.dismiss();
                    if(response.code()==200){
                        cat_id=response.body().getId();
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CreateTraining.this,
                                android.R.layout.simple_spinner_item, response.body().getCatName());
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        categories.setAdapter(dataAdapter);
                        create();

                    }
                    else{
                        new AlertDialog.Builder(CreateTraining.this).setMessage("Failed to connect").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        }).create().show();
                    }

                }

                @Override
                public void onFailure(Call<CategoriesPOJO> call, Throwable t) {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(CreateTraining.this).setMessage("Failed to connect").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    }).create().show();
                }
            });
        }
        else{
            new AlertDialog.Builder(this).setMessage("No Internet connection").setTitle("Network error").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            }).create().show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void create(){
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

                date_et.setText(sdf.format(myCalendar.getTime()));

            }

        };


        date_et.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateTraining.this, date, myCalendar
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
                                        .build(CreateTraining.this);
                        startActivityForResult(intent, 1);
                    } catch (GooglePlayServicesRepairableException e) {
                        // TODO: Handle the error.
                    } catch (GooglePlayServicesNotAvailableException e) {
                        // TODO: Handle the error.
                    }
                }
            }
        });

        fTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog tpd = new TimePickerDialog(CreateTraining.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                fTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                tpd.show();
            }
        });

        tTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog tpd = new TimePickerDialog(CreateTraining.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                tTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                tpd.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().equals("")){
                    name.setError("Name required");
                }

                if(price.getText().toString().equals("")){
                    price.setError("Price required");
                }

                if(date_et.getText().toString().equals("")){
                    date_et.setError("Date required");
                }

                if(kl1.getText().toString().equals("")){
                    kl1.setError("Key learning1 required");
                }

                if(kl2.getText().toString().equals("")){
                    kl2.setError("Key learning2 required");
                }

                if(kl3.getText().toString().equals("")){
                    kl3.setError("Key learning3 required");
                }

                if(duration.getText().toString().equals("")){
                    duration.setError("Duration required");
                }

                if(desc.getText().toString().equals("")){
                    desc.setError("Description required");
                }

                if(fTime.getText().toString().equals("")){
                    fTime.setError("From timing required");
                }

                if(tTime.getText().toString().equals("")){
                    tTime.setError("To timing required");
                }

                if(l==null || location.getText().toString().equals("")){
                    location.setError("Location required");
                }


                if (!name.getText().toString().equals("") && (!location.getText().toString().equals("") && l!=null) && !date_et.getText().toString().equals("") && !kl1.getText().toString().equals("") && !kl2.getText().toString().equals("")  && !location.getText().toString().equals("")  && !kl3.equals("") && !duration.equals("") && !desc.equals("") && !fTime.equals("") && !tTime.equals("")) {
                      register();
                }


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(CreateTraining.this, data);
                l=place.getLatLng();

                location.setText(place.getName());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(CreateTraining.this, data);
                // TODO: Handle the error

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    public void register(){

        if(NetworkCheck.isNetworkAvailable(CreateTraining.this)) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            CreateTrainingRequest createTrainingRequest = ServiceGenerator.createService(CreateTrainingRequest.class, DbHandler.getString(CreateTraining.this, "bearer", ""));
            Call<CreateTrainingPOJO> call = createTrainingRequest.requestResponse(name.getText().toString(), String.valueOf(l.latitude), String.valueOf(l.longitude), location.getText().toString(), date_et.getText().toString(), kl1.getText().toString(), kl2.getText().toString(), kl3.getText().toString(), price.getText().toString(), String.valueOf(cat_id.get(categories.getSelectedItemPosition())), String.valueOf(availability.getSelectedItem()), duration.getText().toString(), desc.getText().toString(),fTime.getText().toString(),tTime.getText().toString());
            Log.e("data", name.getText().toString() + " " + String.valueOf(l.latitude) + " " + String.valueOf(l.longitude) + " " + location.getText().toString() + " " + date_et.getText().toString() + " " + kl1.getText().toString() + " " + kl2.getText().toString() + " " + kl3.getText().toString() + " " + price.getText().toString() + " " + String.valueOf(cat_id.get(categories.getSelectedItemPosition())) + " " + String.valueOf(availability.getSelectedItem()) + " " + duration.getText().toString() + " " + desc.getText().toString());
            call.enqueue(new Callback<CreateTrainingPOJO>() {
                @Override
                public void onResponse(Call<CreateTrainingPOJO> call, Response<CreateTrainingPOJO> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        if (!response.body().getError()) {

                            progressDialog = new ProgressDialog(CreateTraining.this);
                            progressDialog.setMessage("Loading...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            TrainingDetailsRequest trainingDetailsRequest = ServiceGenerator.createService(TrainingDetailsRequest.class, DbHandler.getString(CreateTraining.this, "bearer", ""));
                            Call<CreatedTrainingsPOJO> call2 = trainingDetailsRequest.requestResponse();
                            call2.enqueue(new Callback<CreatedTrainingsPOJO>() {
                                @Override
                                public void onResponse(Call<CreatedTrainingsPOJO> call, Response<CreatedTrainingsPOJO> response) {
                                    progressDialog.dismiss();
                                    if (response.code() == 200) {
                                        if (!response.body().getError()) {
                                            DbHandler.putString(CreateTraining.this,"training_details",gson.toJson(response.body().getTrainings()));
                                            Toast.makeText(CreateTraining.this, "Training created successfully", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(CreateTraining.this, NavigationActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(CreateTraining.this, "Session Expired", Toast.LENGTH_LONG).show();
                                            DbHandler.unsetSession(CreateTraining.this, "isForcedLoggedOut");
                                        }
                                    }
                                    else {
                                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error connecting to server2", Snackbar.LENGTH_SHORT);
                                        coloredSnackbar.warning(snackbar).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<CreatedTrainingsPOJO> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_SHORT);
                                    coloredSnackbar.warning(snackbar).show();
                                }
                            });

                        } else {
                            Toast.makeText(CreateTraining.this, "Session Expired", Toast.LENGTH_LONG).show();
                            DbHandler.unsetSession(CreateTraining.this, "isForcedLoggedOut");
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_SHORT);
                        coloredSnackbar.warning(snackbar).show();
                    }
                }

                @Override
                public void onFailure(Call<CreateTrainingPOJO> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("Error", String.valueOf(t));
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error connecting to server2", Snackbar.LENGTH_SHORT);
                    coloredSnackbar.warning(snackbar).show();

                }
            });
        }
        else{
            Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"No internet connection",Snackbar.LENGTH_LONG);
            coloredSnackbar.alert(snackbar).show();
        }
    }
}
