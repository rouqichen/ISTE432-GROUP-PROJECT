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

    //get from db
    public void fetch() throws DataLayerException {
        String sql = "SELECT mediaid FROM user_media WHERE uid = ?";
        ArrayList<String> vals = new ArrayList<>();
        vals.add("" + uid);

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
