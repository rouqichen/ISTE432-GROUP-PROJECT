package com.group.MediaLibrary.business;

import com.group.MediaLibrary.data.DataLayerException;
import com.group.MediaLibrary.data.MovieDAO;

public class Movie extends Media {

    private int movieid;
    private int runtime;
    private String mpaaRating;

    public Movie(int id, int mediaId) {
        super(mediaId);
        this.movieid = id;
        setType("movie");
    }

    public Movie(int id) {
        super(-1);
        this.movieid = id;
        setType("movie");
    }

    public boolean getByMediaId() {
        MovieDAO dao = new MovieDAO();
        dao.setId(getMediaId());
        try {
            if(dao.getMedia()) {
                setTitle(dao.getTitle());
                setRelease(dao.getRelease());
                setImage(dao.getImage());
                setDescription(dao.getDescription());
                setMovieId(dao.getMovieid());
                setRuntime(dao.getRuntime());
                setMpaaRating(dao.getMpaaRating());
                setGenres(dao.getGenres());
            } else {
                return false;
            }
        } catch (DataLayerException dle) {
            return false;
        }

        return true;
    }

    public int getMovieId() {
        return movieid;
    }

    public void setMovieId(int id) {
        this.movieid = id;
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
