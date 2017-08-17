package com.optimustechproject.project2.Activity;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.optimustechproject.project2.Adapter.adapter_training_item;
import com.optimustechproject.project2.Models.TrainingsPOJO;
import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.DbHandler;

import java.util.ArrayList;
import java.util.Collections;

public class AllTrainings extends AppCompatActivity {

    RecyclerView mrecyclerView;
    RecyclerView.LayoutManager manager;
    adapter_training_item mAdapter;
    TrainingsPOJO data;
    Gson gson=new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_trainings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Trainings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);


        data=gson.fromJson(DbHandler.getString(this,"training_details","{}"),TrainingsPOJO.class);
        mrecyclerView = (RecyclerView) findViewById(R.id.recycler);
       // mrecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        assert mrecyclerView != null;
        mrecyclerView.setHasFixedSize(true);
        manager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerView.setLayoutManager(manager);
        mAdapter = new adapter_training_item(this,data,"allTrainings");
        mrecyclerView.setAdapter(mAdapter);

    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//
//        final MenuItem searchItem = menu.findItem(R.id.action_search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//
//        searchView.setOnQueryTextListener(this);
//
//        return true;
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }



//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            if(resultCode == RESULT_OK) {
//                if (data == null) {
//                    Toast.makeText(this, "No category choosen", Toast.LENGTH_LONG);
//                } else {
//                    String c = data.getStringExtra("category");
//                    sorted_by_category=new ArrayList<>();
//                    for(int i=0;i<trainings.size();i++){
//                        if(trainings.get(i).category.equals(c)){
//                            sorted_by_category.add(trainings.get(i));
//                        }
//                    }
//                    items=sorted_by_category;
//                    mAdapter.notifyDataSetChanged();
//                }
//            }
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id= item.getItemId();
//        switch (id) {
//            case R.id.home:
//                onBackPressed();
//            case R.id.srt_by_price:
//                // app icon in action bar clicked; go home
//                Collections.sort(items, new Comparator<item>() {
//                    @Override
//                    public int compare(item lhs, item rhs) {
//                        Float l = Float.parseFloat(lhs.price);
//                        Float r = Float.parseFloat(rhs.price);
//                        return l.compareTo(r);
//                    }
//                });
//                mAdapter.notifyDataSetChanged();
//                Toast.makeText(this, "Highest price first", Toast.LENGTH_LONG);
//                return true;
//            case R.id.srt_by_name:
//                // app icon in action bar clicked; go home
//                Collections.sort(items, new Comparator<item>() {
//                    @Override
//                    public int compare(item lhs, item rhs) {
//                        return lhs.title.toLowerCase().compareTo(rhs.title.toLowerCase());
//                    }
//                });
//                adapter.notifyDataSetChanged();
//                return true;
//            case R.id.srt_by_category:
//                // app icon in action bar clicked; go home
//                fetch_categories();
//                return true;
//            case R.id.all_trainings:
//                // app icon in action bar clicked; go home
//                items = trainings;
//                mAdapter.notifyDataSetChanged();
//                Toast.makeText(this, "Highest price first", Toast.LENGTH_LONG);
//                return true;
//
//        }
//        return super.onOptionsItemSelected(item);
//
//    }


//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        mAdapter.filter(query);
//        return true;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        mAdapter.filter(newText);
//        return true;
//    }
}
