package com.example.firebaseproject.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebaseproject.Activities.KeysActivity;
import com.example.firebaseproject.Activities.MainActivity;
import com.example.firebaseproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private ImageView imageViewKeys;
    private ImageView imageViewAccessoies;
    private TextView textViewKeys;
    private TextView textViewAccessories;
    private TextView textViewWelcome;
    private ImageView imageViewKey;
    private OnHomeFragmentInteraction onHomeFragmentInteraction;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    private void RunAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.text_animation);
        a.reset();
        imageViewKey.clearAnimation();
        imageViewKey.startAnimation(a);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        imageViewKeys = view.findViewById(R.id.imageViewKeys);
        imageViewAccessoies = view.findViewById(R.id.imageViewAccessories);
        textViewKeys = view.findViewById(R.id.textViewKeys);
        textViewAccessories = view.findViewById(R.id.textViewAccessories);
        textViewWelcome = view.findViewById(R.id.textViewKeyStore);
        imageViewKey = view.findViewById(R.id.imageViewKey);

        imageViewKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKeyFragment();
            }
        });

        imageViewAccessoies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccessoriesFragment();;
            }
        });

        textViewKeys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKeyFragment();;
            }
        });

        textViewAccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccessoriesFragment();
            }
        });
        RunAnimation();
        return view;
    }

    public interface OnHomeFragmentInteraction {
        void openKeyFragment();
        void openAccessoriesFragment();
    }

    public void openKeyFragment(){
        if (onHomeFragmentInteraction != null){
            onHomeFragmentInteraction.openKeyFragment();
        }
    }
    public void openAccessoriesFragment(){
        if (onHomeFragmentInteraction != null){
            onHomeFragmentInteraction.openAccessoriesFragment();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeFragmentInteraction){
            onHomeFragmentInteraction = (OnHomeFragmentInteraction) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onHomeFragmentInteraction = null;
    }

}