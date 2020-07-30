package com.example.firebaseproject.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseproject.Model.Car;
import com.example.firebaseproject.R;
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
 * Use the {@link SelectCarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectCarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private CarFragmentInteraction carFragmentInteraction;

    DatabaseReference databaseCars;
    Spinner spinnerCarBrand;
    Spinner spinnerCarModel;
    Spinner spinnerCarYear;
    Button buttonFilterByCar;

    TextView textViewTest;

    List<Car> carList;
    List<String> sortedBrands;
    List<String> carBrands;
    List<String> carModels;
    List<String> carYears;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelectCarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectCarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectCarFragment newInstance(String param1, String param2) {
        SelectCarFragment fragment = new SelectCarFragment();
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
        if (container != null) {
            container.removeAllViews();
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select_car, container, false);

        databaseCars = FirebaseDatabase.getInstance().getReference("cars");
        spinnerCarBrand = view.findViewById(R.id.spinnerCarBrand);
        spinnerCarModel = view.findViewById(R.id.spinnerCarModel);
        spinnerCarYear = view.findViewById(R.id.spinnerCarYear);
        buttonFilterByCar = view.findViewById(R.id.buttonFilterByCar);

        carList = new ArrayList<>();
        carBrands = new ArrayList<>();
        carModels = new ArrayList<>();
        carYears = new ArrayList<>();
        sortedBrands = new ArrayList<>();

        sortedBrands.add("Brand");
        carModels.add("Model");
        carYears.add("Year");

        return view;
    }
    public interface CarFragmentInteraction{
        void filteredData(String id);
    }

    public void filteredData(String id){
        if (carFragmentInteraction != null){
            carFragmentInteraction.filteredData(id);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CarFragmentInteraction){
            carFragmentInteraction = (CarFragmentInteraction) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        carFragmentInteraction = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseCars.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                carList.clear();

                for (DataSnapshot item : dataSnapshot.getChildren()){

                    Car car = item.getValue(Car.class);
                    carList.add(car);

                    carBrands.add(car.getCar_brand());
                }

                sortedBrands.addAll(carBrands.stream().sorted().distinct().collect(Collectors.toList()));

                spinnerCarBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position > 0) {

                            String brand = String.valueOf(spinnerCarBrand.getSelectedItem());

                            carModels.removeIf(s -> !s.equals("Model"));

                            for (Car car : carList) {
                                if (car.getCar_brand().toLowerCase().equals(brand.toLowerCase())
                                        && !carModels.contains(car.getCar_model())) {
                                    carModels.add(car.getCar_model());
                                }
                            }
                        }else {
                            carModels.removeIf(s -> !s.equals("Model"));
                        }

                        final ArrayAdapter<String> spinnerModelsAdapter = new ArrayAdapter<String>(
                                getContext(), R.layout.spinner_models, (List<String>) new ArrayList<>(carModels));

                        spinnerModelsAdapter.setDropDownViewResource(R.layout.spinner_models);
                        spinnerCarModel.setAdapter(spinnerModelsAdapter);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                spinnerCarModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if (position > 0){

                            carYears.removeIf(s -> !s.equals("Year"));

                            String brand = String.valueOf(spinnerCarBrand.getSelectedItem());
                            String model = String.valueOf(spinnerCarModel.getSelectedItem());

                            for (Car car : carList) {
                                if (car.getCar_brand().equals(brand) && car.getCar_model().equals(model)){
                                    carYears.add(String.valueOf(car.getYear()));
                                }
                            }

                        }else {
                            carYears.removeIf(s -> !s.equals("Year"));
                        }

                        final ArrayAdapter<String> spinnerYearsAdapter = new ArrayAdapter<String>(
                                getContext(), R.layout.spinner_years, (List<String>) new ArrayList<>(carYears));

                        spinnerYearsAdapter.setDropDownViewResource(R.layout.spinner_years);
                        spinnerCarYear.setAdapter(spinnerYearsAdapter);

                        buttonFilterByCar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String brand = String.valueOf(spinnerCarBrand.getSelectedItem());
                                String model = String.valueOf(spinnerCarModel.getSelectedItem());
                                String year = String.valueOf(spinnerCarYear.getSelectedItem());
                                Car car;

                                if (brand.equals("Brand") || model.equals("Model") || year.equals("Year"))
                                    Toast.makeText(getContext(),"All fields are required!", Toast.LENGTH_SHORT).show();

                                if (!year.equals("Year")) {
                                    car = carList.stream()
                                            .filter(car1 -> car1.getCar_brand().equals(brand)
                                                    && car1.getCar_model().equals(model)
                                                    && car1.getYear() == Integer.parseInt(year))
                                            .findFirst().orElse(null);
                                    if (car != null) {
                                        String id = Integer.toString(car.getCar_id());
                                        filteredData(id);
                                    }
                                }

                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                // Initializing an ArrayAdapter
                final ArrayAdapter<String> spinnerBrandsAdapter = new ArrayAdapter<String>(
                        getContext(), R.layout.spinner_brands, (List<String>) new ArrayList<>(sortedBrands));

                spinnerBrandsAdapter.setDropDownViewResource(R.layout.spinner_brands);
                spinnerCarBrand.setAdapter(spinnerBrandsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}