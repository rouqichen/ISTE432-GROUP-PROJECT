package com.group.MediaLibrary.data;

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
        ArrayList<String> movieParams = new ArrayList<>();
        movieParams.add("" + getMovieid());
        movieParams.add("" + getId());
        movieParams.add("" + getRuntime());
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
