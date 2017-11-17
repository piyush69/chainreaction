package chainreaction;



import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class App extends Application
{
    private int m = 15;
    private int n = 10;
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
        root.setPrefSize(600.0, 900.0);

        for(int i = 0; i < m; i++)
            for(int j = 0; j < n; j++)
            {
                cellMatrix[i][j] = new CellTile(i, j);
                cellMatrix[i][j].setTranslateX(j * cellSize);
                cellMatrix[i][j].setTranslateY(i * cellSize);
                root.getChildren().add(cellMatrix[i][j]);
            }

        return root;
    }

    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("Chain Reaction");
        scene = new Scene(createContent());
        currentGame = new Game(m, n, numberOfPlayers, colours, scene, cellSize);
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