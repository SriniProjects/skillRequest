package com.optimustechproject.project2.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.optimustechproject.project2.R;

/**
 * Created by satyam on 18/8/17.
 */

public class fragment_about extends Fragment {

    View parentView;
    ImageView app_icon,fb,insta,g_plus,twi,link;
    CardView contact;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_about, container, false);

        app_icon=(ImageView)parentView.findViewById(R.id.app_icon);
        fb=(ImageView)parentView.findViewById(R.id.fb);
        insta=(ImageView)parentView.findViewById(R.id.insta);
        g_plus=(ImageView)parentView.findViewById(R.id.g_plus);
        twi=(ImageView)parentView.findViewById(R.id.twitter);
        link=(ImageView)parentView.findViewById(R.id.link);
        contact=(CardView) parentView.findViewById(R.id.contact);

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
                startActivity(intent);
            }
        });

        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/"));
                startActivity(intent);
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/company/"));
                startActivity(intent);
            }
        });

        twi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/"));
                startActivity(intent);
            }
        });

        g_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/"));
                startActivity(intent);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","satyamg025@gmail.com", null));
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });

        app_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/"));
                startActivity(intent);
            }
        });

        return parentView;
    }
}
