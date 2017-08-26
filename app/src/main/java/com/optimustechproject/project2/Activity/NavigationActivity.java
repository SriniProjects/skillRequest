package com.optimustechproject.project2.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.optimustechproject.project2.Adapter.adapter_training_item;
import com.optimustechproject.project2.Fragments.fragment_about;
import com.optimustechproject.project2.Fragments.fragment_dashboard;
import com.optimustechproject.project2.Interface.CreatedTrainingsRequest;
import com.optimustechproject.project2.Interface.TrainingDetailsRequest;
import com.optimustechproject.project2.Models.CreatedTrainingsPOJO;
import com.optimustechproject.project2.Models.LoginDataumPOJO;
import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.ColoredSnackbar;
import com.optimustechproject.project2.app.DbHandler;
import com.optimustechproject.project2.app.ImageTransform;
import com.optimustechproject.project2.app.NetworkCheck;
import com.optimustechproject.project2.app.ServiceGenerator;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private boolean doubleBackToExitPressedOnce = false;
    private ColoredSnackbar coloredSnackBar;
    ProgressDialog progressDialog;
    Gson gson=new Gson();
    ImageView notification;
    LoginDataumPOJO dataumPOJO;
    Toolbar toolbar;
    int flg=0;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        notification=(ImageView)findViewById(R.id.notifications);

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavigationActivity.this,NotificationActivity.class));

            }
        });

        dataumPOJO=gson.fromJson(DbHandler.getString(this,"login_data","{}"),LoginDataumPOJO.class);

        if(NetworkCheck.isNetworkAvailable(this)){
            progressDialog = new ProgressDialog(NavigationActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            TrainingDetailsRequest trainingDetailsRequest = ServiceGenerator.createService(TrainingDetailsRequest.class, DbHandler.getString(NavigationActivity.this, "bearer", ""));
            Call<CreatedTrainingsPOJO> call = trainingDetailsRequest.requestResponse();
            call.enqueue(new Callback<CreatedTrainingsPOJO>() {
                @Override
                public void onResponse(Call<CreatedTrainingsPOJO> call, Response<CreatedTrainingsPOJO> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {
                        if (!response.body().getError()) {
                            DbHandler.putString(NavigationActivity.this,"training_details",gson.toJson(response.body().getTrainings()));
                        } else {
                            Toast.makeText(NavigationActivity.this, "Session Expired", Toast.LENGTH_LONG).show();
                            DbHandler.unsetSession(NavigationActivity.this, "isForcedLoggedOut");
                        }
                    }
                    else {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_SHORT);
                        coloredSnackBar.warning(snackbar).show();
                    }
                }

                @Override
                public void onFailure(Call<CreatedTrainingsPOJO> call, Throwable t) {
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Error connecting to server", Snackbar.LENGTH_SHORT);
                    coloredSnackBar.warning(snackbar).show();
                }
            });
        }
        else{
            Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"No internet connection",Snackbar.LENGTH_LONG);
            coloredSnackBar.alert(snackbar).show();
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hview=navigationView.getHeaderView(0);
        ImageView profile=(ImageView) hview.findViewById(R.id.profile);
        TextView name=(TextView)hview.findViewById(R.id.name);
        TextView email=(TextView)hview.findViewById(R.id.email);

        name.setText(dataumPOJO.getName());
        email.setText(dataumPOJO.getEmail());
            Picasso
                    .with(this)
                    .load(dataumPOJO.getPhoto())
                    .placeholder(R.drawable.ic_account_circle_white_48dp)
                    .transform(new ImageTransform())
                    .into(profile);

        Fragment fragment=null;
        Class fragmentClass=null;
        fragmentClass=fragment_dashboard.class;
        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        replaceFragment(fragment);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

            @Override
            public void onBackStackChanged() {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.flContent);
                if (f != null) {
                    updateTitle(f);
                }

            }
        });

    }

    public void updateTitle(Fragment f){
        if(f.getClass().getName().equals(fragment_dashboard.class.getName())){
            setTitle("Dashboard");

        }
        if(f.getClass().getName().equals(fragment_about.class.getName())){
            setTitle("About Us");

        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            Fragment fragment=null;
            Class fragmentClass=null;
            fragmentClass=fragment_dashboard.class;
            FragmentManager fragmentManager = getSupportFragmentManager();
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            replaceFragment(fragment);
            getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

                @Override
                public void onBackStackChanged() {
                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.flContent);
                    if (f != null) {
                        updateTitle(f);
                    }

                }
            });
            flg=0;

            item.setChecked(true);

        } else if (id == R.id.create_training) {
            Intent intent=new Intent(NavigationActivity.this,CreateTraining.class);
            intent.putExtra("operation","insert");
            startActivity(intent);

        } else if (id == R.id.about_us) {

            Fragment fragment=null;
            Class fragmentClass=null;
            fragmentClass=fragment_about.class;
            FragmentManager fragmentManager = getSupportFragmentManager();
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            replaceFragment(fragment);
            getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

                @Override
                public void onBackStackChanged() {
                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.flContent);
                    if (f != null) {
                        updateTitle(f);
                    }

                }
            });
            flg=1;

            item.setChecked(true);

        } else if (id == R.id.sign_out) {

            new AlertDialog.Builder(NavigationActivity.this).setTitle("Logout").setMessage("Are you sure you want to logout?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DbHandler.unsetSession(NavigationActivity.this,"logout");
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();

        } else if (id == R.id.exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else if(id==R.id.my_noti){
            startActivity(new Intent(NavigationActivity.this,NotificationActivity.class));
        }
        else if(id==R.id.all_trainings){
            Intent intent=new Intent(NavigationActivity.this, AllTrainings.class);
            startActivity(intent);
        }
        else if(id==R.id.my_profile){
            Intent intent=new Intent(NavigationActivity.this, ProfileActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;
        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.flContent, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {

        if(flg==0) {
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
        else{
            Fragment fragment=null;
            Class fragmentClass=null;
            fragmentClass=fragment_dashboard.class;
            FragmentManager fragmentManager = getSupportFragmentManager();
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            replaceFragment(fragment);
            getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

                @Override
                public void onBackStackChanged() {
                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.flContent);
                    if (f != null) {
                        updateTitle(f);
                    }

                }
            });
            flg=0;

            navigationView.setCheckedItem(0);
        }
    }


}
