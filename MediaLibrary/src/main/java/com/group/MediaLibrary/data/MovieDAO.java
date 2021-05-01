package com.group.MediaLibrary.data;

import java.sql.Date;
import java.util.ArrayList;

public class MovieDAO extends MediaDAO{

    //attributes
    private int movieid;
    private int runtime;
    private String mpaaRating;

    //constructors
    public MovieDAO() {
        super();

        this.movieid = -1;
    }

    public MovieDAO(int movieid) {
        super();

        this.movieid = movieid;
    }

    @Override
    public boolean getMedia() throws DataLayerException {

        String sql = "SELECT mv.movieid, mv.mediaid, md.title, md.release_date, md.image_url, md.description, mv.runtime, mv.mpaa_rating FROM movie as mv JOIN media as md ON mv.mediaid = md.mediaid WHERE mv.mediaid = ?";
        ArrayList<Object> vals = new ArrayList<>();
        vals.add(getId());

        return fetch(sql, vals);
    }

    public boolean getMovie() throws DataLayerException {

        String sql = "SELECT mv.movieid, mv.mediaid, md.title, md.release_date, md.image_url, md.description, mv.runtime, mv.mpaa_rating FROM movie as mv JOIN media as md ON mv.mediaid = md.mediaid WHERE mv.movieid = ?";
        ArrayList<Object> vals = new ArrayList<>();
        vals.add(getMovieid());

        return fetch(sql, vals);
    }

    /**
     * Helper to fetch movie data with different inputs
     *
     * @param sql Sql query to get movie data
     * @param vals Values for prepared statement
     * @return Success
     * @throws DataLayerException
     */
    private boolean fetch(String sql, ArrayList<Object> vals) throws DataLayerException  {
        //run query
        database.connect();
        ArrayList<ArrayList<String>> rows = database.getData(sql, vals);
        database.close();

        //no results
        if(rows.size() < 1) {
            return false;
        }

        //too many results
        if(rows.size() > 1) {
            throw new DataLayerException("Too many rows with movieid " + movieid);
        }

        //set values
        ArrayList<String> movieValues = rows.get(0);
        setMovieid(Integer.parseInt(movieValues.get(0)));
        setTitle(movieValues.get(2));
        setRelease(Date.valueOf(movieValues.get(3)));
        setImage(movieValues.get(4));
        setDescription(movieValues.get(5));
        setRuntime(Integer.parseInt(movieValues.get(6)));
        setMpaaRating(movieValues.get(7));

        fetchGenres();

        return true;
    }

    /**
     * Save data to movie table
     * @return success
     */
    public boolean saveTypeSpecificData() throws DataLayerException {
        //get movie id if unset
        if(getMovieid() == -1) {
            try {
                setMovieid(Integer.parseInt(database.getData("SELECT MAX(movieid) FROM movie").get(0).get(0)) + 1);
            } catch (NumberFormatException nfe) {
                setMovieid(0);
            }
        }

        //save to db
        ArrayList<Object> movieParams = new ArrayList<>();
        movieParams.add(getMovieid());
        movieParams.add(getId());
        movieParams.add(getRuntime());
        movieParams.add(getMpaaRating());

        int r = database.setData("INSERT INTO movie VALUES (?, ?, ?, ?)", movieParams);
        if(r < 1) {
            //insert failed
            return false;
        }

        return true;
    }

    //getters setters
    public int getMovieid() {
        return movieid;
    }

    public void setMovieid(int movieid) {
        this.movieid = movieid;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating = mpaaRating;
    }
}
