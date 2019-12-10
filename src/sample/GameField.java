package sample;

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
    double imageW = 50;
    double imageH = 50;

    public String[][] arrField;
    public Integer[][] arrMines;
    public Cell[][] arrCells;

    public List<Mine> listOfMines = new LinkedList<>();

    public List<Cell> markedCellsList = new LinkedList<>();
    public List<Cell> cellsWithMinesList = new LinkedList<>();
    public List<Cell> closedCellsList = new LinkedList<>();

    GridPane gridPane;

    public void initialize() {
        createMines();
        buildField();
        calcMinesAround();
        System.out.println();
        show(arrField);
        run();
    }
    public GameField(int rows, int columns, int maxNumberOfMines) {
        this.rows = rows + 2;
        this.columns = columns + 2;
        this.maxNumberOfMines = maxNumberOfMines;
        arrField = new String[this.rows][this.rows];
        arrMines = new Integer[this.rows][this.rows];
        arrCells = new Cell[this.rows][this.rows];
    }

    private void createMines() {
        int counter = 0;
        int allMinesCounter = 0;
        Random random = new Random();
        for (int i = 1; i < rows - 1; i+=2) {
            for (int j = 1; j < columns - 1; j+=2) {
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

    private void calcMinesAround() {
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
                    arrField[i][j] = Integer.toString(numberOfMinesNearby);
                    Cell cell = new Cell(i, j, Integer.toString(numberOfMinesNearby), false);
                    arrCells[i][j] = cell;
                    closedCellsList.add(cell);
                }
                else {
                    arrField[i][j] = "*";
                    Cell cell = new Cell(i, j, "*", true);
                    arrCells[i][j] = cell;
                    cellsWithMinesList.add(cell);
                    closedCellsList.add(cell);
                }
            }
        }
    }

    private void show(Object[][] field) {
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void buildField() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                arrMines[i][j] = 0;
            }
        }
        for (Mine mine : listOfMines) arrMines[mine.i][mine.j] = 1;
    }

    public void draw(Pane playPane) {

        gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: grey");

        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < columns - 1; j++) {
                switch (arrField[i][j]) {
                    case "*":
                        ImageView mineImageView = new ImageView(Images.MINE_IMAGE);
                        mineImageView.setFitWidth(imageW);
                        mineImageView.setFitHeight(imageH);
                        gridPane.add(mineImageView, j, i);
                        break;
                    case "0":
                        ImageView greyImageView = new ImageView(Images.GREY_IMAGE);
                        greyImageView.setFitWidth(imageW);
                        greyImageView.setFitHeight(imageH);
                        gridPane.add(greyImageView, j, i);
                        break;
                    case "1":
                        ImageView oneImageView = new ImageView(Images.ONE_IMAGE);
                        oneImageView.setFitWidth(imageW);
                        oneImageView.setFitHeight(imageH);
                        gridPane.add(oneImageView, j, i);
                        break;
                    case "2":
                        ImageView twoImageView = new ImageView(Images.TWO_IMAGE);
                        twoImageView.setFitWidth(imageW);
                        twoImageView.setFitHeight(imageH);
                        gridPane.add(twoImageView, j, i);
                        break;
                    case "3":
                        ImageView threeImageView = new ImageView(Images.THREE_IMAGE);
                        threeImageView.setFitWidth(imageW);
                        threeImageView.setFitHeight(imageH);
                        gridPane.add(threeImageView, j, i);
                        break;
                    case "4":
                        ImageView fourImageView = new ImageView(Images.FOUR_IMAGE);
                        fourImageView.setFitWidth(imageW);
                        fourImageView.setFitHeight(imageH);
                        gridPane.add(fourImageView, j, i);
                        break;
                    case "5":
                        ImageView fiveImageView = new ImageView(Images.FIVE_IMAGE);
                        fiveImageView.setFitWidth(imageW);
                        fiveImageView.setFitHeight(imageH);
                        gridPane.add(fiveImageView, j, i);
                        break;
                    case "6":
                        ImageView sixImageView = new ImageView(Images.SIX_IMAGE);
                        sixImageView.setFitWidth(imageW);
                        sixImageView.setFitHeight(imageH);
                        gridPane.add(sixImageView, j, i);
                        break;
                    case "7":
                        ImageView sevenImageView = new ImageView(Images.SEVEN_IMAGE);
                        sevenImageView.setFitWidth(imageW);
                        sevenImageView.setFitHeight(imageH);
                        gridPane.add(sevenImageView, j, i);
                        break;
                    case "8":
                        ImageView eightImageView = new ImageView(Images.EIGHT_IMAGE);
                        eightImageView.setFitWidth(imageW);
                        eightImageView.setFitHeight(imageH);
                        gridPane.add(eightImageView, j, i);
                        break;
                    case "9":
                        ImageView nineImageView = new ImageView(Images.NINE_IMAGE);
                        nineImageView.setFitWidth(imageW);
                        nineImageView.setFitHeight(imageH);
                        gridPane.add(nineImageView, j, i);
                        break;
                }
            }
        }

        gridPane.setGridLinesVisible(true);
        playPane.getChildren().add(gridPane);
    }

    private void clearCellsNearby(Cell cell) {
        if (cell == null || cell.value == null) return;
        if (cell.i < 0 || cell.i > rows - 1 || cell.j < 0 || cell.j > columns - 1) return;
        if (cell.isOpened) return;
        if (!cell.hasFlag) {
            cell.camoView.setVisible(false);
            cell.isOpened = true;
        }
        if (!cell.value.equals("0") || cell.hasMine || cell.hasFlag) return;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                clearCellsNearby(arrCells[cell.i + i][cell.j + j]);
            }
        }
    }

    private void run() {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (int i = 1; i < rows - 1; i++) {
                    for (int j = 1; j < columns - 1; j++) {
                        Cell cell = arrCells[i][j];
                        if (!cell.camoView.isVisible()) {
                            closedCellsList.remove(cell);
                            clearCellsNearby(cell);
                        }
                    }
                }
                if (cellsWithMinesList.equals(closedCellsList)) {
                    for (Cell cell : cellsWithMinesList) cell.flagView.setVisible(true);
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
                ImageView camoImageView = new ImageView(Images.CAMO_IMAGE);
                ImageView flagImageView = new ImageView(Images.FLAG_IMAGE);
                camoImageView.setCursor(Cursor.HAND);
                camoImageView.setFitWidth(imageW);
                camoImageView.setFitHeight(imageH);
                flagImageView.setFitWidth(imageW);
                flagImageView.setFitHeight(imageH);
                flagImageView.setVisible(false);
                gridPane.add(camoImageView, j, i);
                gridPane.add(flagImageView, j, i);
                Cell currentCell = arrCells[i][j];
                currentCell.camoView = camoImageView;
                currentCell.flagView = flagImageView;
                camoImageView.setOnMouseClicked(event -> {
                    if (!flagImageView.isVisible()) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            currentCell.camoView.setVisible(false);
                            if (currentCell.hasMine) {
                                for (Cell cell : cellsWithMinesList) cell.camoView.setVisible(false);
                                lose();
                            }
                        }
                        if (event.getButton() == MouseButton.SECONDARY) {
                            if (flagImageView.isVisible()) {
                                currentCell.hasFlag = false;
                                currentCell.flagView.setVisible(false);
                                flagImageView.setVisible(false);
                                markedCellsList.remove(currentCell);
                            }
                            else {
                                currentCell.hasFlag = true;
                                currentCell.flagView.setVisible(true);
                                flagImageView.setVisible(true);
                                markedCellsList.add(currentCell);
                            }
                        }
                    }
                });
                flagImageView.setOnMouseClicked(event -> {
                    if(event.getButton() == MouseButton.SECONDARY) {
                        currentCell.hasFlag = false;
                        currentCell.flagView.setVisible(false);
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
    }
}


