package databases;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.dongnd.screenenglish.MainActivity;

import java.io.File;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import adapter.AddItem;
import adapter.CurrentQuestion;
import adapter.CurrentVocabulary;
import adapter.GmPractiveItem;

/**
 * Created by DongND on 3/27/2017.
 */

public class DbAdapter extends SQLiteOpenHelper{


    public static final String DB_NAME="screenenglish.db";


    private SQLiteDatabase db;
    public String dbPath;

    public MainActivity mainActivity;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public DbAdapter(Context context){
        super(context, DB_NAME, null, 1);
        mainActivity=MainActivity.getInstance();
        sharedPreferences=context.getSharedPreferences("data",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public boolean checkPath(){
        dbPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/ScreenEnglish/"+DB_NAME;

        File fileDB=new File(dbPath);
        if(fileDB.exists()){
           return true;
        }

        return false;
    }

    public SQLiteDatabase getWriteDatabase(){
        if(checkPath()){
            db=SQLiteDatabase.openDatabase(dbPath, null,  SQLiteDatabase.OPEN_READWRITE);
            return db;
        }

        return null;
    }

    /*public Vocabulary getWord(){
        getWriteDatabase();
        String sql="select * from Vocabulary";
        Cursor cursor=db.rawQuery(sql, null);

        cursor.moveToFirst();
        cursor.moveToNext();
        Vocabulary vc=new Vocabulary(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6), cursor.getString(7));

        return vc;

    }*/

    // Trả lại Vocabulary với các chế độ lặp khác nhau
    public Vocabulary getWordRandom(){
        getWriteDatabase();

        String subjectselect=sharedPreferences.getString("subjectselect",null);
        String subjectId=sharedPreferences.getString(subjectselect, null);
        String lap=sharedPreferences.getString("lap", null);

        String[] arrId=subjectId.split(",");
        /*Cursor cursor=db.query("Vocabulary", new String[]{"vc_id","word", "spellingaa", "spellingam",
        "tr_mean", "err1_mean", "err2_mean", "sound"}, "vc_id"+" =?",
                new String[]{arrId[arrId.length-1]}, null, null, null);
        cursor.moveToFirst();

        Vocabulary word=new Vocabulary(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getBlob(7));

        return word;*/
        Vocabulary word=null;
        if(lap.equals("lap1")){
            word=getWordLap1(arrId);
        }else{
            word=getWordLap2(arrId);
        }
        Log.d("tag","word: "+word.getWord());
        return word;

    }

    // Trả lại Vocabulary với chế độ lặp 1
    public Vocabulary getWordLap1(String[] arrId){
        Random R=new Random();
        int stt=R.nextInt(arrId.length);

        Cursor cursor=db.query("Vocabulary", new String[]{"vc_id","word", "spellingaa", "spellingam",
                "tr_mean", "err1_mean", "err2_mean", "sound"}, "vc_id"+" =?",
                new String[]{arrId[stt]}, null, null, null);
        cursor.moveToFirst();
        Vocabulary word=new Vocabulary(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getBlob(7));
        Log.d("tag","Id: "+ arrId[stt]);
        Log.d("tag","Word: " +word.getWord());

        return word;

    }


    // Trả lại Vocabulary với chế độ lặp 2
    public Vocabulary getWordLap2(String[] arrId){
        Cursor cursor=db.query("Vocabulary", new String[]{"vc_id","word", "spellingaa", "spellingam",
                        "tr_mean", "err1_mean", "err2_mean", "sound"}, "vc_id"+" =?",
                new String[]{arrId[arrId.length-1]}, null, null, null);
        cursor.moveToFirst();

        Vocabulary word=new Vocabulary(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getBlob(7));

        return word;
    }

    public EEDict getEDict(){
        getWriteDatabase();
        String sql="select * from EEDict";
        Cursor cursor=db.rawQuery(sql, null);

        EEDict eeDict=new EEDict(cursor.getString(1), "Spelling", cursor.getString(2));

        return eeDict;
    }

    public List<Subject> getAllSubject(){
        List<Subject> arr=new ArrayList<>();
        getWriteDatabase();
        String sql="select * from Subject";
        Cursor cursor=db.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Subject S=new Subject(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getBlob(3));
            arr.add(S);
            cursor.moveToNext();
        }

        return arr;
    }

    public String getIdSubject(String subject){
        getWriteDatabase();
        String str="";

        Cursor cursor1=db.query("Subject", new String[]{"sb_id"}, "name"+" = ?",
                new String[]{subject},null, null, null);

        cursor1.moveToFirst();
        int sb_id=cursor1.getInt(0);

        Cursor cursor2=db.query("Vocabulary", new String[]{"vc_id"}, "vc_ref"+" = ?",
                new String[]{String.valueOf(sb_id)}, null, null, null);
        cursor2.moveToFirst();
        while (!cursor2.isAfterLast()){
            if(cursor2.isLast()){
                str+=cursor2.getInt(0);
            }else{
                str+=cursor2.getInt(0)+",";
            }
            cursor2.moveToNext();
        }

        return str;

    }

    public String getEEMean(int id){
        String result="";
        getWriteDatabase();

        Cursor cursor=db.query("EEDic", new String[]{"mean"}, "ee_ref"+" =?",
                new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        result=cursor.getString(0);

        return result;
    }

    public List<AddItem> getAllVocabularySubject(String subject){
        getWriteDatabase();


        Cursor cursorId=db.query("Subject", new String[]{"sb_id"}, "name"+" =?",
                new String[]{subject}, null, null, null);
        cursorId.moveToFirst();
        int id=cursorId.getInt(0);
        cursorId.close();

        Log.d("tag", id+"");
        List<AddItem> arr=new ArrayList<>();

        Cursor cursor=db.query("Vocabulary", new String[]{"vc_id", "word", "tr_mean"},"vc_ref"+" =?", new String[]{String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
           AddItem item=new AddItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2), false);
            arr.add(item);
            cursor.moveToNext();

        }

        return arr;
    }

    public List<VocabularyItem> getVocabularyArr(ArrayList<Integer> arr){
        getWriteDatabase();
        List<VocabularyItem> list=new ArrayList<>();
        for(int i=0;i<arr.size();i++){
            Cursor cursor=db.query("Vocabulary", new String[]{"vc_id", "word", "spellingaa", "spellingam",
                            "tr_mean", "err1_mean", "err2_mean", "sound"}, "vc_id"+" =?",
                    new String[]{String.valueOf(arr.get(i))}, null, null, null);

            cursor.moveToFirst();
            VocabularyItem vi=new VocabularyItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getBlob(7), null);
            cursor.close();
            list.add(vi);
        }

        return list;
    }

