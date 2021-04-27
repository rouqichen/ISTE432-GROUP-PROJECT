package com.group.MediaLibrary.data;

import java.util.ArrayList;

public class UserDAO {

    private int uid;
    private ArrayList<Integer> ownedMedia;

    public UserDAO(int uid) {
        this.uid = uid;
        ownedMedia = new ArrayList<>();
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
}
