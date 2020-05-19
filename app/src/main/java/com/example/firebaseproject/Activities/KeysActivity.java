package com.example.firebaseproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.firebaseproject.Model.Key;
import com.example.firebaseproject.Model.KeyList;
import com.example.firebaseproject.R;
import com.example.firebaseproject.Service.Implementation.KeyServiceImplementation;
import com.example.firebaseproject.Service.KeyService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KeysActivity extends AppCompatActivity {

    ListView listViewKeys;
    List<Key> keyList;
    List<Key> filteredList;
    DatabaseReference databaseKeys;
    Spinner spinnerKeys;

    KeyServiceImplementation keyServiceImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keys);

        databaseKeys = FirebaseDatabase.getInstance().getReference("keys");
        listViewKeys = (ListView)findViewById(R.id.listViewKeys);
        keyList = new ArrayList<>();
        filteredList = new ArrayList<>();
        spinnerKeys = (Spinner)findViewById(R.id.spinnerKeys);
        keyServiceImpl = new KeyServiceImplementation();
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseKeys.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                keyList.clear();

                for (DataSnapshot item : dataSnapshot.getChildren()){

                    Key key = item.getValue(Key.class);
                    keyList.add(key);

                }

                spinnerKeys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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


                KeyList adapter = new KeyList(KeysActivity.this, keyList);

                listViewKeys.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
