package game;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import solver.Solver;

public class Main extends Application {

    static Scene playScene;
    static Stage mainWindow = new Stage();
    public static Thread mainThread;
    static Button beginButton = new Button("Begin");
    static Button solveButton = new Button("Solve");
    static Button restartButton = new Button("Restart");
    GameField gameField;
    static GridPane gridPane;
    static GridPane visibleGridPane;
    RadioButton autoSolveRadioButton = new RadioButton("Auto Solver");
    RadioButton stepByStepSolveRadioButton = new RadioButton("Step-By-Step Solver");
    ToggleGroup radioButtonsGroup = new ToggleGroup();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        int rows = 4;
        int columns = 4;
//        int maxNumberOfMines = (rows + columns) / 2;
        int maxNumberOfMines = 3;

        Runnable mainTask = new Runnable() {
            @Override
            public void run() {
                Images.load();
                initialiseField(rows, columns, maxNumberOfMines);
            }
        };
        mainThread = new Thread(mainTask);
        mainThread.run();

        restartButton.setOnAction(event -> {
            mainThread.interrupt();
            System.out.println();
            mainThread = new Thread(mainTask);
            mainThread.run();
            beginButton.setDisable(false);
            autoSolveRadioButton.setDisable(false);
            stepByStepSolveRadioButton.setDisable(false);
            if (autoSolveRadioButton.isSelected()) autoSolveRadioButton.setSelected(true);
            else if (stepByStepSolveRadioButton.isSelected()) stepByStepSolveRadioButton.setSelected(true);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void initialiseField(int rows, int columns, int maxNumberOfMines) {

        mainWindow.setMinWidth(columns * 2 * Images.imageW + 150);
        mainWindow.setMinHeight(rows * Images.imageH + 100);
        mainWindow.setMaxWidth(columns * 2 * Images.imageW + 150);
        mainWindow.setMaxHeight(rows * Images.imageH + 100);

        autoSolveRadioButton.setToggleGroup(radioButtonsGroup);
        stepByStepSolveRadioButton.setToggleGroup(radioButtonsGroup);

        gameField = new GameField(rows, columns, maxNumberOfMines);
        gameField.initialize();

        gridPane = new GridPane();
        visibleGridPane = new GridPane();
        VBox vBox = new VBox(beginButton, restartButton, autoSolveRadioButton, stepByStepSolveRadioButton, solveButton);
        HBox mainPane = new HBox(gridPane, vBox, visibleGridPane);

        gameField.draw(gridPane);
        gameField.startGameProcess();
        gameField.draw(visibleGridPane);

        playScene = new Scene(mainPane);
        mainWindow.setScene(playScene);
        mainWindow.show();

        solveButton.setDisable(true);
        beginButton.setOnAction(event1 -> {
            Solver solver = new Solver(gameField);
            if (autoSolveRadioButton.isSelected() || stepByStepSolveRadioButton.isSelected()) {

                beginButton.setDisable(true);
                solveButton.setDisable(false);
                if (autoSolveRadioButton.isSelected()) stepByStepSolveRadioButton.setDisable(true);
                else if (stepByStepSolveRadioButton.isSelected()) autoSolveRadioButton.setDisable(true);

                solveButton.setOnAction(event2 -> {
                    if (autoSolveRadioButton.isSelected()) solver.autoSolve();
                    else if (stepByStepSolveRadioButton.isSelected()) solver.stepByStepSolve();
                });
            }
        });
    }
}