    public List<SubjectGm> getAllSubjecGm(){
        getWriteDatabase();
        List<SubjectGm> arr=new ArrayList<>();
        String sql="select * from SubjectGm";
        Cursor cursor=db.rawQuery(sql, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            SubjectGm S=new SubjectGm(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            arr.add(S);
            cursor.moveToNext();
        }

        return arr;
    }

    public String getIdSubjectGm(String subjectGm){
        getWriteDatabase();
        String str="";

        Cursor cursor_gm=db.query("SubjectGm", new String[]{"sb_id"}, "name"+" =?",
                new String[]{subjectGm}, null, null, null);
        cursor_gm.moveToFirst();

        int id_gm=cursor_gm.getInt(0);

        Cursor cursor_qs=db.query("QuestionGm", new String[]{"qs_id"}, "qs_ref"+" =?",
                new String[]{String.valueOf(id_gm)}, null, null, null);
        cursor_qs.moveToFirst();
        while (!cursor_qs.isAfterLast()){
            if(cursor_qs.isLast()){
                str+=cursor_qs.getInt(0);
            }else{
                str+=cursor_qs.getInt(0)+",";
            }
            cursor_qs.moveToNext();
        }

        return str;
    }

    public Question getRandomQuestion(){
        getWriteDatabase();

        String subjectGm=sharedPreferences.getString("subjectGm", null);
        String sujectId=sharedPreferences.getString(subjectGm, null);

        String[] arrId=sujectId.split(",");

        Cursor cursor=db.query("QuestionGm", new String[]{"qs_id", "question", "tr_answer", "err1_answer", "err2_answer", "qs_ref"},
                "qs_id"+" =?", new String[]{arrId[arrId.length-1]}, null, null, null);
        cursor.moveToFirst();
        Question Q=new Question(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5));

        return Q;
    }

    public List<Integer> getAllIdQuestion(int gmId){
        getWriteDatabase();
        List<Integer> arr=new ArrayList<>();

        Cursor cursor_qs=db.query("QuestionGm", new String[]{"qs_id"}, "qs_ref"+" =?",
                new String[]{String.valueOf(gmId)}, null, null, null);
        cursor_qs.moveToFirst();
        while (!cursor_qs.isAfterLast()){
            arr.add(cursor_qs.getInt(0));
            cursor_qs.moveToNext();
        }

        return arr;
    }

    public String getExGrammar(int id){
        String result="";
        getWriteDatabase();

        Cursor cursor=db.query("ExplainGm", new String[]{"grammar"}, "ex_ref"+" =?",
                new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        result=cursor.getString(0);

        return result;
    }

    public String getScore(int id){
        getWriteDatabase();

        Cursor cursor=db.query("SubjectGm", new String[]{"score"}, "sb_id"+" =?",
                new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();

        Log.d("score", cursor.getString(0));
        return cursor.getString(0);
    }


    public String getStateGrammar(int id){
        String state=null;
        getWriteDatabase();

        Cursor cursor=db.query("SubjectGm", new String[]{"score"}, "sb_id"+" =?",
                new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();

        state=cursor.getString(0);
        return state;
    }

    public List<CurrentQuestion> getAllQuestionId(int id){
        getWriteDatabase();
        List<CurrentQuestion> arr=new ArrayList<>();

        Cursor cursor=db.query("QuestionGm", new String[]{"qs_id", "question", "tr_answer",
                "err1_answer", "err2_answer", "explain"},"qs_ref"+" =?", new String[]{String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            CurrentQuestion C=new CurrentQuestion(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), null);
            arr.add(C);
            cursor.moveToNext();

        }

        return arr;
    }




    public void updateScoreGm(int gmId, String value){
        getWriteDatabase();
        ContentValues content=new ContentValues();
        content.put("score", value);
        db.update("SubjectGm", content, "sb_id= ?", new String[]{String.valueOf(gmId)});

    }

    public CurrentVocabulary getItemError(int id){
        getWriteDatabase();
        Cursor cursor=db.query("Vocabulary", new String[]{"vc_id", "word", "spellingaa", "spellingam",
        "tr_mean", "err1_mean", "err2_mean", "sound", "img"}, "vc_id"+" =?",
                new String[]{String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();

        CurrentVocabulary vi=new CurrentVocabulary(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                cursor.getBlob(7), cursor.getBlob(8), null);
        return  vi;
    }

    public String getStatement(int id){
        getWriteDatabase();

        Cursor cursor=db.query("EEDic", new String[]{"mean"}, "ee_ref"+" =?",
                new String[]{String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        String str=cursor.getString(0);
        String result=str.substring(str.indexOf("<li>")+4, str.indexOf("</li>"));

        return result;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
