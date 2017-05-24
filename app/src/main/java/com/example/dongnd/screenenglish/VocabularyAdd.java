package com.example.dongnd.screenenglish;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapter.AddItem;
import adapter.CurrentQuestion;
import adapter.VocabularyAddAdapter;
import databases.DbAdapter;
import databases.Vocabulary;

public class VocabularyAdd extends AppCompatActivity {


    private String subject;
    private List<AddItem> list;
    private ArrayList<Integer> result;
    private VocabularyAddAdapter adapter;
    private DbAdapter db;
    private ListView list_view;
    private TextView text_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary_add);

        getData();
        init();
    }

    public void getData(){
        Bundle bundle=getIntent().getExtras();
        subject=bundle.getString("Subject");
    }

    public void init(){
        db=new DbAdapter(this);
        list=db.getAllVocabularySubject(subject);
        result=new ArrayList<>();

        text_submit=(TextView)findViewById(R.id.vc_add_submit);
        text_submit.setOnClickListener(submit);

        list_view=(ListView)findViewById(R.id.vc_add_listview);
        adapter=new VocabularyAddAdapter(this, R.layout.item_add ,list);
        list_view.setAdapter(adapter);

    }

    public View.OnClickListener submit=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=getIntent();

            Bundle bundle=new Bundle();
            bundle.putIntegerArrayList("listId", getCheck());

            intent.putExtra("result", bundle);
            setResult(1, intent);
            finish();
        }
    };

    public ArrayList<Integer> getCheck(){
        for(int i=0;i<list.size();i++){
            if(list.get(i).isState()){
                result.add(list.get(i).getId());
            }
        }

        return result;
    }

    @Override
    public void onBackPressed() {
        Intent intent=getIntent();

        Bundle bundle=new Bundle();
        bundle.putIntegerArrayList("listId", getCheck());

        intent.putExtra("result", bundle);
        setResult(1, intent);
        finish();

        super.onBackPressed();
    }
}
