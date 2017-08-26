package com.optimustechproject.project2.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.optimustechproject.project2.Activity.CreateTraining;
import com.optimustechproject.project2.Activity.NavigationActivity;
import com.optimustechproject.project2.Activity.TrainingDetails;
import com.optimustechproject.project2.Interface.DeleteTrainingRequest;
import com.optimustechproject.project2.Interface.EditTrainingRequest;
import com.optimustechproject.project2.Interface.TrainingDetailsRequest;
import com.optimustechproject.project2.Models.CreateTrainingPOJO;
import com.optimustechproject.project2.Models.CreatedTrainingsPOJO;
import com.optimustechproject.project2.Models.TrainingsPOJO;
import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.ColoredSnackbar;
import com.optimustechproject.project2.app.DbHandler;
import com.optimustechproject.project2.app.ImageTransform;
import com.optimustechproject.project2.app.NetworkCheck;
import com.optimustechproject.project2.app.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by satyam on 31/7/17.
 */

public class adapter_crtd_trn_item extends RecyclerView.Adapter<adapter_crtd_trn_item.view_holder>{


    TrainingsPOJO data;
    Context context;
    String from;
    List<Integer> ind=new ArrayList<Integer>();
    ProgressDialog progressDialog;
    Gson gson=new Gson();
    private ColoredSnackbar coloredSnackbar;

    public adapter_crtd_trn_item(Context context,TrainingsPOJO data,String from) {
        this.data=data;
        this.context=context;
        this.from=from;
        for(int i=0;i<data.getTitle().size();i++){
            ind.add(i);
        }
    }

