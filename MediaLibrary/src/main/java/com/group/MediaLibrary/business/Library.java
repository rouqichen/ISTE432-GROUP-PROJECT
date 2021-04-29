package com.group.MediaLibrary.business;

import com.group.MediaLibrary.data.DataLayerException;
import com.group.MediaLibrary.data.MovieDAO;
import com.group.MediaLibrary.data.UserDAO;

import java.util.ArrayList;

public class Library {

    private int userId;
    private ArrayList<Media> library;

    public Library(int userId) {

        this.userId = userId;

    }

    /**
     * Using set user id, loads the user's full library into the array list
     */
    public boolean loadLibrary() {
        UserDAO user = new UserDAO(userId);
        library = new ArrayList<>();

        try {
            user.fetch();

            ArrayList<Integer> mediaIds = user.getOwnedMedia();
            for(Integer mediaId: mediaIds) {
                Media media = new Media(mediaId);
                library.add(media.getTypeMedia());
            }

        } catch (DataLayerException dle) {
            return false;
        }
        return true;
    }

    public boolean saveNewMovie(Movie movie, String location) {
        //verify details are filled out
        if(!movie.verifyDetails()) {
            return false;
        }

        //save movie details to media/movie tables
        MovieDAO movieDAO = new MovieDAO();
        movieDAO.setGenres(movie.getGenres());
        movieDAO.setTitle(movie.getTitle());
        movieDAO.setDescription(movie.getDescription());
        movieDAO.setImage(movie.getImage());
        movieDAO.setRelease(movie.getRelease());
        movieDAO.setRuntime(movie.getRuntime());
        movieDAO.setMpaaRating(movie.getMpaaRating());

        try {
            movieDAO.saveMedia();
        } catch (DataLayerException dle) {
            return false;
        }

        //save to user library
        UserDAO user = new UserDAO(userId);
        return user.postMediaToLibrary(movie.getMediaId(), location);
    }

    public boolean saveExistingMovie(Movie movie, String location) {
        //make sure movie is valid
        if(movie.getByMovieId()) {

            //save to user library
            UserDAO user = new UserDAO(userId);

            return user.postMediaToLibrary(movie.getMediaId(), location);

        } else {
            return false;
        }

    }

    //basic getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ArrayList<Media> getLibrary() {
        return library;
    }

    public void setLibrary(ArrayList<Media> library) {
        this.library = library;
    }
}
