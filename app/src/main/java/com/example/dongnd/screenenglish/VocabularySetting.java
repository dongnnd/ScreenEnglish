package com.example.dongnd.screenenglish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class VocabularySetting extends AppCompatActivity {

    public TextView vcst_subject_title, vcst_subject_curent;
    public ImageView vcst_subject_img;

    public RadioGroup vcst_rd_phienam, vcst_rd_lap;
    public RadioButton vcst_rd_aa, vcst_rd_am, vcst_rd_lap1, vcst_rd_lap2;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_setting);

        sharedPreferences=getSharedPreferences("data", MODE_PRIVATE);
        editor=sharedPreferences.edit();


        initVocabularySetting();
        loadSharePreferences();

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void loadSharePreferences(){
        String subjectSelect=sharedPreferences.getString("subjectselect", null);
        String phienam=sharedPreferences.getString("phienam", null);
        String lap=sharedPreferences.getString("lap", null);

        if(subjectSelect==null){
            editor.putString("subjectselect","Kinh Doanh");
            editor.commit();
            vcst_subject_curent.setText("Chủ đề hiện tại: Kinh Doanh");
        }else{
            vcst_subject_curent.setText("Chủ đề hiện tại: "+subjectSelect);
        }

        if(phienam==null){
            editor.putString("phienam","anh-anh");
            editor.commit();
            vcst_rd_phienam.check(R.id.vcst_rd_aa);
        }else{
            if(phienam.equals("anh-anh")){
                vcst_rd_phienam.check(R.id.vcst_rd_aa);
            }else{
                vcst_rd_phienam.check(R.id.vcst_rd_am);
            }
        }

        if(lap==null){
            editor.putString("lap","lap1");
            editor.commit();
            vcst_rd_lap.check(R.id.vcst_rd_lap1);
        }else {
            if(lap.equals("lap1")){
                vcst_rd_lap.check(R.id.vcst_rd_lap1);
            }else{
                vcst_rd_lap.check(R.id.vcst_rd_lap2);
            }
        }
    }

    public void initVocabularySetting(){
        vcst_subject_title=(TextView)findViewById(R.id.vcst_subject_title);
        vcst_subject_title.setOnClickListener(settingSubject);
        vcst_subject_img=(ImageView)findViewById(R.id.vcst_subject_img);
        vcst_subject_img.setOnClickListener(settingSubject);

        vcst_subject_curent=(TextView)findViewById(R.id.vcst_subject_curent);

        vcst_rd_phienam=(RadioGroup)findViewById(R.id.vcst_radio_phienam);
        vcst_rd_phienam.setOnCheckedChangeListener(radioPhienam);
        vcst_rd_lap=(RadioGroup)findViewById(R.id.vcst_radio_lap);
        vcst_rd_lap.setOnCheckedChangeListener(radioLap);

        vcst_rd_aa=(RadioButton)findViewById(R.id.vcst_rd_aa);
        vcst_rd_am=(RadioButton)findViewById(R.id.vcst_rd_am);

        vcst_rd_lap1=(RadioButton)findViewById(R.id.vcst_rd_lap1);
        vcst_rd_lap2=(RadioButton)findViewById(R.id.vcst_rd_lap2);
    }

    public View.OnClickListener settingSubject=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startSettingSubject();
        }
    };

    public void startSettingSubject(){
        Intent intent=new Intent(this, SubjectVocabulary.class);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data==null){
            return;
        }

        if(requestCode==1){
            Bundle bundle=data.getBundleExtra("titleSubject");
            String numberSubject=bundle.getString("titleSubject");
            vcst_subject_curent.setText("Chủ đề hiện tại: "+numberSubject);
        }
    }

    public RadioGroup.OnCheckedChangeListener radioPhienam=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            if(checkedId==R.id.vcst_rd_aa){
                editor.putString("phienam","anh-anh");
                editor.commit();
            }
            if(checkedId==R.id.vcst_rd_am){
                editor.putString("phienam", "anh-my");
                editor.commit();
            }
        }
    };

    public RadioGroup.OnCheckedChangeListener radioLap=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            if(checkedId==R.id.vcst_rd_lap1){
                editor.putString("lap","lap1");
                editor.commit();
            }
            if(checkedId==R.id.vcst_rd_lap2){
                editor.putString("lap","lap2");
                editor.commit();
            }
        }
    };
}
