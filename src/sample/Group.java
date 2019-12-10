package sample;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private List<Cell> members = new ArrayList<>();
    public int minesNumber;

    public int getMinesNumber() {
        return minesNumber;
    }

    public List<Cell> getMembers() {
        return members;
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
        Group intersectGroup = new Group();
        for (Cell cell : group.members) {
            if (this.members.contains(cell)) intersectGroup.add(cell);
        }
        return intersectGroup;
    }

    public void removeGroup(Group group) {
        for (Cell member : group.members) this.members.remove(member);
    }
}
