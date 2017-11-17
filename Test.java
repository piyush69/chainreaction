//package chainreaction;

/*
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Test extends Application
{
    private final int cellSize = 100;
    private int m = 9;
    private int n = 6;

    private Parent createContent()
    {
        Pane root = new Pane();
        root.setPrefSize(n * cellSize, m * cellSize);

        for(int i = 0; i < m; i++)
            for(int j = 0; j < n; j++)
            {
                CellTile tile = new CellTile();
                tile.setTranslateX(j * cellSize);
                tile.setTranslateY(i * cellSize);
                root.getChildren().add(tile);
            }

        return root;
    }

    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    private class CellTile extends StackPane
    {
        private Text text = new Text(); // To be changed

        public CellTile()
        {
            Rectangle border = new Rectangle(cellSize, cellSize);
            border.setFill(null);
            border.setStroke(Color.BLACK);

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            // To be changed
            text.setFont(Font.font(72));
            setOnMouseClicked(event ->
            {
                if(event.getButton() == MouseButton.PRIMARY)
                {
                    // addOrb
                    text.setText("X");
                }
            });
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
*/

/*
public class Test
{
    public static void main(String[] args)
    {
        String[] colours = new String[8];
        colours[0] = "Red";
        colours[1] = "Green";
        colours[2] = "Blue";
        colours[3] = "Yellow";
        colours[4] = "Orange";
        colours[5] = "Purple";
        colours[6] = "Pink";
        colours[7] = "Indigo";
        Game game = new Game(9, 6, 3, colours);
        game.play();
    }
}
*/