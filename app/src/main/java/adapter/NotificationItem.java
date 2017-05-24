package adapter;

import android.graphics.Bitmap;

/**
 * Created by DongND on 5/1/2017.
 */

public class NotificationItem {

    public String pack;
    public String title;
    public String content;
    public Bitmap img;

    public NotificationItem(){

    }

    public NotificationItem(String pack, String title, String content, Bitmap img){
        this.pack=pack;
        this.title=title;
        this.content=content;
        this.img=img;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
