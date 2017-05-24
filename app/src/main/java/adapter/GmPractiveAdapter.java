package adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dongnd.screenenglish.ChoseGrammar;
import com.example.dongnd.screenenglish.R;
import com.example.dongnd.screenenglish.SubjectVocabulary;

import java.util.List;

import databases.DbAdapter;
import databases.Subject;
import databases.SubjectGm;

/**
 * Created by DongND on 5/3/2017.
 */

public class GmPractiveAdapter extends RecyclerView.Adapter<GmPractiveAdapter.PractiveViewHolder>{



    private Context mContext;
    private List<SubjectGm> gmList;
    private LayoutInflater mLayoutInflater;

    public GmPractiveAdapter(Context context, List<SubjectGm> gmList){
        this.mContext=context;
        this.gmList=gmList;
        mLayoutInflater=LayoutInflater.from(context);

    }

    @Override
    public PractiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=mLayoutInflater.inflate(R.layout.item_gm_practive, parent, false);
        return new PractiveViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PractiveViewHolder holder, int position) {

        SubjectGm gmItem=gmList.get(position);

        holder.gm_title.setText(gmItem.getSb_name());
        Log.d("tag", gmItem.getSb_score());

        if(gmItem.getSb_score().equals("Bạn chưa học bài này")){
            holder.gm_status.setImageResource(R.drawable.ic_status);
            holder.gm_score.setText(gmItem.getSb_score()+"");
        }else{
            holder.gm_status.setImageResource(R.drawable.ic_score);
            holder.gm_score.setText("Score: "+gmItem.getSb_score()+"/20");
        }


    }

    @Override
    public int getItemCount() {
        return gmList.size();
    }



    class PractiveViewHolder extends RecyclerView.ViewHolder{
        private TextView gm_title;
        private TextView gm_score;
        private ImageView gm_status;

        public PractiveViewHolder(View itemView){
            super(itemView);
            gm_title=(TextView)itemView.findViewById(R.id.item_practive_title);
            gm_score=(TextView)itemView.findViewById(R.id.item_practive_score);
            gm_status=(ImageView)itemView.findViewById(R.id.item_practive_status);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubjectGm subjectGm = gmList.get(getAdapterPosition());

                    Intent intent=new Intent(mContext, ChoseGrammar.class);
                    intent.putExtra("gmId", subjectGm.getSb_id());
                    intent.putExtra("gmTitle", subjectGm.getSb_name());
                    intent.putExtra("gmScore", subjectGm.getSb_score());
                    Log.d("tag",subjectGm.getSb_id()+"");
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
