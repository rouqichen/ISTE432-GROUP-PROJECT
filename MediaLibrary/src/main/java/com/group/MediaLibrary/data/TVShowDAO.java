package com.group.MediaLibrary.data;

import java.sql.Date;
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

    @Override
    public boolean getMedia() throws DataLayerException {
        fetchGenres();

        database.connect();

        String sql = "SELECT tv.showid, tv.mediaid, md.title, md.release_date, md.image_url, md.description, tv.episode_length, tv.tv_rating FROM tv_show as tv JOIN media as md ON tv.mediaid = md.mediaid WHERE tv.mediaid = ?";
        ArrayList<String> vals = new ArrayList<>();
        vals.add("" + getId());

        ArrayList<ArrayList<String>> rows = database.getData(sql, vals);

        database.close();

        if(rows.size() < 1) {
            return false;
        }

        if(rows.size() > 1) {
            throw new DataLayerException("Too many rows with showid " + showid);
        }

        ArrayList<String> tvValues = rows.get(0);
        setShowid(Integer.parseInt(tvValues.get(0)));
        setTitle(tvValues.get(2));
        setRelease(Date.valueOf(tvValues.get(3)));
        setImage(tvValues.get(4));
        setDescription(tvValues.get(5));
        setEpisodeLength(Integer.parseInt(tvValues.get(6)));
        setTvRating(tvValues.get(7));

        return true;
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
