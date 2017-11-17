package chainreaction;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Orb
{
    private String colour;

    public Orb(String colour)
    {
        this.colour = colour;
    }

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