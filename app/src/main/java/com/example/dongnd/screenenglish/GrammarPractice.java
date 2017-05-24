package com.example.dongnd.screenenglish;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapter.GmPractiveAdapter;
import adapter.GmPractiveItem;
import databases.DbAdapter;
import databases.SubjectGm;

public class GrammarPractice extends AppCompatActivity {

    private List<SubjectGm> gmList;
    private GmPractiveAdapter gmAdapter;
    public RecyclerView recyclerView;
    private DbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_practice);


        getData();
        initGrammar();
    }


    public void initGrammar(){
        recyclerView=(RecyclerView) findViewById(R.id.gm_practice_list);
        gmAdapter=new GmPractiveAdapter(this, gmList);
        recyclerView.setAdapter(gmAdapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    public void getData(){
        db=new DbAdapter(this);
        gmList=db.getAllSubjecGm();
        Log.d("tag", gmList.size()+"");
    }
}
