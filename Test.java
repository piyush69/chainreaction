package chainreaction;

public class Test
{
    public static Game[] versions;
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

        versions = new Game[1000];
        for (int i = 0; i < 1000 ; i++)
        {
            versions[i] = new Game(9, 6, 2, colours);
        }

        Game game = new Game(9, 6, 2, colours);
        game.play();
    }

    private void serialize()
    {
        // Code to be written.
    }

    private void deserialize()
    {
        // Code to be written.
    }

    public void pause()
    {
        // Code to be written.
    }

    public void undo()
    {
        // Code to be written.
    }

    public void restart()
    {
        // Code to be written.
    }

    public void exit()
    {
        // Code to be written.
    }
}