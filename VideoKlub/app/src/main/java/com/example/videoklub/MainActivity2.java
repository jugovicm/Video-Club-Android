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
import com.example.videoklub.model.Actor;
import com.example.videoklub.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    Spinner spnLista;
    Spinner spnLista2;
    Button btnIzmeni;
    Button btnObrisi;
    Button btnDodaj;
    Button btnPronadji;
    EditText etIme;
    EditText etDatum;
    DatabaseHelper databaseHelper;
    Button btnVideoKlub;
    Button btnOsvezi;

    protected void onStop() {
        super.onStop();
        databaseHelper.closeDB();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        spnLista = (Spinner) findViewById(R.id.spnLista);
        spnLista2 = (Spinner) findViewById(R.id.spnLista2);
        btnIzmeni = (Button) findViewById(R.id.btnIzmeni);
        btnObrisi = (Button) findViewById(R.id.btnObrisi);
        btnDodaj = (Button) findViewById(R.id.btnDodaj);
        btnVideoKlub = (Button) findViewById(R.id.btnVideoKlub);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        etIme = (EditText) findViewById(R.id.etIme2);
        etDatum = (EditText) findViewById(R.id.etDatum2);
        btnPronadji = (Button) findViewById(R.id.btnPronadji);
        btnOsvezi = (Button) findViewById(R.id.btnOsvezi);

        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Actor a1 = new Actor(etIme.getText().toString(), etDatum.getText().toString());
                databaseHelper.createActor(a1);
                List<Actor> la = databaseHelper.getAllActors();
                loadSpinnerDataActors((ArrayList<Actor>) la);
            }
        });

        btnObrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String actor =  spnLista.getSelectedItem().toString();
                List<Actor> a =  databaseHelper.findActors(actor);
                databaseHelper.deleteActor(a.get(0));
                List<Actor> la = databaseHelper.getAllActors();
                loadSpinnerDataActors((ArrayList<Actor>) la);
            }
        });

        btnIzmeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String actor =  spnLista.getSelectedItem().toString();
                List<Actor> a =  databaseHelper.findActors(actor);
                databaseHelper.updateActor(a.get(0), etIme.getText().toString(), etDatum.getText().toString());
                List<Actor> la = databaseHelper.getAllActors();
                loadSpinnerDataActors((ArrayList<Actor>) la);
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
                List<Actor> la = databaseHelper.getAllActorsInMovie(spnLista2.getSelectedItem().toString());
                loadSpinnerDataActors((ArrayList<Actor>) la);
            }
        });

        btnOsvezi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //databaseHelper = new DatabaseHelper(getApplicationContext());
                databaseHelper.createTables();
                List<Actor> la = databaseHelper.getAllActors();
                loadSpinnerDataActors((ArrayList<Actor>) la);
                List<Movie> mv = databaseHelper.getAllMovies();
                loadSpinnerDataMovies((ArrayList<Movie>) mv);
            }
        });

        List<Actor> la = databaseHelper.getAllActors();
        loadSpinnerDataActors((ArrayList<Actor>) la);
        List<Movie> mv = databaseHelper.getAllMovies();
        loadSpinnerDataMovies((ArrayList<Movie>) mv);
    }
    void loadSpinnerDataActors (ArrayList<Actor> al){
        ArrayList<String> actornames = new ArrayList<>();
        for (Actor actor : al){
            actornames.add(actor.getName());
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, actornames);

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