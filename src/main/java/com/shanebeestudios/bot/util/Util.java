package com.shanebeestudios.bot.util;

public class Util {

    public static String[] getSliceOfArray(String[] arr, int start, int end)
    {

        // Get the slice of the Array
        String[] slice = new String[end - start];

        // Copy elements of arr to slice
        for (int i = 0; i < slice.length; i++) {
            slice[i] = arr[start + i];
        }

        // return the slice
        return slice;
    }

    public static int getInt(String string) {
        try {
            int i = Integer.parseInt(string);
            return Math.min(i, 99);
        } catch (NumberFormatException ex) {
        }
        return 1;
    }

    public static int parseInt(String value) {
        try {
            int i = Integer.parseInt(value);
            return i;
        } catch (NumberFormatException ignore) {
            return 0;
        }
    }

}
