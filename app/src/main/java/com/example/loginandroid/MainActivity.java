package com.example.loginandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginandroid.models.GithubRepo;
import com.example.loginandroid.models.GithubUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<GithubUser> {

    public static final String BASE_URL = "http://localhost:8000/api";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    //When clicked
    public void onClick(View view) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        //The Retrofit class generates an implementation of the GitHubService interface.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GithubAPI.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        GithubAPI githubUserAPI = retrofit.create(GithubAPI.class);

        //Which button pressed
        switch (view.getId()) {
            case R.id.loadUserData:
                // prepare call in Retrofit 2.0
                Call<GithubUser> callUser = githubUserAPI.getUser("d3wo");
                //asynchronous call
                callUser.enqueue(this);
                break;

            case R.id.loadRepositories:
                Call<List<GithubRepo>> callRepos = githubUserAPI.getRepos("d3wo");
                //asynchronous call
                callRepos.enqueue(repos);
                break;
        }
    }


    Callback repos = new Callback<List<GithubRepo>>(){

        @Override
        public void onResponse(Call<List<GithubRepo>> call, Response<List<GithubRepo>> response) {
            if (response.isSuccessful()) {
                List<GithubRepo> repos = response.body();
                StringBuilder builder = new StringBuilder();
                for (GithubRepo repo: repos) {
                    builder.append(repo.name + " " + repo.toString() + "\n");
                }
                Toast.makeText(MainActivity.this, builder.toString(), Toast.LENGTH_SHORT).show();
                TextView text = (TextView) findViewById(R.id.txtView);
                text.setText(builder.toString());

            } else
            {
                Toast.makeText(MainActivity.this, "Error code " + response.code(), Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onFailure(Call<List<GithubRepo>> call, Throwable t) {
            Toast.makeText(MainActivity.this, "Did not work " +  t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onResponse(Call<GithubUser> call, Response<GithubUser> response) {
        int code = response.code();
        if (code == 200) {
            GithubUser user = response.body();
            Toast.makeText(this, "Got the user: " + user.email, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Did not work: " + String.valueOf(code), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<GithubUser> call, Throwable t) {
        Toast.makeText(this, "Nope", Toast.LENGTH_LONG).show();

    }


}
