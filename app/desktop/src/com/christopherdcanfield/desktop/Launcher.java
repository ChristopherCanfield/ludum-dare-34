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
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Launcher extends Application
{
	private GridPane root;
	private Stage launcherWindow;
	private LwjglApplication gameApp;

	@Override
	public void start(Stage stage) {
//		root = new Group();
		launcherWindow = stage;
		
		root = new GridPane();
//		root.setGridLinesVisible(true);
		root.setPadding(new Insets(15, 15, 0, 15));
		root.setHgap(20);
		root.setVgap(15);
		
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(100);
		column1.setHalignment(HPos.LEFT);
		root.getColumnConstraints().add(column1);
				
		RowConstraints row1 = new RowConstraints();
		row1.setValignment(VPos.TOP);
		root.getRowConstraints().add(row1);
		
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(57.5);
		root.getRowConstraints().add(row2);
		
		// Title
		Label titleLabel = new Label("xxxxx Game xxxxx");
		titleLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
		GridPane.setHalignment(titleLabel, HPos.CENTER);
		root.add(titleLabel, 0, 0);
		
		// Game description
		Label descriptionText = new Label("blah blah bladf adfds fadsf df sdf");
		descriptionText.setWrapText(true);
		root.add(descriptionText, 0, 1);
		
		// Settings section
		Label settingsLabel = new Label("Settings");
		settingsLabel.setFont(Font.font(null, FontWeight.NORMAL, 18));
		GridPane.setHalignment(settingsLabel, HPos.CENTER);
		root.add(settingsLabel, 0, 2);
		
		GridPane settingsSection = new GridPane();
//		settingsSection.setGridLinesVisible(true);
		settingsSection.setPadding(new Insets(0, 0, 0, 0));
		settingsSection.setHgap(20);
		settingsSection.setVgap(5);
		root.add(settingsSection, 0, 3);
		
		// Display modes
		Label displayModesLabel = new Label("Display Modes");
		settingsSection.add(displayModesLabel, 0, 2);

		ObservableList<PrettifiedDisplayMode> options = getPrettifiedDisplayModes();
		ComboBox<PrettifiedDisplayMode> displayModesComboBox = new ComboBox<>(options);
		DisplayMode desktopDisplayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();
		displayModesComboBox.getSelectionModel().select(new PrettifiedDisplayMode(desktopDisplayMode));
		settingsSection.add(displayModesComboBox, 1, 2);
		
		// Fullscreen checkbox
		Label fullscreenLabel = new Label("Fullscreen");
		settingsSection.add(fullscreenLabel, 0, 4);
		
		CheckBox fullscreenCheckbox = new CheckBox();
		fullscreenCheckbox.setSelected(true);
		settingsSection.add(fullscreenCheckbox, 1, 4);
		
		// Antialiasing checkbox
		Label antialiasingLabel = new Label("Antialiasing");
		settingsSection.add(antialiasingLabel, 0, 6);
		
		CheckBox antialiasingCheckbox = new CheckBox();
		antialiasingCheckbox.setSelected(true);
		settingsSection.add(antialiasingCheckbox, 1, 6);
		
		// Start game button
		Button startGameButton = new Button("Start Game");
		startGameButton.setTextFill(Color.SEAGREEN);
		startGameButton.setFont(Font.font(null, FontWeight.BOLD, 14));
//		startGameButton.setBackground(new Background(new BackgroundFill(Color.MEDIUMSEAGREEN, new CornerRadii(5), new Insets(0, 0, 0, 0))));
		GridPane.setHalignment(startGameButton, HPos.CENTER);
		root.add(startGameButton, 0, 7);
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
					config.backgroundFPS = 30;
					config.title = "Canfield LD34";
					config.fullscreen = fullscreenCheckbox.isSelected();
					config.samples = antialiasingCheckbox.isSelected() ? 8 : 0;
					gameApp = new LwjglApplication(new GameApp(), config);
				} catch (Exception e) {
					stage.show();
				}
			}
		});

		Scene scene = new Scene(root, 500, 600, true, SceneAntialiasing.BALANCED);
		scene.setFill(Color.WHITE);

		stage.setTitle("Canfield Ludum Dare 34");
		stage.setResizable(false);
		stage.centerOnScreen();
		stage.setScene(scene);
		stage.show();
	}
	
	private static ObservableList<PrettifiedDisplayMode> getPrettifiedDisplayModes()
	{
		DisplayMode[] allDisplayModes = LwjglApplicationConfiguration.getDisplayModes();

		ArrayList<PrettifiedDisplayMode> displayModes = new ArrayList<>();
		for (int i = 0; i < allDisplayModes.length; i++) {
			DisplayMode mode = allDisplayModes[i];
			displayModes.add(new PrettifiedDisplayMode(mode));
//		    System.out.println(mode.width + "x" + mode.height + "..." + mode.refreshRate + "..." + mode.bitsPerPixel);
		}
		return FXCollections.observableArrayList(displayModes);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
