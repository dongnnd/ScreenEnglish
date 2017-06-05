package com.example.dongnd.screenenglish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import adapter.GalleryAdapter;
import adapter.GalleryItem;

public class ChoseImgGallery extends AppCompatActivity {

    GridView gridView;
    GalleryAdapter adapter;
    ArrayList<GalleryItem> arr;
    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_img_gallery);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        background=(ImageView)findViewById(R.id.gallery_background);
        background.setImageResource(R.drawable.bg_demo);

        arr=getGalleryItem();
        gridView=(GridView)findViewById(R.id.chose_img_gallery);
        adapter=new GalleryAdapter(this, R.layout.gallery_item, arr);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(largeImg);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public AdapterView.OnItemClickListener largeImg=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showLargeImg(arr.get(position).getImg_large());
        }
    };

    public void showLargeImg(int largeImg){
        Intent intent=new Intent(this, GalleryDetail.class);
        intent.putExtra("largeImg", largeImg);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public ArrayList<GalleryItem> getGalleryItem(){
        ArrayList<GalleryItem> arr=new ArrayList<>();
        arr.add(new GalleryItem(R.drawable.gallery_sm_01, R.drawable.gallery_01));
        arr.add(new GalleryItem(R.drawable.gallery_sm_02, R.drawable.gallery_02));
        arr.add(new GalleryItem(R.drawable.gallery_sm_03, R.drawable.gallery_03));
        arr.add(new GalleryItem(R.drawable.gallery_sm_04, R.drawable.gallery_04));
        arr.add(new GalleryItem(R.drawable.gallery_sm_05, R.drawable.gallery_05));
        arr.add(new GalleryItem(R.drawable.gallery_sm_06, R.drawable.gallery_06));
        arr.add(new GalleryItem(R.drawable.gallery_sm_07, R.drawable.gallery_07));
        arr.add(new GalleryItem(R.drawable.gallery_sm_08, R.drawable.gallery_08));
        arr.add(new GalleryItem(R.drawable.gallery_sm_09, R.drawable.gallery_09));
        arr.add(new GalleryItem(R.drawable.gallery_sm_10, R.drawable.gallery_10));
        arr.add(new GalleryItem(R.drawable.gallery_sm_11, R.drawable.gallery_11));
        arr.add(new GalleryItem(R.drawable.gallery_sm_12, R.drawable.gallery_12));

        return arr;
    }
}
