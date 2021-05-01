package com.group.MediaLibrary.business;

import com.group.MediaLibrary.data.DataLayerException;
import com.group.MediaLibrary.data.MovieDAO;
import com.group.MediaLibrary.data.TVShowDAO;
import com.group.MediaLibrary.service.response.MovieResponse;
import com.group.MediaLibrary.service.response.ShowResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

public class TvShow extends Media {

    private int showId;
    private int episodeLength;
    private String tvRating;

    public TvShow(int id, int mediaId) {
        super(mediaId);
        this.showId = id;
        setType("tvshow");
    }

    public TvShow(int id) {
        super(-1);
        this.showId = id;
        setType("tvshow");
    }

    public boolean getByMediaId() {
        TVShowDAO dao = new TVShowDAO();
        dao.setId(getMediaId());
        try {
            if(dao.getMedia()) {
                setTitle(dao.getTitle());
                setRelease(dao.getRelease());
                setImage(dao.getImage());
                setDescription(dao.getDescription());
                setShowId(dao.getShowid());
                setEpisodeLength(dao.getEpisodeLength());
                setTvRating(dao.getTvRating());
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
     * Get show details from database using movie id
     * @return success
     */
    public boolean getByShowId() {
        TVShowDAO dao = new TVShowDAO(getShowId());
        try {
            if(dao.getShow()) {
                setTitle(dao.getTitle());
                setRelease(dao.getRelease());
                setImage(dao.getImage());
                setDescription(dao.getDescription());
                setShowId(dao.getShowid());
                setEpisodeLength(dao.getEpisodeLength());
                setTvRating(dao.getTvRating());
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
    public ShowResponse getResponse() {
        ShowResponse response = new ShowResponse();

        response.setGenres(getGenres());
        response.setMediaId(getMediaId());
        response.setDescription(getDescription());
        response.setRelease(getRelease());
        response.setImage(getImage());
        response.setTitle(getTitle());
        response.setType(getType());
        response.setShowId(getShowId());
        response.setEpisodeLength(getEpisodeLength());
        response.setTvRating(getTvRating());

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
        if(getGenres().size() < 1 || null == getRelease() || null == getImage() || "".equals(getImage()) ||
                null == getDescription() || "".equals(getDescription()) || getEpisodeLength() < 1 ||
                null == getTvRating() || "".equals(getTvRating())) {

            //make search call to get imdb movie id
            String imdbId = getImdbId();

            //make fetch call to get imdb movie
            JSONObject imdbResults = getImdbSeries(imdbId);

            //genres
            if(getGenres().size() < 1) {
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

            //episode length
            if( getEpisodeLength() < 1) {
                setEpisodeLength(imdbResults.getInt("runtimeMins"));
            }

            //rating
            if( null == getTvRating() || "".equals(getTvRating()) ) {
                setTvRating(imdbResults.getString("contentRating"));
            }

        }

        return true;
    }

    /**
     * Helper method to make a synchronous fetch call to imdb api for the movie
     * @return JSONObject of the movie returned
     */
    private JSONObject getImdbSeries(String imdbId) {
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
        return new JSONObject(response.body().toString());
    }

    /**
     * Helper method to make a synchronous search call to imdb api for the title id
     * @return IMDB id for movie
     */
    private String getImdbId() {
        OkHttpClient httpClient = new OkHttpClient();

        String url = "https://imdb-api.com/en/API/SearchSeries/k_hv7uy0nj/" + getTitle();

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
        return new JSONObject(response.body().toString())
                .getJSONArray("results")
                .getJSONObject(0)
                .getString("id");
    }


    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public int getEpisodeLength() {
        return episodeLength;
    }

    public void setEpisodeLength(int episodeLength) {
        this.episodeLength = episodeLength;
    }

    public String getTvRating() {
        return tvRating;
    }

    public void setTvRating(String tvRating) {
        this.tvRating = tvRating;
    }
}
