package com.example.fossilandroidexam;

public class User {
    public String display_name;
    public String profile_image;
    public String reputation;
    public String location;
    public int age;
    public String user_id;

    @Override
    public String toString() {
        return  display_name
                + "\n " + reputation
                + ", " + location
                + ", " + age;
    }
}
