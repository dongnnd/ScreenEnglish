package adapter;

/**
 * Created by DongND on 5/16/2017.
 */

public class CurrentVocabulary {

    private int id;
    private String word;
    private String spellingaa;
    private String spellingam;
    private String tr_mean;
    private String err1_mean;
    private String err2_mean;
    private byte[] sound;
    private byte[] img;
    private String select;

    public CurrentVocabulary(int id, String word, String spellingaa, String spellingam,
                             String tr_mean, String err1_mean, String err2_mean, byte[] sound,
                             byte[] img, String select){
        this.id=id;
        this.word=word;
        this.spellingaa=spellingaa;
        this.spellingam=spellingam;
        this.tr_mean=tr_mean;
        this.err1_mean=err1_mean;
        this.err2_mean=err2_mean;
        this.sound=sound;
        this.img=img;
        this.select=select;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSpellingaa() {
        return spellingaa;
    }

    public void setSpellingaa(String spellingaa) {
        this.spellingaa = spellingaa;
    }

    public String getSpellingam() {
        return spellingam;
    }

    public void setSpellingam(String spellingam) {
        this.spellingam = spellingam;
    }

    public String getTr_mean() {
        return tr_mean;
    }

    public void setTr_mean(String tr_mean) {
        this.tr_mean = tr_mean;
    }

    public String getErr1_mean() {
        return err1_mean;
    }

    public void setErr1_mean(String err1_mean) {
        this.err1_mean = err1_mean;
    }

    public String getErr2_mean() {
        return err2_mean;
    }

    public void setErr2_mean(String err2_mean) {
        this.err2_mean = err2_mean;
    }

    public byte[] getSound() {
        return sound;
    }

    public void setSound(byte[] sound) {
        this.sound = sound;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }
}
