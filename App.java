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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.*;

public class App extends Application
{
    private int m = 15;
    private int n = 10;
    private double cellSize = 900.0 / m;
    private int numberOfPlayers = 4;
    private Game currentGame;
    private int winner = 0;
    private Scene scene;
    private Pane root;
    private CellTile[][] cellMatrix = new CellTile[m][n];
    private Group[][] groupMatrix = new Group[m][n];

    String[] colours = {"0xff0000","0x00ff00","0x0000ff","0xff00ff","0xffff00","0x00ffff","0x00ff00","0x808080"};

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("Chain Reaction");
        primaryStage.setResizable(false);
        homePage(primaryStage);
    }

    @Override
    public void stop()
    {
        currentGame.exit();
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
        vboxName.setPrefHeight(230);

        VBox vboxNumPlayers = new VBox(10);
        vboxNumPlayers.setAlignment(Pos.CENTER);
        ComboBox<String> myComboBox = new ComboBox<String>();
        myComboBox.getItems().addAll("2 Players", "3 Players", "4 Players", "5 Players", "6 Players", "7 Players", "8 Players");
        myComboBox.setValue("2 Players");
        numberOfPlayers = (Character.getNumericValue(myComboBox.getValue().charAt(0)));

        VBox vboxButtons = new VBox(20);
        vboxButtons.setAlignment(Pos.CENTER);
        Button startbtn = new Button ("Start");
        startbtn.setOnAction(e -> 
            {
                numberOfPlayers = (Character.getNumericValue(myComboBox.getValue().charAt(0)));
                gameBegin(primaryStage, false);
            });
        vboxButtons.getChildren().add(startbtn);
        
        if(new File("gameData.ser").isFile())
        {
            Button resumebtn = new Button ("Resume");
            resumebtn.setOnAction(e -> gameBegin(primaryStage, true) );
            vboxButtons.getChildren().add(resumebtn);
        }

        Button settingsbtn = new Button ("Settings");
        settingsbtn.setOnAction(e -> settings(primaryStage) );
        vboxButtons.getChildren().add(settingsbtn);
        
        VBox vboxGridSize = new VBox(10);
        vboxGridSize.setAlignment(Pos.CENTER);

        Label labelGridSz = new Label("Choose Grid Size");
        ToggleGroup toggleGSize = new ToggleGroup();
        toggleGSize.selectedToggleProperty().addListener(new setGridSize());
        RadioButton radioSmallG = new RadioButton("9 x 6");
        RadioButton radioBigG = new RadioButton("15 x 10");
        radioSmallG.setSelected(true);
        radioSmallG.setToggleGroup(toggleGSize);
        radioBigG.setToggleGroup(toggleGSize);
        vboxGridSize.getChildren().addAll(labelGridSz, radioSmallG, radioBigG);

        
        vboxNumPlayers.getChildren().add(myComboBox);

        vboxButtons.getChildren().addAll(vboxGridSize, vboxNumPlayers);

        vboxButtons.setPrefHeight(230);

        root.setTop(vboxName);
        root.setCenter(vboxButtons);

        Scene scene = new Scene(root, 600.0, 970.0);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void gameBegin(Stage primaryStage, boolean isThereSavedGame)
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
        menuExit.setOnAction((ActionEvent event) -> { currentGame.exit(); homePage(primaryStage); });
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
            }

        Scene scene = new Scene(root);
        if(isThereSavedGame == false)
            currentGame = new Game(m, n, numberOfPlayers, colours, cellSize);
        else
        {
            try
            {
                String filenameGame = "gameData.ser";
                FileInputStream fileGame = new FileInputStream (filenameGame);

                ObjectInputStream inGame = new ObjectInputStream (fileGame);
                
                currentGame = (Game)inGame.readObject();
                
                inGame.close();
                
                fileGame.close();
                currentGame.getBoard().display(groupMatrix,root);
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
            catch (ClassNotFoundException ex)
            {
                System.out.println("ClassNotFoundException" +" is caught");
            }
        }
        primaryStage.setScene(scene);
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
        for (int i = 0; i < 8 ;i++ )
        {
            final int ij = i;
            txt[i] = new Text("Player "+Integer.toString(i+1));
            cp[i] = new ColorPicker(Color.web(colours[i]));
            String hex1 = "0x" + Integer.toHexString(cp[i].getValue().hashCode());
            cp[i].setMinHeight(30.0);
            cp[i].setOnAction((ActionEvent e) -> {
                String clr = "0x" + Integer.toHexString(cp[ij].getValue().hashCode());
                boolean flag = true;
                for(int ptr = 0; ptr < 8; ptr++)
                {
                    if(clr.equals(colours[ptr]))
                    {
                        flag = false;
                        break;
                    }
                }
                if(flag)
                {
                    colours[ij] = clr;
                }
                else
                {
                    cp[ij] = new ColorPicker(Color.web(colours[ij]));
                    cp[ij].setMinHeight(30.0);
                }
            } );
            vboxSettings.getChildren().addAll(cp[i],txt[i]);
        }

        Button backbtn = new Button ("Back");
        backbtn.setOnAction(e -> homePage(primaryStage) );
        vboxSettings.getChildren().add(backbtn);
        root.setCenter(vboxSettings);

        Scene settingspage = new Scene(root);
        primaryStage.setScene(settingspage);
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

    private class setGridSize implements ChangeListener<Toggle>
    {
        @Override
        public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue)
        {    
            RadioButton rb = (RadioButton) newValue;
            String txt = rb.getText();
            if(txt.equals("9 x 6"))
            {
                m = 9;
                n = 6;
                cellSize = 900.0 / m;
            }
            else
            {
                m = 15;
                n = 10;
                cellSize = 900.0 / m;
            }
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}