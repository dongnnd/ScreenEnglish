package databases;

/**
 * Created by DongND on 4/19/2017.
 */

public class Question {

    public int qs_id;
    public String question;
    public String tr_answer;
    public String err1_answer;
    public String err2_answer;
    public int qs_ref;

    public Question(int qs_id, String question, String tr_answer, String err1_answer, String err2_answer, int qs_ref){
        this.qs_id=qs_id;
        this.question=question;
        this.tr_answer=tr_answer;
        this.err1_answer=err1_answer;
        this.err2_answer=err2_answer;
        this.qs_ref=qs_ref;
    }

    public int getQs_id() {
        return qs_id;
    }

    public void setQs_id(int qs_id) {
        this.qs_id = qs_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTr_answer() {
        return tr_answer;
    }

    public void setTr_answer(String tr_answer) {
        this.tr_answer = tr_answer;
    }

    public String getErr1_answer() {
        return err1_answer;
    }

    public void setErr1_answer(String err1_answer) {
        this.err1_answer = err1_answer;
    }

    public String getErr2_answer() {
        return err2_answer;
    }

    public void setErr2_answer(String err2_answer) {
        this.err2_answer = err2_answer;
    }

    public int getQs_ref() {
        return qs_ref;
    }

    public void setQs_ref(int qs_ref) {
        this.qs_ref = qs_ref;
    }
}
