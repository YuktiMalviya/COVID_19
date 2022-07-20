package com.example.covid19;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    List<YouTubeVideos> youTubeVideosList;

    public VideoAdapter() {
    }

    public VideoAdapter(List<YouTubeVideos> youTubeVideosList) {
        this.youTubeVideosList = youTubeVideosList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list,parent,false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.videoWeb.loadData(youTubeVideosList.get(position).getVideoUrl(),"text/html","utf-8");
    }

    @Override
    public int getItemCount() {
        return youTubeVideosList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        WebView videoWeb;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            videoWeb = itemView.findViewById(R.id.videoWebView);

            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.setWebChromeClient(new WebChromeClient());
        }
    }
}
