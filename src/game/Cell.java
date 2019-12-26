package game;


import javafx.scene.image.ImageView;

public class Cell {
    private int i;
    private int j;
    private String value;
    private boolean isOpened = false;
    private boolean hasFlag = false;
    private boolean hasMine;
    private ImageView camoView;
    private ImageView flagView;

    private double probability = 0.0;

    public Cell(int i, int j, String value, boolean hasMine) {
        this.i = i;
        this.j = j;
        this.value = value;
        this.hasMine = hasMine;
        camoView = new ImageView(Images.CAMO_IMAGE);
        flagView = new ImageView(Images.FLAG_IMAGE);
    }

    public Cell() {

    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public boolean hasFlag() {
        return hasFlag;
    }

    public void setFlag(boolean hasFlag) {
        this.hasFlag = hasFlag;
    }

    public boolean hasMine() {
        return hasMine;
    }

    public void setMine(boolean hasMine) {
        this.hasMine = hasMine;
    }

    public ImageView getCamoView() {
        return camoView;
    }

    public void setCamoView(ImageView camoView) {
        this.camoView = camoView;
    }

    public ImageView getFlagView() {
        return flagView;
    }

    public void setFlagView(ImageView flagView) {
        this.flagView = flagView;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }
}
