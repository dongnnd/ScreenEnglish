package com.example.dongnd.screenenglish;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;

import adapter.OnSwipeTouchListener;
import databases.DbAdapter;

public class VocabularyEEDic extends AppCompatActivity{

    private WebView eedic_web;
    private DbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_eedic);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        db=new DbAdapter(this);
        Bundle bundle=getIntent().getExtras();
        int id=bundle.getInt("id");

        Log.d("tag", "A"+id);
        String eedic=db.getEEMean(id);

        eedic_web=(WebView)findViewById(R.id.eedic_web);
        eedic_web.loadData(eedic, "text/html; charset=utf-8", "UTF-8");

        eedic_web.setOnTouchListener(new OnSwipeTouchListener(this){
            public void onSwipeRight() {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }


        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }



}
