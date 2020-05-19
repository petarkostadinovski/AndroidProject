package com.example.firebaseproject.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebaseproject.R;

public class MenuActivity extends AppCompatActivity {

    private TextView textViewKeys;
    private TextView textViewProfile;

    private ImageView imageViewKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        textViewKeys = (TextView)findViewById(R.id.textViewKeys);
        textViewProfile = (TextView)findViewById(R.id.textViewProfile);
        imageViewKeys = (ImageView)findViewById(R.id.imageViewKeys);

        textViewKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, KeysActivity.class));
            }
        });

        textViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, ProfileActivity.class));
            }
        });

        imageViewKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, KeysActivity.class));
            }
        });

    }
}
