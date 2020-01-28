package com.example.startproject2;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    MovieAdapter adapter;
    RecyclerView recyclerView;

    Movie movie;
    String clientId = "1o5FdWHChJv14R9eO8pE";
    String clientSecret = "nWwyegGVn4";
    static RequestQueue requestQueue;
    String text;
    String urlStr = "https://openapi.naver.com/v1/search/movie.json?query=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_search, container, false);

        adapter = new MovieAdapter();
        recyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        SearchView searchView = rootView.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                movie.title = query;
                adapter.notifyDataSetChanged();
                try {
                    text = URLEncoder.encode(movie.title, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                makeRequest();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        movie = new Movie();

        movie.title = "movie title";
        movie.director = "director";
        movie.actor = "person1, pesron2, person3";
        movie.userRating = 5;

        adapter.rootActivity = rootView;
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        return rootView;
    }
    private void makeRequest(){
        urlStr += text;
        StringRequest request = new StringRequest(Request.Method.GET, urlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processResponse(response);
                Log.d("Request", "success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Request", "Error");
            }
        }) {@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-Naver-Client-Id", clientId);
                params.put("X-Naver-Client-Secret", clientSecret);
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }
    private void processResponse(String response){
        Gson gson = new Gson();
        MovieList movieList = gson.fromJson(response, MovieList.class);

        for(int i=0; i<movieList.items.size(); i++){
            Movie movie = movieList.items.get(i);
            adapter.addItem(movie);
        }
        adapter.notifyDataSetChanged();
    }
}
