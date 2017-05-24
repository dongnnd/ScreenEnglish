package com.example.dongnd.screenenglish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import adapter.SubjectAdapter;
import databases.DbAdapter;
import databases.Subject;

public class SubjectVocabulary extends AppCompatActivity {

    public RecyclerView recyclerView;
    public DbAdapter db;
    public List<Subject> arrayList;
    SubjectAdapter subjectAdapter;
    public static SubjectVocabulary subjectVocabulary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_vocabulary);

        subjectVocabulary=this;

        db=new DbAdapter(this);
        arrayList=db.getAllSubject();
        //Toast.makeText(getApplicationContext(), arrayList.get(1).getSb_content()+" ", Toast.LENGTH_LONG).show();

        recyclerView=(RecyclerView)findViewById(R.id.subject_recyc);
        subjectAdapter=new SubjectAdapter(getApplicationContext(), arrayList);
        recyclerView.setAdapter(subjectAdapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public static SubjectVocabulary getInstance(){
        if(subjectVocabulary==null){
            return new SubjectVocabulary();
        }

        return subjectVocabulary;
    }

    public void sentData(String title){
        Intent intent=getIntent();

        Bundle bundle=new Bundle();
        bundle.putString("titleSubject", title);

        intent.putExtra("titleSubject", bundle);
        setResult(1, intent);
        finish();
    }
}
