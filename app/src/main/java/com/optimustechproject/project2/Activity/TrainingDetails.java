package com.optimustechproject.project2.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.optimustechproject.project2.Models.TrainingsPOJO;
import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.DbHandler;

public class TrainingDetails extends AppCompatActivity {

    TextView key_learning,desc,availability,duration,date,timings,venue;
    TrainingsPOJO data;
    Gson gson=new Gson();
    int index;

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

        key_learning=(TextView)findViewById(R.id.key_learnings);
        desc=(TextView)findViewById(R.id.desc);
        availability=(TextView)findViewById(R.id.availability);
        date=(TextView)findViewById(R.id.date);
        timings=(TextView)findViewById(R.id.timings);
        duration=(TextView)findViewById(R.id.duration);
        venue=(TextView)findViewById(R.id.venue);

        key_learning.setText("1. "+data.getKeyLearning1().get(index)+"\n2. "+data.getKeyLearning2().get(index)+"\n3. "+data.getKeyLearning3().get(index));
        desc.setText(data.getDescription().get(index));
        availability.setText(data.getAvailability().get(index));
        date.setText(data.getDate().get(index));
        timings.setText(data.getTimings().get(index));
        duration.setText(data.getDuration().get(index));
        venue.setText(data.getVenue().get(index));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
