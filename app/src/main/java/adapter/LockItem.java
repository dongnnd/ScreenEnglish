package adapter;

/**
 * Created by dongnd on 30/05/2017.
 */

public class LockItem {

    private int img;
    private String title;

    public LockItem(int img, String title){
        this.img=img;
        this.title=title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
