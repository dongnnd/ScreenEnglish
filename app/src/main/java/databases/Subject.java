package databases;

/**
 * Created by DongND on 3/31/2017.
 */
public class Subject {

    public int sb_number;
    public String sb_title;
    public String sb_content;
    public byte[] img;

    public Subject(int number, String title, String content, byte[] img){
        this.sb_number=number;
        this.sb_title=title;
        this.sb_content=content;
        this.img=img;
    }

    public int getSb_number() {
        return sb_number;
    }

    public void setSb_number(int sb_number) {
        this.sb_number = sb_number;
    }

    public String getSb_title() {
        return sb_title;
    }

    public void setSb_title(String sb_title) {
        this.sb_title = sb_title;
    }

    public String getSb_content() {
        return sb_content;
    }

    public void setSb_content(String sb_content) {
        this.sb_content = sb_content;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}