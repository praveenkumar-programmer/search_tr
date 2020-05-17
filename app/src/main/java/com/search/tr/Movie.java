package com.search.tr;

import java.util.List;

public class Movie {
    private String normalized_name, name, url;
    //private String thumbnailUrl;
    private List<String> magnets;

    public Movie() { }

    public String getName() { return name; }

    public String getNormalized_name() {
        return normalized_name;
    }

    public List<String> getMagnets() { return magnets;}

    public String getUrl() { return url;}

    //public String getThumbNailUrl() { return thumbnailUrl;}

}
