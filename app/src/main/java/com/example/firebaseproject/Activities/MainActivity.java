package com.example.firebaseproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebaseproject.Fragments.HomeFragment;
import com.example.firebaseproject.Fragments.KeyFragment;
import com.example.firebaseproject.Fragments.ProfileFragment;
import com.example.firebaseproject.Fragments.SelectCarFragment;
import com.example.firebaseproject.Model.Key;
import com.example.firebaseproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnHomeFragmentInteraction, KeyFragment.OnKeyFragmentInteraction, SelectCarFragment.CarFragmentInteraction {

    private ProfileFragment profileFragment;
    private HomeFragment homeFragment;
    private KeyFragment keyFragment;
    private SelectCarFragment selectCarFragment;
    private FrameLayout selectCarFragment_container;
    private FrameLayout fragment_container;
    private int id;
    private boolean homeClicked;
    private boolean profileClicked;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        profileFragment = new ProfileFragment();
        homeFragment = new HomeFragment();
        keyFragment = new KeyFragment();
        selectCarFragment = new SelectCarFragment();
        selectCarFragment_container = (FrameLayout)findViewById(R.id.selectCarFragment_container);
        fragment_container = (FrameLayout)findViewById(R.id.fragment_container);
        homeClicked = false;
        profileClicked = false;
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        homeClicked = true;
        profileClicked = false;
        homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(fragment_container.getId(), homeFragment, "HOME_FRAGMENT").commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_profile && !profileClicked){
                profileClicked = true;
                homeClicked = false;
                profileFragment = new ProfileFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(fragment_container.getId(), profileFragment, "PROFILE_FRAGMENT").commit();
                return true;
            }
            else if (itemId == R.id.nav_home && !homeClicked){
                homeClicked = true;
                profileClicked = false;
                homeFragment = new HomeFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(fragment_container.getId(), homeFragment, "HOME_FRAGMENT").commit();
                return true;
            }
            else
                return false;
        }
    };

    @Override
    public void openHomeFragment() {
        keyFragment = new KeyFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left);
        transaction.replace(fragment_container.getId(), keyFragment, "KEY_FRAGMENT").commit();
        homeClicked = false;
    }

    @Override
    public void openCarFragment() {
        selectCarFragment = new SelectCarFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        homeClicked = false;
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.replace(fragment_container.getId(), selectCarFragment, "SELECT_CAR_FRAGMENT").commit();
    }

    @Override
    public void filteredData(String id) {
        if (id != null) {
            keyFragment = KeyFragment.keyFragmentInstance(Integer.parseInt(id));
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.addToBackStack(null);
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left);
            transaction.replace(fragment_container.getId(), keyFragment, "KEY_FRAGMENT").commit();
        }else {
            keyFragment = new KeyFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.addToBackStack(null);
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left);
            transaction.replace(fragment_container.getId(), keyFragment, "KEY_FRAGMENT").commit();
        }
    }
}
