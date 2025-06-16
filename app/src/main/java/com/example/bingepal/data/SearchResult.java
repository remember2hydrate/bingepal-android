package com.example.bingepal.data;

public class SearchResult {
    public String id;
    public String title;
    public String type;
    public String description;
    public String poster_url;
    public Integer year;
    public String source;

    // Optional, shown in some results
    public String[] genres;
    public Float rating;
    public Integer rating_count;
    public Integer total_seasons;
    public Integer total_episodes;
    public Integer average_duration;
}
