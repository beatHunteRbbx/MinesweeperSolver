package solver;

import game.Cell;

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
//        for (Cell cell : group.members) if (!members.contains(cell)) return false;
//        return true;
    }

    public Group getIntersect(Group group) {
//        Group localGroup1 = new Group(new ArrayList<>(this.getMembers()), this.getMinesNumber());
//        Group localGroup2 = new Group(new ArrayList<>(group.getMembers()), group.getMinesNumber());

        Group intersectGroup = new Group();
        for (Cell cell : group.getMembers()) {
            if (this.getMembers().contains(cell)) {
                intersectGroup.add(cell);
            }
        }
//        Group largerGroup;
//        Group smallerGroup;
//
//        if (this.getMinesNumber() >= group.getMinesNumber()) {
//            largerGroup = this;
//            smallerGroup = group;
////            intersectGroup.setMinesNumber(this.getMinesNumber() - group.size());
//        }
//        else {
//            largerGroup = group;
//            smallerGroup = this;
////            intersectGroup.setMinesNumber(group.getMinesNumber() - this.size());
//        }
//
//
//        intersectGroup.setMinesNumber(this.getMinesNumber() - group.size());

        intersectGroup.setMinesNumber(minesNumber - (this.size() - intersectGroup.size()));
//        intersectGroup.setMinesNumber(this.size() - (group.size() - intersectGroup.size()) - minesNumber);
//        if (intersectGroup.getMinesNumber() < 0) intersectGroup.setMinesNumber(1);
        if (intersectGroup.getMinesNumber() != group.getMinesNumber()) return null;
        return intersectGroup;
    }

//    public void removeGroup(Group group) {
//        for (Cell member : group.getMembers()) this.getMembers().remove(member);
//    }

    public List<Double> getProbabilities() {
        List<Double> probList = new ArrayList<>();
        for (Cell cell : members) probList.add(cell.getProbability());
        return probList;
    }

    public void substract(Group group) {
        for (Cell member : group.getMembers()) this.members.remove(member);
        this.minesNumber -= group.minesNumber;
    }


}
