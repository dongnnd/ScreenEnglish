package adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.dongnd.screenenglish.R;

import java.util.ArrayList;

/**
 * Created by DongND on 4/11/2017.
 */

public class GalleryAdapter extends ArrayAdapter<GalleryItem>{

    public Context context;
    public int layoutId;
    public ArrayList<GalleryItem> arr;

    public GalleryAdapter(Context context, int layoutId, ArrayList<GalleryItem> arr){
        super(context, layoutId, arr);
        this.context=context;
        this.layoutId=layoutId;
        this.arr=arr;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        ViewHolder holder;

        if(view==null){
            LayoutInflater inflater=((Activity)context).getLayoutInflater();
            view=inflater.inflate(layoutId, parent, false);

            holder=new ViewHolder();
            holder.imageView=(ImageView)view.findViewById(R.id.gallery_item);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }

        holder.imageView.setImageResource(arr.get(position).getImg_small());

        return view;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public static class ViewHolder{
        ImageView imageView;


    }
}
