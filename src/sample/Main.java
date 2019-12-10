package sample;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    Scene playScene;
    static Stage mainWindow = new Stage();
    static Thread mainThread;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        int rows = 10;
        int columns = 10;
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
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void initialiseField(int rows, int columns, int maxNumberOfMines) {
        mainWindow.setMinWidth(600);
        mainWindow.setMinHeight(600);
        mainWindow.setMaxWidth(600);
        mainWindow.setMaxHeight(600);

        GameField gameField = new GameField(rows, columns, maxNumberOfMines);
        gameField.initialize();

        Pane gridPane = new GridPane();
        gameField.draw(gridPane);
        gameField.startGameProcess();

        playScene = new Scene(gridPane);
        mainWindow.setScene(playScene);
        mainWindow.show();

        Solver solver = new Solver(gameField);
        solver.solve();
    }


}
