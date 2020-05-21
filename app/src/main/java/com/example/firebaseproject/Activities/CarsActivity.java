package com.example.firebaseproject.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.firebaseproject.Model.Car;
import com.example.firebaseproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class CarsActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);

        databaseCars = FirebaseDatabase.getInstance().getReference("cars");
        spinnerCarBrand = (Spinner)findViewById(R.id.spinnerCarBrand);
        spinnerCarModel = (Spinner)findViewById(R.id.spinnerCarModel);
        spinnerCarYear = (Spinner)findViewById(R.id.spinnerCarYear);
        buttonFilterByCar = (Button)findViewById(R.id.buttonFilterByCar);

        textViewTest = (TextView)findViewById(R.id.textViewTest);

        carList = new ArrayList<>();
        carBrands = new ArrayList<>();
        carModels = new ArrayList<>();
        carYears = new ArrayList<>();
        sortedBrands = new ArrayList<>();

        sortedBrands.add("Brand");
        carModels.add("Model");
        carYears.add("Year");

    }

    @Override
    protected void onStart() {
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
                                CarsActivity.this, R.layout.spinner_models, (List<String>) new ArrayList<>(carModels));

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
                                CarsActivity.this, R.layout.spinner_years, (List<String>) new ArrayList<>(carYears));

                        spinnerYearsAdapter.setDropDownViewResource(R.layout.spinner_years);
                        spinnerCarYear.setAdapter(spinnerYearsAdapter);

                        buttonFilterByCar.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(CarsActivity.this, KeysActivity.class);

                                String brand = String.valueOf(spinnerCarBrand.getSelectedItem());
                                String model = String.valueOf(spinnerCarModel.getSelectedItem());
                                String year = String.valueOf(spinnerCarYear.getSelectedItem());

                                Car car = carList.stream()
                                        .filter(car1 -> car1.getCar_brand().equals(brand))
                                        .findFirst().orElse(null);

                                intent.putExtra("id",Integer.toString(car.getCar_id()));

//                                textViewTest.setText(Integer.toString(car.getCar_id()));

                                startActivity(intent);
                            }
                        });


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                // Initializing an ArrayAdapter
                final ArrayAdapter<String> spinnerBrandsAdapter = new ArrayAdapter<String>(
                        CarsActivity.this,R.layout.spinner_brands, (List<String>) new ArrayList<>(sortedBrands));

                spinnerBrandsAdapter.setDropDownViewResource(R.layout.spinner_brands);
                spinnerCarBrand.setAdapter(spinnerBrandsAdapter);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
