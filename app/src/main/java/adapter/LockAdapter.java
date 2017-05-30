package adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dongnd.screenenglish.R;

import java.util.List;

/**
 * Created by dongnd on 30/05/2017.
 */

public class LockAdapter extends ArrayAdapter<LockItem>{

    public Activity context;
    public List<LockItem> list;
    public int layoutId;

    public LockAdapter(Activity context, int layoutId, List<LockItem> list){
        super(context, layoutId, list);

        this.context=context;
        this.layoutId=layoutId;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View view=inflater.inflate(R.layout.item_lock, parent, false);

        LockItem item=list.get(position);

        ImageView icon=(ImageView)view.findViewById(R.id.item_dialog_img);
        TextView title=(TextView)view.findViewById(R.id.item_dialog_text);

        icon.setImageResource(item.getImg());
        title.setText(item.getTitle());

        return view;
    }
}
