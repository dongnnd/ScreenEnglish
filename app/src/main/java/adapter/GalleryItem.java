package adapter;

/**
 * Created by DongND on 4/12/2017.
 */

public class GalleryItem {

    public int img_small;
    public int img_large;

    public GalleryItem(int img_small, int img_large){
        this.img_small=img_small;
        this.img_large=img_large;
    }

    public int getImg_small() {
        return img_small;
    }

    public void setImg_small(int img_small) {
        this.img_small = img_small;
    }

    public int getImg_large() {
        return img_large;
    }

    public void setImg_large(int img_large) {
        this.img_large = img_large;
    }
}
