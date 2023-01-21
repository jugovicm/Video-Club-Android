package com.example.videoklub.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.videoklub.model.Actor;
import com.example.videoklub.model.Director;
import com.example.videoklub.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MoviesManager";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    // Table Names
    private static final String TABLE_ACTORS = "actors";
    private static final String TABLE_DIRECTORS = "directors";
    private static final String TABLE_MOVIES = "movies";
    private static final String TABLE_MOVIE_ACTORS = "movie_actors";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_BIRTHDATE = "birth_date";

    // MOVIES Table - column names
    private static final String KEY_DIRECTOR_ID = "director_id";
    private static final String KEY_RELEASEDATE = "release_date";

    //MOVIE_ACTORS Table - column names
    private static final String KEY_MOVIE_ID = "movie_id";
    private static final String KEY_ACTOR_ID = "actor_id";

    // Table Create Statements
    // Actors table create statement
    private static final String CREATE_TABLE_ACTORS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ACTORS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME
            + " TEXT," + KEY_BIRTHDATE + " TEXT" + ")";

    // Directors table create statement
    private static final String CREATE_TABLE_DIRECTORS = "CREATE TABLE IF NOT EXISTS " + TABLE_DIRECTORS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_BIRTHDATE + " TEXT" + ")";

    // Movies table create statement
    private static final String CREATE_TABLE_MOVIES = "CREATE TABLE IF NOT EXISTS "
            + TABLE_MOVIES + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT," + KEY_DIRECTOR_ID + " INTEGER,"
            + KEY_RELEASEDATE + " TEXT" + ")";

    // Movie_actors table create statement
    private static final String CREATE_TABLE_MOVIE_ACTORS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_MOVIE_ACTORS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_MOVIE_ID + " INTEGER,"
            + KEY_ACTOR_ID + " INTEGER" + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_ACTORS);
        db.execSQL(CREATE_TABLE_DIRECTORS);
        db.execSQL(CREATE_TABLE_MOVIES);
        db.execSQL(CREATE_TABLE_MOVIE_ACTORS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIRECTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE_ACTORS);

        // create new tables
        onCreate(db);
    }

    //This to be used when needed to create tables from scratch (maybe after tables are deleted)
    public void createTables() {
        if (db == null)
            db = getWritableDatabase();

        // creating required tables
        db.execSQL(CREATE_TABLE_ACTORS);
        db.execSQL(CREATE_TABLE_DIRECTORS);
        db.execSQL(CREATE_TABLE_MOVIES);
        db.execSQL(CREATE_TABLE_MOVIE_ACTORS);
    }

    //Delete all tables
    public void dropTables(){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIRECTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE_ACTORS);
    }

    /*
     * Creating an actor
     */
    public long createActor(Actor actor) {

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, actor.getName());//kljuc mora da se slaze sa nazivom kolone
        values.put(KEY_BIRTHDATE, actor.getBirthDate());

        // insert row
        long actor_id = db.insert(TABLE_ACTORS, null, values);

        //now we know id obtained after writing actor to a database, update existing actor
        actor.setId(actor_id);

        return actor_id;
    }

    /*
     * Creating a director
     */
    public long createDirector(Director director) {

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, director.getName());
        values.put(KEY_BIRTHDATE, director.getBirthDate());

        // insert row
        long director_id = db.insert(TABLE_DIRECTORS, null, values);
        director.setId(director_id);

        return director_id;
    }


    /*
     * Creating a movie
     */
    public long createMovie(Movie movie) {

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, movie.getName());
        values.put(KEY_DIRECTOR_ID, movie.getDirector().getId());
        values.put(KEY_RELEASEDATE, movie.getReleaseDate());

        // insert row
        long movie_id = db.insert(TABLE_MOVIES, null, values);

        movie.setId(movie_id);

        return movie_id;
    }

    /*
     * Creating a movie
     */
    public void addActorsInMovie(Movie movie) {
        //read all actors for a given movie and for each actor insert a row in a table movie_actors
        for (Actor actor : movie.getActors()){
            ContentValues values = new ContentValues();
            values.put(KEY_MOVIE_ID, movie.getId());
            values.put(KEY_ACTOR_ID, actor.getId());
            // insert row
            db.insert(TABLE_MOVIE_ACTORS, null, values);
        }
    }


    /*
     * get single actor like
     * SELECT * FROM actors WHERE id = 1;
     */
    @SuppressLint("Range")
    public Actor getActor(long actor_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_ACTORS + " WHERE "
                + KEY_ID + " = " + actor_id;
        //Alternative to use selectionArgs in rawQuery
        //String selectQuery = "SELECT  * FROM " + TABLE_ACTORS + " WHERE "
        //        + KEY_ID + " = ?";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        //If using selectionArgs, the number of passed strings must match the number of ? characters
        //within selectQuery string
        //Cursor c = db.rawQuery(selectQuery, new String[]{Long.toString(actor_id)});

        if (c != null)
            c.moveToFirst();

        //create actor based on data read from a database
        Actor ac = new Actor();
        ac.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        ac.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        ac.setBirthDate(c.getString(c.getColumnIndex(KEY_BIRTHDATE)));

        return ac;
    }


    /*
     * Updating an Actor using data in an object actor
     */
    public int updateActor(Actor actor, String strName, String strDatum) {
        actor.setName(strName);
        actor.setBirthDate(strDatum);
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, actor.getName());
        values.put(KEY_BIRTHDATE, actor.getBirthDate());

        // updating row
        return db.update(TABLE_ACTORS, values, KEY_ID +'='+  actor.getId(), null);

    }
    public int updateDirector(Director director, String strName, String strDatum) {
        director.setName(strName);
        director.setBirthDate(strDatum);
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, director.getName());
        values.put(KEY_BIRTHDATE, director.getBirthDate());

        // updating row
        return db.update(TABLE_DIRECTORS, values, KEY_ID +'='+  director.getId(), null);

    }

    public int updateMovie(Movie movie, String strName, String strDatum, String strReziser) {
        movie.setName(strName);
        movie.setReleaseDate(strDatum);
        List<Director> d = findDirectors(strReziser);
        movie.setDirector(d.get(0));
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, movie.getName());
        values.put(KEY_RELEASEDATE, movie.getReleaseDate());
        values.put(KEY_DIRECTOR_ID, movie.getDirector().getId());
        // updating row
        return db.update(TABLE_MOVIES, values, KEY_ID +'='+  movie.getId(), null);

    }
    /*
     * Deleting an Actor using actor id
     */
    public void deleteActor(Actor actor) {
        long actor_id = actor.getId();
        db.delete(TABLE_ACTORS, KEY_ID + " = ?",
                new String[] { String.valueOf(actor_id) });
        db.delete(TABLE_MOVIE_ACTORS, KEY_ACTOR_ID + " = ?",
                new String[] { String.valueOf(actor_id) });
    }

    public void deleteDirector(Director director) {
        long director_id = director.getId();
        db.delete(TABLE_DIRECTORS, KEY_ID + " = ?",
                new String[] { String.valueOf(director_id) });
        db.delete(TABLE_MOVIES, KEY_DIRECTOR_ID + " = ?",
                new String[] { String.valueOf(director_id) });
    }

    public void deleteMovie(Movie movie) {
        long movie_id = movie.getId();
        db.delete(TABLE_MOVIES, KEY_ID + " = ?",
                new String[] { String.valueOf(movie_id) });
        db.delete(TABLE_MOVIE_ACTORS, KEY_MOVIE_ID + " = ?",
                new String[] { String.valueOf(movie_id) });
    }

    /*
     * get single director like
     * SELECT * FROM directors WHERE id = 1;
     */
    @SuppressLint("Range")
    public Director getDirector(long director_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_DIRECTORS + " WHERE "
                + KEY_ID + " = " + director_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Director d = new Director();
        d.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        d.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        d.setBirthDate(c.getString(c.getColumnIndex(KEY_BIRTHDATE)));

        return d;
    }

    /*
     * get single movie like
     * SELECT * FROM movies WHERE id = 1;
     */

    @SuppressLint("Range")
    public Movie getMovie(long movie_id) {

        String selectQuery = "SELECT  * FROM " + TABLE_MOVIES + " WHERE "
                + KEY_ID + " = " + movie_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Movie m = new Movie();
        m.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        m.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        //obtain director based on director id read from table movies
        Director d = getDirector(c.getInt(c.getColumnIndex(KEY_DIRECTOR_ID)));
        m.setDirector(d);
        m.setReleaseDate(c.getString(c.getColumnIndex(KEY_RELEASEDATE)));

        return m;
    }


    /*
     * getting all actors
     * SELECT * FROM actors;
     * */
    @SuppressLint("Range")
    public ArrayList<Actor> getAllActors() {
        ArrayList<Actor> actors = new ArrayList<Actor>();
        String selectQuery = "SELECT  * FROM " + TABLE_ACTORS;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Actor a = new Actor();
                a.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                a.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                a.setBirthDate(c.getString(c.getColumnIndex(KEY_BIRTHDATE)));

                // adding to todo list
                actors.add(a);
            } while (c.moveToNext());
        }

        return actors;
    }


    /*
     * getting all directors;
     * SELECT * FROM directors;
     * */
    @SuppressLint("Range")
    public ArrayList<Director> getAllDirectors() {
        ArrayList<Director> directors = new ArrayList<Director>();
        String selectQuery = "SELECT  * FROM " + TABLE_DIRECTORS;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Director d = new Director();
                d.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                d.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                d.setBirthDate(c.getString(c.getColumnIndex(KEY_BIRTHDATE)));
                directors.add(d);
            } while (c.moveToNext());
        }

        return directors;
    }

    /*
     * getting all actors acting in a movie
     * SELECT a.id, m.name FROM actors a, movies m, movie_actors ma WHERE m.name LIKE ‘Pulp%’ AND
     * m.id = ma.movie_id AND a.id = ma.actor_id;
     * */
    public List<Actor> getAllActorsInMovie(String movieName) {
        List<Actor> actors = new ArrayList<Actor>();

       // List<Movie> m = findMovies(movieName);
       // long m_id = m.get(0).getId();

       String selectQuery = "SELECT  a." + KEY_ID + ", m." + KEY_NAME + " FROM " + TABLE_ACTORS + " a, "
                + TABLE_MOVIES + " m, " + TABLE_MOVIE_ACTORS + " ma WHERE UPPER(m."
                + KEY_NAME + ") LIKE '" + movieName.toUpperCase() + "%'" + " AND m." + KEY_ID
                + " = " + "ma." + KEY_MOVIE_ID + " AND a." + KEY_ID
                + " = " + "ma." + KEY_ACTOR_ID;
       /* String selectQuery = "SELECT  a." + KEY_ACTOR_ID + " FROM " + TABLE_MOVIE_ACTORS + " WHERE "
                + KEY_MOVIE_ID + " = " + m_id;*/
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                @SuppressLint("Range") long actor_id = c.getInt(c.getColumnIndex(KEY_ID));
                Actor a = getActor(actor_id);
                actors.add(a);
            } while (c.moveToNext());
        }

        return actors;
    }

    @SuppressLint("Range")
    public ArrayList<Movie> findAllMoviesDirectors(String DirectorName) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        String selectQuery = "SELECT  m." + KEY_ID + " FROM "
                + TABLE_MOVIES + " m, " + TABLE_DIRECTORS + " d WHERE UPPER(d."
                + KEY_NAME + ") LIKE '" + DirectorName.toUpperCase() + "%'" + " AND d." + KEY_ID
                + " = " + "m." + KEY_DIRECTOR_ID;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                @SuppressLint("Range") long movie_id = c.getInt(c.getColumnIndex(KEY_ID));
                Movie m = getMovie(movie_id);
                movies.add(m);
            } while (c.moveToNext());
            /*do {
                Movie m = new Movie();
                m.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                m.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                m.setReleaseDate((c.getString(c.getColumnIndex(KEY_RELEASEDATE))));
                Director d = getDirector((c.getInt(c.getColumnIndex(KEY_DIRECTOR_ID))));
                m.setDirector(d);

                movies.add(m);
            } while (c.moveToNext());*/
        }

        return movies;
    }
    @SuppressLint("Range")
    public ArrayList<Movie> findAllMoviesActors(String ActorName) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        String selectQuery = "SELECT  m." + KEY_ID + ", a." + KEY_NAME + " FROM " + TABLE_ACTORS + " a, "
                + TABLE_MOVIES + " m, " + TABLE_MOVIE_ACTORS + " ma WHERE UPPER(a."
                + KEY_NAME + ") LIKE '" + ActorName.toUpperCase() + "%'" + " AND m." + KEY_ID
                + " = " + "ma." + KEY_MOVIE_ID + " AND a." + KEY_ID
                + " = " + "ma." + KEY_ACTOR_ID;
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                @SuppressLint("Range") long movie_id = c.getInt(c.getColumnIndex(KEY_ID));
                Movie m = getMovie(movie_id);
                movies.add(m);
            } while (c.moveToNext());
            /*do {
                Movie m = new Movie();
                m.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                m.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                m.setReleaseDate((c.getString(c.getColumnIndex(KEY_RELEASEDATE))));
                Director d = getDirector((c.getInt(c.getColumnIndex(KEY_DIRECTOR_ID))));
                m.setDirector(d);

                movies.add(m);
            } while (c.moveToNext());*/
        }

        return movies;
    }
    public List<Director> getDirectorMovie(String movieName) {
        List<Director> directors = new ArrayList<Director>();

        // List<Movie> m = findMovies(movieName);
        // long m_id = m.get(0).getId();

        String selectQuery = "SELECT  d." + KEY_ID + " FROM "
                + TABLE_DIRECTORS + " d, " + TABLE_MOVIES + " m WHERE UPPER(m."
                + KEY_NAME + ") LIKE '" + movieName.toUpperCase() + "%'" + " AND d." + KEY_ID
                + " = " + "m." + KEY_DIRECTOR_ID;;
        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                @SuppressLint("Range") long dirctor_id = c.getInt(c.getColumnIndex(KEY_ID));
                Director d = getDirector(dirctor_id);
                directors.add(d);
            } while (c.moveToNext());
        }

        return directors;
    }


    /*
     * getting all movies
     * SELECT * FROM movies;
     * */
    @SuppressLint("Range")
    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIES;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Movie m = new Movie();
                m.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                m.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                m.setReleaseDate((c.getString(c.getColumnIndex(KEY_RELEASEDATE))));
                Director d = getDirector((c.getInt(c.getColumnIndex(KEY_DIRECTOR_ID))));
                m.setDirector(d);

                movies.add(m);
            } while (c.moveToNext());
        }

        return movies;
    }
    public List<Actor> findActors(String strGlumac) {
        List<Actor> actors = new ArrayList<Actor>();
        String selectQuery = "SELECT  * FROM " + TABLE_ACTORS + " WHERE "
                + KEY_NAME + " LIKE '%" + strGlumac.toUpperCase() + "%'";


        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                @SuppressLint("Range") long actor_id = c.getInt(c.getColumnIndex(KEY_ID));
                Actor a = getActor(actor_id);
                actors.add(a);
            } while (c.moveToNext());
        }

        return actors;
    }
    @SuppressLint("Range")
    public ArrayList<Director> findDirectors(String strReziser) {
        ArrayList<Director> directors = new ArrayList<Director>();
        String selectQuery = "SELECT  * FROM " + TABLE_DIRECTORS + " WHERE "
                + KEY_NAME + " LIKE '%" + strReziser.toUpperCase() + "%'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Director d = new Director();
                d.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                d.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                d.setBirthDate(c.getString(c.getColumnIndex(KEY_BIRTHDATE)));
                directors.add(d);
            } while (c.moveToNext());
        }

        return directors;
    }

    @SuppressLint("Range")
    public ArrayList<Movie> findMovies(String strFilm) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIES + " WHERE "
                + KEY_NAME + " LIKE '%" + strFilm.toUpperCase() + "%'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Movie m = new Movie();
                m.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                m.setName((c.getString(c.getColumnIndex(KEY_NAME))));
                m.setReleaseDate((c.getString(c.getColumnIndex(KEY_RELEASEDATE))));
                Director d = getDirector((c.getInt(c.getColumnIndex(KEY_DIRECTOR_ID))));
                m.setDirector(d);

                movies.add(m);
            } while (c.moveToNext());
        }

        return movies;
    }

    // closing database
    public void closeDB() {
        if (db != null && db.isOpen())
            db.close();
    }


}
