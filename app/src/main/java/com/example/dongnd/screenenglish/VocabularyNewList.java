package com.example.dongnd.screenenglish;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.DialogAdapter;
import adapter.NewListAdapter;
import databases.DbAdapter;
import databases.Subject;
import databases.VocabularyItem;

public class VocabularyNewList extends AppCompatActivity {

    private RecyclerView recyclerView;
    public NewListAdapter adapter;
    public List<VocabularyItem> list=new ArrayList<>();
    private ImageView nl_add;
    private List<Subject> nameSubject;
    private DbAdapter db;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    private Button btn_luyentap;
    public static VocabularyNewList vocabularyNewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vocabularyNewList=this;
        setContentView(R.layout.activity_vocabulary_new_list);

        sharedPreferences=getSharedPreferences("data", MODE_PRIVATE);
        editor=sharedPreferences.edit();
        init();
    }

    public void init(){

        db=new DbAdapter(this);
        nameSubject=db.getAllSubject();
        String currentList=sharedPreferences.getString("currentList", null);
        if(currentList!=null && currentList!=""){
            String[] arr=currentList.split(",");
            ArrayList<Integer> arrId=new ArrayList<>();
            for(int i=0;i<arr.length;i++){
                arrId.add(Integer.parseInt(arr[i]));
            }

            list=db.getVocabularyArr(arrId);
        }

        nl_add=(ImageView) findViewById(R.id.nl_add);
        nl_add.setOnClickListener(nlAdd);
        recyclerView=(RecyclerView)findViewById(R.id.nl_recyclerView);
        adapter=new NewListAdapter(this, list);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        btn_luyentap=(Button)findViewById(R.id.nl_luyentap);
        btn_luyentap.setOnClickListener(btnLuyentap);
    }

    public View.OnClickListener nlAdd=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog();
        }
    };

    public void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Chọn chủ đề");
        builder.setIcon(R.drawable.ic_select);

        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, R.layout.item_dialog);
        DialogAdapter adapter=new DialogAdapter(this, R.layout.item_dialog, nameSubject);
        for(int i=0;i<nameSubject.size();i++){
            arrayAdapter.add(nameSubject.get(i).getSb_title());
        }

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(getApplicationContext(), VocabularyAdd.class);
                intent.putExtra("Subject", nameSubject.get(which).getSb_title());
                startActivityForResult(intent, 1);
            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            Bundle bundle=data.getBundleExtra("result");
            ArrayList<Integer> arr=bundle.getIntegerArrayList("listId");



            String currentList=sharedPreferences.getString("currentList", null);
            if(currentList==null){
                list=db.getVocabularyArr(arr);
            }else{
                List<VocabularyItem> newList=db.getVocabularyArr(arr);
                for(int i=0;i<newList.size();i++){
                    if(!currentList.contains(String.valueOf(newList.get(i).getVc_id()))) {
                        list.add(newList.get(i));
                    }
                }
            }
            adapter=new NewListAdapter(this, list);
            recyclerView.setAdapter(adapter);
            String str="";
            for(int i=0;i<list.size();i++){
                if(i==list.size()-1){
                    str+=list.get(i).getVc_id();
                }else{
                    str+=list.get(i).getVc_id()+",";
                }
            }

            editor.putString("currentList", str);
            editor.commit();
        }
    }

    public View.OnClickListener btnLuyentap=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(list.size()==0){
                Toast.makeText(getApplicationContext(), "Danh sách từ trống, hãy chọn thêm từ", Toast.LENGTH_SHORT).show();
            }else{
                startPractice();
            }
        }
    };

    public void startPractice(){
        Intent intent=new Intent(getApplicationContext(), VocabularyCureent.class);
        startActivity(intent);
    }

    public static VocabularyNewList getInstance(){
        if(vocabularyNewList==null){
            vocabularyNewList=new VocabularyNewList();
        }

        return vocabularyNewList;
    }
}
