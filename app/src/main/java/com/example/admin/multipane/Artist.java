package com.example.admin.multipane;

/**
 * Created by Admin on 8/9/2017.
 */

public class Artist {
    int id;
    String name;
    String nationality;
    String gender;
    byte[] bitmap;

    public Artist(int id, String name, String nationality, String gender, byte[] bitmap) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.gender = gender;
        this.bitmap = bitmap;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getNationality() { return nationality; }
    public String getGender() { return gender; }
    public byte[] getBitmap() { return bitmap; }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBitmap(byte[] bitmap) {
        this.bitmap = bitmap;
    }
}
