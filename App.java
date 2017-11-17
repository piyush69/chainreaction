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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class App extends Application
{
    private int m = 15;
    private int n = 10;
    private final double cellSize = 900.0 / m;
    private int numberOfPlayers = 3;
    private Game currentGame;
    private boolean gameInProgress = true;
    private int winner = 0;
    private Scene scene;
    private Pane root;
    private CellTile[][] cellMatrix = new CellTile[m][n];
    private Group[][] groupMatrix = new Group[m][n];

    // To be removed
    String[] colours = {"#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF", "#FF88888", "#88FF88"};

    private Parent createContent()
    {
        root = new Pane();
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
        menuUndo.setOnAction((ActionEvent event) -> { currentGame.getCurrentPlayer().undoTurn(groupMatrix, root);
            for(int i = 0; i < numberOfPlayers; i++)
            {
                System.out.println("Player " + (i + 1));
                System.out.println("Cells occupied: " + currentGame.getPlayer(i).getNumberOfCellsOccupied());
                System.out.println("Fair chance: " + currentGame.getPlayer(i).gotFairChance());
            }
            System.out.println();
        });
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

        return root;
    }

    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("Chain Reaction");
        scene = new Scene(createContent());
        currentGame = new Game(m, n, numberOfPlayers, colours, cellSize);
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
                    int result = currentGame.move(i, j, groupMatrix, root);

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

                for(int k = 0; k < numberOfPlayers; k++)
                {
                    System.out.println("Player " + (k + 1));
                    System.out.println("Cells occupied: " + currentGame.getPlayer(k).getNumberOfCellsOccupied());
                    System.out.println("Fair chance: " + currentGame.getPlayer(k).gotFairChance());
                }
                System.out.println();
            });
        }
    }



    public static void main(String[] args)
    {
        launch(args);
    }
}