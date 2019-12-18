package sample;

import java.util.ArrayList;
import java.util.List;

public class Group {

    public Group(List<Cell> members, int minesNumber) {
        this.members = members;
        this.minesNumber = minesNumber;
    }
    public Group() {

    }

    private List<Cell> members = new ArrayList<>();
    private int minesNumber;

    public int getMinesNumber() {
        return minesNumber;
    }

    public List<Cell> getMembers() {
        return members;
    }

    public void setMinesNumber(int minesNumber) {
        this.minesNumber = minesNumber;
    }

    public void add(Cell cell) {
        members.add(cell);
    }

    public void remove(Cell cell) {
        members.remove(cell);
    }

    public int size() {
        return members.size();
    }

    public boolean intersects(Group group) {
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

    public Group getIntersect(Group group) {
        Group localGroup1 = new Group(new ArrayList<>(this.getMembers()), this.getMinesNumber());
        Group localGroup2 = new Group(new ArrayList<>(group.getMembers()), group.getMinesNumber());

        Group intersectGroup = new Group();
        for (Cell cell : group.getMembers()) {
            if (this.getMembers().contains(cell)) {
                intersectGroup.add(cell);
                localGroup1.remove(cell);
                localGroup2.remove(cell);
            }
        }
        if (this.getMinesNumber() > group.getMinesNumber()) {
            intersectGroup.setMinesNumber(this.getMinesNumber() - localGroup2.size());
        }
        else if (this.getMinesNumber() < group.getMinesNumber()) {
            intersectGroup.setMinesNumber(group.getMinesNumber() - localGroup1.size());
        }
        return intersectGroup;
    }

    public void removeGroup(Group group) {
        for (Cell member : group.getMembers()) this.getMembers().remove(member);
    }

    public List<Double> getProbabilities() {
        List<Double> probList = new ArrayList<>();
        for (Cell cell : members) probList.add(cell.probability);
        return probList;
    }


}
