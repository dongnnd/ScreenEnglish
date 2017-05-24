package databases;

/**
 * Created by DongND on 4/19/2017.
 */

public class SubjectGm {

    public int sb_id;
    public String sb_name;
    public String sb_score;

    public SubjectGm(int sb_id, String sb_name, String sb_score){
        this.sb_id=sb_id;
        this.sb_name=sb_name;
        this.sb_score=sb_score;
    }

    public int getSb_id() {
        return sb_id;
    }

    public void setSb_id(int sb_id) {
        this.sb_id = sb_id;
    }

    public String getSb_name() {
        return sb_name;
    }

    public void setSb_name(String sb_name) {
        this.sb_name = sb_name;
    }

    public String getSb_score() {
        return sb_score;
    }

    public void setSb_score(String sb_score) {
        this.sb_score = sb_score;
    }
}
