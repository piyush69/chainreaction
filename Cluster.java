package chainreaction;



import javafx.scene.Group;
import javafx.scene.shape.Sphere;


/**
 * The Cluster class is used to group spheres into clusters in order to be able to apply rotation animation to them as a single unit.
 */
public class Cluster
{
    private String colour;
    private int number;

    /**
     * @param colour The colour of the spheres in the cluster
     * @param number The number of spheres in the cluster
     */
    public Cluster(String colour, int number)
    {
        this.colour = colour;
        this.number = number;
    }

    /**
     * @param cellSize Side-length of the cell, based on which the radius of each sphere is to be set
     * @return Group of the desired number of spheres with the desired colour and radius equal to one-sixth of cellSize
     */
    public Group createCluster(double cellSize)
    {
        Group cluster = new Group();
        Orb orb = new Orb(colour);

        if(number == 1)
        {
            Sphere o = orb.makeOrb(cellSize);
            o.setLayoutX(cellSize / 2);
            o.setLayoutY(cellSize / 2);
            cluster.getChildren().add(o);
        }

        if(number == 2)
        {
            Sphere o1 = orb.makeOrb(cellSize);
            Sphere o2 = orb.makeOrb(cellSize);
            o1.setLayoutX(cellSize / 2 - cellSize / 6);
            o1.setLayoutY(cellSize / 2);
            o2.setLayoutX(cellSize / 2 + cellSize / 6);
            o2.setLayoutY(cellSize / 2);
            cluster.getChildren().addAll(o1, o2);
        }

        if(number == 3)
        {
            Sphere o1 = orb.makeOrb(cellSize);
            Sphere o2 = orb.makeOrb(cellSize);
            Sphere o3 = orb.makeOrb(cellSize);
            o1.setLayoutX(cellSize / 2);
            o1.setLayoutY(cellSize / 2 - cellSize / 5);
            o2.setLayoutX(cellSize / 2 - (Math.sqrt(3) / 2) * cellSize / 5);
            o2.setLayoutY(cellSize / 2 + ((double) 1 / 2) * cellSize / 5);
            o3.setLayoutX(cellSize / 2 + (Math.sqrt(3) / 2) * cellSize / 5);
            o3.setLayoutY(cellSize / 2 + ((double) 1 / 2) * cellSize / 5);
            cluster.getChildren().addAll(o1, o2, o3);
        }

        return cluster;
    }
}
