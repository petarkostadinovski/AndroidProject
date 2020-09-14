package com.example.firebaseproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseproject.Activities.LoginActivity;
import com.example.firebaseproject.Activities.MainActivity;
import com.example.firebaseproject.Activities.RegistrationActivity;
import com.example.firebaseproject.Model.Key;
import com.example.firebaseproject.Model.KeyList;
import com.example.firebaseproject.Model.ProductList;
import com.example.firebaseproject.Model.ProductRating;
import com.example.firebaseproject.Model.UserProduct;
import com.example.firebaseproject.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_IMAGE = "image_bitmap";
    private static final String ARG_PARAM2 = "param2";

    private FirebaseAuth firebaseAuth;
    private ListView listViewUserProducts;
    private ProductList adapter;
    private Spinner spinnerProducts;
    private OnProfileFragmentInteraction onProfileFragmentInteraction;
    private ProgressBar progressBarProfile;
    private DatabaseReference dataBaseUserProduct;
    private DatabaseReference dataBaseUserRatings;
    private DatabaseReference dataBaseKeys;
    private List<UserProduct> productList;
    private List<Key> keyList;
    private List<UserProduct> ratedProducts;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private TextView textViewChangeProfilePicture;
    private ImageView imageViewGoogleIcon;
    GoogleSignInClient mGoogleSignInClient;
    // TODO: Rename and change types of parameters
    private Bitmap image_argument;
    private String mParam2;
    private boolean swapToProductList;
    private boolean ratedProductsIsEmpty;

    FirebaseUser user;


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstanceProfileFragmentImage(Bitmap bitmap) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle b = new Bundle();
        b.putParcelable(ARG_IMAGE, bitmap);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

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
        ratedProducts = new ArrayList<>();
        progressBarProfile = view.findViewById(R.id.progressBarProfile);
        swapToProductList = false;
        imageViewGoogleIcon = view.findViewById(R.id.imageViewGoogleIcon);
        imageViewGoogleIcon.setVisibility(View.GONE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        if (user == null) {
            startActivity(new Intent(getContext(), RegistrationActivity.class));
            return null;
        }else{
            textViewUserEmail = view.findViewById(R.id.textViewUserEmail);
            textViewUserEmail.setText(user.getEmail());
            buttonLogout = view.findViewById(R.id.buttonLogout);


            FirebaseUser userProfile = FirebaseAuth.getInstance().getCurrentUser();
            for (UserInfo profile : userProfile.getProviderData()) {
                String providerId = profile.getProviderId();
                if (providerId.equals(GoogleAuthProvider.PROVIDER_ID))
                    imageViewGoogleIcon.setVisibility(View.VISIBLE);
            }



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
                    Toast.makeText(getContext(),"Successfully signed out!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    mGoogleSignInClient.signOut();

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
                    if (getActivity() != null)
                        adapter = new ProductList(getActivity(), productList);
                    if (productList.isEmpty()) {
                        progressBarProfile.setVisibility(View.GONE);
                        Toast.makeText(getContext(),"Your product list is empty!", Toast.LENGTH_SHORT).show();
                        listViewUserProducts.setAdapter(null);
                    }
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
                    ratedProducts.clear();
                    ratedProductsIsEmpty = true;
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ProductRating product = item.getValue(ProductRating.class);
                        ratedProducts.add(product.getProduct());
                    }
                    if (!ratedProducts.isEmpty())
                        ratedProductsIsEmpty = false;
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
                        if (swapToProductList && productList.isEmpty()) {
                            Toast.makeText(getContext(), "Your product list is empty!", Toast.LENGTH_SHORT).show();
                            swapToProductList = false;
                            listViewUserProducts.setAdapter(null);
                        }
                        else
                            listViewUserProducts.setAdapter(adapter);

                    }else{
                        adapter = new ProductList(getActivity(), ratedProducts);
                        swapToProductList = true;
                        if(!ratedProducts.isEmpty())
                            listViewUserProducts.setAdapter(adapter);
                        else {
                            listViewUserProducts.setAdapter(null);
                            if (ratedProductsIsEmpty) {
                                Toast.makeText(getContext(), "Your rated product list is empty!", Toast.LENGTH_SHORT).show();
                            }
                        }
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
        void addProfilePicture();
    }

    public void openHomeFragment(){
        if (onProfileFragmentInteraction != null){
            onProfileFragmentInteraction.openHomeFrgment();
        }
    }
    public void addProfilePicture(){
        if (onProfileFragmentInteraction != null){
            onProfileFragmentInteraction.addProfilePicture();
        }
    }
    public void itemClickProfileFragment(UserProduct userProduct){
        if (onProfileFragmentInteraction != null){
            onProfileFragmentInteraction.itemClickProfileFragment(userProduct);
        }
    }
}