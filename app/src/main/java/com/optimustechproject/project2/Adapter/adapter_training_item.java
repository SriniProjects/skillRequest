package com.optimustechproject.project2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.optimustechproject.project2.Activity.TrainingDetails;
import com.optimustechproject.project2.Models.TrainingsPOJO;
import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.ImageTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satyam on 31/7/17.
 */

public class adapter_training_item extends RecyclerView.Adapter<adapter_training_item.view_holder>{


    TrainingsPOJO data;
    Context context;
    String from;
    List<Integer> ind=new ArrayList<Integer>();

    public adapter_training_item(Context context,TrainingsPOJO data,String from) {
        this.data=data;
        this.context=context;
        this.from=from;
        for(int i=0;i<data.getTitle().size();i++){
            ind.add(i);
        }
    }

    public class view_holder extends RecyclerView.ViewHolder{
        TextView title,price,location;
        ImageView imageView;
        public view_holder(View itemView) {
            super(itemView);

            title=(TextView)itemView.findViewById(R.id.title);
            price=(TextView)itemView.findViewById(R.id.price);
            location=(TextView)itemView.findViewById(R.id.location);
            imageView=(ImageView) itemView.findViewById(R.id.imageView);

            //////////// VIEW TRAINING DETAILS //////////

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int in=ind.get(getAdapterPosition());
                    Intent intent=new Intent(context, TrainingDetails.class);
                    intent.putExtra("index",in);
                    intent.putExtra("from",from);
                    intent.putExtra("key_learning1",data.getKeyLearning1().get(in));
                    intent.putExtra("key_learning2",data.getKeyLearning2().get(in));
                    intent.putExtra("key_learning3",data.getKeyLearning3().get(in));
                    intent.putExtra("title",data.getTitle().get(in));
                    intent.putExtra("date",data.getDate().get(in));
                    intent.putExtra("training_id",data.getId().get(in));
                    intent.putExtra("desc",data.getDescription().get(in));
                    intent.putExtra("timings",data.getTimings().get(in));
                    intent.putExtra("duration",data.getDuration().get(in));
                    intent.putExtra("category",data.getCategory().get(in));
                    intent.putExtra("enquiry_status",data.getEnquiryStatus().get(in));
                    intent.putExtra("venue",data.getVenue().get(in));
                    intent.putExtra("photo",data.getPhoto().get(in));
                    intent.putExtra("availability",data.getAvailability().get(in));
                    context.startActivity(intent);
                }
            });

        }
    }

    public void setFilter(TrainingsPOJO pojo) {
        data=null;
        data=pojo;
        notifyDataSetChanged();
    }

    @Override
    public view_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_training,parent,false);
        return new view_holder(view);
    }

    @Override
    public void onBindViewHolder(adapter_training_item.view_holder holder, int position) {


       holder.title.setText(data.getTitle().get(position));
        holder.price.setText("Rs. "+data.getPrice().get(position));
       // Toast.makeText(context,data.getEnquiryStatus().get(position),Toast.LENGTH_LONG).show();
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