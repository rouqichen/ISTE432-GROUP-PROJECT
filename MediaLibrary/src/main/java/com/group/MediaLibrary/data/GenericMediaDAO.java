package com.group.MediaLibrary.data;

public class GenericMediaDAO extends MediaDAO {

    @Override
    public boolean getMedia() throws DataLayerException {
        return false;
    }

    @Override
    public boolean saveTypeSpecificData() throws DataLayerException {
        return false;
    }

    public GenericMediaDAO(int mediaid) {
        super(mediaid);
    }
}
