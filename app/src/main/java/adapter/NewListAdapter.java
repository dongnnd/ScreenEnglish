package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dongnd.screenenglish.R;
import com.example.dongnd.screenenglish.VocabularyNewList;

import java.util.List;

import databases.Vocabulary;
import databases.VocabularyItem;

/**
 * Created by DongND on 5/14/2017.
 */

public class NewListAdapter extends RecyclerView.Adapter<NewListAdapter.AdapterHolder>{

    public Context context;
    public List<VocabularyItem> list;
    public LayoutInflater inflater;
    public VocabularyItem item;
    public VocabularyNewList vocabularyNewList=VocabularyNewList.getInstance();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public NewListAdapter(Context context, List<VocabularyItem> list){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);

        sharedPreferences=context.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    @Override
    public void onBindViewHolder(AdapterHolder holder, int position) {
        item=list.get(position);
        String learned=sharedPreferences.getString("learned", null);
        if(learned!=null){
            if(learned.contains(item.getVc_id()+"")){
                holder.img_01.setImageResource(R.drawable.ic_check);
            }else{
                holder.img_01.setImageResource(R.drawable.ic_uncheck);
            }
        }else{
            holder.img_01.setImageResource(R.drawable.ic_uncheck);
        }


        holder.text_view.setText(item.getWord());
    }

    @Override
    public AdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_new_list, parent, false);
        return new NewListAdapter.AdapterHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AdapterHolder extends RecyclerView.ViewHolder{
        public ImageView img_01, img_02;
        public TextView text_view;

        public AdapterHolder(final View view){
            super(view);



            img_01=(ImageView)view.findViewById(R.id.nl_img1);


            img_02=(ImageView)view.findViewById(R.id.nl_img2);
            text_view=(TextView)view.findViewById(R.id.nl_title);


            img_02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(vocabularyNewList);
                    builder.setTitle("Bạn có muốn xóa không?");
                    builder.setIcon(R.drawable.ic_uncheck);

                    builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String str=vocabularyNewList.sharedPreferences.getString("currentList", null);
                            String learned=vocabularyNewList.sharedPreferences.getString("learned", null);

                            String[] strArr=str.split(",");

                           if(learned.contains(list.get(getAdapterPosition()).getVc_id()+"")){
                               String[] learnedArr=learned.split(",");
                               if(learnedArr.length==1){
                                   editor.putString("learned", null);
                                   editor.commit();
                               }else{
                                   if(learnedArr[0].equals(list.get(getAdapterPosition()).getVc_id()+"")){
                                       learned=learned.replace(list.get(getAdapterPosition()).getVc_id()+",", "");
                                       editor.putString("learned", learned);
                                       editor.commit();
                                   }else{
                                       learned=learned.replace("," +list.get(getAdapterPosition()).getVc_id(), "");
                                       editor.putString("learned", learned);
                                       editor.commit();
                                   }
                               }

                            }else{

                           }

                            if(strArr.length==1){
                                vocabularyNewList.editor.putString("currentList", null);
                                vocabularyNewList.editor.commit();
                            }else{

                                if(strArr[0].equals(list.get(getAdapterPosition()).getVc_id()+"")){
                                    str=str.replace(list.get(getAdapterPosition()).getVc_id()+",", "");
                                }else{
                                    str=str.replace(","+ list.get(getAdapterPosition()).getVc_id(), "");
                                }
                                vocabularyNewList.editor.putString("currentList", str);
                                vocabularyNewList.editor.commit();
                            }
                            vocabularyNewList.list.remove(getAdapterPosition());
                            vocabularyNewList.adapter.notifyDataSetChanged();
                        }
                    });
                    builder.show();
                }
            });

            /*if(learned.contains(list.get(getAdapterPosition()).getVc_id()+"")){
                img_01.setImageResource(R.drawable.ic_check);
            }else{
                img_01.setImageResource(R.drawable.ic_uncheck);
            }*/
        }
    }
}
