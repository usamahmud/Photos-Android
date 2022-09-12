package com.example.photos78;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable{

    static final long serialVersionUID = 1L;

    private String name;

    private ArrayList<Photo> photos;

    /**
     * constructor for Album
     * the album initially contains no photos
    // * @param user the user associated with the album
     * @param albumName the desired name for the album
     */
    public Album( String albumName) {
        name = albumName;
        photos = new ArrayList<Photo>();
    }

    /**
     * sets album name
     * @param aName the desired new name for album
     */
    public void setName(String aName) {
        name = aName;
    }

    /**
     * returns the album name
     * @return the album name
     */
    public String getName() {
        return name;
    }


    /**
     * adds the new photo to the albums list of photos
     * @param newPhoto the photo to be added
     * @return true if the photo has been added
     */
    public boolean addPhoto(Photo newPhoto) {
        if (photos.contains(newPhoto)) {
            return false;
        }

        photos.add(newPhoto);
        return true;
    }

    /**
     * returns the list of photos for this album
     * @return the photo list
     */
    public ArrayList<Photo> getPhotoList() {
        return photos;
    }

    public void setPhotoList(ArrayList<Photo> photoList) {
        this.photos = photoList;
    }


    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }
        Album album = (Album) o;
        return name.equals(album.getName());

    }

}
