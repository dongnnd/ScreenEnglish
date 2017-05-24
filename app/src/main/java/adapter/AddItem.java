package adapter;

/**
 * Created by DongND on 5/15/2017.
 */

public class AddItem {
    public int id;
    public String title;
    public String subTitle;
    public boolean state;

    public AddItem(int id, String title, String subTitle, boolean state){
        this.id=id;
        this.title=title;
        this.subTitle=subTitle;
        this.state=state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
