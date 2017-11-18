package chainreaction;



import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;


/**
 * The Orb class is used to create spheres of the desired colour and radius.
 */
public class Orb
{
    private String colour;

    /**
     * @param colour The colour of the sphere
     */
    public Orb(String colour)
    {
        this.colour = colour;
    }

    /**
     * @param cellSize Side-length of the cell, based on which the radius of the sphere is to be set
     * @return Sphere of the desired colour and radius equal to one-sixth of cellSize
     */
    public Sphere makeOrb(double cellSize)
    {
        PhongMaterial colourMaterial = new PhongMaterial();
        colourMaterial.setSpecularColor(Color.web(colour));
        colourMaterial.setDiffuseColor(Color.web(colour));

        Sphere orb = new Sphere();
        orb.setMaterial(colourMaterial);
        orb.setRadius(cellSize / 6);

        return orb;
    }
}