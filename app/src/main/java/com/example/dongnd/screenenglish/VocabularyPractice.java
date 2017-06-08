package com.example.dongnd.screenenglish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VocabularyPractice extends AppCompatActivity {



    private Button vp_chose, vp_error, vp_remind;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_practice);

        sharedPreferences=getSharedPreferences("data", MODE_PRIVATE);

        init();
    }

    public void init(){

        vp_chose=(Button)findViewById(R.id.vp_chose_list);
        vp_chose.setOnClickListener(vpClick);
        vp_error=(Button)findViewById(R.id.vp_error_list);
        vp_error.setOnClickListener(vpClick);

        vp_remind=(Button)findViewById(R.id.vp_chose_remind);
        vp_remind.setOnClickListener(vpClick);
    }

    public View.OnClickListener vpClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           if(v.getId()==R.id.vp_chose_list){
               Intent intent=new Intent(getApplicationContext(), VocabularyNewList.class);
               startActivity(intent);
           }else if(v.getId()==R.id.vp_error_list){
               String list_error=sharedPreferences.getString("list_error", null);
               if(list_error==null|| list_error==""){
                   Toast.makeText(getApplicationContext(), "Danh sách hiện tại trống", Toast.LENGTH_SHORT).show();
               }else{
                   Intent intent=new Intent(getApplicationContext(), VocabularyError.class);
                   startActivity(intent);
               }

           }else if(v.getId()==R.id.vp_chose_remind){
                Intent intent=new Intent(getApplicationContext(), VocabularyRemind.class);
               startActivity(intent);
           }
        }
    };


}
