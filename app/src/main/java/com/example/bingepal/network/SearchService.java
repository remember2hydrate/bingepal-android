package com.example.bingepal.network;
import com.example.bingepal.data.LogEntry;
import com.example.bingepal.data.SearchResult;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SearchService {
    @GET("api/search")
    Call<List<SearchResult>> searchContent(
            @Query("query") String query,
            @Query("type") String type
    );

    @GET("api/detail")
    Call<SearchResult> getDetail(
            @Query("type") String type,
            @Query("id") String id
    );

    @POST("/api/log")
    Call<Void> logSearch(@Body LogEntry entry);
}

