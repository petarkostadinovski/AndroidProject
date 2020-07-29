package com.example.firebaseproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.firebaseproject.Activities.LoginActivity;
import com.example.firebaseproject.Activities.MainActivity;
import com.example.firebaseproject.Activities.RegistrationActivity;
import com.example.firebaseproject.Model.Key;
import com.example.firebaseproject.Model.KeyList;
import com.example.firebaseproject.Model.ProductList;
import com.example.firebaseproject.Model.UserProduct;
import com.example.firebaseproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseAuth firebaseAuth;
    private ListView listViewUserProducts;
    private ProductList adapter;
    private Spinner spinnerProducts;
    private OnProfileFragmentInteraction onProfileFragmentInteraction;
    private ProgressBar progressBarProfile;
    private DatabaseReference dataBaseUserProduct;
    private DatabaseReference dataBaseUserRatings;
    private List<UserProduct> productList;
    private List<UserProduct> ratedProducts;
    private List<String> ratedProductsNames;
    private TextView textViewUserEmail;
    private Button buttonLogout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseUser user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile2, container, false);
        firebaseAuth = firebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        productList = new ArrayList<>();
        listViewUserProducts = view.findViewById(R.id.listViewUserProducts);
        spinnerProducts = view.findViewById(R.id.spinnerProducts);
        ratedProductsNames = new ArrayList<>();
        ratedProducts = new ArrayList<>();
        progressBarProfile = view.findViewById(R.id.progressBarProfile);

        if (user == null) {
            startActivity(new Intent(getContext(), RegistrationActivity.class));
            return null;
        }else{
            textViewUserEmail = view.findViewById(R.id.textViewUserEmail);
            textViewUserEmail.setText(user.getEmail());
            buttonLogout = view.findViewById(R.id.buttonLogout);

            listViewUserProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    UserProduct product = (UserProduct) listViewUserProducts.getItemAtPosition(position);
                    itemClickProfileFragment(product);
                }
            });

            buttonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.signOut();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            });

            return view;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (user != null) {

            dataBaseUserProduct = FirebaseDatabase.getInstance().getReference("user_product").child(user.getUid());
            dataBaseUserRatings = FirebaseDatabase.getInstance().getReference("product_rating").child(user.getUid());

            dataBaseUserProduct.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    productList.clear();

                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        UserProduct product = item.getValue(UserProduct.class);
                        productList.add(product);
                    }
                    progressBarProfile.setVisibility(View.VISIBLE);
                    adapter = new ProductList(getActivity(), productList);
                    if (!adapter.isEmpty())
                        progressBarProfile.setVisibility(View.GONE);
                    listViewUserProducts.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            dataBaseUserRatings.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ratedProductsNames.clear();

                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        String name = item.getKey();
                        ratedProductsNames.add(name);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            spinnerProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0){
                        adapter = new ProductList(getActivity(), productList);
                        listViewUserProducts.setAdapter(adapter);
                    }else{
                        for (String name : ratedProductsNames){
                            for (UserProduct product : productList){
                                if (product.getName().equals(name))
                                    ratedProducts.add(product);
                            }
                        }
                        adapter = new ProductList(getActivity(), ratedProducts);
                        listViewUserProducts.setAdapter(adapter);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnProfileFragmentInteraction){
            onProfileFragmentInteraction = (OnProfileFragmentInteraction) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onProfileFragmentInteraction = null;
    }

    public interface OnProfileFragmentInteraction{
        void openHomeFrgment();
        void itemClickProfileFragment(UserProduct product);
    }

    public void openHomeFragment(){
        if (onProfileFragmentInteraction != null){
            onProfileFragmentInteraction.openHomeFrgment();
        }
    }
    public void itemClickProfileFragment(UserProduct userProduct){
        if (onProfileFragmentInteraction != null){
            onProfileFragmentInteraction.itemClickProfileFragment(userProduct);
        }
    }
}