package com.codepath.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.adapters.ArticleAdapter;
import com.codepath.nytimessearch.fragments.FilterDialogFragment;
import com.codepath.nytimessearch.helpers.EndlessRecyclerViewScrollListener;
import com.codepath.nytimessearch.helpers.ItemClickSupport;
import com.codepath.nytimessearch.models.Filter;
import com.codepath.nytimessearch.rest.RestClient;
import com.codepath.nytimessearch.rest.models.Article;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogListener {
    private final String BASE_URL = "https://api.nytimes.com/";
    private RestClient restClient = new RestClient();
    private Filter filter = new Filter();
    private FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance(filter);
    private ArticleAdapter articleAdapter = new ArticleAdapter();

    @BindView(R.id.searchLayout)
    RelativeLayout searchLayout;

    @BindView(R.id.rvArticles)
    RecyclerView rvArticles;

    @BindView(R.id.tbSearch)
    Toolbar tbSearch;

    MenuItem miActionProgressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setSupportActionBar(tbSearch);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                4, StaggeredGridLayoutManager.VERTICAL);
        rvArticles.setAdapter(articleAdapter);
        rvArticles.setLayoutManager(staggeredGridLayoutManager);
        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                filter.setPage(page);
                search(false);
            }
        });
        ItemClickSupport.addTo(rvArticles).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(SearchActivity.this, ArticleActivity.class);
                Article article = articleAdapter.getItem(position);
                intent.putExtra("url", article.getWebUrl());
                intent.putExtra("title", article.getHeadline().getMain());
                startActivity(intent);
            }
        });
//        if (!isNetworkAvailable()) {
//            Snackbar.make(findViewById(R.id.searchLayout),
//                    String.format("Network is not available."),
//                    Snackbar.LENGTH_LONG).show();
//        }
        search(true);
    }

    public void search(boolean clear) {
        if (miActionProgressItem != null) miActionProgressItem.setVisible(true);
        if (clear) {
            articleAdapter.clear();
            articleAdapter.notifyDataSetChanged();
        }
        restClient.getApiService().searchArticle(filter.getQuery(),
                filter.getBeginDateText(),
                filter.getSortOrder(),
                filter.getNewsType(),
                filter.getPage()).enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                if (response.body() != null) {
                    articleAdapter.addAll(response.body());
                    int curSize = articleAdapter.getItemCount();
                    articleAdapter.notifyItemRangeChanged(curSize, response.body().size());
                } else {
                    Snackbar.make(findViewById(R.id.searchLayout),
                            String.format("Loaded 0 articles"),
                            Snackbar.LENGTH_LONG).show();
                }
                miActionProgressItem.setVisible(false);
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                Snackbar.make(findViewById(R.id.searchLayout),
                        String.format("Loading failed: %s", t.getMessage()),
                        Snackbar.LENGTH_LONG)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                search(false);
                            }
                        }).show();

                miActionProgressItem.setVisible(false);
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
                filter.setQuery(query);
                search(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                showFilterDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showFilterDialog() {
        FragmentManager manager = getSupportFragmentManager();
        filterDialogFragment.show(manager, "filter_dialog_fragment");
    }

    @Override
    public void onSaveFilterDialog(Filter filter) {
        this.filter = filter;
        search(true);
    }

//    private Boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
//    }
}
