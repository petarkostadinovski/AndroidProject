package com.example.firebaseproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.firebaseproject.Activities.LoginActivity;
import com.example.firebaseproject.Activities.MainActivity;
import com.example.firebaseproject.Activities.RegistrationActivity;
import com.example.firebaseproject.Model.Key;
import com.example.firebaseproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

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

    private OnProfileFragmentInteraction onProfileFragmentInteraction;

    private DatabaseReference dataBaseUserProduct;
    private List<Key> productList;
    private TextView textViewUserEmail;
    private Button buttonLogout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        FirebaseUser user = firebaseAuth.getCurrentUser();
        listViewUserProducts = view.findViewById(R.id.listViewUsersProducts);

        if (user == null) {
            startActivity(new Intent(getContext(), RegistrationActivity.class));
            return null;
        }else{
            textViewUserEmail = view.findViewById(R.id.textViewUserEmail);
            textViewUserEmail.setText(user.getEmail());
            buttonLogout = view.findViewById(R.id.buttonLogout);

            dataBaseUserProduct = FirebaseDatabase.getInstance().getReference(user.getUid());

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

        dataBaseUserProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
    }

    public void openHomeFragment(){
        if (onProfileFragmentInteraction != null){
            onProfileFragmentInteraction.openHomeFrgment();
        }
    }



}