package com.group.MediaLibrary.business;

import com.group.MediaLibrary.data.DataLayerException;
import com.group.MediaLibrary.data.UserDAO;

import java.util.ArrayList;

public class Library {

    private int userId;
    private ArrayList<Media> library;

    public Library(int userId) {

        this.userId = userId;
        loadLibrary();

    }

    /**
     * Using set user id, loads the user's full library into the array list
     */
    private boolean loadLibrary() {
        UserDAO user = new UserDAO(userId);
        library = new ArrayList<>();

        try {
            user.fetch();

            ArrayList<Integer> mediaIds = user.getOwnedMedia();
            for(Integer mediaId: mediaIds) {
                Media media = new Media(mediaId);
                library.add(media.getTypeMedia());
            }

        } catch (DataLayerException dle) {
            return false;
        }
        return true;
    }

    //basic getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ArrayList<Media> getLibrary() {
        return library;
    }

    public void setLibrary(ArrayList<Media> library) {
        this.library = library;
    }
}
