package databases;

/**
 * Created by DongND on 3/29/2017.
 */

public class EEDict {

    public String word;
    public String spelling;
    public String mean;

    public EEDict(String word, String spelling, String mean){
        this.word=word;
        this.spelling=spelling;
        this.mean=mean;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSpelling() {
        return spelling;
    }

    public void setSpelling(String spelling) {
        this.spelling = spelling;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }
}
