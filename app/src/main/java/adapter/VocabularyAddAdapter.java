package adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dongnd.screenenglish.R;

import java.util.List;

import databases.Vocabulary;

/**
 * Created by DongND on 5/14/2017.
 */

public class VocabularyAddAdapter extends ArrayAdapter<AddItem>{

    public Activity context;
    public List<AddItem> list;
    public int layoutId;
    public AddItem item;

    public VocabularyAddAdapter(Activity context,int layoutId ,List<AddItem> list){
        super(context, layoutId, list);
        this.context=context;
        this.list=list;
        this.layoutId=layoutId;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View view=inflater.inflate(R.layout.item_add, parent, false);

        item=list.get(position);
        TextView title=(TextView)view.findViewById(R.id.item_add_title);
        TextView sub=(TextView)view.findViewById(R.id.item_add_sub);
        final ImageView img01=(ImageView)view.findViewById(R.id.item_add_img);
        img01.setImageResource(R.drawable.ic_uncheck);

        CheckBox checkBox=(CheckBox)view.findViewById(R.id.item_add_check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    list.get(position).setState(true);
                }else{
                    list.get(position).setState(false);
                }
            }
        });

        title.setText(item.getTitle());
        sub.setText(item.getSubTitle());

        return view;
    }
}
