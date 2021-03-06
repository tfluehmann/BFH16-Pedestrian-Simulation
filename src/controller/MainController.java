package controller;

import EventListener.SimulationFinishedListener;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import manager.PathManager;
import manager.PerimeterManager;
import manager.SimulationManager;
import manager.SpawnManager;
import manager.areamanagers.GoalAreaManager;
import manager.areamanagers.ObstacleManager;
import manager.areamanagers.SpawnAreaManager;
import model.*;
import model.areas.Area;
import model.areas.GoalArea;
import model.areas.Obstacle;
import model.areas.SpawnArea;
import model.persons.Person;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by suter1 on 28.10.2016.
 * Initializes the full window and manages all the buttons
 */
public class MainController implements Initializable, SimulationFinishedListener {

	private ObstacleManager obstacleManager = ObstacleManager.getInstance();
	private SpawnAreaManager spawnAreaManager = SpawnAreaManager.getInstance();
	private GoalAreaManager goalAreaManager = GoalAreaManager.getInstance();

	@FXML
	private Room simulationRoom;

	@FXML
	private TextField totalPersons;

	@FXML
	private CheckBox isWeighted;

	@FXML
	private Slider sliderYoung;

	@FXML
	private Slider sliderMidage;

	@FXML
	private Slider sliderOld;

	@FXML
	private Slider sliderHandicap;

	@FXML
	private TextField weightYoung;

	@FXML
	private TextField weightMidage;

	@FXML
	private TextField weightOld;

	@FXML
	private TextField weightHandicap;

	@FXML
	private Button spawnButton;

	@FXML
	private Button startButton;

	@FXML
	private Button resetButton;

	@FXML
	private AnchorPane basePane;

	@FXML
	private Label statTime;

	@FXML
	private Slider simulationSpeed;

	@FXML
	private Label statTotalPersons;

	@FXML
	private Label statYoungPersons;

	@FXML
	private Label statMidagePersons;

	@FXML
	private Label statOldPersons;

	@FXML
	private Label statHandicappedPersons;

	@FXML
	private Button showStats;

	@FXML
	private AnchorPane logoPane;

	private SimulationManager simulationManager = SimulationManager.getInstance();
	private SpawnManager spMgr = SpawnManager.getInstance();
	private ConfigModel cfg = ConfigModel.getInstance();

	private List<Slider> sliders = new ArrayList<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
        obstacleManager.setRoom(simulationRoom);
		goalAreaManager.setRoom(simulationRoom);
		spawnAreaManager.setRoom(simulationRoom);
		ConfigModel configModel = ConfigModel.getInstance();

		basePane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - 40);
		basePane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 20);
		logoPane.setLayoutY(configModel.getRoomHeight() - 70);
        setButtons(false, true, true, true);

		weightYoung.textProperty().bindBidirectional(sliderYoung.valueProperty(), NumberFormat.getNumberInstance());
		weightMidage.textProperty().bindBidirectional(sliderMidage.valueProperty(), NumberFormat.getNumberInstance());
		weightOld.textProperty().bindBidirectional(sliderOld.valueProperty(), NumberFormat.getNumberInstance());
		weightHandicap.textProperty().bindBidirectional(sliderHandicap.valueProperty(), NumberFormat.getNumberInstance());

		Slider[] slid = {sliderYoung, sliderMidage, sliderOld, sliderHandicap};
		sliders.addAll(Arrays.asList(slid));

        for (Slider slider : sliders)
            slider.valueProperty().addListener((observable, oldValue, newValue) -> slider.setValue(newValue.intValue()));
        new CreateAreaContextMenu(goalAreaManager, spawnAreaManager, obstacleManager, configModel, simulationRoom);

		/*
	     * Numberlistener from user "javasuns"
		 * on: http://stackoverflow.com/a/37360657
		 */
		ChangeListener<String> forceNumberListener = (observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*"))
				((StringProperty) observable).set(oldValue);
		};

