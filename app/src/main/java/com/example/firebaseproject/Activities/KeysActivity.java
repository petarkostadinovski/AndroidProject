package com.example.firebaseproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.firebaseproject.Model.Car;
import com.example.firebaseproject.Model.Key;
import com.example.firebaseproject.Model.KeyList;
import com.example.firebaseproject.R;
import com.example.firebaseproject.Service.Implementation.KeyServiceImplementation;
import com.example.firebaseproject.Service.KeyService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KeysActivity extends AppCompatActivity {

    ListView listViewKeys;
    List<Key> keyList;
    List<Key> filteredList;
    List<Car> carList;
    List<Key> filteredByCar;

    ImageView imageViewBackFromKeys;
    ImageView imageViewSearchKeys;
    ImageView imageViewChooseCar;

    EditText editTextSearchByName;

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

        keyServiceImpl = new KeyServiceImplementation();

    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile){

                Intent intent = new Intent(KeysActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            if (itemId == R.id.nav_home){
                Intent intent = new Intent(KeysActivity.this, MenuActivity.class);
                startActivity(intent);
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
                startActivity(new Intent(KeysActivity.this, CarsActivity.class));
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

                        String searchedValue = String.valueOf(editTextSearchByName.getText());

                        List<Key> searchedList = keyList.stream()
                                .filter(key -> key.getName().toLowerCase().contains(searchedValue.toLowerCase()))
                                .collect(Collectors.toList());

                        spinnerKeys.setSelection(0);

                        KeyList adapter = new KeyList(KeysActivity.this, searchedList);
                        listViewKeys.setAdapter(adapter);
                    }
                });

                spinnerKeys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position != 0)
                            editTextSearchByName.setText("");

                        if (position == 1)
                            filteredList = keyServiceImpl.filterKeyByNameAsc(keyList);
                        else if (position == 2)
                            filteredList = keyServiceImpl.filterKeyByNameDesc(keyList);
                        else if (position == 3)
                            filteredList = keyServiceImpl.filterAvailableKeys(keyList);
                        else if (position == 4)
                            filteredList = keyServiceImpl.filterNotAvailableKeys(keyList);
                        else
                            filteredList = keyList;

                        KeyList adapter = new KeyList(KeysActivity.this, filteredList);
                        listViewKeys.setAdapter(adapter);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                if (getIntent().getStringExtra("id") != null) {

                    int carId = Integer.parseInt(getIntent().getStringExtra("id"));

                    filteredByCar = keyList.stream()
                            .filter(key -> key.getCar_id() == carId)
                            .collect(Collectors.toList());

                    KeyList adapter = new KeyList(KeysActivity.this, filteredByCar);
                    listViewKeys.setAdapter(adapter);

                }else {
                    KeyList adapter = new KeyList(KeysActivity.this, keyList);
                    listViewKeys.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
