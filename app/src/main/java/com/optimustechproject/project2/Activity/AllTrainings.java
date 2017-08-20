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
import android.view.MenuInflater;
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
import java.util.List;

public class AllTrainings extends AppCompatActivity implements SearchView.OnQueryTextListener {

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
        getSupportActionBar().setTitle("All Trainings");
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_manu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        mAdapter.setFilter(data);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {

                        return true; // Return true to expand action view
                    }
                });
        return true;
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final TrainingsPOJO pojo = filter(data, newText);

        mAdapter.setFilter(pojo);
        return true;
    }
    private TrainingsPOJO filter(TrainingsPOJO models, String query) {
        query = query.toLowerCase();
        final TrainingsPOJO filteredModelList = gson.fromJson("{}",TrainingsPOJO.class);
        List<String> id=new ArrayList<String>(),kl1=new ArrayList<String>(),kl2=new ArrayList<String>(),kl3=new ArrayList<String>(),title=new ArrayList<String>(),price=new ArrayList<String>(),categ=new ArrayList<String>(),dur=new ArrayList<String>(),photo=new ArrayList<String>(),avail=new ArrayList<String>(),desc=new ArrayList<String>(),date=new ArrayList<String>(),tim=new ArrayList<String>(),ven=new ArrayList<String>(),ven_la=new ArrayList<String>(),ven_lo=new ArrayList<String>(),enq=new ArrayList<String>();
        for (int i=0;i<models.getTitle().size();i++) {
            final String text = models.getTitle().get(i).toLowerCase();
            if (text.contains(query)) {
                id.add(models.getId().get(i));
                kl1.add(models.getKeyLearning1().get(i));
                kl2.add(models.getKeyLearning2().get(i));
                kl3.add(models.getKeyLearning3().get(i));
                title.add(models.getTitle().get(i));
                tim.add(models.getTimings().get(i));
                categ.add(models.getCategory().get(i));
                dur.add(models.getDuration().get(i));
                date.add(models.getDate().get(i));
                ven.add(models.getVenue().get(i));
                ven_la.add(models.getVenueLatitude().get(i));
                ven_lo.add(models.getVenueLongitude().get(i));
                price.add(models.getPrice().get(i));
                desc.add(models.getDescription().get(i));
                enq.add(models.getEnquiryStatus().get(i));
                photo.add(models.getPhoto().get(i));
                avail.add(models.getAvailability().get(i));
            }
        }

        filteredModelList.setPrice(price);
        filteredModelList.setVenue(ven);
        filteredModelList.setVenueLatitude(ven_la);
        filteredModelList.setTitle(title);
        filteredModelList.setTimings(tim);
        filteredModelList.setId(id);
        filteredModelList.setKeyLearning1(kl1);
        filteredModelList.setKeyLearning2(kl2);
        filteredModelList.setKeyLearning3(kl3);
        filteredModelList.setDate(date);
        filteredModelList.setDescription(desc);
        filteredModelList.setAvailability(avail);
        filteredModelList.setPhoto(photo);
        filteredModelList.setEnquiryStatus(enq);
        filteredModelList.setCategory(categ);
        filteredModelList.setVenueLongitude(ven_lo);
        filteredModelList.setDuration(dur);

        return filteredModelList;
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
