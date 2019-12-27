package solver;

import game.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Group {

    public Group(List<Cell> members, int minesNumber) {
        this.members = members;
        this.minesNumber = minesNumber;
    }
    public Group() {

    }

    private List<Cell> members = new ArrayList<>();
    private int minesNumber;

    int getMinesNumber() {
        return minesNumber;
    }

    List<Cell> getMembers() {
        return members;
    }

    void setMinesNumber(int minesNumber) {
        this.minesNumber = minesNumber;
    }

    void add(Cell cell) {
        members.add(cell);
    }

    public void remove(Cell cell) {
        members.remove(cell);
    }

    int size() {
        return members.size();
    }

    boolean intersects(Group group) {
        boolean hasIntersects = false;
        for (Cell cell : group.members) {
            if (this.members.contains(cell)) {
                hasIntersects = true;
                break;
            }
        }
        if (hasIntersects) return true;
        else return false;
    }

    Group getIntersect(Group group) {
        Group intersectGroup = new Group();
        for (Cell cell : group.getMembers()) {
            if (this.getMembers().contains(cell)) {
                intersectGroup.add(cell);
            }
        }
        intersectGroup.setMinesNumber(minesNumber - (this.size() - intersectGroup.size()));
        if (intersectGroup.getMinesNumber() != group.getMinesNumber()) return null;
        return intersectGroup;
    }

    List<Double> getProbabilities() {
        List<Double> probList = new ArrayList<>();
        for (Cell cell : members) probList.add(cell.getProbability());
        return probList;
    }

    void subtract(Group group) {
        for (Cell member : group.getMembers()) this.members.remove(member);
        this.minesNumber -= group.minesNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return minesNumber == group.minesNumber &&
                Objects.equals(members, group.members) &&
                group.members.containsAll(this.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(members, minesNumber);
    }
}
