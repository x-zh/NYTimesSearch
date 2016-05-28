package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.rest.models.Article;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by charlie_zhou on 5/25/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int WITH_PHOTO = 1;
    private final int TEXT_ONLY = 2;


    private List<Article> articles = new ArrayList<Article>();

    public ArticleAdapter() {
    }

    public void addAll(List<Article> articles) {
        this.articles.addAll(articles);
    }

    public void clear() { this.articles.clear(); }


    public static class WithPhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImage)
        ImageView ivImage;

        @BindView(R.id.tvHeadLine)
        TextView tvHeadline;

        public WithPhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class TextOnlyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvHeadLine)
        TextView tvHeadLine;

        public TextOnlyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder;
        // Inflate the custom layout
        View contactView;
        switch (viewType) {
            case WITH_PHOTO:
                contactView = inflater.inflate(R.layout.item_article_with_photo, parent, false);
                viewHolder = new WithPhotoViewHolder(contactView);
                break;
            default:
                contactView = inflater.inflate(R.layout.item_article_text_only, parent, false);
                viewHolder = new TextOnlyViewHolder(contactView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = articles.get(position);
        switch (holder.getItemViewType()) {
            case WITH_PHOTO:
                WithPhotoViewHolder holder2 = (WithPhotoViewHolder) holder;
                holder2.ivImage.setImageResource(0);
                holder2.tvHeadline.setText(article.getHeadline().getMain());
                if (article.getImageUrl() != null) {
                    Glide.with(holder2.ivImage.getContext())
                            .load(article.getImageUrl())
                            .placeholder(R.mipmap.ic_launcher)
                            .fitCenter().into(holder2.ivImage);
                }
                break;
            default:
                TextOnlyViewHolder holder1 = (TextOnlyViewHolder) holder;
                holder1.tvHeadLine.setText(article.getHeadline().getMain());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (articles.get(position).getImageUrl() == null) ? TEXT_ONLY : WITH_PHOTO;
    }

    public Article getItem(int position) {
        return articles.get(position);
    }
}
