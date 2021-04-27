package com.group.MediaLibrary.business;

import com.group.MediaLibrary.data.DataLayerException;
import com.group.MediaLibrary.data.MovieDAO;
import com.group.MediaLibrary.data.TVShowDAO;

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
