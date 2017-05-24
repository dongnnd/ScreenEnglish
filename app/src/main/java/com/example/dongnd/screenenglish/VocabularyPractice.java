package com.example.dongnd.screenenglish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VocabularyPractice extends AppCompatActivity {

    private TextView vp_text;
    private String title="(Bạn có thể học các từ vựng đã chọn sai trên màn hình khóa hoặc tạo mới " +
            "từ danh sách bằng cách chọn chức năng tương ứng)";

    private Button vp_chose, vp_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_practice);

        init();
    }

    public void init(){
        vp_text=(TextView)findViewById(R.id.vp_text);
        vp_text.setText(title);

        vp_chose=(Button)findViewById(R.id.vp_chose_list);
        vp_chose.setOnClickListener(vpClick);
        vp_error=(Button)findViewById(R.id.vp_error_list);
        vp_error.setOnClickListener(vpClick);
    }

    public View.OnClickListener vpClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           if(v.getId()==R.id.vp_chose_list){
               Intent intent=new Intent(getApplicationContext(), VocabularyNewList.class);
               startActivity(intent);
           }else if(v.getId()==R.id.vp_error_list){
               Intent intent=new Intent(getApplicationContext(), VocabularyError.class);
               intent.putExtra("type","list_error");
               startActivity(intent);
           }
        }
    };
}
