package com.example.dongnd.screenenglish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ResetPass extends AppCompatActivity implements View.OnClickListener{

    public TextView ps_title;
    public ImageView ps_pass1, ps_pass2, ps_pass3, ps_pass4;

    public ImageView ps_background;

    public ImageView ps_0, ps_1, ps_2, ps_3, ps_4, ps_5,
            ps_6, ps_7, ps_8, ps_9;

    public TextView ps_cancel, ps_del;

    public static final int REQUEST_CODE_OLDPIN=3;

    public ArrayList<Integer> userpass=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_screen);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initSetpass();
    }

    public void initSetpass(){
        ps_title=(TextView)findViewById(R.id.ps_title);
        ps_title.setText("Nhập mật khẩu cũ");

        ps_background=(ImageView)findViewById(R.id.ps_background);
        ps_background.setImageResource(R.drawable.pass_screen);

        ps_pass1=(ImageView)findViewById(R.id.ps_pass1);
        ps_pass1.setOnClickListener(this);
        ps_pass2=(ImageView)findViewById(R.id.ps_pass2);
        ps_pass2.setOnClickListener(this);
        ps_pass3=(ImageView)findViewById(R.id.ps_pass3);
        ps_pass3.setOnClickListener(this);
        ps_pass4=(ImageView)findViewById(R.id.ps_pass4);
        ps_pass4.setOnClickListener(this);

        ps_0=(ImageView)findViewById(R.id.ps_0);
        ps_0.setOnClickListener(this);
        ps_1=(ImageView)findViewById(R.id.ps_1);
        ps_1.setOnClickListener(this);
        ps_2=(ImageView)findViewById(R.id.ps_2);
        ps_2.setOnClickListener(this);
        ps_3=(ImageView)findViewById(R.id.ps_3);
        ps_3.setOnClickListener(this);
        ps_4=(ImageView)findViewById(R.id.ps_4);
        ps_4.setOnClickListener(this);
        ps_5=(ImageView)findViewById(R.id.ps_5);
        ps_5.setOnClickListener(this);
        ps_6=(ImageView)findViewById(R.id.ps_6);
        ps_6.setOnClickListener(this);
        ps_7=(ImageView)findViewById(R.id.ps_7);
        ps_7.setOnClickListener(this);
        ps_8=(ImageView)findViewById(R.id.ps_8);
        ps_8.setOnClickListener(this);
        ps_9=(ImageView)findViewById(R.id.ps_9);
        ps_9.setOnClickListener(this);

        ps_cancel=(TextView)findViewById(R.id.ps_cancel);
        ps_cancel.setOnClickListener(this);
        ps_del=(TextView)findViewById(R.id.ps_del);
        ps_del.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ps_0:
                userpass.add(0);
                lenghPass();
                break;
            case R.id.ps_1:
                userpass.add(1);
                lenghPass();
                break;
            case R.id.ps_2:
                userpass.add(2);
                lenghPass();
                break;
            case R.id.ps_3:
                userpass.add(3);
                lenghPass();
                break;
            case R.id.ps_4:
                userpass.add(4);
                lenghPass();
                break;
            case R.id.ps_5:
                userpass.add(5);
                lenghPass();
                break;
            case R.id.ps_6:
                userpass.add(6);
                lenghPass();
                break;
            case R.id.ps_7:
                userpass.add(7);
                lenghPass();
                break;
            case R.id.ps_8:
                userpass.add(8);
                lenghPass();
                break;

            case R.id.ps_9:
                userpass.add(9);
                lenghPass();
                break;
            case R.id.ps_del:
                deletePass();
                lenghPass();
                break;
            case R.id.ps_cancel:
                finish();
        }
    }

    public void deletePass(){
        if(userpass.size()>0){
            userpass.remove(userpass.size()-1);
        }
    }

    public void lenghPass(){
        int size_pass=userpass.size();
        switch (size_pass){
            case 0:
                ps_pass1.setImageResource(R.drawable.open_dot);
                ps_pass2.setImageResource(R.drawable.open_dot);
                ps_pass3.setImageResource(R.drawable.open_dot);
                ps_pass4.setImageResource(R.drawable.open_dot);
                break;
            case 1:
                ps_pass1.setImageResource(R.drawable.close_dot);
                ps_pass2.setImageResource(R.drawable.open_dot);
                ps_pass3.setImageResource(R.drawable.open_dot);
                ps_pass4.setImageResource(R.drawable.open_dot);
                break;
            case 2:
                ps_pass1.setImageResource(R.drawable.close_dot);
                ps_pass2.setImageResource(R.drawable.close_dot);
                ps_pass3.setImageResource(R.drawable.open_dot);
                ps_pass4.setImageResource(R.drawable.open_dot);
                break;
            case 3:
                ps_pass1.setImageResource(R.drawable.close_dot);
                ps_pass2.setImageResource(R.drawable.close_dot);
                ps_pass3.setImageResource(R.drawable.close_dot);
                ps_pass4.setImageResource(R.drawable.open_dot);
                break;
            case 4:
                ps_pass1.setImageResource(R.drawable.close_dot);
                ps_pass2.setImageResource(R.drawable.close_dot);
                ps_pass3.setImageResource(R.drawable.close_dot);
                ps_pass4.setImageResource(R.drawable.close_dot);
                sentResult();
                break;
        }
    }

    public void sentResult(){
        Intent intent=getIntent();

        String old_pass="";
        for(int i=0;i<userpass.size();i++){
            old_pass+=userpass.get(i);
        }

        Bundle bundle=new Bundle();
        bundle.putString("old_pass", old_pass);

        intent.putExtra("old_pass", bundle);
        setResult(REQUEST_CODE_OLDPIN, intent);
        finish();
    }
}
