package sample;

import javafx.animation.AnimationTimer;

import java.util.*;

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
    public List<Group> filteredGroupsList = new ArrayList<>();

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
                filterGroups(groupsList);
                correctProbabilities();
                openCells(filteredGroupsList);
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
        if (cell == null || cell.flagView.isVisible()) click();
        else if (!cell.value.equals("0")) {
            cell.camoView.setVisible(false);
//            cell.isOpened = true;
            if (cell.value.equals("*")) {
                gameField.lose();
                return;
            }
            click();
        }
        else {
            cell.camoView.setVisible(false);
//            cell.isOpened = true;
        }
    }
    public void createGroups() {
        groupsList.clear();
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                Cell currentCell = arrCells[i][j];
                if (!currentCell.camoView.isVisible() && !currentCell.value.equals("0") && !currentCell.value.equals("*")) {
                    if (currentCell.hasMine) {
                        Main.mainThread.interrupt();
                        return;
                    }
                    if (!currentCell.camoView.isVisible() && !doneCells.contains(arrCells[i][j])) {
                        Group group = new Group();
                        group.setMinesNumber(Integer.parseInt(currentCell.value));
                        for (int x = -1; x < 2; x++) {
                            for (int y = -1; y < 2; y++) {
                                Cell neighbour = arrCells[currentCell.i + x][currentCell.j + y];
                                if (neighbour != null && !neighbour.isOpened) group.add(neighbour);
                            }
                        }
                        groupsList.add(group);
                        doneCells.add(currentCell);

                        //вычисление вероятности нахождения мины в ячейках у каждой ячейки из группы
                        for (Cell cell : group.getMembers()) {
                            if (cell.getProbability() == 0) {
                                cell.setProbability(group.getMinesNumber() / group.getMembers().size());
                            }
                            else {
                                double probability = calcProbability(cell);
                                cell.setProbability(probability);
                            }
                        }
                    }
                }
            }
        }
    }

    public void filterGroups(List<Group> groupsList) {
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
                    if (largerGroup.getMembers().containsAll(smallerGroup.getMembers())) {
                        largerGroup.removeGroup(smallerGroup);
                        largerGroup.setMinesNumber(largerGroup.getMinesNumber() - smallerGroup.getMinesNumber());
                        Group intersectsGroup = new Group(
                                new ArrayList<>(largerGroup.getMembers()),
                                largerGroup.getMinesNumber()
                        );
                        filteredGroupsList.add(intersectsGroup);
                    }
                    else if (group.intersects(groupToCompare)) {   //если группы пересекаются
                        if (group.getMinesNumber() > groupToCompare.getMinesNumber()) {   //определяем большую группу по количеству мин
                            largerGroup = group;
                            smallerGroup = groupToCompare;
                        }
                        else {
                            largerGroup = groupToCompare;
                            smallerGroup = group;
                        }
                        Group intersectGroup = largerGroup.getIntersect(smallerGroup);
                        if (intersectGroup != null) {
                            filteredGroupsList.add(intersectGroup);
                            largerGroup.removeGroup(smallerGroup);
                            smallerGroup.removeGroup(largerGroup);

                        }
                    }
                }
            }
    }

    public void openCells(List<Group> groupsList) {
        for (Group group : groupsList) {
            if (group.getMinesNumber() == 0) {
                for (Cell cell : group.getMembers()) {
                    if (!arrCells[cell.i][cell.j].hasFlag) {
                        arrCells[cell.i][cell.j].camoView.setVisible(false);
                        arrCells[cell.i][cell.j].isOpened = true;
                    }
                }
            }
            else if (group.getMinesNumber() == group.size()) {
                for (Cell cell : group.getMembers()) {
                    if (!cell.isOpened) {
                        arrCells[cell.i][cell.j].hasFlag = true;
                        arrCells[cell.i][cell.j].flagView.setVisible(true);
                    }
                }
            }
            else {
                double min = 10000000.0;
                Cell cellToOpen = new Cell(1, 1, "0", false);
                for (Cell cell : group.getMembers()) {
                    if (cell.getProbability() < min) {
                        min = cell.getProbability();
                        cellToOpen = cell;
                    }
                }
                arrCells[cellToOpen.i][cellToOpen.j].camoView.setVisible(false);
                arrCells[cellToOpen.i][cellToOpen.j].isOpened = true;
            }

        }
    }

    public void correctProbabilities() {
        Map<Cell, Double> cellsMap = new HashMap<>();
        for (Group group : filteredGroupsList) {
            for (Cell cell : group.getMembers()) {
                Double value = cellsMap.get(cell);
                if (value == null) cellsMap.put(cell, (double) group.getMinesNumber() / group.size());
                else {
                    double probability = calcProbability(cell);
                    cellsMap.put(cell, probability);
                }
            }
        }

        //корректировка вероятностей с учетом того, что
        //сумма вероятностей должна быть равна количеству мин в группе
//        boolean repeat;
//        do {
//            repeat = false;
            for (Group group : filteredGroupsList) {
                List<Double> probList = group.getProbabilities();
                Double sum = 0.0;
                for (Double prob : probList) sum += prob;
                int mines = group.getMinesNumber() * 100;
                if (Math.abs(sum - mines) > 1) {
//                    repeat = true;

                    //Prob.correct(prob,mines);


                    for (int i = 0; i < group.size(); i++) {
                        double value = probList.get(i);
                        group.getMembers().get(i).setProbability(value);
                    }
                }
            }
//        }
//        while (repeat);
    }

    //Вероятность наступления события А, состоящего в появлении хотя бы одного из событий
    // А1, А2,..., Аn, независимых в совокупности, равна разности между единицей и
    // произведением вероятностей противоположных событий. А=1-(1-A1)*(1-A2)*....*(1-An)
    public double calcProbability(Cell cell) {
        double probability =
                1 -
                (1 - (arrCells[cell.i - 1][cell.j - 1] != null ? arrCells[cell.i - 1][cell.j - 1].probability : 0)) *
                (1 - (arrCells[cell.i - 1][cell.j] != null ? arrCells[cell.i - 1][cell.j].probability : 0)) *
                (1 - (arrCells[cell.i - 1][cell.j + 1] != null ? arrCells[cell.i - 1][cell.j + 1].probability : 0)) *
                (1 - (arrCells[cell.i][cell.j + 1] != null ? arrCells[cell.i][cell.j + 1].probability : 0)) *
                (1 - (arrCells[cell.i + 1][cell.j + 1] != null ? arrCells[cell.i + 1][cell.j + 1].probability : 0)) *
                (1 - (arrCells[cell.i + 1][cell.j] != null ? arrCells[cell.i + 1][cell.j].probability : 0)) *
                (1 - (arrCells[cell.i + 1][cell.j - 1] != null ? arrCells[cell.i + 1][cell.j - 1].probability : 0)) *
                (1 - (arrCells[cell.i][cell.j - 1] != null ? arrCells[cell.i][cell.j - 1].probability : 0));
        return probability;

    }
}
