package com.christopherdcanfield.desktop;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;



import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Launcher extends Application
{
	private Group root;

	@Override
	public void start(Stage stage) {
		root = new Group();
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice display = ge.getDefaultScreenDevice();
		DisplayMode[] availableModes = display.getDisplayModes();

		for (DisplayMode mode : availableModes) {
		    System.out.println(mode.getWidth() + "x" + mode.getHeight() + "..." + mode.getRefreshRate());
		}
		
		ObservableList<String> options = FXCollections.observableArrayList(
				"Option 1",
				"Option 2",
				"Option 3"
				);
		ComboBox<String> comboBox = new ComboBox<String>(options);
		root.getChildren().add(comboBox);
		
		
		Scene scene = new Scene(root, 500, 600, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.WHITE);

		stage.setTitle("Canfield Ludum Dare 34");
		stage.centerOnScreen();
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
