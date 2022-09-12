package com.example.photos78;

import android.net.Uri;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Photo implements Serializable {

    static final long serialVersionUID = 1L;

    private String caption;

    private String stringUri;

    private ArrayList<Tag> tags;

    /**
     * constructor for a photo with no tags
     * @param caption the caption for the photo
     * @param stringUri the path of the photo, can be either global or local path
     */
    public Photo(String caption, String stringUri) {
        this.caption = caption;
        this.stringUri = stringUri;
        tags = new ArrayList<Tag>();
    }

    /**
     * constructor for a photo that already has tags
     * @param caption the caption for the photo
     * @param stringUri the path of the photo, can be either global or local path
     * @param tags the list of tags for the photo
     */
    public Photo(String caption, String stringUri, ArrayList<Tag> tags) {
        this.caption = caption;
        this.stringUri = stringUri;
        this.tags = tags;
    }

    /**
     * adds a tag to the photo's list of tags
     * @param name the name/type of the tag, e.g. location or person
     * @param value the value of the tags
     * @return true if the tag has been added to the list
     */
    public boolean addTag(String name, String value) {

        Tag newTag = new Tag(name, value);

        for (Tag tag: tags) {
            if (newTag.equals(tag))
                return false;
            if (newTag.getName().equals("location") && tag.getName().equals("location")) {
                return false;
            }
        }

        tags.add(newTag);
        return true;

    }

    /**
     * removes the given tag
     * @param tag the tag that should be removed
     */
    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    /**
     * returns the list of tags
     * @return tags list
     */
    public ArrayList<Tag> getTags() {
        return tags;
    }

    /**
     * returns the string representation of the tag list
     * @return tag list
     */
    public String printTags() {
        String result = "[";
        for (Tag tag: tags) {
            result = result + tag.toString() + ", ";
        }
        if (tags.size() == 0)
            return "[]";

        return result.substring(0, result.length()-2) + "]";
    }

    /**
     * sets photo caption to given name
     * @param name the new caption
     */
    public void setCaption(String name) {
        caption = name;
    }

    /**
     * returns the caption of the photo
     * @return caption
     */
    public String getCaption() {
        return caption;
    }

    /**
     * returns the path of the photo
     * @return photo
     */
    public String getStringUri() {
        return stringUri;
    }

    /**
     * checks if this photo is equal to the given photo
     * @param o the photo that is checked for equality
     * @return true if the two photos contain the same information
     */
    public boolean equals(Object o) {
        if(o == null)
            return false;
        Photo photo = (Photo) o;
        return stringUri.equals(photo.getStringUri());
    }

    /**
     * returns the string representation of the photo
     */
    public String toString() {
        return caption + " \"" + stringUri + "\"";
    }

}
