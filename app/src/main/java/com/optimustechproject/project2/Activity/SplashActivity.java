package com.optimustechproject.project2.Activity;


import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.optimustechproject.project2.R;
import com.optimustechproject.project2.app.DbHandler;


public class SplashActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        Thread splashTread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    sleep(2500);
//                } catch (Exception e) {
//
//                } finally {
//                    if(DbHandler.getBoolean(SplashActivity.this,"isLoggedIn",false)){
//                        Intent intent = new Intent(SplashActivity.this,NavigationActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                    else {
//                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            };
//        };
//        splashTread.start();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public static class Placeholder extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public Placeholder() {
        }

        public static Placeholder newInstance(int sectionNumber) {
            Placeholder fragment = new Placeholder();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        View rootView;
        TextView skip;
        Button launch;

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {

            if(getArguments().getInt(ARG_SECTION_NUMBER)==1) {
                rootView = inflater.inflate(R.layout.fragment_splash1, container, false);

                skip=(TextView)rootView.findViewById(R.id.skip);
                skip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(DbHandler.getBoolean(getContext(),"isLoggedIn",false)){
                            Intent intent = new Intent(getContext(),NavigationActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                });

            }
            if(getArguments().getInt(ARG_SECTION_NUMBER)==2){
                rootView = inflater.inflate(R.layout.fragment_splash2, container, false);
                skip=(TextView)rootView.findViewById(R.id.skip);
                skip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(DbHandler.getBoolean(getContext(),"isLoggedIn",false)){
                        Intent intent = new Intent(getContext(),NavigationActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    else {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                            getActivity().finish();
                    }
                    }
                });
            }
            if(getArguments().getInt(ARG_SECTION_NUMBER)==3){
                rootView = inflater.inflate(R.layout.fragment_splash3, container, false);
                launch=(Button) rootView.findViewById(R.id.launch);
                launch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(DbHandler.getBoolean(getContext(),"isLoggedIn",false)){
                            Intent intent = new Intent(getContext(),NavigationActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else {
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                });
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

            return Placeholder.newInstance(position + 1);
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
                    return "";
                case 1:
                    return "";
                case 2:
                    return "";
            }
            return null;
        }
    }
}

