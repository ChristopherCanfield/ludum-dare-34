package com.christopherdcanfield.desktop;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Launcher extends Application
{
	private Group root;

	@Override
	public void start(Stage stage) {
		root = new Group();

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice display = ge.getDefaultScreenDevice();
		DisplayMode[] allDisplayModes = display.getDisplayModes();

		Set<DisplayModeWrapper> displayModes = new HashSet<>();
		for (int i = 0; i < allDisplayModes.length; i++) {
			DisplayMode mode = allDisplayModes[i];
			displayModes.add(new DisplayModeWrapper(mode, i));
		    System.out.println(mode.getWidth() + "x" + mode.getHeight() + "..." + mode.getRefreshRate() + "..." + mode.getBitDepth());
		}
		ArrayList<DisplayModeWrapper> sortedDisplayModes = new ArrayList<>(displayModes);
		Collections.sort(sortedDisplayModes);

		ObservableList<DisplayModeWrapper> options = FXCollections.observableArrayList(sortedDisplayModes);
		ComboBox<DisplayModeWrapper> comboBox = new ComboBox<>(options);
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
