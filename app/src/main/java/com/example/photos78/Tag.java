package com.example.photos78;

import java.io.Serializable;

public class Tag implements Serializable {

    static final long serialVersionUID = 1L;

    private String name, value;

    /**
     * constructor for a tag
     * @param name the name/type of the tag
     * @param value the value of the tag
     */
    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * returns the name of the tag
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * returns the value of the tag
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * checks if this tag is equivalent to the given tag
     * @param o the tag that is checked for equality
     * @return true if the two tags are the same
     */
    public boolean equals(Object o) {
        if (o == null)
            return false;
        Tag tag = (Tag) o;
        if (name.equals("Location") && tag.getName().equals("Location"))
            return true;
        return name.equals(tag.getName()) && value.equals(tag.getValue());
    }

    public boolean searchEquals(Tag tag) {
        if (tag == null || !name.equals(tag.getName()))
            return false;

        if (tag.getValue().length() > value.length())
            return false;

        return (value.substring(0, tag.getValue().length()).equalsIgnoreCase(tag.getValue()));


    }

    /**
     * returns the String representation of this tag
     */
    public String toString() {
        return name + ": " + value;
    }
}
