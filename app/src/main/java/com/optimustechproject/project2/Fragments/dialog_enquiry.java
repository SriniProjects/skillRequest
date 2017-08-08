package com.optimustechproject.project2.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.optimustechproject.project2.Activity.MainActivity;
import com.optimustechproject.project2.Activity.NavigationActivity;
import com.optimustechproject.project2.Activity.TrainingDetails;
import com.optimustechproject.project2.Activity.URLs;
import com.optimustechproject.project2.Interface.EnquiriesRequest;
import com.optimustechproject.project2.Models.EnquiriesPOJO;
import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.ColoredSnackbar;
import com.optimustechproject.project2.app.DbHandler;
import com.optimustechproject.project2.app.NetworkCheck;
import com.optimustechproject.project2.app.ServiceGenerator;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by satyam on 3/8/17.
 */

public class dialog_enquiry extends DialogFragment {

    View view;
    ProgressDialog dialog;
    int training_id;
    public RequestQueue queue;
    public EditText message;
    public Button send;
    private ColoredSnackbar coloredSnackbar;

    public static dialog_enquiry instance(String training_id){

        dialog_enquiry dialog_enquiry=new dialog_enquiry();
        Bundle b=new Bundle();
        b.putString("training_id",training_id);
        dialog_enquiry.setArguments(b);
        return dialog_enquiry;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_enquiry, null);
        builder.setView(view);

        queue = Volley.newRequestQueue(getContext());
        message = (EditText) view.findViewById(R.id.enquiry);
        send = (Button)view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String s = message.getText().toString();
                if (s.length() > 100) {
                    Toast.makeText(getContext(), "message length exceeded maximum limit", Toast.LENGTH_LONG).show();
                } else if (s.length() == 0) {
                    Toast.makeText(getContext(), "message field epmty", Toast.LENGTH_LONG).show();
                } else {

                    if(NetworkCheck.isNetworkAvailable(getContext())) {

                        dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Please wait !!");
                        dialog.show();

                        EnquiriesRequest enquiriesRequest = ServiceGenerator.createService(EnquiriesRequest.class, DbHandler.getString(getContext(), "bearer", ""));
                        Call<EnquiriesPOJO> call = enquiriesRequest.requestResponse(getArguments().getString("training_id"), message.getText().toString());
                        call.enqueue(new Callback<EnquiriesPOJO>() {
                            @Override
                            public void onResponse(Call<EnquiriesPOJO> call, retrofit2.Response<EnquiriesPOJO> response) {
                                dialog.dismiss();
                                if (response.code() == 200) {
                                    if (!response.body().getError()) {
                                        send_enquiry(s);
                                    } else {
                                        Toast.makeText(getContext(),"Session Expired",Toast.LENGTH_LONG).show();
                                        DbHandler.unsetSession(getContext(),"isForcedLoggedOut");

                                    }
                                } else {
                                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_LONG);
                                    coloredSnackbar.warning(snackbar).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<EnquiriesPOJO> call, Throwable t) {
                                dialog.dismiss();
                                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_LONG);
                                coloredSnackbar.warning(snackbar).show();
                            }
                        });
                    }
                    else{
                        Snackbar snackbar=Snackbar.make(getActivity().findViewById(android.R.id.content),"No internet connection",Snackbar.LENGTH_LONG);
                        coloredSnackbar.alert(snackbar).show();
                    }


                }
            }
        });
        return  builder.create();
    }

    public void send_enquiry(final String s) {
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait !!");
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_ROOT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.i("response", response);

                        Toast.makeText(getContext(), "Message send!!", Toast.LENGTH_LONG).show();
                        TrainingDetails.change();
                        //dismiss();
                        Intent intent=new Intent(getContext(), NavigationActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Message not send!!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences sh = getActivity().getSharedPreferences("user", MODE_PRIVATE);
                params.put("type", "enquiry");
                   /* params.put("name",sh.getString("name",null));
                    params.put("email",sh.getString("email",null));
                    params.put("contact",sh.getString("contact",null));  */
                params.put("message", s);
                params.put("training_id", getArguments().getInt("training_id") + "");
                params.put("user_id", sh.getInt("usr_id", 0) + "");
                return params;
            }
        };
        queue.add(stringRequest);
    }



}
