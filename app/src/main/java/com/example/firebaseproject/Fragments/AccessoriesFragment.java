package com.example.firebaseproject.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.security.KeyChain;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.firebaseproject.Model.AccessoriesList;
import com.example.firebaseproject.Model.Car;
import com.example.firebaseproject.Model.Key;
import com.example.firebaseproject.Model.KeyList;
import com.example.firebaseproject.Model.Keychain;
import com.example.firebaseproject.R;
import com.example.firebaseproject.Service.Implementation.AccessoriesServiceImplementation;
import com.example.firebaseproject.Service.Implementation.KeyServiceImplementation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccessoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccessoriesFragment extends Fragment {

    AccessoriesList adapter;

    // TODO: Rename and change types of parameters
    private List<Key> keyListArgument;
    private int carIdArgument;

    private OnAccessoriesFragmentInteraction onAccessoriesFragmentInteraction;
    private ProgressBar progressBar;

    ListView listViewAccessories;
    List<Keychain> keychainList;
    List<Keychain> filteredList;

    ImageView imageViewSearchKeys;
    public ImageView imageViewChooseCar;

    KeyFragment.OnKeyFragmentInteraction onKeyFragmentInteraction;

    TextView textViewKeys;

    KeyFragment keyFragment;
    SelectCarFragment selectCarFragment;

    String searchedValue;

    EditText editTextSearchByName;

    FrameLayout container_keyFragment;
    FrameLayout container_carFragment;

    Spinner spinnerAccessories;

    DatabaseReference databaseKeychains;

    AccessoriesServiceImplementation accessoriesServiceImpl;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccessoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccessoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccessoriesFragment newInstance(String param1, String param2) {
        AccessoriesFragment fragment = new AccessoriesFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accessories, container, false);
        listViewAccessories = view.findViewById(R.id.listViewAccessories);
        listViewAccessories.setAdapter(adapter);
        databaseKeychains = FirebaseDatabase.getInstance().getReference("keychains");

        listViewAccessories = view.findViewById(R.id.listViewAccessories);
        keychainList = new ArrayList<>();
        filteredList = new ArrayList<>();
        spinnerAccessories = view.findViewById(R.id.spinnerAccessories);
        editTextSearchByName = view.findViewById(R.id.editTextSearchByName);
        imageViewSearchKeys = view.findViewById(R.id.imageViewSearchKeys);
        imageViewChooseCar = view.findViewById(R.id.imageViewChooseCar);
        container_keyFragment = view.findViewById(R.id.container_keyFragment);
        container_carFragment = view.findViewById(R.id.container_carFragment);
        textViewKeys = view.findViewById(R.id.textViewKeys);
        searchedValue = String.valueOf(editTextSearchByName.getText());
        accessoriesServiceImpl = new AccessoriesServiceImplementation();
        progressBar = view.findViewById(R.id.progressBar1);

        listViewAccessories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Keychain keychain = (Keychain) listViewAccessories.getItemAtPosition(position);
                itemClick(keychain);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseKeychains.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                keychainList.clear();

                for (DataSnapshot item : dataSnapshot.getChildren()){

                    Keychain keychain = item.getValue(Keychain.class);
                    keychainList.add(keychain);

                }
                if(getActivity() != null)
                    adapter = new AccessoriesList(getActivity(), keychainList);
                if (!adapter.isEmpty())
                    progressBar.setVisibility(View.GONE);
                listViewAccessories.setAdapter(adapter);


                imageViewSearchKeys.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchedValue = String.valueOf(editTextSearchByName.getText());
                        filteredList = keychainList.stream()
                                .filter(keychain -> keychain.getName().toLowerCase().contains(searchedValue.toLowerCase()))
                                .collect(Collectors.toList());

                        spinnerAccessories.setSelection(0);
                        progressBar.setVisibility(View.VISIBLE);
                        adapter = new AccessoriesList(getActivity(), filteredList);
                        if (!adapter.isEmpty())
                            progressBar.setVisibility(View.GONE);
                        listViewAccessories.setAdapter(adapter);
                    }
                });

                spinnerAccessories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 1) {
                            progressBar.setVisibility(View.VISIBLE);
                            adapter = new AccessoriesList(getActivity(), accessoriesServiceImpl.filterKeyByNameAsc(keychainList));
                            if (!adapter.isEmpty())
                                progressBar.setVisibility(View.GONE);
                            listViewAccessories.setAdapter(adapter);
                        }
                        else if (position == 2) {
                            progressBar.setVisibility(View.VISIBLE);
                            adapter = new AccessoriesList(getActivity(), accessoriesServiceImpl.filterKeyByNameDesc(keychainList));
                            if (!adapter.isEmpty())
                                progressBar.setVisibility(View.GONE);
                            listViewAccessories.setAdapter(adapter);
                        }
                        else if (position == 3)
                        {
                            progressBar.setVisibility(View.VISIBLE);
                            adapter = new AccessoriesList(getActivity(), accessoriesServiceImpl.filterAvailableKeys(keychainList));
                            if (!adapter.isEmpty())
                                progressBar.setVisibility(View.GONE);
                            listViewAccessories.setAdapter(adapter);
                        }
                        else if (position == 4) {
                            progressBar.setVisibility(View.VISIBLE);
                            adapter = new AccessoriesList(getActivity(), accessoriesServiceImpl.filterNotAvailableKeys(keychainList));
                            if (!adapter.isEmpty())
                                progressBar.setVisibility(View.GONE);
                            listViewAccessories.setAdapter(adapter);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAccessoriesFragmentInteraction){
            onAccessoriesFragmentInteraction = (OnAccessoriesFragmentInteraction) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onAccessoriesFragmentInteraction = null;
    }

    public interface OnAccessoriesFragmentInteraction{
        void itemClick(Keychain keychain);
    }
    public void itemClick(Keychain keychain){
        if (onAccessoriesFragmentInteraction != null){
            onAccessoriesFragmentInteraction.itemClick(keychain);
        }
    }

}