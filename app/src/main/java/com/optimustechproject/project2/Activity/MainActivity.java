package com.optimustechproject.project2.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.optimustechproject.project2.Interface.UpdateProfileRequest;
import com.optimustechproject.project2.Models.LoginDataumPOJO;
import com.optimustechproject.project2.Models.RegDataPOJO;
import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.ColoredSnackbar;
import com.optimustechproject.project2.app.DbHandler;
import com.optimustechproject.project2.app.NetworkCheck;
import com.optimustechproject.project2.app.ServiceGenerator;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText first_name,last_name,dob,mobile,email,password,location;
    AppCompatButton submit;
    LatLng l=null;
    TextView pwd_txt;
    ImageView male,female;
    String gender="J";
    ProgressDialog progressDialog;
    private ColoredSnackbar coloredSnackbar;
    Calendar myCalendar = Calendar.getInstance();
    Gson gson=new Gson();
    ImageView img;
    String responseString=null;
    private int STORAGE_PERMISSION_CODE = 23;
    String filepath="",filename="";
    int flg_pr=0;

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


         if(getIntent().getExtras().getString("status").equals("update")){
             LoginDataumPOJO data=gson.fromJson(DbHandler.getString(MainActivity.this,"login_data","{}"),LoginDataumPOJO.class);
             String n[]=data.getName().split(" ");
             first_name.setText(n[0]);
             last_name.setText(n[1]);
             email.setText(data.getEmail());
             mobile.setText(data.getMobile());
             dob.setText(data.getDob());
             location.setText(data.getLocation());
             password.setText(data.getPwd());
             l=new LatLng(Double.valueOf(data.getLatitude()),Double.valueOf(data.getLongitude()));

             String ar[]=data.getPhoto().split("/");
             filename=ar[ar.length-1];

             if(data.getGender().equals("M")){
                 Picasso
                         .with(MainActivity.this)
                         .load(data.getPhoto())
                         .placeholder(R.drawable.male_account)
                         .into(img);
                 male.setColorFilter(getResources().getColor(R.color.colorAccent));
                 gender = "M";
                 female.setColorFilter(null);
             }
             else{
                 Picasso
                         .with(MainActivity.this)
                         .load(data.getPhoto())
                         .placeholder(R.drawable.female_account)
                         .into(img);
                 female.setColorFilter(getResources().getColor(R.color.colorAccent));
                 gender = "F";
                 male.setColorFilter(null);
             }
         }

         img.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(NetworkCheck.isNetworkAvailable(MainActivity.this)) {

//                    progressDialog = new ProgressDialog(CreateTraining.this);
//                    progressDialog.setMessage("Loading...");
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();

                     if(isReadStorageAllowed()) {
                         Intent intent = new Intent();
                         intent.setType("*/*");
                         intent.setAction(Intent.ACTION_GET_CONTENT);
                         startActivityForResult(Intent.createChooser(intent, "Select File"), 0);
                     }
                     else{
                         requestStoragePermission();
                     }

                 }
                 else{
                     new AlertDialog.Builder(MainActivity.this).setMessage("No Internet connection").setTitle("Network error").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             onBackPressed();
                         }
                     }).create().show();
                 }
             }
         });

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
                    gender = "M";
                    female.setColorFilter(null);
                if(flg_pr==0) {
                    img.setImageDrawable(getResources().getDrawable(R.drawable.male_account));
                }
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    female.setColorFilter(getResources().getColor(R.color.colorAccent));
                    gender = "F";
                    male.setColorFilter(null);
                if(flg_pr==0) {
                    img.setImageDrawable(getResources().getDrawable(R.drawable.female_account));
                }
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
                        if(getIntent().getExtras().getString("status").equals("update")){
                            updateProfile(password.getText().toString());
                        }
                        else {
                            register(password.getText().toString());
                        }
                    }
                }
                else{
                    if (!first_name.getText().toString().equals("") && !last_name.getText().toString().equals("") && !dob.getText().toString().equals("") && !mobile.getText().toString().equals("") && !email.getText().toString().equals("")  && !location.getText().toString().equals("") && l != null && !gender.equals("J")) {
                        if(getIntent().getExtras().getString("status").equals("update")){
                            updateProfile("");
                        }
                        else {
                            register("");
                        }
                    }

                }

            }
        });

    }


    private boolean isReadStorageAllowed() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        return false;
    }

    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
                Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }


    @SuppressLint("NewApi")
    public String getFilPath(Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn( contentUri, null, null);
            }
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                }


                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn( contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }




    public String getDataColumn(Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



    private void uploadFile(final String filePath, final String fileName) {
        class UF extends AsyncTask<String, String, String> {
            InputStream inputStream;
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Uploading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {

//                String mob=params[2];
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                // nameValuePairs.add(new BasicNameValuePair("mobile", mob));


                String fp = params[0];
                String fn = params[1];
                try {
                    HttpPost httpPost;

                    HttpClient httpClient = new DefaultHttpClient();
                    httpPost = new HttpPost("http://optimustechproject2017002.000webhostapp.com/skills_req/UploadFile_profile.php");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    File file = new File(fp);

                    FileBody fileBody = new FileBody(file);
                    MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                    multipartEntity.addPart("file", fileBody);
                    httpPost.setEntity(multipartEntity);

                    HttpResponse httpResponse = httpClient.execute(httpPost);


                    HttpEntity entity = httpResponse.getEntity();
                    int statusCode = httpResponse.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        responseString = EntityUtils.toString(entity);
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }

                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                } catch (IOException e) {
                    responseString = e.toString();
                }

                return responseString;

            }

            @Override
            protected void onPostExecute(String result) {

                progressDialog.dismiss();
                Log.e("TAG", "Response from server: " + result);

                super.onPostExecute(result);
                String s = result.trim();
                Log.e("TAG", "Response from server: " + s);

            }
        }



        UF l = new UF();
        l.execute(filePath,fileName);


    }

    public void updateProfile(String pwd){
        if(NetworkCheck.isNetworkAvailable(MainActivity.this)){
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final UpdateProfileRequest updateProfileRequest= ServiceGenerator.createService(UpdateProfileRequest.class,DbHandler.getString(MainActivity.this,"bearer",""));
            Call<RegDataPOJO> call=updateProfileRequest.requestResponse(first_name.getText().toString(),last_name.getText().toString(),dob.getText().toString(),gender,mobile.getText().toString(),String.valueOf(l.latitude),String.valueOf(l.longitude),email.getText().toString(),pwd,location.getText().toString(), FirebaseInstanceId.getInstance().getToken(),filename);
            Log.e("det",first_name.getText().toString()+last_name.getText().toString()+dob.getText().toString()+gender+mobile.getText().toString()+String.valueOf(l.latitude)+String.valueOf(l.longitude)+email.getText().toString()+password.getText().toString()+location.getText().toString()+ FirebaseInstanceId.getInstance().getToken()+" "+filename);
            call.enqueue(new Callback<RegDataPOJO>() {
                @Override
                public void onResponse(Call<RegDataPOJO> call, Response<RegDataPOJO> response) {
                    progressDialog.dismiss();
                    if(response.code()==200){
                        if(!response.body().getError()){

                            Log.e("Error",String.valueOf(response.body()));
                            Toast.makeText(MainActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                            DbHandler.setSession(MainActivity.this,gson.toJson(response.body().getData()),response.body().getData().getToken());
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




    public void register(String pwd){
        if(NetworkCheck.isNetworkAvailable(MainActivity.this)){
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final RegistrationRequest registrationRequest= ServiceGenerator.createService(RegistrationRequest.class);
            Call<RegDataPOJO> call=registrationRequest.requestResponse(first_name.getText().toString(),last_name.getText().toString(),dob.getText().toString(),gender,mobile.getText().toString(),String.valueOf(l.latitude),String.valueOf(l.longitude),email.getText().toString(),pwd,location.getText().toString(), FirebaseInstanceId.getInstance().getToken(),filename);
            Log.e("det",first_name.getText().toString()+last_name.getText().toString()+dob.getText().toString()+gender+mobile.getText().toString()+String.valueOf(l.latitude)+String.valueOf(l.longitude)+email.getText().toString()+password.getText().toString()+location.getText().toString()+ FirebaseInstanceId.getInstance().getToken());
            call.enqueue(new Callback<RegDataPOJO>() {
                @Override
                public void onResponse(Call<RegDataPOJO> call, Response<RegDataPOJO> response) {
                    progressDialog.dismiss();
                    if(response.code()==200){
                        if(!response.body().getError()){

                            Log.e("Error",String.valueOf(response.body()));
                            Toast.makeText(MainActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                            DbHandler.setSession(MainActivity.this,gson.toJson(response.body().getData()),response.body().getData().getToken());
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
        if(requestCode==0) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                File myFile = new File(uri.toString());
                filepath = getFilPath(uri);
                File sdCardRoot = Environment.getExternalStorageDirectory();
                File yourDir = new File(sdCardRoot, filepath);
                filename = yourDir.getName();

                Bitmap bmp = BitmapFactory.decodeFile(filepath);
                img.setImageBitmap(bmp);

                uploadFile(filepath, filename);
                flg_pr=1;

            }
        }
    }


}
