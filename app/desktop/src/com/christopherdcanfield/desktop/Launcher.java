package com.christopherdcanfield.desktop;

import java.util.ArrayList;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.christopherdcanfield.GameApp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Launcher extends Application
{
	private Group root;
	private Stage launcherWindow;
	private LwjglApplication gameApp;

	@Override
	public void start(Stage stage) {
		root = new Group();
		launcherWindow = stage;

		DisplayMode[] allDisplayModes = LwjglApplicationConfiguration.getDisplayModes();

//		Set<DisplayModeWrapper> displayModes = new HashSet<>();
		ArrayList<PrettifiedDisplayMode> displayModes = new ArrayList<>();
		for (int i = 0; i < allDisplayModes.length; i++) {
			DisplayMode mode = allDisplayModes[i];
			displayModes.add(new PrettifiedDisplayMode(mode));
		    System.out.println(mode.width + "x" + mode.height + "..." + mode.refreshRate + "..." + mode.bitsPerPixel);
		}
//		ArrayList<DisplayModeWrapper> sortedDisplayModes = new ArrayList<>(displayModes);
//		Collections.sort(sortedDisplayModes);

		ObservableList<PrettifiedDisplayMode> options = FXCollections.observableArrayList(displayModes);
		ComboBox<PrettifiedDisplayMode> displayModesComboBox = new ComboBox<>(options);
		DisplayMode desktopDisplayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();
		displayModesComboBox.getSelectionModel().select(new PrettifiedDisplayMode(desktopDisplayMode));
		root.getChildren().add(displayModesComboBox);
		
		Button startGameButton = new Button("Launch Game");
		root.getChildren().add(startGameButton);
		startGameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage.hide();
				
				try {
					PrettifiedDisplayMode mode = displayModesComboBox.valueProperty().get();
	
					LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
					config.setFromDisplayMode(mode.underlying);
					config.forceExit = false;
					config.foregroundFPS = 30;
					config.fullscreen = false;
					gameApp = new LwjglApplication(new GameApp(), config);
				} catch (Exception e) {
					stage.show();
				}
			}
		});

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
