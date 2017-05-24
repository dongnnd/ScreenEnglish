package com.example.dongnd.screenenglish;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class GrammarExplain extends AppCompatActivity {

    private WebView webView;
    private String grammar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_explain);

        Bundle bundle=getIntent().getExtras();
        grammar=bundle.getString("grammar");

        webView=(WebView)findViewById(R.id.gr_explain);
        webView.loadData(grammar, "text/html; charset=utf-8","UTF-8");
    }
}
