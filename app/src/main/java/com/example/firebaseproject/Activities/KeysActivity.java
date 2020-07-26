package com.example.firebaseproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.firebaseproject.Fragments.KeyFragment;
import com.example.firebaseproject.Fragments.SelectCarFragment;
import com.example.firebaseproject.Model.Car;
import com.example.firebaseproject.Model.Key;
import com.example.firebaseproject.R;
import com.example.firebaseproject.Service.Implementation.KeyServiceImplementation;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KeysActivity extends AppCompatActivity implements SelectCarFragment.CarFragmentInteraction {

    ListView listViewKeys;
    List<Key> keyList;
    List<Key> filteredList;
    List<Car> carList;
    List<Key> filteredByCar;

    ImageView imageViewSearchKeys;
    public ImageView imageViewChooseCar;

    TextView textViewKeys;

    KeyFragment keyFragment;
    SelectCarFragment selectCarFragment;

    String searchedValue;

    EditText editTextSearchByName;

    FrameLayout container_keyFragment;
    FrameLayout container_carFragment;

    Spinner spinnerKeys;
    Spinner spinnerCarBrand;
    Spinner spinnerCarModel;
    Spinner spinnerCarYear;

    DatabaseReference databaseKeys;

    KeyServiceImplementation keyServiceImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keys);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        databaseKeys = FirebaseDatabase.getInstance().getReference("keys");

        listViewKeys = (ListView)findViewById(R.id.listViewKeys);
        keyList = new ArrayList<>();
        carList = new ArrayList<>();
        filteredByCar = new ArrayList<>();
        filteredList = new ArrayList<>();
        spinnerKeys = (Spinner)findViewById(R.id.spinnerKeys);
        editTextSearchByName = (EditText)findViewById(R.id.editTextSearchByName);
        imageViewSearchKeys = (ImageView)findViewById(R.id.imageViewSearchKeys);
        imageViewChooseCar = (ImageView)findViewById(R.id.imageViewChooseCar);
        spinnerCarBrand = (Spinner)findViewById(R.id.spinnerCarBrand);
        spinnerCarModel = (Spinner)findViewById(R.id.spinnerCarModel);
        spinnerCarYear = (Spinner)findViewById(R.id.spinnerCarYear);
        container_keyFragment = (FrameLayout) findViewById(R.id.container_keyFragment);
        container_carFragment = (FrameLayout) findViewById(R.id.container_carFragment);
        textViewKeys = (TextView) findViewById(R.id.textViewKeys);
        searchedValue = String.valueOf(editTextSearchByName.getText());
        keyServiceImpl = new KeyServiceImplementation();
        openKeyFragment(keyList);

    }

    public void openKeyFragment(List<Key> list){
        keyFragment = KeyFragment.keyFragmentInstance(list);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (selectCarFragment != null && selectCarFragment.isAdded())
            transaction.hide(selectCarFragment);
        imageViewChooseCar.setEnabled(true);
        //transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_left);
        transaction.addToBackStack(null);
        transaction.add(container_keyFragment.getId(), keyFragment, "KEY_FRAGMENT").commit();
    }

    public void openCarFragment(){
        selectCarFragment = new SelectCarFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.detach(keyFragment);
        transaction.add(container_carFragment.getId(), selectCarFragment, "CAR_FRAGMENT").commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile){
                Intent intent = new Intent(KeysActivity.this, MainActivity.class);
                startActivity(intent);
                intent.putExtra("id", itemId);
                return true;
            }
            else if (itemId == R.id.nav_home){
                Intent intent = new Intent(KeysActivity.this, MainActivity.class);
                startActivity(intent);
                intent.putExtra("id", itemId);
                return true;
            }
            else
                return false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        imageViewChooseCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageViewChooseCar.setEnabled(false);
                openCarFragment();
            }
        });

        databaseKeys.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                keyList.clear();

                for (DataSnapshot item : dataSnapshot.getChildren()){

                    Key key = item.getValue(Key.class);
                    keyList.add(key);

                }

                imageViewSearchKeys.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchedValue = String.valueOf(editTextSearchByName.getText());
                        filteredList = keyList.stream()
                                .filter(key -> key.getName().toLowerCase().contains(searchedValue.toLowerCase()))
                                .collect(Collectors.toList());

                        spinnerKeys.setSelection(0);
                        openKeyFragment(filteredList);
                    }
                });

                spinnerKeys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 1)
                            openKeyFragment(keyServiceImpl.filterKeyByNameAsc(keyList));
                        else if (position == 2)
                            openKeyFragment(keyServiceImpl.filterKeyByNameDesc(keyList));
                        else if (position == 3)
                            openKeyFragment(keyServiceImpl.filterAvailableKeys(keyList));
                        else if (position == 4)
                            openKeyFragment(keyServiceImpl.filterNotAvailableKeys(keyList));
                        else
                            editTextSearchByName.setText("");

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void filteredData(String id) {

        if (id != null) {
            int carId = Integer.parseInt(id);
            filteredList = keyList.stream()
                    .filter(key -> key.getCar_id() == carId)
                    .collect(Collectors.toList());

            openKeyFragment(filteredList);

            imageViewChooseCar.setEnabled(true);
        }

    }

}
