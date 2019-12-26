package game;

public class Mine {
    private int i;
    private int j;

    Mine(int i, int j) {
        this.i = i;
        this.j = j;
    }

    int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    int getJ() {
        return j;
    }

    void setJ(int j) {
        this.j = j;
    }
}