//		Make sure, that only numbers (int) are entered in Textfields.
		weightYoung.textProperty().addListener(forceNumberListener);
		weightMidage.textProperty().addListener(forceNumberListener);
		weightOld.textProperty().addListener(forceNumberListener);
		weightHandicap.textProperty().addListener(forceNumberListener);
		simulationManager.getSpeedProperty().bind(simulationSpeed.valueProperty());
		simulationManager.addSimulationFinishedListener(this);

		spawnButton.setOnAction((event) -> spawnPressed());

		startButton.setOnAction((event) -> {
			if (startButton.getText().equals("Start")) {
				startPressed();
			} else {
				stopPressed();
			}
		});

		resetButton.setOnAction((event -> resetPressed()));
		showStats.setOnAction(event -> showStatsPressed());
	}

	private void spawnPressed() {
        validateStart();
        if (spawnAreaManager.getSpawnAreas().isEmpty() || goalAreaManager.getGoalAreas().isEmpty()) {
			new MissingAreasAlert(this.basePane);
			return;
		}
        setDraggable(spawnAreaManager.getSpawnAreas(), false);
        PathManager pathManager = spMgr.getPathManager();
		for (GoalArea ga : GoalAreaManager.getInstance().getGoalAreas()) {
			pathManager.addTarget(new TargetVertex(ga.getMiddle()));
			ga.setDraggable(false);
			for (Anchor a : ga.getAnchors()) {
				a.setDraggable(false);
			}
		}
		for (Obstacle obstacle : obstacleManager.getObstacles()) {
			obstacle.setDraggable(false);
			for (Anchor a : obstacle.getAnchors()) {
				a.setDraggable(false);
			}
			obstacle.calculateCornersAndVertices();
			for (Position p : obstacle.getEdgePoints())
				pathManager.getVertexList().add(new Vertex(p));
			pathManager.getObstacleEdges().addAll(obstacle.getEdges());
		}

		pathManager.findValidEdges(simulationRoom);
		pathManager.calculatePaths();

		//Save data form the config window for usage in simulation.
		cfg.setTotalPersons(getTotalPersons());
		cfg.setWeighted(getIsWeighted());
		cfg.setWeightedYoungPersons(getWeightYoung());
		cfg.setWeightedMidAgePersons(getWeightMidage());
		cfg.setWeightedOldPersons(getWeightOld());
		cfg.setWeightedHandicappedPersons(getWeightHandicap());

		spMgr.createPersons();
		for (Person p : spMgr.getPersons()) {
			p.setDraggable(true);
		}
		this.simulationRoom.getChildren().addAll(spMgr.getPersons());

		Statistic stats = Statistic.getInstance();
		stats.calculatePersons();
		statTotalPersons.textProperty().set("" + stats.getTotalPersons());
		statYoungPersons.textProperty().set("" + stats.getNumberYoungPersons());
		statMidagePersons.textProperty().set("" + stats.getNumberMidagePersons());
		statOldPersons.textProperty().set("" + stats.getNumberOldPersons());
		statHandicappedPersons.textProperty().set("" + stats.getNumberHandicappedPersons());

		statTime.textProperty().setValue("0 s");

        setButtons(true, false, false, false);
    }

	private void stopPressed() {
		startButton.setText("Start");
        simulationManager.getSimulationTask().cancel();
        for (Person p : spMgr.getPersons()) {
			p.setDraggable(true);
		}
		enableStatsButton();
		toggleGoalAndSpawnAreas(true);
		startButton.setDisable(false);
	}

	private void startPressed() {
		startButton.setText("Pause");
		showStats.setDisable(true);
		for (Person p : spMgr.getPersons()) {
			p.setDraggable(false);
		}
		simulationManager.start(statTime, Integer.parseInt(statTime.getText().replace(" s", "")));
		toggleGoalAndSpawnAreas(false);
		startButton.setDisable(false);
	}

	private void toggleGoalAndSpawnAreas(boolean visible) {
		for (GoalArea goal : GoalAreaManager.getInstance().getGoalAreas())
			goal.setVisible(visible);
		for (SpawnArea goal : SpawnAreaManager.getInstance().getSpawnAreas())
			goal.setVisible(visible);
		for (Node node : simulationRoom.getChildren())
			if (node instanceof Anchor) node.setVisible(visible);
	}

	private void resetPressed() {
		simulationRoom.getChildren().removeIf(item -> (item instanceof Person || item instanceof Line));
		SpawnManager.getInstance().clear();
		PerimeterManager.getInstance().clear();
        setButtons(false, false, true, true);
        statTime.textProperty().unbind();
		statTime.textProperty().setValue("0 s");
        setDraggable(spawnAreaManager.getSpawnAreas(), true);
        setDraggable(goalAreaManager.getGoalAreas(), true);
        setDraggable(obstacleManager.getObstacles(), true);
    }

    private void setDraggable(List<? extends Area> areaList, boolean draggable) {
        for (Area area : areaList) {
            area.setDraggable(draggable);
            for (Anchor a : area.getAnchors())
                a.setDraggable(draggable);
        }
    }

	private void showStatsPressed() {
		try {
			Statistic.getInstance();
			FXMLLoader ldr = new FXMLLoader();
			ldr.setLocation(getClass().getResource("/view/Statistics.fxml"));
			AnchorPane page = ldr.load();
			Stage dialogStage = new Stage();
			dialogStage.initOwner(this.basePane.getScene().getWindow());
			dialogStage.setTitle("Statistics");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			dialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    private void setButtons(boolean spawnButtonDisabled, boolean resetButtonDisabled, boolean startButtonDisabled, boolean statisticButtonDisabled) {
        spawnButton.setDisable(spawnButtonDisabled);
        resetButton.setDisable(resetButtonDisabled);
        startButton.setDisable(startButtonDisabled);
        showStats.setDisable(statisticButtonDisabled);
    }

	private int sliderSum() {
		int sum = 0;
		for (Slider slider : sliders) {
			sum += Math.floor(slider.getValue() + 0.5f);
		}
		return sum;
	}

    private void validateStart() {
        TextField[] textFields = {weightOld, weightYoung, weightMidage, weightHandicap};
        if ((sliderSum() != 100) && isWeighted.isSelected()) {
            for (TextField t : textFields)
                t.getStyleClass().add("error");
            new SliderAlert(this.basePane);
        }
        for (TextField t : textFields)
            t.getStyleClass().remove("error");
    }

    private boolean spawnAllowed() {
        return sliderSum() == 100 && getIsWeighted();
    }

	private Boolean getIsWeighted() {
		return this.isWeighted.isSelected();
	}

	private double textFieldToDouble(TextField text) {
		return new Double(text.getText());
	}

	private int textFieldToInt(TextField text) {
		return new Integer(text.getText());
	}

	private double getTotalPersons() {
		return textFieldToDouble(this.totalPersons);
	}

	private double getWeightYoung() {
		return textFieldToInt(this.weightYoung);
	}

	private int getWeightMidage() {
		return textFieldToInt(this.weightMidage);
	}

	private int getWeightOld() {
		return textFieldToInt(this.weightOld);
	}

	private int getWeightHandicap() {
		return textFieldToInt(this.weightHandicap);
	}

	private void enableStatsButton() {
		this.showStats.setDisable(false);
	}

	@Override
    public void simulationFinished(boolean isCancelled) {
        stopPressed();
        startButton.setDisable(!isCancelled);
    }
}
