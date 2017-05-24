package adapter;

/**
 * Created by DongND on 5/3/2017.
 */

public class GmPractiveItem {

    private String title;
    private String score;

    public GmPractiveItem(String title, String score){
        this.title=title;
        this.score=score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
