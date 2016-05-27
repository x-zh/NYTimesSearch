package com.codepath.nytimessearch.activities;

import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.adapters.ArticleAdapter;
import com.codepath.nytimessearch.rest.RestClient;
import com.codepath.nytimessearch.rest.models.Article;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private final String BASE_URL = "https://api.nytimes.com/";
    RestClient restClient = new RestClient();

    @BindView(R.id.searchLayout)
    RelativeLayout searchLayout;

    @BindView(R.id.rvArticles)
    RecyclerView rvArticles;

    @BindView(R.id.tbSearch)
    Toolbar tbSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(tbSearch);
    }

    public void search(String query) {
        restClient.getApiService().searchArticle(query).enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                ArticleAdapter articleAdapter = new ArticleAdapter(response.body());
                rvArticles.setAdapter(articleAdapter);
                rvArticles.setLayoutManager(new GridLayoutManager(SearchActivity.this, 3));
                Snackbar.make(findViewById(R.id.searchLayout),
                        String.format("Loaded %d articles", response.body() != null ? response.body().size() : 0),
                        Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                Snackbar.make(findViewById(R.id.searchLayout),
                        String.format("Loading failed: %s", t.getMessage()),
                        Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }
}
