package com.example.dongnd.screenenglish;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import adapter.CurrentVocabulary;
import databases.DbAdapter;
import databases.VocabularyItem;

public class VocabularyError extends AppCompatActivity {

    private String strList;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<CurrentVocabulary> listError=new ArrayList<>();
    private DbAdapter db;

    private TextView err_question, err_spelling, err_eg, err_result, err_tile;
    private RadioGroup err_rdGroup;
    private RadioButton err_rd1, err_rd2, err_rd3;
    private ImageView err_img;

    private Button err_next, err_pre;
    private int current=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_error);

        sharedPreferences=getSharedPreferences("data", MODE_PRIVATE);
        editor=sharedPreferences.edit();
        db=new DbAdapter(this);
        getData();

        init();

        loadFist();

    }

    public void getData(){

        Bundle bundle=getIntent().getExtras();
        String type=bundle.getString("type");
        if(type.equals("newList")){
            strList=sharedPreferences.getString("currentList", null);
        }else{
            strList=sharedPreferences.getString("list_error", null);
        }


        if(strList==null){

        }else{
            String[] arrList=strList.split(",");
            for(int i=0;i<arrList.length;i++){
                int id=Integer.parseInt(arrList[i]);
                CurrentVocabulary vi=db.getItemError(id);
                listError.add(vi);
            }
        }
        Log.d("tag", listError.size()+"");
    }

    public void init(){
        err_question=(TextView)findViewById(R.id.err_question);
        err_spelling=(TextView)findViewById(R.id.err_spelling);

        err_eg=(TextView)findViewById(R.id.err_eg);

        err_rdGroup=(RadioGroup)findViewById(R.id.err_rdGroup);
        err_rdGroup.setOnCheckedChangeListener(errGroup);

        err_rd1=(RadioButton)findViewById(R.id.err_rd1);
        err_rd2=(RadioButton)findViewById(R.id.err_rd2);
        err_rd3=(RadioButton)findViewById(R.id.err_rd3);

        err_img=(ImageView)findViewById(R.id.err_img);

        err_eg=(TextView)findViewById(R.id.err_eg);

        err_tile=(TextView)findViewById(R.id.err_tile);

        err_result=(TextView)findViewById(R.id.err_result);

        err_next=(Button)findViewById(R.id.err_next);
        err_next.setOnClickListener(errNext);

        err_pre=(Button)findViewById(R.id.err_pre);
        err_pre.setEnabled(false);
        err_pre.setOnClickListener(errPre);
    }

    public void loadFist(){
        err_question.setText(listError.get(0).getWord());
        err_spelling.setText("BrE: "+listError.get(0).getSpellingaa()+";"+ " NAme "+listError.get(0).getSpellingam());

        err_rd1.setText(listError.get(0).getTr_mean());
        err_rd2.setText(listError.get(0).getErr1_mean());
        err_rd3.setText(listError.get(0).getErr2_mean());

        Bitmap bm = BitmapFactory.decodeByteArray(listError.get(0).getImg(), 0, listError.get(0).getImg().length);
        err_img.setImageBitmap(bm);
        err_eg.setText("Eg: "+db.getStatement(listError.get(0).getId()));
    }

    public View.OnClickListener errNext=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            err_rdGroup.clearCheck();
            err_result.setText(null);

            err_pre.setEnabled(true);
            if(current==listError.size()-2){
                current++;
                err_next.setEnabled(false);
            }else{
                current++;
            }
            CurrentVocabulary vi=listError.get(current);
            loadState(vi);
        }
    };

    public View.OnClickListener errPre=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            err_rdGroup.clearCheck();
            err_result.setText(null);

            err_next.setEnabled(true);
            if(current==1){
                current+=-1;
                err_pre.setEnabled(false);
            }else{
                current+=-1;
            }
            CurrentVocabulary vi=listError.get(current);
            loadState(vi);
        }
    };

    public void loadState(CurrentVocabulary item){
        err_question.setText(item.getWord());
        err_spelling.setText("BrE "+item.getSpellingaa()+"; "+"NAmE "+item.getSpellingam());

        if(item.getImg()!=null){
            Bitmap bm = BitmapFactory.decodeByteArray(item.getImg(), 0, item.getImg().length);
            err_img.setImageBitmap(bm);
        }else{
            err_img.setImageBitmap(null);
        }

        radomAnswer(item.getTr_mean(), item.getErr1_mean(), item.getErr2_mean());
        if(item.getSelect()!=null){
            String answear=item.getSelect();
            if(err_rd1.getText().toString().equals(answear)){
                err_rd1.setChecked(true);
                if(answear.equals(item.getTr_mean())){
                    err_result.setText("Đáp án chính xác");
                }else{
                    err_result.setText("Đáp án không chính xác");
                }
            }else if(err_rd2.getText().toString().equals(answear)){
                err_rd2.setChecked(true);
                if(answear.equals(item.getTr_mean())){
                    err_result.setText("Đáp án chính xác");
                }else{
                    err_result.setText("Đáp án không chính xác");
                }
            }else{
                err_rd3.setChecked(true);
                if(answear.equals(item.getTr_mean())){
                    err_result.setText("Đáp án chính xác");
                }else{
                    err_result.setText("Đáp án không chính xác");
                }
            }
            disableRadio();
        }else{
            enableRadio();
        }

        err_eg.setText("Eg: "+db.getStatement(item.getId()));

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


        switch (rd1){
            case 1:
                err_rd1.setText(tr);
                break;
            case 2:
                err_rd1.setText(err1);
                break;
            case 3:
                err_rd1.setText(err2);
                break;
        }
        switch (rd2){
            case 1:
                err_rd2.setText(tr);
                break;
            case 2:
                err_rd2.setText(err1);
                break;
            case 3:
                err_rd2.setText(err2);
                break;
        }
        switch (rd3){
            case 1:
                err_rd3.setText(tr);
                break;
            case 2:
                err_rd3.setText(err1);
                break;
            case 3:
                err_rd3.setText(err2);
                break;
        }
    }

    public RadioGroup.OnCheckedChangeListener errGroup=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){
                case R.id.err_rd1:
                    listError.get(current).setSelect(err_rd1.getText().toString());
                    checkRadio(err_rd1);
                    break;
                case R.id.err_rd2:

                    listError.get(current).setSelect(err_rd2.getText().toString());
                    checkRadio(err_rd2);
                    break;
                case R.id.err_rd3:

                    listError.get(current).setSelect(err_rd3.getText().toString());
                    checkRadio(err_rd3);
                    break;
            }
            disableRadio();
        }
    };

    public void checkRadio(RadioButton radio){
        if(radio.getText().equals(listError.get(current).getTr_mean())){
            err_tile.setText("Tỉ lệ: "+printFloaf(getTl())+"%");
            err_result.setTextColor(Color.GREEN);
            err_result.setText("(Đáp án chính xác)");
        }else{
            err_result.setTextColor(Color.RED);
            err_result.setText("(Đáp án không chính xác)");
        }
    }

    public void disableRadio(){
        err_rd1.setEnabled(false);
        err_rd2.setEnabled(false);
        err_rd3.setEnabled(false);
    }

    public void enableRadio(){
        err_rd1.setChecked(false);
        err_rd2.setChecked(false);
        err_rd3.setChecked(false);

        err_rd1.setEnabled(true);
        err_rd2.setEnabled(true);
        err_rd3.setEnabled(true);
    }

    public float getTl(){
        int number=0;
        for(int i=0; i<listError.size();i++){
            if(listError.get(i).getSelect()!=null){
                if(listError.get(i).getTr_mean().equals(listError.get(i).getSelect())){
                    number++;
                }
            }
        }

        return (float)number/listError.size()*100;
    }

    public String printFloaf(float input){
        DecimalFormat df=new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return  df.format(input);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveData();
    }

    public void saveData(){
        String str="";
        for(int i=0;i<listError.size();i++){
            String answear=listError.get(i).getSelect();
            if(answear!=null){
                if(answear==listError.get(i).getTr_mean()){
                    if(str==""){
                        str+=listError.get(i).getId();
                    }else{
                        str+=","+listError.get(i).getId();
                    }
                }
            }
        }

        editor.putString("learned", str);
        editor.commit();
    }
}
