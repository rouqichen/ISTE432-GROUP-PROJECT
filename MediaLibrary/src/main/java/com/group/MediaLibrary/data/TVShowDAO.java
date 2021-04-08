package com.group.MediaLibrary.data;

import java.util.ArrayList;

public class TVShowDAO extends MediaDAO {

    //attributes
    private int showid;
    private int episodeLength;
    private String tvRating;

    //constructors
    public TVShowDAO() {
        super();

        this.showid = -1;
    }

    public TVShowDAO(int showid) {
        super();

        this.showid = showid;
    }


    /**
     * Save data to tv_show table
     * @return success
     */
    public boolean saveTypeSpecificData() throws DataLayerException {
        //get movie id if unset
        if(getShowid() == -1) {
            try {
                setShowid(Integer.parseInt(database.getData("SELECT MAX(movieid) FROM movie").get(0).get(0)) + 1);
            } catch (NumberFormatException nfe) {
                setShowid(0);
            }
        }

        //save to db
        ArrayList<String> tvParams = new ArrayList<>();
        tvParams.add("" + getShowid());
        tvParams.add("" + getId());
        tvParams.add("" + getEpisodeLength());
        tvParams.add(getTvRating());

        int r = database.setData("INSERT INTO tv_show VALUES (?, ?, ?, ?)", tvParams);
        if(r < 1) {
            //insert failed
            return false;
        }

        return true;
    }


    //getters and setters
    public int getShowid() {
        return showid;
    }

    public void setShowid(int showid) {
        this.showid = showid;
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
