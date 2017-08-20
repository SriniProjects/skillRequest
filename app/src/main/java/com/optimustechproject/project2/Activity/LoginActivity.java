package com.optimustechproject.project2.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.optimustechproject.project2.Interface.CheckLoginRequest;
import com.optimustechproject.project2.Interface.LoginRequest;
import com.optimustechproject.project2.Models.CheckLoginPOJO;
import com.optimustechproject.project2.Models.LoginDataPOJO;
import com.optimustechproject.project2.Models.LoginDataumPOJO;
import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.ColoredSnackbar;
import com.optimustechproject.project2.app.DbHandler;
import com.optimustechproject.project2.app.NetworkCheck;
import com.optimustechproject.project2.app.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button register,login;
    Toolbar toolbar;
    EditText username;
    EditText password;
    Gson gson=new Gson();
    public Button google_login;
    ProgressDialog progressDialog;
    private boolean doubleBackToExitPressedOnce = false;
    private ColoredSnackbar coloredSnackBar;

    private GoogleSignInOptions gso;

    private GoogleApiClient mGoogleApiClient;
    public ProgressDialog dialog;
   // public RequestQueue queue;

    private int RC_SIGN_IN = 100;

    private View.OnClickListener snackBarListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            login();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Login");
        setSupportActionBar(toolbar);

        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);

        login=(Button)findViewById(R.id.login);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //queue = Volley.newRequestQueue(getApplicationContext());
        google_login = (Button) findViewById(R.id.google);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        //Setting onclick listener to signing button
        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage("Please wait !!");
                dialog.show();
                signIn();
            }
        });


        register=(Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("regType","normal");
                intent.putExtra("status","registration");
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("")){
                    username.setError("Username required");
                }
                if(password.getText().toString().equals("")){
                    password.setError("Password required");
                }

                if(!username.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    login();
                }


            }
        });
    }

    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        dialog.dismiss();
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String fname=acct.getDisplayName();
            String email=acct.getEmail();

            checkLogin(fname,email);

            //gf_login(fname,email);
        } else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    public void checkLogin(final String name, final String email){

        if(NetworkCheck.isNetworkAvailable(LoginActivity.this)) {

            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Verifying...");
            progressDialog.show();

            CheckLoginRequest checkLoginRequest = ServiceGenerator.createService(CheckLoginRequest.class);
            Call<CheckLoginPOJO> call = checkLoginRequest.requestResponse(email,FirebaseInstanceId.getInstance().getToken());
            call.enqueue(new Callback<CheckLoginPOJO>() {


                @Override
                public void onResponse(Call<CheckLoginPOJO> call, Response<CheckLoginPOJO> response) {
                    if(response.code()==200) {
                        progressDialog.dismiss();
                        if(response.body().getError()){
                            DbHandler.setSession(LoginActivity.this,gson.toJson(response.body().getData()),response.body().getData().getToken());
                            DbHandler.putString(LoginActivity.this,"training_details",gson.toJson(response.body().getData().getTrainings()));
                            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("name",name);
                            intent.putExtra("email",email);
                            intent.putExtra("status","registration");
                            intent.putExtra("regType","google");
                            startActivity(intent);

                        }
                    }
                    else{
                        progressDialog.dismiss();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_SHORT);
                        coloredSnackBar.warning(snackbar).show();



                    }
                }

                @Override
                public void onFailure(Call<CheckLoginPOJO> call, Throwable t) {
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_SHORT);
                    coloredSnackBar.warning(snackbar).show();


                }
            });
        }
        else{
            Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"No internet connection",Snackbar.LENGTH_LONG).setAction("Retry", snackBarListener);
            coloredSnackBar.alert(snackbar).show();


        }


    }

    public void login(){

        if(NetworkCheck.isNetworkAvailable(LoginActivity.this)){

            progressDialog=new ProgressDialog(LoginActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Authenticating");
            progressDialog.show();

            LoginRequest loginRequest= ServiceGenerator.createService(LoginRequest.class,username.getText().toString(),password.getText().toString());
            Call<LoginDataPOJO> call=loginRequest.requestResponse(FirebaseInstanceId.getInstance().getToken());
            call.enqueue(new Callback<LoginDataPOJO>() {
                @Override
                public void onResponse(Call<LoginDataPOJO> call, Response<LoginDataPOJO> response) {
                    if(response.code()==200){
                        progressDialog.dismiss();
                        Log.e("login",String.valueOf(response.body().getError()));
                        LoginDataumPOJO data=response.body().getData();
                        if(!response.body().getError()){
                            Toast.makeText(LoginActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                            DbHandler.setSession(LoginActivity.this,gson.toJson(data),data.getToken());
                            DbHandler.putString(LoginActivity.this,"training_details",gson.toJson(data.getTrainings()));
                            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setMessage(response.body().getMessage())
                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // onBackPressed();
                                        }
                                    }).show();

                        }
                    }
                    else{
                        progressDialog.dismiss();
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_SHORT);
                        coloredSnackBar.warning(snackbar).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginDataPOJO> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("loginerror",String.valueOf(t));
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_SHORT);
                    coloredSnackBar.warning(snackbar).show();
                }
            });


        }

        else{
            Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"No internet connection",Snackbar.LENGTH_LONG).setAction("Retry", snackBarListener);
            coloredSnackBar.alert(snackbar).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Press back again to exit", Snackbar.LENGTH_SHORT);
        coloredSnackBar.info(snackbar).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}