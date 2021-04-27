package com.group.MediaLibrary.data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public abstract class MediaDAO {

    //attributes
    List<String> genres;
    int id;
    String title;
    Date release;
    String image;
    String description;
    PostgreSQLDatabase database;

    //constructors
    public MediaDAO() {
        this.id = -1;
        database = new PostgreSQLDatabase();
    }

    public MediaDAO(int id) {
        this.id = id;
        database = new PostgreSQLDatabase();
    }

    /**
     * Get this media object from the database
     * @return
     * @throws DataLayerException
     */
    public abstract boolean getMedia() throws DataLayerException;

    public boolean fetchGenres() throws DataLayerException {

        String sql = "SELECT name FROM genre JOIN media_genre ON genre.genreid = media_genre.genreid WHERE mediaid = ?";
        ArrayList<String> vals = new ArrayList<>();
        vals.add("" + id);

        database.connect();
        ArrayList<ArrayList<String>> rows = database.getData(sql, vals);
        database.close();

        ArrayList<String> genres = new ArrayList<>();

        for(ArrayList<String> genreRow: rows) {
            genres.add(genreRow.get(0));
        }

        setGenres(genres);

        return true;
    }

    public boolean isMovie() throws DataLayerException {
        database.connect();

        String sql = "SELECT * FROM movie WHERE mediaid = ?";
        ArrayList<String> vals = new ArrayList<>();
        vals.add("" + id);

        ArrayList<ArrayList<String>> rows = database.getData(sql, vals);

        database.close();

        return rows.size() > 0;
    }

    public boolean isTvShow() throws DataLayerException {
        database.connect();

        String sql = "SELECT * FROM tv_show WHERE mediaid = ?";
        ArrayList<String> vals = new ArrayList<>();
        vals.add("" + id);

        ArrayList<ArrayList<String>> rows = database.getData(sql, vals);

        database.close();

        return rows.size() > 0;
    }

    /**
     * Save this media to the database (partially abstract, full implementation in child classes)
     * @return success
     */
    public boolean saveMedia() throws DataLayerException {
        database.connect();
        database.startTransaction();

        //save to media table
        if(!saveGeneralData()) {
            database.rollbackTransaction();
            database.close();
            return false;
        }

        //save to genre, media_genre tables
        if(!saveGenres()) {
            database.rollbackTransaction();
            database.close();
            return false;
        }

        //save to movie or tv_show table
        if(!saveTypeSpecificData()) {
            database.rollbackTransaction();
            database.close();
            return false;
        }

        database.commitTransaction();
        database.close();
        return true;
    }

    /**
     * Save media info to DB, assumes attributes are set (id exempt)
     * @return success
     * @throws DataLayerException
     */
    public boolean saveGeneralData() throws DataLayerException {
        //get id if unset
        if(getId() == -1) {
            try {
                setId(Integer.parseInt(database.getData("SELECT MAX(mediaid) FROM media").get(0).get(0)) + 1);
            } catch (NumberFormatException nfe) {
                setId(0);
            }
        }

        //save to db
        ArrayList<String> mediaParams = new ArrayList<>();
        mediaParams.add("" + getId());
        mediaParams.add(getTitle());
        mediaParams.add(getRelease().toString());
        mediaParams.add(getImage());
        mediaParams.add(getDescription());

        int r = database.setData("INSERT INTO media VALUES (?, ?, ?, ?, ?)", mediaParams);
        if(r < 1) {
            //insert failed
            return false;
        }

        return true;

    }

    /**
     * Save genres to DB
     * @return success
     * @throws DataLayerException
     */
    public boolean saveGenres() throws DataLayerException {
        //foreach genre
        for(String genre: genres) {
            String genreid;
            //get id if already in db
            ArrayList<String> quickCheck = new ArrayList<>();
            quickCheck.add(genre);

            ArrayList<ArrayList<String>> res = database.getData("SELECT genreid FROM genre WHERE name = ?", quickCheck);

            if(res.size() > 0) {
                //already in database
                genreid = res.get(0).get(0);

            //else add to db
            } else {
                int maxGenre;
                try {
                    maxGenre = Integer.parseInt(database.getData("SELECT MAX(genreid) FROM genre").get(0).get(0)) + 1;
                } catch (NumberFormatException nfe) {
                    maxGenre = 0;
                }

                ArrayList<String> addGenre = new ArrayList<>();
                addGenre.add("" + maxGenre);
                addGenre.add(genre);

                int r = database.setData("INSERT INTO genre VALUES(?, ?)", addGenre);

                if(r < 1) {
                    //insert failed
                    return false;
                }

                //get id now
                ArrayList<String> getId = new ArrayList<>();
                getId.add(genre);

                ArrayList<ArrayList<String>> genreidres = database.getData("SELECT genreid FROM genre WHERE name = ?", getId);
                genreid = genreidres.get(0).get(0);
            }

            //finally, save relation to media
            ArrayList<String> saveGenre = new ArrayList<>();
            saveGenre.add("" + getId());
            saveGenre.add(genreid);
            int r = database.setData("INSERT INTO media_genre VALUES(?, ?)", saveGenre);

            if(r < 1) {
                //insert failed
                return false;
            }
        }

        return true;
    }

    /**
     * Override to save data specific to media type
     * @return success
     */
    public abstract boolean saveTypeSpecificData() throws DataLayerException;

    //getter ssetters
    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

}
