package com.example.videoklub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.videoklub.helper.DatabaseHelper;
import com.example.videoklub.model.Director;
import com.example.videoklub.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {

    Spinner spnLista;
    Spinner spnLista2;
    Button btnIzmeni;
    Button btnObrisi;
    Button btnDodaj;
    EditText etIme;
    EditText etDatum;
    DatabaseHelper databaseHelper;
    Button btnVideoKlub;
    Button btnPronadji;
    Button btnOsvezi;
    protected void onStop() {
        super.onStop();
        databaseHelper.closeDB();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        btnOsvezi = (Button) findViewById(R.id.btnOsvezi);

        spnLista = (Spinner) findViewById(R.id.spnLista);
        spnLista2 = (Spinner) findViewById(R.id.spnLista2);

        btnIzmeni = (Button) findViewById(R.id.btnIzmeni);
        btnObrisi = (Button) findViewById(R.id.btnObrisi);
        btnDodaj = (Button) findViewById(R.id.btnDodaj);
        btnVideoKlub = (Button) findViewById(R.id.btnVideoKlub);
        btnPronadji = (Button) findViewById(R.id.btnPronadji);


        databaseHelper = new DatabaseHelper(getApplicationContext());
        etIme = (EditText) findViewById(R.id.etIme2);
        etDatum = (EditText) findViewById(R.id.etDatum2);

        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Director d1 = new Director(etIme.getText().toString(), etDatum.getText().toString());
                databaseHelper.createDirector(d1);
                List<Director> ld = databaseHelper.getAllDirectors();
                loadSpinnerDataDirectors((ArrayList<Director>) ld);
            }
        });

        btnObrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String director =  spnLista.getSelectedItem().toString();
                List<Director> d =  databaseHelper.findDirectors(director);
                databaseHelper.deleteDirector(d.get(0));
                List<Director> ld = databaseHelper.getAllDirectors();
                loadSpinnerDataDirectors((ArrayList<Director>) ld);
            }
        });

        btnIzmeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String director =  spnLista.getSelectedItem().toString();
                List<Director> d =  databaseHelper.findDirectors(director);
                databaseHelper.updateDirector(d.get(0), etIme.getText().toString(), etDatum.getText().toString());
                List<Director> ld = databaseHelper.getAllDirectors();
                loadSpinnerDataDirectors((ArrayList<Director>) ld);
            }
        });

        btnVideoKlub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnPronadji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String movie =  spnLista2.getSelectedItem().toString();
                // List<Movie> m =  databaseHelper.findMovies(movie);
                List<Director> ld  = databaseHelper.getDirectorMovie(spnLista2.getSelectedItem().toString());
                loadSpinnerDataDirectors((ArrayList<Director>) ld);
            }
        });

        btnOsvezi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.createTables();
                List<Director> ld = databaseHelper.getAllDirectors();
                loadSpinnerDataDirectors((ArrayList<Director>) ld);
                List<Movie> mv = databaseHelper.getAllMovies();
                loadSpinnerDataMovies((ArrayList<Movie>) mv);
            }
        });
        List<Director> ld = databaseHelper.getAllDirectors();
        loadSpinnerDataDirectors((ArrayList<Director>) ld);
        List<Movie> mv = databaseHelper.getAllMovies();
        loadSpinnerDataMovies((ArrayList<Movie>) mv);
    }
    void loadSpinnerDataDirectors (ArrayList<Director> al){
        ArrayList<String> directornames = new ArrayList<>();
        for (Director director : al){
            directornames.add(director.getName());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, directornames);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spnLista.setAdapter(dataAdapter);
    }
    void loadSpinnerDataMovies (ArrayList<Movie> al){
        ArrayList<String> movienames = new ArrayList<>();
        for (Movie movie : al){
            movienames.add(movie.getName());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, movienames);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spnLista2.setAdapter(dataAdapter);
    }
}