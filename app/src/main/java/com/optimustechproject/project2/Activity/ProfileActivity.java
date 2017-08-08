package com.optimustechproject.project2.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.optimustechproject.project2.Adapter.adapter_training_item;
import com.optimustechproject.project2.Fragments.dialog_users_location;
import com.optimustechproject.project2.Interface.CreatedTrainingsRequest;
import com.optimustechproject.project2.Models.CreatedTrainingsPOJO;
import com.optimustechproject.project2.Models.LoginDataPOJO;
import com.optimustechproject.project2.Models.LoginDataumPOJO;
import com.optimustechproject.project2.Models.TrainingsPOJO;
import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.ColoredSnackbar;
import com.optimustechproject.project2.app.DbHandler;
import com.optimustechproject.project2.app.NetworkCheck;
import com.optimustechproject.project2.app.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static ViewPager mViewPager;

    static LoginDataumPOJO data;
    Gson gson=new Gson();
    static View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data=gson.fromJson(DbHandler.getString(this,"login_data","}"),LoginDataumPOJO.class);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        TextView name,email,mobile,location;
        ProgressDialog progressDialog;
        private ColoredSnackbar coloredSnackbar;
        RecyclerView mrecyclerView;
        RecyclerView.LayoutManager manager;
        adapter_training_item mAdapter;

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {

            if(getArguments().getInt(ARG_SECTION_NUMBER)==1) {
                rootView = inflater.inflate(R.layout.fragment_profile, container, false);

                name=(TextView)rootView.findViewById(R.id.name1);
                email=(TextView)rootView.findViewById(R.id.email1);
                mobile=(TextView)rootView.findViewById(R.id.mobile1);
                location=(TextView)rootView.findViewById(R.id.location1);

                name.setText(data.getName());
                email.setText(data.getEmail());
                mobile.setText(data.getMobile());
                location.setText(data.getLocation());

                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_users_location dialog_users_location = new dialog_users_location();
                        dialog_users_location.show(getFragmentManager(),"Location");
                    }
                });


            }
            if(getArguments().getInt(ARG_SECTION_NUMBER)==2){
                rootView = inflater.inflate(R.layout.fragment_created_trainings, container, false);

                if(NetworkCheck.isNetworkAvailable(getContext())) {
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    CreatedTrainingsRequest createdTrainingsRequest = ServiceGenerator.createService(CreatedTrainingsRequest.class, DbHandler.getString(getContext(), "bearer", ""));
                    Call<CreatedTrainingsPOJO> call = createdTrainingsRequest.requestResponse();
                    call.enqueue(new Callback<CreatedTrainingsPOJO>() {
                        @Override
                        public void onResponse(Call<CreatedTrainingsPOJO> call, Response<CreatedTrainingsPOJO> response) {
                            progressDialog.dismiss();
                            if (response.code() == 200) {
                                if (!response.body().getError()) {
                                    mrecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
                                    // mrecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
                                    assert mrecyclerView != null;
                                    mrecyclerView.setHasFixedSize(true);
                                    manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                                    mrecyclerView.setLayoutManager(manager);
                                    mAdapter = new adapter_training_item(getContext(), response.body().getTrainings());
                                    mrecyclerView.setAdapter(mAdapter);
                                } else {
                                    Toast.makeText(getContext(), "Session Expired", Toast.LENGTH_LONG).show();
                                    DbHandler.unsetSession(getContext(), "isForcedLoggedOut");
                                }
                            } else {
                                new AlertDialog.Builder(getActivity()).setCancelable(false).setMessage("Error connecting to server").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().onBackPressed();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<CreatedTrainingsPOJO> call, Throwable t) {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(getActivity()).setCancelable(false).setMessage("Error connecting to server").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().onBackPressed();
                                }
                            });
                        }
                    });
                }
                else{
                    new AlertDialog.Builder(getActivity()).setCancelable(false).setMessage("Not internet connection").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().onBackPressed();
                        }
                    });
                }


            }
            if(getArguments().getInt(ARG_SECTION_NUMBER)==3){
                rootView = inflater.inflate(R.layout.fragment_registered_trainings, container, false);



            }
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "My Profile";
                case 1:
                    return "Created Trainings";
                case 2:
                    return "Registered Trainings";
            }
            return null;
        }
    }
}
