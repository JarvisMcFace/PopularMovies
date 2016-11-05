package com.hughesdigitalimage.popularmovies.util;

import java.util.List;

/**
 * Created by David on 11/4/16.
 */

public abstract class ListUtils {

    public ListUtils() {
    }

    public static <T> boolean isEmpty(List<T> list) {
        if (list == null) return true;
        if (list.size() == 0) return true;
        return false;
    }

    public static <T> boolean isNotEmpty(List<T> list){
        return !isEmpty(list);
    }
}
