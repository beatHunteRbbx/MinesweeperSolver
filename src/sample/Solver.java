package sample;

import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Solver {

    public int maxNumberOfMines;
    public int rows;
    public int columns;

    public String[][] arrField;
    public Integer[][] arrMines;
    public Cell[][] arrCells;

    public List<Mine> listOfMines = new LinkedList<>();

    public List<Cell> markedCellsList = new LinkedList<>();
    public List<Cell> cellsWithMinesList = new LinkedList<>();
    public List<Cell> closedCellsList = new LinkedList<>();

    public List<Group> groupsList = new ArrayList<>();

    public List<Cell> doneCells = new ArrayList<>();
    public GameField gameField;

    public Solver(GameField gameField) {
        this.gameField = gameField;
        arrCells = gameField.arrCells;
        rows = gameField.rows;
        columns = gameField.columns;
    }
    public void solve() {
        click();
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                createGroups();
                filterGroups();
                openCells(groupsList);
            }
        };
        animationTimer.start();

    }

    public void click() {
        Random random = new Random();
        int maxForI = rows - 1;
        int minForI = 1;

        int maxForJ = columns - 1;
        int minForJ = 1;

        int i = random.nextInt((maxForI - minForI) + 1) + minForI;
        int j = random.nextInt((maxForJ - minForJ) + 1) + minForJ;
        Cell cell = arrCells[i][j];
        if (cell == null) click();
        else if (!cell.value.equals("0")) {
            cell.camoView.setVisible(false);
            cell.isOpened = true;
            if (cell.value.equals("*")) {
                gameField.lose();
                return;
            }
            click();
        }
        else {
            cell.camoView.setVisible(false);
            cell.isOpened = true;
        }
    }
    public void createGroups() {
        groupsList.clear();
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                Cell currentCell = arrCells[i][j];
                if (currentCell.hasMine) {
                    Main.mainThread.interrupt();
                    break;
                }
                if (currentCell.isOpened && !doneCells.contains(arrCells[i][j])) {
                    Group group = new Group();
                    group.minesNumber = Integer.parseInt(currentCell.value);
                    for (int x = -1; i < 2; i++) {
                        for (int y = -1; j < 2; j++) {
                            Cell neighbour = arrCells[currentCell.i + x][currentCell.j + y];
                            if (neighbour != null && !neighbour.isOpened) group.add(neighbour);
                        }
                    }
                    groupsList.add(group);
                    doneCells.add(currentCell);
                }
            }
        }
    }

    public void filterGroups() {
        boolean repeat;
        do {
            repeat = false;
            for (int i = 0; i < groupsList.size() - 1; i++) { //проходим по списку групп
                Group group = groupsList.get(i);
                //сравниваем выбранную группу с остальными группами из списка
                for (int j = i + 1; j < groupsList.size(); j++) {
                    Group groupToCompare = groupsList.get(j);
                    if (group.equals(groupToCompare)) {
                        groupsList.remove(j--);
                        break;
                    }

                    Group largerGroup;
                    Group smallerGroup;

                    if (group.size() > groupToCompare.size()) { //определяем какая группа из двух больше
                        largerGroup = group;
                        smallerGroup = groupToCompare;
                    }
                    else {
                        largerGroup = groupToCompare;
                        smallerGroup = group;
                    }
                    if (largerGroup.getMembers().contains(smallerGroup.getMembers())) {
                        largerGroup.removeGroup(smallerGroup);
                        repeat = true;
                    }
                    else if (group.intersects(groupToCompare)) {
                        if (group.getMinesNumber() > groupToCompare.getMinesNumber()) {
                            largerGroup = group;
                            smallerGroup = groupToCompare;
                        }
                        else {
                            largerGroup = groupToCompare;
                            smallerGroup = group;
                        }
                        Group intersectGroup = largerGroup.getIntersect(smallerGroup);
                        if (intersectGroup != null) {
                            groupsList.add(intersectGroup);
                            largerGroup.removeGroup(smallerGroup);
                            smallerGroup.removeGroup(largerGroup);
                            repeat = true;
                        }
                    }
                }
            }
        } while (repeat);
    }

    public void openCells(List<Group> groupsList) {
        for (Group group : groupsList) {
            for (Cell cell : group.getMembers()) {
                arrCells[cell.i][cell.j].camoView.setVisible(false);
            }
        }
    }
}
