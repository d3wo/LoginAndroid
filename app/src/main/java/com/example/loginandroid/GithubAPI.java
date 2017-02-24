package com.example.loginandroid;

import com.example.loginandroid.models.GithubRepo;
import com.example.loginandroid.models.GithubUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Dewo on 2/24/2017.
 */

public interface GithubAPI {

    String ENDPOINT = "https://api.github.com";

    @GET("/users/{user}")
    Call<GithubUser> getUser(@Path("user") String user);

    @GET("users/{user}/repos")
    Call<List<GithubRepo>> getRepos(@Path("user") String user);


}
