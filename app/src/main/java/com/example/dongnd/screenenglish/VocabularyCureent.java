package com.example.dongnd.screenenglish;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import adapter.CurrentVocabulary;
import adapter.OnSwipeTouchListener;
import databases.DbAdapter;

public class VocabularyCureent extends AppCompatActivity {

    private String strList;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<CurrentVocabulary> listError=new ArrayList<>();
    private DbAdapter db;

    private TextView err_question, err_spelling, err_eg, err_result, err_tile;
    private RadioGroup err_rdGroup;
    private RadioButton err_rd1, err_rd2, err_rd3;
    private ImageView err_img, err_background, err_speaker, err_eedic;
    private String[] arrList;

    private RelativeLayout relativeLayout;

    private ImageView err_next, err_pre;
    private int current=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_error);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences=getSharedPreferences("data", MODE_PRIVATE);
        editor=sharedPreferences.edit();
        db=new DbAdapter(this);

        getData();

        init();


        loadFist();

    }

    public void getBackground() {
        String strBackground=sharedPreferences.getString("background", null);
        if(strBackground==null){
            err_background.setImageResource(R.drawable.a28);

        }else{
            if(strBackground.contains("/")){
                File file=new File(strBackground);
                Bitmap bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());
                err_background.setImageBitmap(bitmap);
            }else{
                err_background.setImageResource(Integer.parseInt(strBackground));
            }
        }



    }

    public void getData(){



        strList=sharedPreferences.getString("currentList", null);


        arrList=strList.split(",");
        for(int i=0;i<arrList.length;i++){
            int id=Integer.parseInt(arrList[i]);
            CurrentVocabulary vi=db.getItemError(id);
            listError.add(vi);
        }


        Log.d("tag", listError.size()+"");
    }

    public void init(){
        relativeLayout=(RelativeLayout)findViewById(R.id.err_relative_layout);
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(this){
            public void onSwipeLeft() {
                Intent intent=new Intent(getApplicationContext(), VocabularyEEDic. class);
                intent.putExtra("id", listError.get(current).getId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        err_background=(ImageView)findViewById(R.id.err_background);
        err_background.setImageResource(R.drawable.bg_demo);

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

        err_next=(ImageView) findViewById(R.id.err_next);
        err_next.setOnClickListener(errNext);

        err_speaker=(ImageView)findViewById(R.id.err_speaker);
        err_speaker.setOnClickListener(speaker);


        err_pre=(ImageView) findViewById(R.id.err_pre);
        err_pre.setEnabled(false);
        err_pre.setOnClickListener(errPre);
    }

    public void loadFist(){
        err_question.setText(listError.get(0).getWord());
        err_spelling.setText("BrE: "+listError.get(0).getSpellingaa()+";"+ " NAme "+listError.get(0).getSpellingam());

        err_rd1.setText(listError.get(0).getTr_mean());
        err_rd2.setText(listError.get(0).getErr1_mean());
        err_rd3.setText(listError.get(0).getErr2_mean());


        if(listError.get(0).getImg()!=null){
            Bitmap bm = BitmapFactory.decodeByteArray(listError.get(0).getImg(), 0, listError.get(0).getImg().length);
            err_img.setImageBitmap(bm);
        }else{
            err_img.setImageResource(R.drawable.aa);
        }

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

    public View.OnClickListener eedic=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getApplicationContext(), VocabularyEEDic. class);
            intent.putExtra("id", listError.get(current).getId());
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    public View.OnClickListener speaker=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                File Mytemp = File.createTempFile("TCL", "mp3");
                Mytemp.deleteOnExit();
                FileOutputStream fos = new FileOutputStream(Mytemp);
                fos.write(listError.get(current).getSound());
                fos.close();

                MediaPlayer mediaPlayer = new MediaPlayer();

                FileInputStream MyFile = new FileInputStream(Mytemp);
                mediaPlayer.setDataSource(MyFile.getFD());

                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException ex) {
                String s = ex.toString();
                ex.printStackTrace();
            }
        }
    };

    public void loadState(CurrentVocabulary item){
        err_question.setText(item.getWord());
        err_spelling.setText("BrE "+item.getSpellingaa()+"; "+"NAmE "+item.getSpellingam());

        if(item.getImg()!=null){
            Bitmap bm = BitmapFactory.decodeByteArray(item.getImg(), 0, item.getImg().length);
            err_img.setImageBitmap(bm);
        }else{
            err_img.setImageResource(R.drawable.aa);
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

        if(db.getStatement(item.getId()).length()>70){
            err_eg.setTextSize(15);
            err_eg.setText("Eg: "+db.getStatement(item.getId()));
        }else{
            err_eg.setText("Eg: "+db.getStatement(item.getId()));
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
                    checkComplete();
                    break;
                case R.id.err_rd2:
                    listError.get(current).setSelect(err_rd2.getText().toString());
                    checkRadio(err_rd2);
                    checkComplete();
                    break;
                case R.id.err_rd3:

                    listError.get(current).setSelect(err_rd3.getText().toString());
                    checkRadio(err_rd3);
                    checkComplete();
                    break;
            }
            disableRadio();
        }
    };
    public void checkComplete(){
        int count=0;
        for(int i=0;i<listError.size();i++){
            if(listError.get(i).getSelect()!=null){
                count++;
            }
        }
        if(count==listError.size()){
            showResult();
        }
    }

    public void showResult(){
        int correct=0;
        for(int i=0;i<listError.size();i++){
            if(listError.get(i).getSelect().toLowerCase().equals(listError.get(i).getTr_mean().toLowerCase())){
                correct+=1;
            }
        }

        if(correct==0){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Bạn chưa học đúng từ nào.");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.show();
        }else{
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Số từ đúng: "+correct+"/"+listError.size());
            builder.setMessage("Các từ đúng sẽ được chuyển qua và ưu tiên hiển thị trên màn hình khóa");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.show();
        }
    }

    public void resetListError(){
        String tr_id="";
        for(int i=0;i<listError.size();i++){
            if(listError.get(i).getSelect().toLowerCase().equals(listError.get(i).getTr_mean().toLowerCase())){
                if(tr_id.equals("")){
                    tr_id+=listError.get(i).getId();
                }else{
                    tr_id+=","+listError.get(i).getId();
                }
            }
        }

        String[] arrTr_Id=tr_id.split(",");
        if(arrTr_Id.length!=0){

            for(int i=0;i<arrTr_Id.length;i++){
                if(arrTr_Id[i].equals(arrList[0])){

                }else{

                }
            }


            String subjectselect=sharedPreferences.getString("subjectselect", null);
            String subjectId=sharedPreferences.getString(subjectselect, null);

            if(subjectId==""||subjectId==null){
                subjectId=tr_id;
            }else{
                subjectId=subjectId+","+tr_id;
            }

            editor.putString(subjectselect, subjectId);
            editor.commit();
        }



    }

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
