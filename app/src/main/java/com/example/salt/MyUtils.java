package com.example.salt;

public class MyUtils {
    private MyUtils() {}

    public static int[] getTimeComponents(long ms) {
        int[] components = new int[3];
        components[0] = (int) (ms / 1000 / 60 / 60 % 24); // get current hour from epoch time
        components[1] = (int) (ms / 1000 / 60 % 60); // get current minutes from epoch time
        components[2] = (int)(ms / 1000 % 60); // get current seconds from epoch time
        return components;
    }

    /*
    this method formats each time components to ensure they are returned 2 characters each
     */
    public static String[] formatTimeComponents(int[] components) {
        String[] timeStrings = new String[3];
        timeStrings[0] = components[0] >= 10 ? String.valueOf(components[0]) : String.valueOf("0" + components[0]);
        timeStrings[1] = components[1] >= 10 ? String.valueOf(components[1]) : String.valueOf("0" + components[1]);
        timeStrings[2] = components[2] >= 10 ? String.valueOf(components[2]) : String.valueOf("0" + components[2]);
        return timeStrings;
    }
}

