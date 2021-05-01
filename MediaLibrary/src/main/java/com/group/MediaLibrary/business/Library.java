package com.group.MediaLibrary.business;

import com.group.MediaLibrary.data.DataLayerException;
import com.group.MediaLibrary.data.MovieDAO;
import com.group.MediaLibrary.data.TVShowDAO;
import com.group.MediaLibrary.data.UserDAO;
import com.group.MediaLibrary.service.response.MediaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Library {

    private int userId;
    private ArrayList<Media> library;

    private static final Logger logger = LoggerFactory.getLogger(Movie.class);

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
            user.fetchAll();

            loadMediaFromUserDAO(user);

        } catch (DataLayerException dle) {
            return false;
        }

        return true;
    }

    /**
     * Load items in the user's library which have the passed genre
     * @param genre Genre to filter by
     * @return Library
     */
    public boolean loadLibraryByGenre(String genre) {
        UserDAO user = new UserDAO(userId);
        library = new ArrayList<>();

        try {
            user.fetchByGenre(genre);

            loadMediaFromUserDAO(user);

        } catch (DataLayerException dle) {
            return false;
        }

        return true;
    }

    /**
     * Load items which include the passed string in the title
     * @param title Search string
     * @return Library
     */
    public boolean loadLibraryByTitle(String title) {
        UserDAO user = new UserDAO(userId);
        library = new ArrayList<>();

        try {
            user.fetchByTitle(title);

            loadMediaFromUserDAO(user);

        } catch (DataLayerException dle) {
            return false;
        }

        return true;
    }

    /**
     * Load items which have a run time or episode length between min and max
     * @param minLength Minimum length to include
     * @param maxLength Maximum length to include
     * @return Library
     */
    public boolean loadLibraryByLength(int minLength, int maxLength) {
        UserDAO user = new UserDAO(userId);
        library = new ArrayList<>();

        try {
            user.fetchByLength(minLength, maxLength);

            loadMediaFromUserDAO(user);

        } catch (DataLayerException dle) {
            return false;
        }

        return true;
    }

    /**
     * Private helper to load Media objects into list from list of ids
     *
     * @param user
     * @return
     */
    private void loadMediaFromUserDAO(UserDAO user) {
        ArrayList<Integer> mediaIds = user.getOwnedMedia();
        for(Integer mediaId: mediaIds) {
            Media media = new Media(mediaId).getTypeMedia();
            media.fetchLocation(getUserId());
            library.add(media);
        }
    }

    /**
     * Save movie to user's library that does not already exist in database
     * @param movie Movie with values from post request
     * @param location Where the user owns the movie
     * @return success
     */
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
        return user.postMediaToLibrary(movieDAO.getId(), location);
    }

    /**
     * Save show to user's library that does not already exist in database
     * @param show Tv Show with values from post request
     * @param location Where the user owns the show
     * @return success
     */
    public boolean saveNewShow(TvShow show, String location) {
        //verify details are filled out
        if(!show.verifyDetails()) {
            return false;
        }

        //save details to media/tv_show tables
        TVShowDAO showDAO = new TVShowDAO();
        showDAO.setGenres(show.getGenres());
        showDAO.setTitle(show.getTitle());
        showDAO.setDescription(show.getDescription());
        showDAO.setImage(show.getImage());
        showDAO.setRelease(show.getRelease());
        showDAO.setEpisodeLength(show.getEpisodeLength());
        showDAO.setTvRating(show.getTvRating());

        try {
            showDAO.saveMedia();
        } catch (DataLayerException dle) {
            return false;
        }

        //save to user library
        UserDAO user = new UserDAO(userId);
        return user.postMediaToLibrary(showDAO.getId(), location);
    }

    /**
     * Save movie to user's library that exists in database
     * @param movie Movie with values from post request
     * @param location Where the user owns the movie
     * @return success
     */
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

    /**
     * Save show to user's library that exists in database
     * @param show Tv Show with values from post request
     * @param location Where the user owns the show
     * @return success
     */
    public boolean saveExistingShow(TvShow show, String location) {
        //make sure show is valid
        if(show.getByShowId()) {

            //save to user library
            UserDAO user = new UserDAO(userId);

            return user.postMediaToLibrary(show.getMediaId(), location);

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

    public ArrayList<MediaResponse> getLibraryResponse() {
        ArrayList<MediaResponse> responses = new ArrayList<>();

        for(Media media: getLibrary()) {
            responses.add(media.getResponse());
        }

        return responses;
    }
}
