package application;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Translation extends Application
{

	@Override
	public void start(Stage primaryStage)
	{
		final PhongMaterial redMaterial = new PhongMaterial();
		redMaterial.setSpecularColor(Color.RED);
		redMaterial.setDiffuseColor(Color.RED);
		
		final PhongMaterial greenMaterial = new PhongMaterial();
		greenMaterial.setSpecularColor(Color.GREEN);
		greenMaterial.setDiffuseColor(Color.GREEN);
		
		Sphere orb1 = new Sphere();
		orb1.setMaterial(redMaterial);
		orb1.setRadius(25);
		orb1.setLayoutX(200);
		orb1.setLayoutY(250);
		
		TranslateTransition translate = new TranslateTransition();
		translate.setDuration(Duration.seconds(1));
		translate.setToX(50);
		translate.setNode(orb1);
		translate.play();
		
		Sphere orb2 = new Sphere();
		orb2.setMaterial(greenMaterial);
		orb2.setRadius(25);
		orb2.setLayoutX(300);
		orb2.setLayoutY(250);
		
		Pane root = new Pane();
		root.getChildren().add(orb1);
		root.getChildren().add(orb2);
		Scene scene = new Scene(root, 600, 600);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Orb Animation");
		primaryStage.show();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
