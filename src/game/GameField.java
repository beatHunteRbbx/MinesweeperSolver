package game;

import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.*;

public class GameField {

    public int maxNumberOfMines;
    public int rows;
    public int columns;

    //карта расставленных мин, содержащая нули и единицы, чтобы было удобно расчитывать
    //количество мин рядом с ячейкой и присваивать вычисленное значение ячейке.
    public Integer[][] arrMines;

    public Cell[][] arrCells;


    public List<Mine> listOfMines = new ArrayList<>();

    public List<Cell> markedCellsList = new ArrayList<>();
    public List<Cell> cellsWithMinesList = new ArrayList<>();
    public List<Cell> closedCellsList = new ArrayList<>();

    GridPane gridPane;

    public void initialize() {
        createMines();
        buildField();
        calcMinesAround();
//        show(arrCells);
        run();
    }
    public GameField(int rows, int columns, int maxNumberOfMines) {
        this.rows = rows + 2;
        this.columns = columns + 2;
        this.maxNumberOfMines = maxNumberOfMines;
        arrMines = new Integer[this.rows][this.columns];
        arrCells = new Cell[this.rows][this.columns];
    }

    private void createMines() {
        int counter = 0;
        int allMinesCounter = 0;
        Random random = new Random();
        for (int i = 1; i < (rows - 1) / 2; i+=random.nextInt((2 - 1) + 1) + 1) {
            for (int j = 1; j < (columns - 1) / 2; j+=random.nextInt((2 - 1) + 1) + 1) {
                int number = random.nextInt(2);
                if (number == 1) {
                    listOfMines.add(new Mine(i, j));
                    counter++;
                    allMinesCounter++;
                }
                if (allMinesCounter == maxNumberOfMines) return;
                if (counter == columns / 2) {
                    counter = 0;
                    break;
                }
            }
        }

        for (int i = (rows - 1) / 2; i < rows - 1; i+=random.nextInt((2 - 1) + 1) + 1) {
            for (int j = (columns - 1) / 2; j < columns - 1; j+=random.nextInt((2 - 1) + 1) + 1) {
                int number = random.nextInt(2);
                if (number == 1) {
                    listOfMines.add(new Mine(i, j));
                    counter++;
                    allMinesCounter++;
                }
                if (allMinesCounter == maxNumberOfMines) return;
                if (counter == columns / 2) {
                    counter = 0;
                    break;
                }
            }
        }
    }

