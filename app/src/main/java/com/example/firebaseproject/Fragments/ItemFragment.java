package com.example.firebaseproject.Fragments;

import android.graphics.Color;
import android.media.Rating;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseproject.Model.Key;
import com.example.firebaseproject.Model.ProductRating;
import com.example.firebaseproject.Model.UserProduct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import com.example.firebaseproject.R;

import java.util.ArrayList;
import java.util.List;

public class ItemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ITEM_NAME = "name";
    private static final String ITEM_PRICE = "price";
    private static final String IMAGE_URL = "url";
    private static final String ITEM_DESCRIPTION = "description";
    private static final String ITEM_ONSTOCK = "onStock";

    private RatingBar ratingBar;
    private boolean added = false;

    // TODO: Rename and change types of parameters

    private String item_name;
    private String item_description;
    private String item_imgUrl;
    private int item_price;
    private long item_onStock;

    private ImageView imageViewItem;
    private TextView textViewItemName;
    private TextView textViewRemoveRating;
    private TextView textViewItemDescription;
    private TextView textViewItemOnStock;
    private TextView textViewItemPrice;
    private TextView textViewAverageRating;
    private TextView textViewTotalRatings;
    private Button btnAdd;

    List<ProductRating> itemRatings;

    private int rating = 0;
    private double sumRatings = 0;
    private int numRatings = 0;

    private DatabaseReference databaseUsers_id;
    private DatabaseReference dataBaseRatings;
    private DatabaseReference dataBaseUserProduct;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    public ItemFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ItemFragment itemFragmentInstance(String name, String description, String imageUrl, int price, long onStock) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(ITEM_NAME, name);
        args.putString(ITEM_DESCRIPTION, description);
        args.putString(IMAGE_URL, imageUrl);
        args.putInt(ITEM_PRICE, price);
        args.putLong(ITEM_ONSTOCK, onStock);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item_name = getArguments().getString(ITEM_NAME);
            item_description = getArguments().getString(ITEM_DESCRIPTION);
            item_imgUrl = getArguments().getString(IMAGE_URL);
            item_price = getArguments().getInt(ITEM_PRICE);
            item_onStock = getArguments().getLong(ITEM_ONSTOCK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        imageViewItem = view.findViewById(R.id.imageViewItem);
        textViewItemDescription = view.findViewById(R.id.textViewItemDescription);
        textViewItemName = view.findViewById(R.id.textViewItemName);
        textViewItemOnStock = view.findViewById(R.id.textViewOnStockItem);
        textViewItemPrice = view.findViewById(R.id.textViewItemPrice);
        textViewAverageRating = view.findViewById(R.id.textViewAverageRating);
        textViewTotalRatings = view.findViewById(R.id.textViewTotalRatings);
        textViewRemoveRating = view.findViewById(R.id.textViewRemoveRating);
        btnAdd = view.findViewById(R.id.btnAddItem);
        ratingBar = view.findViewById(R.id.ratingBar);
        itemRatings = new ArrayList<>();
        firebaseAuth = firebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        textViewRemoveRating.setVisibility(View.GONE);

        databaseUsers_id = FirebaseDatabase.getInstance().getReference("product_rating");
        dataBaseUserProduct = FirebaseDatabase.getInstance().getReference("user_product");

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (user != null){
                    if (rating != 0){
                        addRating(rating);
                        Toast.makeText(getContext(), "Product " + item_name + " successfully rated (" + rating + ").", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getContext(),"Please log in!",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null)
                    addButtonClick();
                else {
                    Toast.makeText(getContext(),"Please log in!",Toast.LENGTH_LONG).show();
                }
            }
        });

        if (getArguments() != null){
            textViewItemName.setText("Name: " + item_name);
            textViewItemDescription.setText(item_description);
            textViewItemPrice.setText(item_price + " ден.");
            textViewItemOnStock.setText(item_onStock == 1 ? "On stock: Available" : "On stock: Not available");
            textViewItemOnStock.setTextColor(item_onStock == 1 ? Color.GREEN : Color.RED);
            Picasso.get().load(item_imgUrl).into(imageViewItem);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseUsers_id.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemRatings.clear();

                for (DataSnapshot item : dataSnapshot.getChildren()){

                    if (item.hasChild(item_name)) {
                        ProductRating rating = item.child(item_name).getValue(ProductRating.class);
                        itemRatings.add(rating);
                    }

                }
                sumRatings = itemRatings.stream().mapToDouble(ProductRating::getRating).sum();
                numRatings = itemRatings.size();
                textViewAverageRating.setText(numRatings == 0 ? "Average rating: 0.00" : String.format("Average rating: %.2f", sumRatings / numRatings));
                textViewTotalRatings.setText(String.format("Total ratings: %d", numRatings));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (user != null) {
            dataBaseUserProduct.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user.getUid()) && dataSnapshot.child(user.getUid()).hasChild(item_name)){
                        added = true;
                        btnAdd.setText("remove from your products");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            databaseUsers_id.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(user.getUid()) && dataSnapshot.child(user.getUid()).hasChild(item_name)){
                        ProductRating rating = dataSnapshot.child(user.getUid()).child(item_name).getValue(ProductRating.class);
                        ratingBar.setRating(rating.getRating());
                        textViewRemoveRating.setVisibility(View.VISIBLE);
                    }

                    textViewRemoveRating.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeRating();
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void addRating(float rating){
        String key_id = item_name;
        String user_id = user.getUid();
        UserProduct product = new UserProduct(item_name, item_description, item_imgUrl, item_onStock, item_price);
        ProductRating productRating = new ProductRating(product, rating);
        databaseUsers_id.child(user_id).child(key_id).setValue(productRating);
    }

    public void addButtonClick(){
        if (!added) {
            String user_id = user.getUid();
            UserProduct userProduct = new UserProduct(item_name, item_description, item_imgUrl, item_onStock, item_price);
            dataBaseUserProduct.child(user_id).child(item_name).setValue(userProduct);
            Toast.makeText(getContext(), "Product " + item_name + " successfully added.", Toast.LENGTH_LONG).show();
            btnAdd.setText("remove from your products");
            added = true;
        }else{
            String user_id = user.getUid();
            UserProduct userProduct = new UserProduct(item_name, item_description, item_imgUrl, item_onStock, item_price);
            dataBaseUserProduct.child(user_id).child(item_name).removeValue();
            Toast.makeText(getContext(), "Product " + item_name + " successfully removed.", Toast.LENGTH_LONG).show();
            added = false;
            btnAdd.setText("add to your products");
        }
    }

    public void removeRating(){
        Toast.makeText(getContext(), "Rating on product " + item_name + " successfully removed.", Toast.LENGTH_LONG).show();
        databaseUsers_id.child(user.getUid()).child(item_name).removeValue();
        ratingBar.setRating(0);
        textViewRemoveRating.setVisibility(View.GONE);
    }

}