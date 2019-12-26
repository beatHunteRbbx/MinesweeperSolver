package solver;

import game.Cell;
import game.GameField;
import game.Main;
import javafx.animation.AnimationTimer;

import java.util.*;

public class Solver {

    private int rows;
    private int columns;

    private Cell[][] arrCells;

    private List<Group> groupsList = new ArrayList<>();
    private List<Group> filteredGroupsList = new ArrayList<>();

    private GameField gameField;

    private boolean hasLost;
    private boolean isFirstClick;

    public Solver(GameField gameField) {
        this.gameField = gameField;
        arrCells = gameField.arrCells;
        rows = gameField.rows;
        columns = gameField.columns;
        hasLost = false;
        isFirstClick = true;
    }

    public void autoSolve() {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                    if (hasLost) return;
                    click();
                    createGroups();
                    filterGroups(groupsList);
                    correctProbabilities();
                    openCells(filteredGroupsList);
            }
        };
        animationTimer.start();
    }

    public void stepByStepSolve() {
        click();
        createGroups();
        filterGroups(groupsList);
        correctProbabilities();
        openCells(filteredGroupsList);
    }

    private void click() {
        if (isFirstClick) {
            Random random = new Random();
            int maxForI = rows - 2;
            int minForI = 1;

            int maxForJ = columns - 2;
            int minForJ = 1;

            int i = random.nextInt((maxForI - minForI) + minForI) + minForI;
            int j = random.nextInt((maxForJ - minForJ) + minForJ) + minForJ;
            Cell cell = arrCells[i][j];

                if (cell == null || cell.getFlagView().isVisible() || cell.hasMine()) {
                    click();
                }
                else if (cell.getCamoView() != null) {
                    isFirstClick = false;
                    cell.getCamoView().setVisible(false);
                    cell.setOpened(true);
                    if (cell.getValue().equals("0")) {
                        gameField.clearNeighboursCells(cell);
                    }
                }
        }
        else {
            createGroups();
            filterGroups(groupsList);
            correctProbabilities();
            double min = 10000000.0;
            Cell cellToOpen = new Cell();
            for (Group group : filteredGroupsList) {
                for (Cell currentCell : group.getMembers()) {
                    if (currentCell.getProbability() < min) {
                        min = currentCell.getProbability();
                        cellToOpen = currentCell;
                    }
                }
            }
            if (!arrCells[cellToOpen.getI()][cellToOpen.getJ()].hasFlag()) {
                arrCells[cellToOpen.getI()][cellToOpen.getJ()].getCamoView().setVisible(false);
                arrCells[cellToOpen.getI()][cellToOpen.getJ()].setOpened(true);
                if (arrCells[cellToOpen.getI()][cellToOpen.getJ()].getValue().equals("*")) {
                    gameField.lose();
                    hasLost = true;
                    return;
                }
                if (arrCells[cellToOpen.getI()][cellToOpen.getJ()].getValue().equals("0")) {
                    gameField.clearNeighboursCells(cellToOpen);
                }
            }
        }
    }
    private void createGroups() {
        groupsList.clear();

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                Cell currentCell = arrCells[i][j];
                if (currentCell.isOpened() && !currentCell.getValue().equals("0")) {
                    if (currentCell.hasMine()) {
                        Main.mainThread.interrupt();
                        return;
                    }
                        Group group = new Group();
                        group.setMinesNumber(Integer.parseInt(currentCell.getValue()));
                        for (int x = -1; x < 2; x++) {
                            for (int y = -1; y < 2; y++) {
                                Cell neighbour = arrCells[currentCell.getI() + x][currentCell.getJ() + y];
                                    if (neighbour != null && !neighbour.isOpened()) {
                                        if (    (neighbour.getI() < rows - 1 && neighbour.getI() > 0) &&
                                                (neighbour.getJ() < columns - 1 && neighbour.getJ() > 0) ) {
                                            group.add(neighbour);
                                        }
                                }
                            }
                        }
                        groupsList.add(group);

                        //вычисление вероятности нахождения мины в ячейках у каждой ячейки из группы
                        for (Cell cell : group.getMembers()) {
                            double probability = (double) group.getMinesNumber() / group.size();
                            if (cell.getProbability() == 0.0) {
                                cell.setProbability(probability);
                            }
                            else {
                                cell.setProbability(Probability.calculate(cell.getProbability(), probability));
                            }
                        }
                }
            }
        }
    }

    private void filterGroups(List<Group> groupsList) {
            filteredGroupsList.clear();
            if (groupsList.size() == 1) {
                filteredGroupsList.addAll(groupsList);
                return;
            }


                for (int i = 0; i < groupsList.size() - 1; i++) { //проходим по списку групп
                    Group group = groupsList.get(i);
                    //сравниваем выбранную группу с остальными группами из списка
                    for (int j = i + 1; j < groupsList.size(); j++) {
                        Group groupToCompare = groupsList.get(j);
//                        if (groupToCompare.size() == groupToCompare.getMinesNumber()) continue;
                        if (group.equals(groupToCompare)) {
                            groupsList.remove(j--);
                            break;
                        }

                        Group largerGroup;
                        Group smallerGroup;

                        if (group.size() >= groupToCompare.size()) { //определяем какая группа из двух больше
                            largerGroup = group;
                            smallerGroup = groupToCompare;
                        } else {
                            largerGroup = groupToCompare;
                            smallerGroup = group;
                        }
                        if (largerGroup.getMembers().containsAll(smallerGroup.getMembers())) {
                            largerGroup.subtract(smallerGroup);

//                        largerGroup.removeGroup(smallerGroup);
//                        largerGroup.setMinesNumber(largerGroup.getMinesNumber() - smallerGroup.getMinesNumber());
//                        Group intersectsGroup = new Group(
//                                new ArrayList<>(largerGroup.getMembers()),
//                                largerGroup.getMinesNumber()
//                        );
//                            filteredGroupsList.add(largerGroup);
                        } else if (group.intersects(groupToCompare)) {   //если группы пересекаются
                            if (group.getMinesNumber() >= groupToCompare.getMinesNumber()) {   //определяем большую группу по количеству мин
                                largerGroup = group;
                                smallerGroup = groupToCompare;
                            } else {
                                largerGroup = groupToCompare;
                                smallerGroup = group;
                            }
                            Group intersectGroup = largerGroup.getIntersect(smallerGroup);
                            if (intersectGroup != null) {
                                filteredGroupsList.add(intersectGroup);

                                largerGroup.subtract(intersectGroup);
                                smallerGroup.subtract(intersectGroup);
                            }
                        } else {
                            if (!filteredGroupsList.contains(group)) filteredGroupsList.add(group);
                            if (!filteredGroupsList.contains(groupToCompare)) filteredGroupsList.add(groupToCompare);
                        }
                    }
                }
            List<Group> tempList = new ArrayList<>(filteredGroupsList);
            for (Group group : filteredGroupsList) {
                if (group.size() == 0 && group.getMinesNumber() == 0) tempList.remove(group);
            }
            filteredGroupsList = tempList;
    }

    private void openCells(List<Group> groupsList) {
        for (Group group : groupsList) {
            if (group.getMinesNumber() == 0) {
                for (Cell cell : group.getMembers()) {
                    if (!arrCells[cell.getI()][cell.getJ()].hasFlag()) {
                        arrCells[cell.getI()][cell.getJ()].getCamoView().setVisible(false);
                        arrCells[cell.getI()][cell.getJ()].setOpened(true);
                        if (arrCells[cell.getI()][cell.getJ()].getValue().equals("0")) gameField.clearNeighboursCells(cell);
                    }

                }
            }
            else if (group.getMinesNumber() == group.size()) {
                for (Cell cell : group.getMembers()) {
                    if (!cell.isOpened()) {
                        arrCells[cell.getI()][cell.getJ()].setFlag(true);
                        arrCells[cell.getI()][cell.getJ()].getFlagView().setVisible(true);
                    }
                }
            }
            else {
                double min = 10000000.0;
                Cell cellToOpen = new Cell();
                Collections.shuffle(group.getMembers());
                for (Cell cell : group.getMembers()) {
                    if (cell.getProbability() < min) {
                        min = cell.getProbability();
                        cellToOpen = cell;
                    }
                }
                arrCells[cellToOpen.getI()][cellToOpen.getJ()].getCamoView().setVisible(false);
                arrCells[cellToOpen.getI()][cellToOpen.getJ()].setOpened(true);
                if (arrCells[cellToOpen.getI()][cellToOpen.getJ()].hasMine()) {
                    gameField.lose();
                    hasLost = true;
                    return;
                }
                if (arrCells[cellToOpen.getI()][cellToOpen.getJ()].getValue().equals("0")) {
                    gameField.clearNeighboursCells(cellToOpen);
                }
            }

        }
    }

    private void correctProbabilities() {
        Map<Cell, Double> cellsMap = new HashMap<>();
        for (Group group : filteredGroupsList) {
            for (Cell cell : group.getMembers()) {
                Double value = cellsMap.get(cell);
                if (value == null) cellsMap.put(cell, (double) group.getMinesNumber() / group.size());
                else {
                    cellsMap.put(cell, Probability.calculate(value, (double) group.getMinesNumber() / group.size()));
                }
            }
        }

        //корректировка вероятностей с учетом того, что
        //сумма вероятностей должна быть равна количеству мин в группе
                for (Group group : filteredGroupsList) {
                    List<Double> probList = group.getProbabilities();
                    Double sum = 0.0;
                    for (Double prob : probList) sum += prob;
                    int mines = group.getMinesNumber();
                    if (Math.abs(sum - mines) > 1) {


                        Probability.correct(probList, mines);

                        for (int i = 0; i < group.size(); i++) {
                            double value = probList.get(i);
                            group.getMembers().get(i).setProbability(value);
                        }
                    }
                }
        for (Cell cell: cellsMap.keySet()){  // перестраховка
            if (cell.getProbability()> 0.99) cell.setProbability(0.99);
            if (cell.getProbability()< 0.0) cell.setProbability(0.0);
        }
    }
}
