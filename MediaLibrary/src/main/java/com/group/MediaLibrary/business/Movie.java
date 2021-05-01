package com.group.MediaLibrary.business;

import com.group.MediaLibrary.data.DataLayerException;
import com.group.MediaLibrary.data.MovieDAO;
import com.group.MediaLibrary.service.response.MediaResponse;
import com.group.MediaLibrary.service.response.MovieResponse;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Movie extends Media {

    private int movieid;
    private int runtime;
    private String mpaaRating;

    private static final Logger logger = LoggerFactory.getLogger(Movie.class);

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

    public Movie() {
        super(-1);
        this.movieid = -1;
        setType("movie");
    }

    /**
     * Get movie details from database using media id
     * @return
     */
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

    /**
     * Get movie details from database using movie id
     * @return success
     */
    public boolean getByMovieId() {
        MovieDAO dao = new MovieDAO(getMovieId());
        try {
            if(dao.getMovie()) {
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

    @Override
    public MovieResponse getResponse() {
        MovieResponse response = new MovieResponse();

        response.setGenres(getGenres());
        response.setMediaId(getMediaId());
        response.setDescription(getDescription());
        response.setRelease(getRelease());
        response.setImage(getImage());
        response.setTitle(getTitle());
        response.setType(getType());
        response.setLocation(getLocation());
        response.setMovieid(getMovieId());
        response.setRuntime(getRuntime());
        response.setMpaaRating(getMpaaRating());

        return response;
    }

    /**
     * Verify set details, fills in missing details from IMDB
     * @return If details are valid
     */
    public boolean verifyDetails() {
        //title is required to be valid
        if(null == getTitle() || "".equals(getTitle())) {
            return false;
        }

        //if any missing, get from IMDB
        if(null == getGenres() || getGenres().size() < 1 || null == getRelease() || null == getImage() || "".equals(getImage()) ||
                null == getDescription() || "".equals(getDescription()) || runtime < 1 ||
                null == getMpaaRating() || "".equals(getMpaaRating())) {

            //make search call to get imdb movie id
            String imdbId = getImdbId();

            //make fetch call to get imdb movie
            JSONObject imdbResults = getImdbMovie(imdbId);

            //genres
            if(null == getGenres() || getGenres().size() < 1) {
                ArrayList<String> genres = new ArrayList<>();

                JSONArray genresJson = imdbResults.getJSONArray("genreList");
                genresJson.forEach(item -> {
                    JSONObject jsonObject = (JSONObject) item;
                    genres.add(jsonObject.getString("value"));
                });

                setGenres(genres);
            }

            //release
            if( null == getRelease() ) {
                setRelease(Date.valueOf(imdbResults.getString("releaseDate")));
            }

            //image
            if( null == getImage() || "".equals(getImage())) {
                setImage(imdbResults.getString("image"));
            }

            //description
            if( null == getDescription() || "".equals(getDescription()) ) {
                setDescription(imdbResults.getString("plot"));
            }

            //runtime
            if( getRuntime() < 1) {
                setRuntime(imdbResults.getInt("runtimeMins"));
            }

            //rating
            if( null == getMpaaRating() || "".equals(getMpaaRating()) ) {
                setMpaaRating(imdbResults.getString("contentRating"));
            }

        }

        return true;
    }

    /**
     * Helper method to make a synchronous fetch call to imdb api for the movie
     * @return JSONObject of the movie returned
     */
    private JSONObject getImdbMovie(String imdbId) {
        OkHttpClient httpClient = new OkHttpClient();

        String url = "https://imdb-api.com/en/API/Title/k_hv7uy0nj/" + imdbId;

        //build request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .build();

        Response response;

        //make api seach call
        try {
            response = httpClient.newCall(request).execute();
        } catch (IOException ioe) {
            return null;
        }

        //response failed
        if(!response.isSuccessful()) {
            return null;
        }

        //get JSONObject
        try {
            return new JSONObject(response.body().string());
        } catch (IOException ioe) {
            return null;
        }
    }

    /**
     * Helper method to make a synchronous search call to imdb api for the title id
     * @return IMDB id for movie
     */
    private String getImdbId() {
        OkHttpClient httpClient = new OkHttpClient();

        String url = "https://imdb-api.com/en/API/SearchMovie/k_hv7uy0nj/" + getTitle();

        //build request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .build();

        Response response;

        //make api seach call
        try {
            response = httpClient.newCall(request).execute();
        } catch (IOException ioe) {
            return "";
        }

        //response failed
        if(!response.isSuccessful()) {
            return "";
        }

        //get id for first result
        try {
            JSONObject json = new JSONObject(response.body().string());
            logger.info("Response:" + json.toString());
            return json
                    .getJSONArray("results")
                    .getJSONObject(0)
                    .getString("id");
        } catch (IOException ioe) {
            return null;
        }
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
