package com.example.dongnd.screenenglish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import databases.DbAdapter;

public class ChoseGrammar extends AppCompatActivity {

    private int gmId;
    private String gmTitle, gmScore;
    private TextView title, score;
    private DbAdapter db;
    private ImageView show;
    private String grammar;
    private Button gm_button;

    private TextView socaudung, trangthai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_grammar);

        getData();

        db=new DbAdapter(this);
        grammar=db.getExGrammar(gmId);


        if(db.getScore(gmId)==null){
            Toast.makeText(getApplicationContext(),"Cơ sở dữ liệu chưa sẵn sàng", Toast.LENGTH_SHORT).show();
        }else{
            setNameTitle();
        }


    }



    public void setNameTitle(){

        show=(ImageView)findViewById(R.id.chose_show);
        show.setOnClickListener(showGrammar);

        gm_button=(Button)findViewById(R.id.chose_button);
        gm_button.setOnClickListener(gmButton);

        title=(TextView)findViewById(R.id.title);
        title.setText(gmTitle);

        score=(TextView)findViewById(R.id.chose_socaudung);
        score.setText(db.getScore(gmId));

        socaudung=(TextView)findViewById(R.id.chose_socaudung);
        trangthai=(TextView)findViewById(R.id.chose_trangthai);
        String state=db.getStateGrammar(gmId);
        if(state.equals("Bạn chưa học bài này")){
            socaudung.setText("Số câu đúng: 0");
            trangthai.setText("(Chưa hoàn thành)");
        }else{
            socaudung.setText("Số câu đúng: "+state);
            if(Integer.parseInt(state)/30>0.8){
                trangthai.setText("Hoàn thành");

            }else{
                trangthai.setText("(Chưa hoàn thành)");
            }
        }
    }

    public void getData(){
        Bundle bundle=getIntent().getExtras();
        gmId=bundle.getInt("gmId");
        gmTitle=bundle.getString("gmTitle");
        gmScore=bundle.getString("gmScore");


    }

    public View.OnClickListener showGrammar=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(ChoseGrammar.this, GrammarExplain.class);
            intent.putExtra("grammar", grammar);
            startActivity(intent);
        }
    };

    public View.OnClickListener gmButton=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*Intent intent=new Intent(ChoseGrammar.this, PracticeDetail.class);
            intent.putExtra("gmId", gmId);
            startActivityForResult(intent, 2);*/
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==2){
            Bundle bundle=data.getBundleExtra("result");
            String numberTrue=bundle.getString("numberTrue");

            score.setText("Số câu đúng:"+numberTrue);
            int number=Integer.parseInt(numberTrue);
            if((float)number/20>0.8){
                trangthai.setText("(Đã hoàn thành)");
            }else{
                trangthai.setText("(Chưa hoàn thành)");
            }
        }
    }
}
