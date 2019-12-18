package sample;


import javafx.scene.image.ImageView;

public class Cell {
    int i;
    int j;
    String value;
    boolean isOpened = false;
    boolean hasFlag = false;
    boolean hasMine;
    ImageView camoView;
    ImageView flagView;

    double probability = 0.0;

    public Cell(int i, int j, String value, boolean hasMine) {
        this.i = i;
        this.j = j;
        this.value = value;
        this.hasMine = hasMine;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public double getProbability() {
        return probability;
    }
}
