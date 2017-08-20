package com.optimustechproject.project2.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.optimustechproject.project2.Adapter.adapter_notification;
import com.optimustechproject.project2.Adapter.adapter_training_item;
import com.optimustechproject.project2.Models.NotificationsPOJO;
import com.optimustechproject.project2.Models.TrainingsPOJO;
import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.DbHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView mrecyclerView;
    LinearLayoutManager linearLayoutManager;
    adapter_notification mAdapter;
    List<NotificationsPOJO> data=new ArrayList<NotificationsPOJO>();
    Gson gson=new Gson();
    LinearLayout no_noti;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NotificationActivity.this,NavigationActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar;
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Notifications");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        no_noti=(LinearLayout)findViewById(R.id.no_noti);

        mrecyclerView = (RecyclerView) findViewById(R.id.recycler);

        Type type = new TypeToken<List<NotificationsPOJO>>() {}.getType();

        if(DbHandler.contains(this,"notificationList")) {
            data = gson.fromJson(DbHandler.getString(this, "notificationList", ""), type);

            if(data.size()>0) {
                no_noti.setVisibility(View.GONE);
                Collections.sort(data, new Comparator<NotificationsPOJO>() {
                    @Override
                    public int compare(NotificationsPOJO o1, NotificationsPOJO o2) {
                        return o2.getTimeStamp().compareTo(o1.getTimeStamp());
                    }
                });
                // mrecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
                if (!data.toString().equals("")) {
                    assert mrecyclerView != null;
                    mrecyclerView.setHasFixedSize(true);
                    linearLayoutManager = new LinearLayoutManager(this);
                    mrecyclerView.setLayoutManager(linearLayoutManager);
                    mAdapter = new adapter_notification(this, data);
                    mrecyclerView.setAdapter(mAdapter);
                }
            }
            else{
                mrecyclerView.setVisibility(View.GONE);
            }
        }
        else{
            mrecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

}
