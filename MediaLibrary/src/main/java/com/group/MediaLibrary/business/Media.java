package com.group.MediaLibrary.business;

import com.group.MediaLibrary.data.*;

import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Media {

    //attributes
    private List<String> genres;
    private int mediaId;
    private String title;
    private Date release;
    private String image;
    private String description;
    private String type;

    public Media(int mediaId) {
        this.mediaId = mediaId;
    }

    public boolean isMovie() {
        return type.equalsIgnoreCase("movie");
    }

    public boolean isTVShow() {
        return type.equalsIgnoreCase("tvshow");
    }

    public Media getTypeMedia() {

        //find out what type I am from the database
        MediaDAO dao = new GenericMediaDAO(mediaId);

        try {
            //return movie if movie
            if (dao.isMovie()) {
                Movie movie = new Movie(-1, mediaId);
                if(movie.getByMediaId()) {
                    return movie;
                }
                return null;

            //return show if tv show
            } else if (dao.isTvShow()) {
                TvShow show = new TvShow(-1, mediaId);
                if(show.getByMediaId()) {
                    return show;
                }
                return null;
            }
        } catch (DataLayerException dle) {
            return null;
        }

        //return null if in neither table
        return null;

    }

    //getters and setters
    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getRelease() {
        return release;
    }

    public void setRelease(Date release) {
        this.release = release;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
