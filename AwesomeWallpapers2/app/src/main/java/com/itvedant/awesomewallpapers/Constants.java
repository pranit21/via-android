package com.itvedant.awesomewallpapers;

/**
 * Created by Pranit on 26-08-2016.
 */

public class Constants {
    public static final String FLICKR_API_KEY = "ff2c27b56a8c4af4861d4f4124d7668d";

    public static final String API_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key="+FLICKR_API_KEY+"&tags=nature&tag_mode=any&per_page=25&page=1&format=json&nojsoncallback=1";;

    // Number of columns of Grid View
    public static final int NO_OF_GRID_COLUMNS = 3; // in dp

    // Gridview image padding
    public static final int GRID_PADDING = 4; // in dp

    public static final String PHOTO_URL = "photo_url";

    public static final int IMAGE_WIDTH = 800; // in dp
    public static final int IMAGE_HEIGHT = 800; // in dp
}
