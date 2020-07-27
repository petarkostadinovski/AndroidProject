package com.example.firebaseproject.Fragments;

import android.content.Context;
import android.graphics.Path;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.firebaseproject.Activities.KeysActivity;
import com.example.firebaseproject.Model.Car;
import com.example.firebaseproject.Model.Key;
import com.example.firebaseproject.Model.KeyList;
import com.example.firebaseproject.R;
import com.example.firebaseproject.Service.Implementation.KeyServiceImplementation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KeyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEYLIST = "keylist";
    private static final String CAR_ID = "car_id";

    KeyList adapter;

    // TODO: Rename and change types of parameters
    private List<Key> keyListArgument;
    private int carIdArgument;

    private ProgressBar progressBar;

    ListView listViewKeys;
    List<Key> keyList;
    List<Key> filteredList;
    List<Car> carList;
    List<Key> filteredByCar;

    ImageView imageViewSearchKeys;
    public ImageView imageViewChooseCar;

    OnKeyFragmentInteraction onKeyFragmentInteraction;

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

    public KeyFragment() {
        // Required empty public constructor
    }

    public static KeyFragment keyFragmentInstance(List<Key> keyList) {
        KeyFragment fragment = new KeyFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEYLIST, (Serializable) keyList);
        fragment.setArguments(args);
        return fragment;
    }

    public static KeyFragment keyFragmentInstance(Integer carId) {
        KeyFragment fragment = new KeyFragment();
        Bundle args = new Bundle();
        args.putInt(CAR_ID, carId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            keyListArgument = (List<Key>) getArguments().getSerializable(KEYLIST);
            carIdArgument = getArguments().getInt(CAR_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }
        View view = inflater.inflate(R.layout.fragment_key, container, false);
        listViewKeys = view.findViewById(R.id.listViewKeys);
        listViewKeys.setAdapter(adapter);
        databaseKeys = FirebaseDatabase.getInstance().getReference("keys");

        listViewKeys = view.findViewById(R.id.listViewKeys);
        keyList = new ArrayList<>();
        carList = new ArrayList<>();
        filteredByCar = new ArrayList<>();
        filteredList = new ArrayList<>();
        spinnerKeys = view.findViewById(R.id.spinnerKeys);
        editTextSearchByName = view.findViewById(R.id.editTextSearchByName);
        imageViewSearchKeys = view.findViewById(R.id.imageViewSearchKeys);
        imageViewChooseCar = view.findViewById(R.id.imageViewChooseCar);
        spinnerCarBrand = view.findViewById(R.id.spinnerCarBrand);
        spinnerCarModel = view.findViewById(R.id.spinnerCarModel);
        spinnerCarYear = view.findViewById(R.id.spinnerCarYear);
        container_keyFragment = view.findViewById(R.id.container_keyFragment);
        container_carFragment = view.findViewById(R.id.container_carFragment);
        textViewKeys = view.findViewById(R.id.textViewKeys);
        searchedValue = String.valueOf(editTextSearchByName.getText());
        keyServiceImpl = new KeyServiceImplementation();
        progressBar = view.findViewById(R.id.progressBar1);

        listViewKeys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Key key = (Key) listViewKeys.getItemAtPosition(position);
                itemClick(key);
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnKeyFragmentInteraction){
            onKeyFragmentInteraction = (OnKeyFragmentInteraction) context;
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        onKeyFragmentInteraction = null;

    }
    @Override
    public void onStart() {
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
                if (getArguments() != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    List<Key> filteredById = keyList.stream()
                            .filter(key -> key.getCar_id() == carIdArgument)
                            .collect(Collectors.toList());
                    adapter = new KeyList(getActivity(), filteredById);
                    if (!adapter.isEmpty())
                        progressBar.setVisibility(View.GONE);
                    listViewKeys.setAdapter(adapter);
                }else {
                    adapter = new KeyList(getActivity(), keyList);
                    if (!adapter.isEmpty())
                        progressBar.setVisibility(View.GONE);
                    listViewKeys.setAdapter(adapter);
                }

                imageViewSearchKeys.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchedValue = String.valueOf(editTextSearchByName.getText());
                        filteredList = keyList.stream()
                                .filter(key -> key.getName().toLowerCase().contains(searchedValue.toLowerCase()))
                                .collect(Collectors.toList());

                        spinnerKeys.setSelection(0);
                        progressBar.setVisibility(View.VISIBLE);
                        adapter = new KeyList(getActivity(), filteredList);
                        if (!adapter.isEmpty())
                            progressBar.setVisibility(View.GONE);
                        listViewKeys.setAdapter(adapter);
                    }
                });

                spinnerKeys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 1) {
                            progressBar.setVisibility(View.VISIBLE);
                            adapter = new KeyList(getActivity(), keyServiceImpl.filterKeyByNameAsc(keyList));
                            if (!adapter.isEmpty())
                                progressBar.setVisibility(View.GONE);
                            listViewKeys.setAdapter(adapter);
                        }
                        else if (position == 2) {
                            progressBar.setVisibility(View.VISIBLE);
                            adapter = new KeyList(getActivity(), keyServiceImpl.filterKeyByNameDesc(keyList));
                            if (!adapter.isEmpty())
                                progressBar.setVisibility(View.GONE);
                            listViewKeys.setAdapter(adapter);
                        }
                        else if (position == 3)
                        {
                            progressBar.setVisibility(View.VISIBLE);
                            adapter = new KeyList(getActivity(), keyServiceImpl.filterAvailableKeys(keyList));
                            if (!adapter.isEmpty())
                                progressBar.setVisibility(View.GONE);
                            listViewKeys.setAdapter(adapter);
                        }
                        else if (position == 4) {
                            progressBar.setVisibility(View.VISIBLE);
                            adapter = new KeyList(getActivity(), keyServiceImpl.filterNotAvailableKeys(keyList));
                            if (!adapter.isEmpty())
                                progressBar.setVisibility(View.GONE);
                            listViewKeys.setAdapter(adapter);
                        }
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

    public interface OnKeyFragmentInteraction{
        void openCarFragment();
        void itemClick(Key key);
    }
    public void openCarFragment(){
        if (onKeyFragmentInteraction != null)
            onKeyFragmentInteraction.openCarFragment();
    }
    public void itemClick(Key key){
        if (onKeyFragmentInteraction != null){
            onKeyFragmentInteraction.itemClick(key);
        }
    }

}