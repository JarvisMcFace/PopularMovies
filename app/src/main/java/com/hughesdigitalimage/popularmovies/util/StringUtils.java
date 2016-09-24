package com.hughesdigitalimage.popularmovies.util;

/**
 * Created by David on 9/23/16.
 */

public class StringUtils {

    private StringUtils(){}

    public static boolean isEmpty (String value){
        return value == null || value.trim().length()==0;
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }
}
