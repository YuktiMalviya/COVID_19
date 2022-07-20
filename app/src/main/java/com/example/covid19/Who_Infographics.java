package com.example.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Who_Infographics extends AppCompatActivity {

    private ViewPager2 viewPager2;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who__infographics);

        viewPager2 = findViewById(R.id.viewPagerImageSlider);

        final List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.image1));
        sliderItems.add(new SliderItem(R.drawable.image2));
        sliderItems.add(new SliderItem(R.drawable.image3));
        sliderItems.add(new SliderItem(R.drawable.image4));
        sliderItems.add(new SliderItem(R.drawable.image5));
        sliderItems.add(new SliderItem(R.drawable.image6));
        sliderItems.add(new SliderItem(R.drawable.image7));

        viewPager2.setAdapter(new SliderAdapter(sliderItems,viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r + 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                handler.removeCallbacks(sliderRunnable);
//                handler.postDelayed(sliderRunnable,3000);
//
//            }
//        });
    }

//    private Runnable sliderRunnable =  new Runnable() {
//        @Override
//        public void run() {
//            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
//        }
//    };
}
