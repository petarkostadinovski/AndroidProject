package com.example.firebaseproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.firebaseproject.Fragments.AccessoriesFragment;
import com.example.firebaseproject.Fragments.HomeFragment;
import com.example.firebaseproject.Fragments.ItemFragment;
import com.example.firebaseproject.Fragments.KeyFragment;
import com.example.firebaseproject.Fragments.ProfileFragment;
import com.example.firebaseproject.Fragments.SelectCarFragment;
import com.example.firebaseproject.Model.Key;
import com.example.firebaseproject.Model.Keychain;
import com.example.firebaseproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnHomeFragmentInteraction, KeyFragment.OnKeyFragmentInteraction, SelectCarFragment.CarFragmentInteraction, AccessoriesFragment.OnAccessoriesFragmentInteraction, ProfileFragment.OnProfileFragmentInteraction {

    private ProfileFragment profileFragment;
    private HomeFragment homeFragment;
    private KeyFragment keyFragment;
    private SelectCarFragment selectCarFragment;
    private ItemFragment itemFragment;

    private AccessoriesFragment accessoriesFragment;
    private FrameLayout selectCarFragment_container;
    private FrameLayout fragment_container;
    private int id;

    @Override
    public void itemClick(Key key) {
        itemFragment = ItemFragment.itemFragmentInstance(key.getName(), key.getDescription(), key.getImage_url(), key.getPrice(), key.getOn_stock());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(fragment_container.getId(), itemFragment, "ITEM_FRAGMENT").commit();
    }

    private List<Fragment> fragments;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        profileFragment = new ProfileFragment();
        homeFragment = new HomeFragment();
        keyFragment = new KeyFragment();
        selectCarFragment = new SelectCarFragment();
        accessoriesFragment = new AccessoriesFragment();
        itemFragment = new ItemFragment();
        selectCarFragment_container = (FrameLayout)findViewById(R.id.selectCarFragment_container);
        fragment_container = (FrameLayout)findViewById(R.id.fragment_container);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
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

            firebaseAuth = firebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (itemId == R.id.nav_profile){
                AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                profileFragment = new ProfileFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(fragment_container.getId(), profileFragment, "PROFILE_FRAGMENT").commit();
                return true;
            }
            else if (itemId == R.id.nav_home){
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
    public void openAccessoriesFragment() {
        accessoriesFragment = new AccessoriesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left);
        transaction.replace(fragment_container.getId(), accessoriesFragment, "ACCESSORIES_FRAGMENT").commit();
    }

    @Override
    public void openKeyFragment() {
        keyFragment = new KeyFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left);
        transaction.replace(fragment_container.getId(), keyFragment, "KEY_FRAGMENT").commit();
    }

    @Override
    public void openCarFragment() {
        selectCarFragment = new SelectCarFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
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

    @Override
    public void itemClick(Keychain keychain) {
        itemFragment = ItemFragment.itemFragmentInstance(keychain.getName(), keychain.getDescription(), keychain.getImage_url(), keychain.getPrice(), keychain.getOn_stock());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(fragment_container.getId(), itemFragment, "ITEM_FRAGMENT").commit();
    }

    @Override
    public void openHomeFrgment() {
//        homeFragment = new HomeFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.addToBackStack(null);
//        transaction.replace(fragment_container.getId(), homeFragment, "HOME_FRAGMENT").commit();
    }
}
