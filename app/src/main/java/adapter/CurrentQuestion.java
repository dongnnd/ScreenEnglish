package adapter;

/**
 * Created by DongND on 5/9/2017.
 */

public class CurrentQuestion {
    public int id;
    public String question;
    public String tr_answear;
    public String err1_answear, err2_answear;
    public String select;

    public String explain;

    public CurrentQuestion(){

    }

    public CurrentQuestion(int id, String question, String tr_answear, String err1_answear, String err2_answear,
                           String explain, String select){
        this.id=id;
        this.question=question;
        this.tr_answear=tr_answear;
        this.err1_answear=err1_answear;
        this.err2_answear=err2_answear;
        this.explain=explain;
        this.select=select;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTr_answear() {
        return tr_answear;
    }

    public void setTr_answear(String tr_answear) {
        this.tr_answear = tr_answear;
    }

    public String getErr1_answear() {
        return err1_answear;
    }

    public void setErr1_answear(String err1_answear) {
        this.err1_answear = err1_answear;
    }

    public String getErr2_answear() {
        return err2_answear;
    }

    public void setErr2_answear(String err2_answear) {
        this.err2_answear = err2_answear;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
}
