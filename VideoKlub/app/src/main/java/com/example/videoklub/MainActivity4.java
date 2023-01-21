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

public class MainActivity4 extends AppCompatActivity {

    Spinner spnLista;
    Spinner spnListaGlumaca;
    Button btnIzmeni;
    Button btnObrisi;
    Button btnDodaj;
    Button btnDodajGlumca;
    EditText etIme;
    EditText etDatum;
    EditText etReziser;
    DatabaseHelper databaseHelper;
    Button btnVideoKlub;
    Spinner spnPretragaGlumac;
    Spinner spnPretragaReziser;
    Button btnPretraziRez;
    Button btnPretraziGlu;
    Button btnOsvezi;

    protected void onStop() {
        super.onStop();
        databaseHelper.closeDB();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        btnVideoKlub = (Button) findViewById(R.id.btnVideoKlub);
        spnLista = (Spinner) findViewById(R.id.spnLista);
        spnListaGlumaca = (Spinner) findViewById(R.id.spnListaGlumaca);
        btnIzmeni = (Button) findViewById(R.id.btnIzmeni);
        btnObrisi = (Button) findViewById(R.id.btnObrisi);
        btnDodaj = (Button) findViewById(R.id.btnDodaj);
        btnDodajGlumca = (Button) findViewById(R.id.btnDodajGlumca);

        spnPretragaGlumac = (Spinner) findViewById(R.id.spnListaGlumacaPretraga);
        spnPretragaReziser = (Spinner) findViewById(R.id.spnListaReziseraPretraga);
        btnPretraziGlu = (Button) findViewById(R.id.btnPretraziGlumca);
        btnPretraziRez = (Button) findViewById(R.id.btnPretraziRezisera);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        etIme = (EditText) findViewById(R.id.etIme2);
        etDatum = (EditText) findViewById(R.id.etDatum2);
        etReziser = (EditText) findViewById(R.id.etReziser2);
        btnOsvezi = (Button) findViewById(R.id.btnOsvezi);

        btnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Movie m1 = new Movie(etIme.getText().toString(), etDatum.getText().toString());
                List <Director> d1 = databaseHelper.findDirectors(etReziser.getText().toString());
                m1.setDirector(d1.get(0));
                databaseHelper.createMovie(m1);
                List<Movie> mv = databaseHelper.getAllMovies();
                loadSpinnerDataMovies((ArrayList<Movie>) mv);
            }
        });

        btnObrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movie =  spnLista.getSelectedItem().toString();
                List<Movie> m =  databaseHelper.findMovies(movie);
                databaseHelper.deleteMovie(m.get(0));
                List<Movie> mv = databaseHelper.getAllMovies();
                loadSpinnerDataMovies((ArrayList<Movie>) mv);
            }
        });

        btnIzmeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movie =  spnLista.getSelectedItem().toString();
                List<Movie> m =  databaseHelper.findMovies(movie);
                databaseHelper.updateMovie(m.get(0), etIme.getText().toString(), etDatum.getText().toString(),etReziser.getText().toString());
                List<Movie> mv = databaseHelper.getAllMovies();
                loadSpinnerDataMovies((ArrayList<Movie>) mv);
            }
        });

        btnDodajGlumca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movie =  spnLista.getSelectedItem().toString();
                List<Movie> m =  databaseHelper.findMovies(movie);
                m.get(0).addActor(databaseHelper.findActors(spnListaGlumaca.getSelectedItem().toString()).get(0));
                databaseHelper.addActorsInMovie(m.get(0));
            }
        });

        btnPretraziRez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Movie> mv = databaseHelper.findAllMoviesDirectors(spnPretragaReziser.getSelectedItem().toString());
                loadSpinnerDataMovies((ArrayList<Movie>) mv);
            }
        });

        btnPretraziGlu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Movie> mv = databaseHelper.findAllMoviesActors(spnPretragaGlumac.getSelectedItem().toString());
                loadSpinnerDataMovies((ArrayList<Movie>) mv);
            }
        });

        btnVideoKlub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        btnOsvezi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //databaseHelper = new DatabaseHelper(getApplicationContext());
                databaseHelper.createTables();
                List<Actor> la = databaseHelper.getAllActors();
                loadSpinnerDataActors((ArrayList<Actor>) la);
                List<Director> ld = databaseHelper.getAllDirectors();
                loadSpinnerDataDirectors((ArrayList<Director>) ld);
                List<Movie> mv = databaseHelper.getAllMovies();
                loadSpinnerDataMovies((ArrayList<Movie>) mv);
            }
        });

        List<Movie> mv = databaseHelper.getAllMovies();
        loadSpinnerDataMovies((ArrayList<Movie>) mv);

        List<Actor> la = databaseHelper.getAllActors();
        loadSpinnerDataActors((ArrayList<Actor>) la);

        List<Director> ld = databaseHelper.getAllDirectors();
        loadSpinnerDataDirectors((ArrayList<Director>) ld);
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
        spnLista.setAdapter(dataAdapter);
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
        spnListaGlumaca.setAdapter(dataAdapter);
        spnPretragaGlumac.setAdapter(dataAdapter);
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
        spnPretragaReziser.setAdapter(dataAdapter);
    }
}