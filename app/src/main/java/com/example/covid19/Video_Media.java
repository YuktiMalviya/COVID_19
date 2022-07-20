package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.Vector;

public class Video_Media extends AppCompatActivity {

    RecyclerView recyclerView;
    Vector<YouTubeVideos> youTubeVideos = new Vector<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video__media);

        recyclerView = findViewById(R.id.video_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        youTubeVideos.add(new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/LLEM5kzPE3c\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        youTubeVideos.add(new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/YlafvtwzSho\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        youTubeVideos.add(new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/phc1UAQQ81s\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        youTubeVideos.add(new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/leht6mIk8fA\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        youTubeVideos.add(new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Jkp-Y8lQLPs\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        youTubeVideos.add(new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/OQSadxYAiZQ\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        youTubeVideos.add(new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/ykFkRSjvDPM\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        youTubeVideos.add(new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Aqy3yajOJ0s\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));
        youTubeVideos.add(new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Aqy3yajOJ0s\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>"));

        VideoAdapter videoAdapter = new VideoAdapter(youTubeVideos);
        recyclerView.setAdapter(videoAdapter);

    }
}
