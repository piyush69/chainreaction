package chainreaction;

import java.util.*;
import javafx.application.Application;

import javafx.animation.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;

import javafx.stage.Stage;

public class App extends Application
{
    private int m = 9;
    private int n = 6;
    private final double cellSize = 900.0 / m;
    private int numberOfPlayers = 2;
    private Game currentGame;
    private boolean gameInProgress = true;
    private int winner = 0;
    private Scene scene;
    private CellTile[][] cellMatrix = new CellTile[m][n];

    // To be removed
    String[] colours = {"Red", "Green", "Blue", "Yellow", "Orange", "Purple", "Pink", "Indigo"};

    private Parent createContent()
    {
        Pane root = new Pane();
        root.setPrefSize(600.0, 970.0);

        HBox hboxFileMenu = new HBox();

        MenuBar mbar = new MenuBar();
        mbar.prefWidthProperty().bind(root.widthProperty());

        Menu fileMenu = new Menu("Options");
        MenuItem menuRestart = new MenuItem("Restart");
        //menuRestart.setOnAction((ActionEvent event) -> { gameBegin(primaryStage); });
        MenuItem menuExit = new MenuItem("Exit");
        //menuExit.setOnAction((ActionEvent event) -> { MainPage(primaryStage); });
        fileMenu.getItems().addAll(menuRestart ,menuExit);

        Menu editMenu = new Menu("Edit");
        MenuItem menuUndo = new MenuItem("Undo");
        //menuUndo.setOnAction((ActionEvent event) -> { gameBegin(primaryStage); });
        editMenu.getItems().add(menuUndo);

        mbar.getMenus().addAll(fileMenu, editMenu);
        hboxFileMenu.getChildren().add(mbar);

        root.getChildren().add(hboxFileMenu);

        for(int i = 0; i < m; i++)
            for(int j = 0; j < n; j++)
            {
                cellMatrix[i][j] = new CellTile(i, j);
                cellMatrix[i][j].setTranslateX(5 + j * cellSize);
                cellMatrix[i][j].setTranslateY(60 + i * cellSize);
                root.getChildren().add(cellMatrix[i][j]);
            }

        return root;
    }

    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("Chain Reaction");
        currentGame = new Game(m, n, numberOfPlayers, colours);
        scene = new Scene(createContent());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        //homePage(primaryStage);
    }

    public void homePage(Stage primaryStage)
    {
        BorderPane root = new BorderPane();
        root.setPrefSize(600.0, 970.0);

        Text name = new Text("Chain Reaction");
        name.setFill(Color.STEELBLUE);
        name.setFont(Font.font("SanSerif", FontWeight.BOLD, 60));
        Reflection ref = new Reflection();
        name.setEffect(ref);

        VBox vboxName = new VBox();
        vboxName.setAlignment(Pos.CENTER);
        vboxName.getChildren().add(name);
        vboxName.setPrefHeight(330);

        VBox vboxButtons = new VBox(50);
        vboxButtons.setAlignment(Pos.CENTER);
        Button startbtn = new Button ("Start");
        //startbtn.setOnAction(e -> gameBegin(primaryStage) );
        Button resumebtn = new Button ("Resume");
        //resumebtn.setOnAction(e -> gameBegin(primaryStage) ); 
        vboxButtons.getChildren().addAll(startbtn, resumebtn);


        //Button settingsbtn = new Button ("Settings");
        // settingsbtn.setOnAction(e -> settings(primaryStage) );

        root.setTop(vboxName);
        // root.setBottom();
        // root.setLeft();
        // root.setRight();
        root.setCenter(vboxButtons);
        
        Scene scene = new Scene(root, 600.0, 970.0);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private class CellTile extends StackPane
    {
        private Text text = new Text();
        private int row;
        private int col;

        public CellTile(int i, int j)
        {
            row = i;
            col = j;

            Rectangle border = new Rectangle(cellSize, cellSize);
            border.setFill(null);
            border.setStroke(Color.BLACK);

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            // To be changed
            //text.setFont(Font.font(72));

            setOnMouseClicked(event ->
            {
                if(event.getButton() == MouseButton.PRIMARY && gameInProgress)
                {
                    int result = currentGame.move(i, j);

                    /*
                    if(result == 0)
                    {
                        // Change colour of grid.
                    }
                    */

                    // Player has won.
                    if(result > 0)
                    {
                        // Include in GUI
                        System.out.println("Player " + result + " wins!");
                        gameInProgress = false;
                    }
                }
            });
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}