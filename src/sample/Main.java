package sample;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    Scene playScene;
    static Stage mainWindow = new Stage();
    static Thread mainThread;
    static Button solveButton = new Button("Solve");
    static Button restartButton = new Button("Restart");
    GameField gameField;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        int rows = 5;
        int columns = 5;
        int maxNumberOfMines = (rows + columns) / 2;


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
            solveButton.setDisable(false);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void initialiseField(int rows, int columns, int maxNumberOfMines) {
//        mainWindow.setMinWidth(1000);
//        mainWindow.setMinHeight(1000);
//        mainWindow.setMaxWidth(1000);
//        mainWindow.setMaxHeight(1000);


        mainWindow.setMinWidth(columns * 2 * 50 + 200);
        mainWindow.setMinHeight(rows * 50 + 100);
        mainWindow.setMaxWidth(columns * 2 * 50 + 200);
        mainWindow.setMaxHeight(rows * 50 + 100);


        gameField = new GameField(rows, columns, maxNumberOfMines);
        gameField.initialize();
        HBox mainPane = new HBox();
        GridPane gridPane = new GridPane();
        GridPane visibleGridPane = new GridPane();
        mainPane.getChildren().addAll(gridPane, solveButton, restartButton, visibleGridPane);
        gameField.draw(gridPane);
        gameField.startGameProcess();
        gameField.draw(visibleGridPane);

        playScene = new Scene(mainPane);
        mainWindow.setScene(playScene);
        mainWindow.show();

        solveButton.setOnAction(event -> {
            Solver solver = new Solver(gameField);
            solver.solve();
//            solveButton.setDisable(true);
        });


    }

}
