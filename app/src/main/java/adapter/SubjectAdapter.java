package adapter;

/**
 * Created by DongND on 3/31/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dongnd.screenenglish.R;
import com.example.dongnd.screenenglish.SubjectVocabulary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import databases.DbAdapter;
import databases.Subject;

/**
 * Created by DongND on 3/23/2017.
 */

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>{

    private Context mContext;
    private List<Subject> mSubjects;
    private LayoutInflater mLayoutInflater;
    private SubjectVocabulary subjectVocabulary;
    private DbAdapter db;


    public SubjectAdapter(Context context, List<Subject> subjects){
        mContext=context;
        mSubjects=subjects;
        mLayoutInflater=LayoutInflater.from(context);
        db=new DbAdapter(context);
    }

    @Override
    public SubjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=mLayoutInflater.inflate(R.layout.item_vocabulary_subject, parent, false);
        return new SubjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SubjectViewHolder holder, int position) {

        Subject subject=mSubjects.get(position);

        if(subject.getImg()!=null){
            Bitmap bitmap= BitmapFactory.decodeByteArray(subject.getImg(), 0, subject.getImg().length);
            holder.sb_img.setImageBitmap(bitmap);
        }
        holder.sb_title.setText(subject.getSb_title());
        holder.sb_content.setText(subject.getSb_content());

    }

    @Override
    public int getItemCount() {
        return mSubjects.size();
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder{
        private ImageView sb_img;
        private TextView sb_title;
        private TextView sb_content;

        public SubjectViewHolder(View itemView){
            super(itemView);
            sb_img=(ImageView)itemView.findViewById(R.id.item_number);
            sb_title=(TextView)itemView.findViewById(R.id.item_title);
            sb_content=(TextView)itemView.findViewById(R.id.item_content);

            subjectVocabulary=SubjectVocabulary.getInstance();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Subject subject = mSubjects.get(getAdapterPosition());

                    SharedPreferences sharedPreferences=mContext.getSharedPreferences("data",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("subjectselect", subject.getSb_title());
                    editor.putString(subject.getSb_title(), db.getIdSubject(subject.getSb_title()));
                    editor.commit();

                    subjectVocabulary.sentData(subject.getSb_title());
                }
            });

        }
    }
}