package chainreaction;

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
        Game game = new Game(9, 6, 2, colours);
        game.play();
    }
}