    public void calcMinesAround() {
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                if (arrMines[i][j] == 0) {
                    /*
                    подсчет мин вокруг ячейки

                    > > >
                    ^ 0 v
                    < < <
                     */
                    int numberOfMinesNearby =
                            arrMines[i - 1][j - 1] +
                            arrMines[i - 1][j] +
                            arrMines[i - 1][j + 1] +
                            arrMines[i][j + 1] +
                            arrMines[i + 1][j + 1] +
                            arrMines[i + 1][j] +
                            arrMines[i + 1][j - 1] +
                            arrMines[i][j - 1];
                    arrCells[i][j].setValue(Integer.toString(numberOfMinesNearby));
                    Cell cell = new Cell(i, j, Integer.toString(numberOfMinesNearby), false);
                    arrCells[i][j] = cell;
                    closedCellsList.add(cell);
                }
                else {
                    arrCells[i][j].setValue("*");
                    Cell cell = new Cell(i, j, "*", true);
                    arrCells[i][j] = cell;
                    cellsWithMinesList.add(cell);
                    closedCellsList.add(cell);
                }
            }
        }
    }

    private void show(Cell[][] field) {
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                System.out.print(field[i][j].getValue() + " ");
            }
            System.out.println();
        }
    }

    public void buildField() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                arrMines[i][j] = 0;
                arrCells[i][j] = new Cell(i, j, "0", false);
            }
        }
        for (Mine mine : listOfMines) arrMines[mine.getI()][mine.getJ()] = 1;
    }

    public void draw(Pane playPane) {

        gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: grey");

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                switch (arrCells[i][j].getValue()) {
                    case "*":
                        ImageView mineImageView = new ImageView(Images.MINE_IMAGE);
                        mineImageView.setFitWidth(Images.imageW);
                        mineImageView.setFitHeight(Images.imageH);
                        gridPane.add(mineImageView, j, i);
                        break;
                    case "0":
                        ImageView greyImageView = new ImageView(Images.GREY_IMAGE);
                        greyImageView.setFitWidth(Images.imageW);
                        greyImageView.setFitHeight(Images.imageH);
                        gridPane.add(greyImageView, j, i);
                        break;
                    case "1":
                        ImageView oneImageView = new ImageView(Images.ONE_IMAGE);
                        oneImageView.setFitWidth(Images.imageW);
                        oneImageView.setFitHeight(Images.imageH);
                        gridPane.add(oneImageView, j, i);
                        break;
                    case "2":
                        ImageView twoImageView = new ImageView(Images.TWO_IMAGE);
                        twoImageView.setFitWidth(Images.imageW);
                        twoImageView.setFitHeight(Images.imageH);
                        gridPane.add(twoImageView, j, i);
                        break;
                    case "3":
                        ImageView threeImageView = new ImageView(Images.THREE_IMAGE);
                        threeImageView.setFitWidth(Images.imageW);
                        threeImageView.setFitHeight(Images.imageH);
                        gridPane.add(threeImageView, j, i);
                        break;
                    case "4":
                        ImageView fourImageView = new ImageView(Images.FOUR_IMAGE);
                        fourImageView.setFitWidth(Images.imageW);
                        fourImageView.setFitHeight(Images.imageH);
                        gridPane.add(fourImageView, j, i);
                        break;
                    case "5":
                        ImageView fiveImageView = new ImageView(Images.FIVE_IMAGE);
                        fiveImageView.setFitWidth(Images.imageW);
                        fiveImageView.setFitHeight(Images.imageH);
                        gridPane.add(fiveImageView, j, i);
                        break;
                    case "6":
                        ImageView sixImageView = new ImageView(Images.SIX_IMAGE);
                        sixImageView.setFitWidth(Images.imageW);
                        sixImageView.setFitHeight(Images.imageH);
                        gridPane.add(sixImageView, j, i);
                        break;
                    case "7":
                        ImageView sevenImageView = new ImageView(Images.SEVEN_IMAGE);
                        sevenImageView.setFitWidth(Images.imageW);
                        sevenImageView.setFitHeight(Images.imageH);
                        gridPane.add(sevenImageView, j, i);
                        break;
                    case "8":
                        ImageView eightImageView = new ImageView(Images.EIGHT_IMAGE);
                        eightImageView.setFitWidth(Images.imageW);
                        eightImageView.setFitHeight(Images.imageH);
                        gridPane.add(eightImageView, j, i);
                        break;
                    case "9":
                        ImageView nineImageView = new ImageView(Images.NINE_IMAGE);
                        nineImageView.setFitWidth(Images.imageW);
                        nineImageView.setFitHeight(Images.imageH);
                        gridPane.add(nineImageView, j, i);
                        break;
                }
            }
        }

        gridPane.setGridLinesVisible(true);
        playPane.getChildren().add(gridPane);
    }

    public void clearNeighboursCells(Cell cell) {
        if (    !(cell.getI() < 1 || cell.getI() > rows - 2) ||
                !(cell.getJ() < 1 || cell.getJ() > columns - 2)) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    Cell neighbour = arrCells[cell.getI() + i][cell.getJ() + j];
                    if (neighbour == null || neighbour.getValue() == null) continue;
                    if (    (neighbour.getI() < 1 || neighbour.getI() > rows - 2) ||
                            (neighbour.getJ() < 1 || neighbour.getJ() > columns - 2)) continue;
                    if (neighbour.isOpened()) continue;
                    if (!neighbour.hasFlag()) {
                        neighbour.getCamoView().setVisible(false);
                        neighbour.setOpened(true);
                        if (neighbour.getValue().equals("0")) {
                            clearNeighboursCells(neighbour);
                        }
                    }
                }
            }
        }
    }

    public void run() {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (int i = 1; i < rows - 1; i++) {
                    for (int j = 1; j < columns - 1; j++) {
                        Cell cell = arrCells[i][j];
                        if (!cell.getCamoView().isVisible()) {
                            closedCellsList.remove(cell);
                            if (cell.getValue().equals("0")) clearNeighboursCells(cell);
                        }
                    }
                }
                if (cellsWithMinesList.containsAll(closedCellsList)) {
                    for (Cell cell : cellsWithMinesList) {
                        cell.getFlagView().setVisible(true);
//                        victory();
                    }
                }

//                System.out.println("cellsWithMinesList.size()) " + cellsWithMinesList.size());
//                System.out.println("closedCellsList.size() " + closedCellsList.size());
//                System.out.println("markedCellsList.size() " + markedCellsList.size());
//                System.out.println("cellsWithMinesList.equals(markedCellsList) " + cellsWithMinesList.equals(markedCellsList));
//                System.out.println("cellsWithMinesList.equals(closedCellsList) " + cellsWithMinesList.equals(closedCellsList));
//                System.out.println("_____________________________");
//                for (Mine mine : listOfMines) System.out.println("i: " + mine.i + " " + "j:" + mine.j);
//                System.out.println("---------------");
//                for (Cell cell : markedCellsList) System.out.println("i: " + cell.i + " " + "j:" + cell.j);

            }
        };
        animationTimer.start();
    }

    public void startGameProcess() {
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                Cell currentCell = arrCells[i][j];
                ImageView camoImageView = currentCell.getCamoView();
                ImageView flagImageView = currentCell.getFlagView();
                camoImageView.setCursor(Cursor.HAND);
                camoImageView.setFitWidth(Images.imageW);
                camoImageView.setFitHeight(Images.imageH);
                flagImageView.setFitWidth(Images.imageW);
                flagImageView.setFitHeight(Images.imageH);
                flagImageView.setVisible(false);
                gridPane.add(camoImageView, j, i);
                gridPane.add(flagImageView, j, i);
                camoImageView.setOnMouseClicked(event -> {
                    if (!flagImageView.isVisible()) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            currentCell.getCamoView().setVisible(false);
                            if (currentCell.hasMine()) {
                                for (Cell cell : cellsWithMinesList) {
                                    cell.getCamoView().setVisible(false);
                                    cell.setOpened(true);
                                }
                                lose();
                            }
                        }
                        if (event.getButton() == MouseButton.SECONDARY) {
                            if (flagImageView.isVisible()) {
                                currentCell.setFlag(false);
                                currentCell.getFlagView().setVisible(false);
                                flagImageView.setVisible(false);
                                markedCellsList.remove(currentCell);
                            }
                            else {
                                currentCell.setFlag(true);
                                currentCell.getFlagView().setVisible(true);
                                flagImageView.setVisible(true);
                                markedCellsList.add(currentCell);
                            }
                        }
                    }
                });
                flagImageView.setOnMouseClicked(event -> {
                    if(event.getButton() == MouseButton.SECONDARY) {
                        currentCell.setFlag(false);
                        currentCell.getFlagView().setVisible(false);
                        flagImageView.setVisible(false);
                        markedCellsList.remove(currentCell);
                    }
                });
            }
        }
    }

    public void lose() {
        System.out.println("Вы проиграли");
        gridPane.setDisable(true);
        Main.solveButton.setDisable(true);
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                Cell currentCell = arrCells[i][j];
                currentCell.getCamoView().setDisable(true);
            }
        }
        Main.mainThread.interrupt();
    }

    public void victory() {
        System.out.println("Вы выиграли");
        gridPane.setDisable(true);
    }

    public List<Cell> getCellNeighbours(Cell cell) {
        List<Cell> neighboursList = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                neighboursList.add(arrCells[cell.getI() + i][cell.getJ() + j]);
            }
        }
        return neighboursList;
    }
}


