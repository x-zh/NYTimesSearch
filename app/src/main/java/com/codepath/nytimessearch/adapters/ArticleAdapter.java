package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.rest.models.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by charlie_zhou on 5/25/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> articles = new ArrayList<Article>();

    public ArticleAdapter() {
    }

    public void addAll(List<Article> articles) {
        this.articles.addAll(articles);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.tvHeadLine)
        TextView tvHeadline;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_article, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.ivImage.setImageResource(0);
        holder.tvHeadline.setText(article.getHeadline().getMain());
        if (article.getImageUrl() != null) {
            Picasso.with(holder.ivImage.getContext())
                    .load(article.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit().centerCrop().into(holder.ivImage);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
