package com.example.firebaseproject.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import com.example.firebaseproject.R;

public class ItemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ITEM_NAME = "name";
    private static final String ITEM_PRICE = "price";
    private static final String IMAGE_URL = "url";
    private static final String ITEM_DESCRIPTION = "description";
    private static final String ITEM_ONSTOCK = "onStock";

    private ImageView imageViewRateIcon;

    // TODO: Rename and change types of parameters

    private String item_name;
    private String item_description;
    private String item_imgUrl;
    private int item_price;
    private long item_onStock;

    private ImageView imageViewItem;
    private TextView textViewItemName;
    private TextView textViewItemDescription;
    private TextView textViewItemOnStock;
    private TextView textViewItemPrice;



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

        imageViewRateIcon = view.findViewById(R.id.imageViewRateIcon);
        imageViewItem = view.findViewById(R.id.imageViewItem);
        textViewItemDescription = view.findViewById(R.id.textViewItemDescription);
        textViewItemName = view.findViewById(R.id.textViewItemName);
        textViewItemOnStock = view.findViewById(R.id.textViewOnStockItem);
        textViewItemPrice = view.findViewById(R.id.textViewItemPrice);

        if (getArguments() != null){
            textViewItemName.setText("Name: " + item_name);
            textViewItemDescription.setText(item_description);
            textViewItemPrice.setText(String.valueOf(item_price) + " ден.");
            textViewItemOnStock.setText(item_onStock == 1 ? "On stock: Available" : "On stock: Not available");
            textViewItemOnStock.setTextColor(item_onStock == 1 ? Color.GREEN : Color.RED);
            Picasso.get().load(item_imgUrl).into(imageViewItem);
        }

        return view;
    }
}