    public class view_holder extends RecyclerView.ViewHolder{
        TextView title,price,location;
        ImageView imageView,edit,delete;
        LinearLayout edit_ll;
        public view_holder(View itemView) {
            super(itemView);

            title=(TextView)itemView.findViewById(R.id.title);
            price=(TextView)itemView.findViewById(R.id.price);
            location=(TextView)itemView.findViewById(R.id.location);
            imageView=(ImageView) itemView.findViewById(R.id.imageView);
            edit_ll=(LinearLayout)itemView.findViewById(R.id.edit_ll);
            edit=(ImageView)itemView.findViewById(R.id.edit);
            delete=(ImageView)itemView.findViewById(R.id.delete);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    edit_ll.setVisibility(View.VISIBLE);

                    return false;
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context).setMessage("Are you sure you want to delete this training?").setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(NetworkCheck.isNetworkAvailable(context)) {
                                progressDialog = new ProgressDialog(context);
                                progressDialog.setMessage("Loading...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                DeleteTrainingRequest deleteTrainingRequest = ServiceGenerator.createService(DeleteTrainingRequest.class, DbHandler.getString(context, "bearer", ""));
                                //Call<CreateTrainingPOJO> call = createTrainingRequest.requestResponse(name.getText().toString(), String.valueOf(l.latitude), String.valueOf(l.longitude), location.getText().toString(), date_et.getText().toString(), kl1.getText().toString(), kl2.getText().toString(), kl3.getText().toString(), price.getText().toString(), String.valueOf(cat_id.get(categories.getSelectedItemPosition())), String.valueOf(availability.getSelectedItem()), duration.getText().toString(), desc.getText().toString(),fTime.getText().toString(),tTime.getText().toString(),filename);
                                Call<CreateTrainingPOJO> call = deleteTrainingRequest.requestResponse(data.getId().get(getAdapterPosition()));
                                call.enqueue(new Callback<CreateTrainingPOJO>() {
                                    @Override
                                    public void onResponse(Call<CreateTrainingPOJO> call, Response<CreateTrainingPOJO> response) {
                                        progressDialog.dismiss();
                                        if (response.code() == 200) {
                                            if (!response.body().getError()) {

                                                progressDialog = new ProgressDialog(context);
                                                progressDialog.setMessage("Loading...");
                                                progressDialog.setCancelable(false);
                                                progressDialog.show();

                                                TrainingDetailsRequest trainingDetailsRequest = ServiceGenerator.createService(TrainingDetailsRequest.class, DbHandler.getString(context, "bearer", ""));
                                                Call<CreatedTrainingsPOJO> call2 = trainingDetailsRequest.requestResponse();
                                                call2.enqueue(new Callback<CreatedTrainingsPOJO>() {
                                                    @Override
                                                    public void onResponse(Call<CreatedTrainingsPOJO> call, Response<CreatedTrainingsPOJO> response) {
                                                        progressDialog.dismiss();
                                                        if (response.code() == 200) {
                                                            if (!response.body().getError()) {
                                                                DbHandler.putString(context,"training_details",gson.toJson(response.body().getTrainings()));
                                                                Toast.makeText(context, "Training deleted successfully", Toast.LENGTH_LONG).show();
                                                                Intent intent = new Intent(context, NavigationActivity.class);
                                                                context.startActivity(intent);
                                                            } else {
                                                                Toast.makeText(context, "Session Expired", Toast.LENGTH_LONG).show();
                                                                DbHandler.unsetSession(context, "isForcedLoggedOut");
                                                            }
                                                        }
                                                        else {
                                                           new AlertDialog.Builder(context).setTitle("Error").setMessage("Error connecting to server").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                               @Override
                                                               public void onClick(DialogInterface dialog, int which) {

                                                               }
                                                           }).create().show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<CreatedTrainingsPOJO> call, Throwable t) {
                                                        progressDialog.dismiss();
                                                        new AlertDialog.Builder(context).setTitle("Error").setMessage("Error connecting to server").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                            }
                                                        }).create().show();
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(context, "Session Expired", Toast.LENGTH_LONG).show();
                                                DbHandler.unsetSession(context, "isForcedLoggedOut");
                                            }
                                        } else {
                                            new AlertDialog.Builder(context).setTitle("Error").setMessage("Error connecting to server").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).create().show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<CreateTrainingPOJO> call, Throwable t) {
                                        progressDialog.dismiss();
                                        Log.e("Error", String.valueOf(t));
                                        new AlertDialog.Builder(context).setTitle("Error").setMessage("Error connecting to server").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).create().show();

                                    }
                                });
                            }
                            else{
                                new AlertDialog.Builder(context).setTitle("Error").setMessage("No internet connection").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                            }
                        }
                    }).create().show();
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int in=ind.get(getAdapterPosition());
                    Intent intent=new Intent(context, CreateTraining.class);
                    intent.putExtra("operation","edit");
                    intent.putExtra("index",in);
                    intent.putExtra("from",from);
                    intent.putExtra("key_learning1",data.getKeyLearning1().get(in));
                    intent.putExtra("key_learning2",data.getKeyLearning2().get(in));
                    intent.putExtra("key_learning3",data.getKeyLearning3().get(getAdapterPosition()));
                    intent.putExtra("title",data.getTitle().get(in));
                    intent.putExtra("date",data.getDate().get(in));
                    intent.putExtra("training_id",data.getId().get(in));
                    intent.putExtra("desc",data.getDescription().get(in));
                    intent.putExtra("latitude",data.getVenueLatitude().get(in));
                    intent.putExtra("longitude",data.getVenueLongitude().get(in));
                    intent.putExtra("timings",data.getTimings().get(in));
                    intent.putExtra("price",data.getPrice().get(in));
                    intent.putExtra("duration",data.getDuration().get(in));
                    intent.putExtra("category",data.getCategory().get(in));
                    intent.putExtra("enquiry_status",data.getEnquiryStatus().get(in));
                    intent.putExtra("venue",data.getVenue().get(in));
                    intent.putExtra("photo",data.getPhoto().get(in));
                    intent.putExtra("availability",data.getAvailability().get(in));
                    context.startActivity(intent);
                }
            });

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int in=ind.get(getAdapterPosition());
//                    Intent intent=new Intent(context, TrainingDetails.class);
//                    intent.putExtra("index",in);
//                    intent.putExtra("from",from);
//                    intent.putExtra("key_learning1",data.getKeyLearning1().get(in));
//                    intent.putExtra("key_learning2",data.getKeyLearning1().get(in));
//                    intent.putExtra("key_learning3",data.getKeyLearning1().get(getAdapterPosition()));
//                    intent.putExtra("title",data.getTitle().get(in));
//                    intent.putExtra("date",data.getDate().get(in));
//                    intent.putExtra("training_id",data.getId().get(in));
//                    intent.putExtra("desc",data.getDescription().get(in));
//                    intent.putExtra("timings",data.getTimings().get(in));
//                    intent.putExtra("duration",data.getDuration().get(in));
//                    intent.putExtra("category",data.getCategory().get(in));
//                    intent.putExtra("enquiry_status",data.getEnquiryStatus().get(in));
//                    intent.putExtra("venue",data.getVenue().get(in));
//                    intent.putExtra("photo",data.getPhoto().get(in));
//                    intent.putExtra("availability",data.getAvailability().get(in));
//                    context.startActivity(intent);
//                }
//            });

        }
    }

    public void setFilter(TrainingsPOJO pojo) {
        data=null;
        data=pojo;
        notifyDataSetChanged();
    }

    @Override
    public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_created_training,parent,false);
        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(adapter_crtd_trn_item.view_holder holder, int position) {


        holder.title.setText(data.getTitle().get(position));
        holder.price.setText("Rs. "+data.getPrice().get(position));
        // Toast.makeText(context,data.getPhoto().get(position),Toast.LENGTH_LONG).show();
        holder.location.setText(data.getVenue().get(position));
        Picasso
                .with(context)
                .load(data.getPhoto().get(position))
                .placeholder(R.mipmap.ic_launcher)
                .transform(new ImageTransform())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return data.getTitle().size();
    }

}