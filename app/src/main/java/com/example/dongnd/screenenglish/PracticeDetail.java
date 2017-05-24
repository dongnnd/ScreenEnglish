package com.example.dongnd.screenenglish;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import adapter.CurrentQuestion;
import databases.DbAdapter;

public class PracticeDetail extends AppCompatActivity {

    private int gmId;
    private int currentQs=0;
    private float tile=0;
    private DbAdapter db;
    private List<CurrentQuestion> list=new ArrayList<>();

    private TextView pr_cau, pr_tile, pr_question, pr_explain, pr_result;
    private RadioGroup pr_Group;
    private RadioButton pr_rd_01, pr_rd_02, pr_rd_03;
    private Button pr_btn_pre, pr_btn_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_detail);

        Bundle bundle=getIntent().getExtras();
        gmId=bundle.getInt("gmId");

        db=new DbAdapter(this);
        list=db.getAllQuestionId(gmId);
        init();

        getFirstQuestion();
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    public void sendResult(){
        Intent intent=getIntent();
        Bundle bundle=new Bundle();
        bundle.putString("numberTrue", getNumberTrue());
        intent.putExtra("result", bundle);
        setResult(2, intent);
    }

    public void init(){
        pr_cau=(TextView)findViewById(R.id.pr_cau);
        pr_tile=(TextView)findViewById(R.id.pr_tile);
        pr_explain=(TextView)findViewById(R.id.pr_explain);
        pr_question=(TextView)findViewById(R.id.pr_question);

        pr_Group=(RadioGroup)findViewById(R.id.pr_radioGr);
        pr_Group.setOnCheckedChangeListener(prGroup);
        pr_rd_01=(RadioButton)findViewById(R.id.pr_rd_01);
        pr_rd_02=(RadioButton)findViewById(R.id.pr_rd_02);
        pr_rd_03=(RadioButton)findViewById(R.id.pr_rd_03);

        pr_explain=(TextView)findViewById(R.id.pr_explain);
        pr_result=(TextView)findViewById(R.id.pr_result);

        pr_btn_pre=(Button)findViewById(R.id.pr_btn_pre);
        pr_btn_pre.setOnClickListener(btnPre);
        pr_btn_next=(Button)findViewById(R.id.pr_btn_next);
        pr_btn_next.setOnClickListener(btnNext);
        if(currentQs==0){
            pr_btn_pre.setEnabled(false);
        }
    }

    public void getFirstQuestion(){
        pr_cau.setText("Câu "+(currentQs+1)+"/20");
        pr_tile.setText("Tỉ lệ: 0%");
        pr_question.setText(list.get(currentQs).getQuestion());
        radomAnswer(list.get(currentQs).getTr_answear(), list.get(currentQs).getErr1_answear(), list.get(currentQs).getErr2_answear());

    }

    public void loadStateQuestion(){
        radomAnswer(list.get(currentQs).getTr_answear(), list.get(currentQs).getErr1_answear(), list.get(currentQs).getErr2_answear());
        if(list.get(currentQs).getSelect()!=null){
            String answer=list.get(currentQs).getSelect();
            if(pr_rd_01.getText().toString().equals(answer)){
                pr_rd_01.setChecked(true);
                if(pr_rd_01.getText().toString().equals(list.get(currentQs).getTr_answear())){
                    pr_result.setText("Đáp án chính xác");
                }else{
                    pr_result.setText("Đáp án không chính xác");
                }
            }else if(pr_rd_02.getText().toString().equals(answer)){
                pr_rd_02.setChecked(true);
                if(pr_rd_02.getText().toString().equals(list.get(currentQs).getTr_answear())){
                    pr_result.setText("Đáp án chính xác");
                }else{
                    pr_result.setText("Đáp án không chính xác");
                }
            }else{
                if(pr_rd_03.getText().toString().equals(list.get(currentQs).getTr_answear())){
                    pr_result.setText("Đáp án chính xác");
                }else{
                    pr_result.setText("Đáp án không chính xác");
                }
                pr_rd_03.setChecked(true);
            }
            disableRadio();
        }else{
            enableRadio();
        }

    }

    public void radomAnswer(String tr, String err1, String err2){
        Random R=new Random();
        int rd1=1 + R.nextInt(3);
        int rd2=0;
        do{
            rd2=1 + R.nextInt(3);
        }while(rd2==rd1);
        int rd3=0;
        do{
            rd3=1 + R.nextInt(3);
        }while(rd3==rd1 || rd3==rd2);

        Log.d("tag", rd1+"|"+rd2+"|"+rd3);

        switch (rd1){
            case 1:
                pr_rd_01.setText(tr);
                break;
            case 2:
                pr_rd_01.setText(err1);
                break;
            case 3:
                pr_rd_01.setText(err2);
                break;
        }
        switch (rd2){
            case 1:
                pr_rd_02.setText(tr);
                break;
            case 2:
                pr_rd_02.setText(err1);
                break;
            case 3:
                pr_rd_02.setText(err2);
                break;
        }
        switch (rd3){
            case 1:
                pr_rd_03.setText(tr);
                break;
            case 2:
                pr_rd_03.setText(err1);
                break;
            case 3:
                pr_rd_03.setText(err2);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        sendResult();
        db.updateScoreGm(gmId, getNumberTrue());
        super.onBackPressed();

    }

    public View.OnClickListener btnNext=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pr_Group.clearCheck();
            pr_explain.setText(null);
            pr_result.setText(null);

            if(currentQs<list.size()-2){
                pr_btn_pre.setEnabled(true);
                currentQs+=1;
            }else if(currentQs==list.size()-2){
                currentQs+=1;
                pr_btn_next.setEnabled(false);
            }
            Log.d("tag", "QS: "+currentQs);
            pr_cau.setText("Câu "+(currentQs+1)+"/20");
            pr_tile.setText("Tỉ lệ: "+printFloaf(getTl())+"%");
            pr_question.setText(list.get(currentQs).getQuestion());
            loadStateQuestion();

        }
    };

    public View.OnClickListener btnPre=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pr_Group.clearCheck();
            pr_explain.setText(null);
            pr_result.setText(null);



            if(currentQs==1){
                currentQs+=-1;
                pr_btn_pre.setEnabled(false);

            }else if(currentQs>1){
                pr_btn_next.setEnabled(true);
                currentQs+=-1;
            }
            Log.d("tag", "QS: "+currentQs);

            pr_cau.setText("Câu "+(currentQs+1)+"/20");
            pr_tile.setText("Tỉ lệ: "+printFloaf(getTl())+"%");
            pr_question.setText(list.get(currentQs).getQuestion());
            loadStateQuestion();
        }
    };


    public RadioGroup.OnCheckedChangeListener prGroup=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){
                case R.id.pr_rd_01:
                    list.get(currentQs).setSelect(pr_rd_01.getText().toString());
                    radioClick(pr_rd_01);
                    pr_explain.setText(list.get(currentQs).getExplain());
                    checkComplite();
                    break;
                case R.id.pr_rd_02:
                    list.get(currentQs).setSelect(pr_rd_02.getText().toString());
                    radioClick(pr_rd_02);
                    pr_explain.setText(list.get(currentQs).getExplain());
                    checkComplite();
                    break;
                case R.id.pr_rd_03:
                    list.get(currentQs).setSelect(pr_rd_03.getText().toString());
                    radioClick(pr_rd_03);
                    pr_explain.setText(list.get(currentQs).getExplain());
                    checkComplite();
                    break;
            }
            disableRadio();
        }
    };

    public String getNumberTrue(){
        int number=0;
        for(int i=0; i<list.size();i++){
            if(list.get(i).getSelect()!=null){
                if(list.get(i).getTr_answear().equals(list.get(i).getSelect())){
                    number++;
                }
            }
        }
        return number+"";
    }

    public void checkComplite(){
        int number=0;
        for(int i=0; i<list.size();i++){
            if(list.get(i).getSelect()!=null){
                number+=1;
            }
        }

        if(number==list.size()){
            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            if(getTl()>80.00){
                builder.setTitle("Chúc mừng, bạn đã hoàn thành bài học!");
            }else{
                builder.setTitle("Rất tiếc, bạn chưa hoàn thành bài học này!");
            }
            builder.setNegativeButton("Quay lại", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   dialog.cancel();
                }
            });

            AlertDialog dialog=builder.create();
            dialog.show();
        }
    }

    public void radioClick(RadioButton radio){
        if(checkAnswer(radio.getText().toString())){
            pr_tile.setText("Tỉ lệ: "+printFloaf(getTl())+"%");
            pr_result.setTextColor(Color.GREEN);
            pr_explain.setTextColor(Color.GREEN);
            pr_result.setText("Đáp án chính xác");
        }else{
            pr_explain.setTextColor(Color.RED);
            pr_result.setTextColor(Color.RED);
            pr_result.setText("Đáp án không chính xác");
        }
    }

    public float getTl(){
        int number=0;
        for(int i=0; i<list.size();i++){
            if(list.get(i).getSelect()!=null){
                if(list.get(i).getTr_answear().equals(list.get(i).getSelect())){
                    number++;
                }
            }
        }

        return (float)number/list.size()*100;
    }

    public String printFloaf(float input){
        DecimalFormat df=new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return  df.format(input);
    }

    public boolean checkAnswer(String answer){
        if(list.get(currentQs).getTr_answear().equals(answer)){
            return true;
        }

        return false;
    }

    public void disableRadio(){
        pr_rd_01.setEnabled(false);
        pr_rd_02.setEnabled(false);
        pr_rd_03.setEnabled(false);
    }

    public void enableRadio(){
        pr_rd_01.setChecked(false);
        pr_rd_02.setChecked(false);
        pr_rd_03.setChecked(false);

        pr_rd_01.setEnabled(true);
        pr_rd_02.setEnabled(true);
        pr_rd_03.setEnabled(true);
    }
}
