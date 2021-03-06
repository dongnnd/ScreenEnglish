package com.example.dongnd.screenenglish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class AlbumDetail extends AppCompatActivity {

    private int largeImg;
    private ImageView imageView;

    private Button btn_cancel, btn_thietlap;
    private TextClock textClock;
    private TextView textDate;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getData();

        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void init(){
        imageView=(ImageView)findViewById(R.id.gl_background);
        File file=new File(path);

        /*final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;*/
        Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
        imageView.setImageBitmap(bitmap);


        Typeface type = Typeface.createFromAsset(getAssets(), "lightfont.otf");
        textClock=(TextClock)findViewById(R.id.gl_clock);
        textClock.setTypeface(type);

        textDate=(TextView)findViewById(R.id.gl_date);
        textDate.setTypeface(type);

        btn_cancel=(Button)findViewById(R.id.gl_cancel);
        btn_cancel.setOnClickListener(cancel);
        btn_thietlap=(Button)findViewById(R.id.gl_thietlap);
        btn_thietlap.setOnClickListener(thietlap);
    }

    public View.OnClickListener cancel=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public View.OnClickListener thietlap=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences sharedPreferences=getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();

            editor.putString("background", path);
            editor.commit();

            Toast.makeText(getApplicationContext(),"Đã lưu thay đổi", Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    public void getData(){
        Bundle bundle=getIntent().getExtras();
        path=bundle.getString("path");
    }
}
