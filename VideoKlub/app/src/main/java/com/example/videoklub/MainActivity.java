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
import com.example.videoklub.model.Director;
import com.example.videoklub.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    Spinner spnActors;
    Spinner spnDirectors;
    Spinner spnMovies;
    Button btnPretraziFilm;
    Button btnPretraziGlumce;
    Button btnPretraziRezisere;
    EditText pretragaGlumac;
    EditText pretragaReziser;
    EditText pretragaFilm;
    Button btGlumciUredi;
    Button btReziseriUredi;
    Button btFilmoviUredi;
    Button btnOsvezi;

    @Override
    protected void onStop() {
        super.onStop();
        databaseHelper.closeDB();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spnActors = (Spinner) findViewById(R.id.spGlumci);
        spnDirectors = (Spinner) findViewById(R.id.spReziseri);
        spnMovies = (Spinner) findViewById(R.id.spFilmovi);
        btGlumciUredi = (Button) findViewById(R.id.btnGlumciUredi);
        btReziseriUredi = (Button) findViewById(R.id.btnReziseriUredi);
        btFilmoviUredi = (Button) findViewById(R.id.btnFilmUredi);

        btnPretraziGlumce = (Button) findViewById(R.id.btnGlumacPronadji) ;
        pretragaGlumac = (EditText) findViewById(R.id.etGlumac);

        btnPretraziRezisere = (Button) findViewById(R.id.btnReziserPronadji);
        pretragaReziser = (EditText) findViewById(R.id.etReziser);

        btnPretraziFilm = (Button) findViewById(R.id.btnFilmPronadji);
        pretragaFilm = (EditText) findViewById(R.id.etFilm);

        btnOsvezi = (Button) findViewById(R.id.btnPretraziSve);

        btnPretraziGlumce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strGlumac = pretragaGlumac.getText().toString();
                List<Actor> la = databaseHelper.findActors(strGlumac);
                loadSpinnerDataActors((ArrayList<Actor>) la);
            }
        });

        btnPretraziRezisere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strReziser = pretragaReziser.getText().toString();
                List<Director> ld = databaseHelper.findDirectors(strReziser);
                loadSpinnerDataDirectors((ArrayList<Director>) ld);
            }
        });

        btnPretraziFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strFilm = pretragaFilm.getText().toString();
                List<Movie> mv = databaseHelper.findMovies(strFilm);
                loadSpinnerDataMovies((ArrayList<Movie>) mv);
            }
        });

        btGlumciUredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
        });
        btReziseriUredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(intent2);
            }
        });

        btFilmoviUredi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MainActivity.this, MainActivity4.class);
                startActivity(intent3);
            }
        });

        btnOsvezi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //databaseHelper = new DatabaseHelper(getApplicationContext());
                createTablesAndInitData();
            }
        });

        databaseHelper = new DatabaseHelper(getApplicationContext());
        createTablesAndInitData();

    }
    void createTablesAndInitData(){
        databaseHelper.createTables();

        if (databaseHelper.getAllActors().size() == 0) {
            Actor a1 = new Actor("Brad Pitt", "18-12-1963");
            Actor a2 = new Actor("Edward Norton", "18-08-1969");
            Actor a3 = new Actor("Samuel L. Jackson", "21-12-1948");
            Actor a4 = new Actor("Bruce Willis", "19-03-1955");
            Actor a5 = new Actor("Leonardo DiCaprio", "11-11-1974");
            Actor a6 = new Actor("Matt Damon", "08-10-1970");
            Director d1 = new Director("Quentin Tarantino", "27-03-1963");
            Director d2 = new Director("Martin Scorsese", "17-11-1942");
            Director d3 = new Director("David Fincher", "28-08-1962");
            Director d4 = new Director("Terry Gilliam", "22-11-1940");

            databaseHelper.createActor(a1);
            databaseHelper.createActor(a2);
            databaseHelper.createActor(a3);
            databaseHelper.createActor(a4);
            databaseHelper.createActor(a5);
            databaseHelper.createActor(a6);
            databaseHelper.createDirector(d1);
            databaseHelper.createDirector(d2);
            databaseHelper.createDirector(d3);
            databaseHelper.createDirector(d4);
            Movie m1 = new Movie("Pulp fiction", "23-09-1994");
            m1.setDirector(d1);
            databaseHelper.createMovie(m1);
            m1.addActor(a4);
            m1.addActor(a3);
            databaseHelper.addActorsInMovie(m1);
            Movie m2 = new Movie("Fight club", "15-10-1999");
            m2.setDirector(d3);
            databaseHelper.createMovie(m2);
            m2.addActor(a1);
            m2.addActor(a2);
            databaseHelper.addActorsInMovie(m2);
            Movie m3 = new Movie("12 monkeys", "29-12-1995");
            m3.setDirector(d4);
            databaseHelper.createMovie(m3);
            m3.addActor(a1);
            m3.addActor(a4);
            databaseHelper.addActorsInMovie(m3);
            Movie m4 = new Movie("Django Unchained", "11-12-2012");
            m4.setDirector(d1);
            databaseHelper.createMovie(m4);
            m4.addActor(a3);
            m4.addActor(a5);
            databaseHelper.addActorsInMovie(m4);
            Movie m5 = new Movie("Shutter island", "13-02-2010");
            m5.setDirector(d2);
            databaseHelper.createMovie(m5);
            m5.addActor(a5);
            databaseHelper.addActorsInMovie(m5);
            Movie m6 = new Movie("The departed", "26-09-2006");
            m6.setDirector(d2);
            databaseHelper.createMovie(m6);
            m6.addActor(a5);
            m6.addActor(a6);
            databaseHelper.addActorsInMovie(m6);
            Movie m7 = new Movie("Once upon a time in Hollywood", "21-05-2019");
            m7.setDirector(d1);
            databaseHelper.createMovie(m7);
            m7.addActor(a1);
            m7.addActor(a5);
            databaseHelper.addActorsInMovie(m7);
        }

        List<Actor> la = databaseHelper.getAllActors();
        loadSpinnerDataActors((ArrayList<Actor>) la);
        List<Director> ld = databaseHelper.getAllDirectors();
        loadSpinnerDataDirectors((ArrayList<Director>) ld);
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
        spnActors.setAdapter(dataAdapter);
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
        spnDirectors.setAdapter(dataAdapter);
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
        spnMovies.setAdapter(dataAdapter);
    }
}