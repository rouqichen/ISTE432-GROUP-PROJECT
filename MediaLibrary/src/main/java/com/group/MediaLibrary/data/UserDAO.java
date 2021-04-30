package com.group.MediaLibrary.data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;

public class UserDAO {

    private int uid;
    private ArrayList<Integer> ownedMedia;

    public UserDAO(int uid) {
        this.uid = uid;
        ownedMedia = new ArrayList<>();
    }

    public UserDAO() {

    }

    /**
     *
     * @param username
     * @param password
     * @return
     * @throws DataLayerException
     */
    public int login(String username, String password) throws DataLayerException {
        //prepare db query
        String sql = "SELECT password, uid FROM lib_user WHERE username LIKE ?";
        ArrayList<String> vals = new ArrayList<>();
        vals.add(username);

        PostgreSQLDatabase db = new PostgreSQLDatabase();
        ArrayList<ArrayList<String>> resultList;

        //get password, uid from database
        try {
            db.connect();
            resultList = db.getData(sql, vals);
            db.close();
        } catch (DataLayerException dle) {
            throw new DataLayerException(dle, "Error logging user in");
        }

        //bad username
        if(resultList.size() < 1) {
            return -1;
        }

        //check password
        String hashedPassword = resultList.get(0).get(0);

        password = hash(password);

        if(password.equals(hashedPassword)) {
            //return uid
            setUid(Integer.parseInt(resultList.get(0).get(1)));

            return getUid();
        }

        //bad password
        return -1;

    }

    public boolean register(String username, String password) throws DataLayerException {
        String sql = "INSERT INTO lib_user (username, password) VALUES (?, ?)";
        ArrayList<String> vals = new ArrayList<>();
        vals.add(username);
        vals.add(hash(password));

        PostgreSQLDatabase db = new PostgreSQLDatabase();

        int numAffectted = 0;
        try {
            db.connect();
            numAffectted = db.setData(sql, vals);
            db.close();
        } catch (DataLayerException dle) {
            return false;
        }

        return numAffectted > 0;
    }

    //get from db
    public void fetchAll() throws DataLayerException {
        String sql = "SELECT mediaid FROM user_media WHERE uid = ?";
        ArrayList<String> vals = new ArrayList<>();
        vals.add("" + uid);

        fetch(sql, vals);

    }

    public void fetchByGenre(String genre) throws DataLayerException {
        String sql = "SELECT lib.mediaid FROM user_media as lib "
                + "JOIN media_genre as mg ON lib.mediaid = mg.mediaid "
                + "JOIN genre ON genre.genreid = mg.genreid "
                + "WHERE genre.name LIKE ? "
                + "AND lib.uid = ?";
        ArrayList<String> vals = new ArrayList<>();
        vals.add(genre);
        vals.add("" + uid);

        fetch(sql, vals);
    }

    public void fetchByTitle(String title) throws DataLayerException {
        String sql = "SELECT lib.mediaid FROM user_media as lib "
                + "JOIN media ON lib.mediaid = media.mediaid "
                + "WHERE media.title LIKE ? "
                + "AND lib.uid = ?";
        ArrayList<String> vals = new ArrayList<>();
        vals.add("%" + title + "%");
        vals.add("" + uid);

        fetch(sql, vals);
    }

    public void fetchByLength(int minLength, int maxLength) throws DataLayerException {
        String sql = "SELECT lib.mediaid FROM movie "
                + "RIGHT JOIN user_media as lib ON lib.mediaid = movie.mediaid "
                + "LEFT JOIN tv_show as tv ON lib.mediaid = tv.mediaid "
                + "WHERE (episode_length > ? OR runtime > ?) "
                + "AND (episode_length < ? OR runtime < ?) "
                + "AND lib.uid = ?";
        ArrayList<String> vals = new ArrayList<>();
        vals.add("" + minLength);
        vals.add("" + minLength);
        vals.add("" + maxLength);
        vals.add("" + maxLength);
        vals.add("" + uid);

        fetch(sql, vals);
    }

    private void fetch(String sql, ArrayList<String> vals) throws DataLayerException {
        PostgreSQLDatabase db = new PostgreSQLDatabase();

        try {
            db.connect();

            ArrayList<ArrayList<String>> mediaIds = db.getData(sql, vals);
            for(ArrayList<String> row: mediaIds) {
                ownedMedia.add(Integer.valueOf(row.get(0)));
            }

            db.close();
        } catch (DataLayerException dle) {
            db.close();
            throw new DataLayerException(dle, "Error getting user library");
        }
    }

    public boolean postMediaToLibrary(int mediaId, String location) {
        String sql = "INSERT INTO user_media(uid, mediaid, location) VALUES(?, ?, ?)";
        ArrayList<String> vals = new ArrayList<>();
        vals.add("" + getUid());
        vals.add("" + mediaId);
        vals.add(location);

        PostgreSQLDatabase db = new PostgreSQLDatabase();

        try {
            db.connect();
            int numAffected = db.setData(sql, vals);
            db.close();

            return numAffected > 0;
        } catch (DataLayerException dle) {
            return false;
        }
    }

    //getter and setter
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public ArrayList<Integer> getOwnedMedia() {
        return ownedMedia;
    }

    public void setOwnedMedia(ArrayList<Integer> ownedMedia) {
        this.ownedMedia = ownedMedia;
    }

    /**
     * Static helper function to hash passwords
     *
     * @param password String to be hashed
     * @return hashed password
     * @throws DataLayerException
     */
    public static String hash(String password) throws DataLayerException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException nsae) {
            throw new DataLayerException(nsae);
        }
        md.update(salt);
        byte[] hashedPassByte = md.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashedPassByte);
    }
}
