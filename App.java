package chainreaction;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.effect.Reflection;
import javafx.stage.Stage;
import javafx.util.Duration;


public class App extends Application
{
    private int m = 9;
    private int n = 6;
    private final double cellSize = 900.0 / m;
    private int numberOfPlayers = 2;
    private Game currentGame;
    //private boolean gameInProgress = true;
    private int winner = 0;
    private Scene scene;
    private Pane root;
    private CellTile[][] cellMatrix = new CellTile[m][n];
    private Group[][] groupMatrix = new Group[m][n];

    // To be removed
    //String[] colours = {"#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF", "#880088", "#000000"};
    String[] colours = {"0xff0000","0x00ff00","0x0000ff","0xff00ff","0xffff00","0x00ffff","0x00ff00","0x808080"};


    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("Chain Reaction");
        homePage(primaryStage);
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
        startbtn.setOnAction(e -> gameBegin(primaryStage) );
        Button resumebtn = new Button ("Resume");
        //resumebtn.setOnAction(e -> gameBegin(primaryStage) );
        Button settingsbtn = new Button ("Settings");
        settingsbtn.setOnAction(e -> settings(primaryStage) );
        vboxButtons.getChildren().addAll(startbtn, resumebtn, settingsbtn);

        root.setTop(vboxName);
        // root.setBottom();
        // root.setLeft();
        // root.setRight();
        root.setCenter(vboxButtons);

        Scene scene = new Scene(root, 600.0, 970.0);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void gameBegin(Stage primaryStage)
    {
        root = new Pane();
        root.setPrefSize(600.0, 970.0);

        HBox hboxFileMenu = new HBox();

        MenuBar mbar = new MenuBar();
        mbar.prefWidthProperty().bind(root.widthProperty());

        Menu fileMenu = new Menu("Options");
        MenuItem menuRestart = new MenuItem("Restart");
        menuRestart.setOnAction((ActionEvent event) -> { currentGame.restart(groupMatrix, root, cellSize); });
        MenuItem menuExit = new MenuItem("Exit");
        //menuExit.setOnAction((ActionEvent event) -> { homePage(primaryStage); });
        fileMenu.getItems().addAll(menuRestart ,menuExit);

        Menu editMenu = new Menu("Edit");
        MenuItem menuUndo = new MenuItem("Undo");
        menuUndo.setOnAction((ActionEvent event) -> { currentGame.undo(groupMatrix, root); });
        editMenu.getItems().add(menuUndo);

        mbar.getMenus().addAll(fileMenu, editMenu);
        hboxFileMenu.getChildren().add(mbar);

        root.getChildren().add(hboxFileMenu);

        for(int i = 0; i < m; i++)
            for(int j = 0; j < n; j++)
            {
                cellMatrix[i][j] = new CellTile(i, j);
                cellMatrix[i][j].setTranslateX(j * cellSize);
                cellMatrix[i][j].setTranslateY(60 + i * cellSize);
                root.getChildren().add(cellMatrix[i][j]);

                Cluster c = new Cluster("#000000", 0);
                Group g = c.createCluster(cellSize);

                groupMatrix[i][j] = g;
                groupMatrix[i][j].setTranslateX(j * cellSize);
                groupMatrix[i][j].setTranslateY(60 + i * cellSize);
                groupMatrix[i][j].setMouseTransparent(true);
                RotateTransition rotate = new RotateTransition(Duration.millis(1000), groupMatrix[i][j]);
                rotate.setByAngle(360);
                rotate.setCycleCount(Timeline.INDEFINITE);
                rotate.setInterpolator(Interpolator.LINEAR);
                rotate.play();
                root.getChildren().add(groupMatrix[i][j]);

                /*
                if(i == 5 && j == 5)
                {
                    groupMatrix[i][j].getChildren().clear();
                    Cluster c1 = new Cluster("#FF0000", 3);
                    Group g1 = c1.createCluster(cellSize);
                    groupMatrix[i][j] = g1;
                    groupMatrix[i][j].setTranslateX(j * cellSize);
                    groupMatrix[i][j].setTranslateY(i * cellSize);
                    rotate = new RotateTransition(Duration.millis(1000), groupMatrix[i][j]);
                    rotate.setByAngle(360);
                    rotate.setCycleCount(Timeline.INDEFINITE);
                    rotate.setInterpolator(Interpolator.LINEAR);
                    rotate.play();
                    root.getChildren().add(groupMatrix[i][j]);
                }
                */

                /*
                Cluster c1 = new Cluster("#FF0000", 3);
                Group g1 = c.createCluster(cellSize);
                groupMatrix[i][j] = g1;
                root.getChildren().add(groupMatrix[i][j]);
                */
            }

        Scene scene = new Scene(root);
        currentGame = new Game(m, n, numberOfPlayers, colours, cellSize);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void settings(Stage primaryStage)
    {
        primaryStage.setTitle("Chain Reaction - Settings");

        BorderPane root = new BorderPane();
        root.setPrefSize(600.0, 970.0);

        VBox vboxSettings = new VBox(10);
        //vboxSettings.setAlignment(Pos.BASELINE_CENTER);
        vboxSettings.setAlignment(Pos.CENTER);
        Label heading = new Label("Choose Player Colours");
        //heading.setPadding(new Insets(5));
        Font font = Font.font(20);
        heading.setFont(font);

        ColorPicker[] cp = new ColorPicker[8];
        Text[] txt = new Text[8];
        vboxSettings.getChildren().add(heading);
        //String[] colours = {"FF0000", "008000", "000080", "FF00FF", "FFFF00", "00FFFF", "00FF00", "808080"};
        for (int i = 0; i < 8 ;i++ )
        {
            txt[i] = new Text("Player "+Integer.toString(i+1));
            cp[i] = new ColorPicker(Color.web("#"+colours[i]));
            System.out.println(cp[i].getValue());
            cp[i].setMinHeight(30.0);
            cp[i].setOnAction((ActionEvent e) -> {
                /*txt[i].setFill(cp[i].getValue());*/ /*System.out.println(cp[i].getValue()); */
            } );
            vboxSettings.getChildren().addAll(cp[i],txt[i]);
        }

        Button backbtn = new Button ("Back");
        backbtn.setOnAction(e -> homePage(primaryStage) );
        vboxSettings.getChildren().add(backbtn);
        root.setCenter(vboxSettings);

        Scene settingspage = new Scene(root);
        primaryStage.setScene(settingspage);
        //primaryStage.show();
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
                if(event.getButton() == MouseButton.PRIMARY && currentGame.getGameInProgress())
                {
                    //int result = currentGame.move(i, j, groupMatrix, root);
                    currentGame.move(i, j, groupMatrix, root);
                    /*
                    if(result == 0)
                    {
                        // Change colour of grid.
                    }
                    */

                    // Player has won.
                    /*
                    if(result > 0)
                    {
                        // Include in GUI
                        System.out.println("Player " + result + " wins!");
                        gameInProgress = false;
                    }
                    */
                }

                //for debugging purposes
                // for(int k = 0; k < numberOfPlayers; k++)
                // {
                //     System.out.println("Player " + (k + 1));
                //     System.out.println("Cells occupied: " + currentGame.getPlayer(k).getNumberOfCellsOccupied());
                //     System.out.println("Fair chance: " + currentGame.getPlayer(k).gotFairChance());
                // }
                //System.out.println("Round No : "+ currentGame.currentRound);
                //System.out.println();
            });
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}