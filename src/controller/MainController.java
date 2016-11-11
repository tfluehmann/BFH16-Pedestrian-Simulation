package controller;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import manager.PathManager;
import manager.SimulationManager;
import manager.SpawnManager;
import manager.areamanagers.GoalAreaManager;
import manager.areamanagers.ObstacleManager;
import manager.areamanagers.SpawnAreaManager;
import model.ConfigModel;
import model.Position;
import model.Room;
import model.Vertex;
import model.areas.GoalArea;
import model.areas.Obstacle;
import model.areas.SpawnArea;
import model.persons.Person;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

/**
 * Created by suter1 on 28.10.2016.
 */
public class MainController implements Initializable {

    private ObstacleManager obstacleManager = ObstacleManager.getInstance();
    private SpawnAreaManager spawnAreaManger = SpawnAreaManager.getInstance();
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
	private Label time;

	@FXML
	private Slider simulationSpeed;

	private SimulationManager simulationManager = SimulationManager.getInstance();
	private SpawnManager spMgr = SpawnManager.getInstance();
	private ConfigModel cfg = ConfigModel.getInstance();


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		obstacleManager.setRoom(simulationRoom);
		goalAreaManager.setRoom(simulationRoom);
		spawnAreaManger.setRoom(simulationRoom);

        basePane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - 40);
        basePane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 20);
		startButton.setDisable(true);
		resetButton.setDisable(true);

		weightYoung.textProperty().bindBidirectional(sliderYoung.valueProperty(), NumberFormat.getNumberInstance());
		weightMidage.textProperty().bindBidirectional(sliderMidage.valueProperty(), NumberFormat.getNumberInstance());
		weightOld.textProperty().bindBidirectional(sliderOld.valueProperty(), NumberFormat.getNumberInstance());
		weightHandicap.textProperty().bindBidirectional(sliderHandicap.valueProperty(), NumberFormat.getNumberInstance());

//		Accept only int for the sliders. And check that the sum of all Sliders is 100%.
		sliderYoung.valueProperty().addListener((observable, oldValue, newValue) -> {
			sliderYoung.setValue(Math.round(newValue.doubleValue()));
			calculateSlider();
		});

		sliderMidage.valueProperty().addListener((observable, oldValue, newValue) -> {
			sliderMidage.setValue(Math.round(newValue.doubleValue()));
			calculateSlider();
		});

		sliderOld.valueProperty().addListener((observable, oldValue, newValue) -> {
			sliderOld.setValue(Math.round(newValue.doubleValue()));
			calculateSlider();
		});

		sliderHandicap.valueProperty().addListener((observable, oldValue, newValue) -> {
			sliderHandicap.setValue(Math.round(newValue.doubleValue()));
			calculateSlider();
        });

        ContextMenu cm = new ContextMenu();
        MenuItem goalItem = new MenuItem("Goal area");
        MenuItem spawnItem = new MenuItem("Spawn area");
        Menu obstacleMenu = new Menu("Obstacles");

		goalItem.setOnAction((e) -> goalAreaManager.add(GoalArea.createWithNEdges(4, GoalArea.class)));
		spawnItem.setOnAction((e) -> spawnAreaManger.add(SpawnArea.createWithNEdges(4, SpawnArea.class)));

        String[] polygonNames = {"Triangle", "Rectangle", "Pentagon", "Hexagon", "Heptagon", "Octagon"};
        for (int corners = 3, i = 0; corners <= 8; corners++, i++) {
            MenuItem item = new MenuItem(polygonNames[i]);
            final int cornerCount = corners;
			item.setOnAction((e) -> obstacleManager.add(Obstacle.createWithNEdges(cornerCount, Obstacle.class)));
			obstacleMenu.getItems().add(item);
        }

        cm.getItems().addAll(goalItem, spawnItem, obstacleMenu);

        simulationRoom.setOnMouseClicked((event) -> {
            if (event.getButton().toString().equals("SECONDARY"))
                cm.show(simulationRoom, event.getScreenX(), event.getSceneY());
        });

		/**
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
		simulationManager.speedProperty.bind(simulationSpeed.valueProperty());

		spawnButton.setOnAction((event) -> {
			PathManager pathManager = spMgr.getPathManager();
			Vertex goal = null;
			for (GoalArea ga : GoalAreaManager.getInstance().getGoalAreas()) {
				goal = new Vertex(ga.getPosition());
				pathManager.addTarget(goal);
			}
			System.out.println(obstacleManager.getObstacles().size());
			for (Obstacle obstacle : obstacleManager.getObstacles()) {
				//FIXME TO BE CONTINUED -> calculate stuff on spawn, or on redraw
				for (Position p : obstacle.getEdgePoints())
					pathManager.getVertexList().add(new Vertex(p));
				pathManager.getObstacleEdges().addAll(obstacle.getEdges());
			}
			pathManager.findValidEdges(simulationRoom);
			pathManager.crapFindAlgorithm(goal);


			/**
			 * Save data form the config window for usage in simulation.
			 */
			cfg.setTotalPersons(getTotalPersons());
			cfg.setWeighted(getIsWeighted());
			cfg.setWeightedYoungPersons(getWeightYoung());
			cfg.setWeightedMidAgePersons(getWeightMidage());
			cfg.setWeightedOldPersons(getWeightOld());
			cfg.setWeightedHandicappedPersons(getWeightHandicap());

			spMgr.createPersons();
			this.simulationRoom.getChildren().addAll(spMgr.getPersons());

			spawnButton.setDisable(true);
			startButton.setDisable(false);
			resetButton.setDisable(false);

			/**
			 * Merge meter from the config to pixel for the view.
			 */
//			cfg.calculateRoomSize();
		});

		startButton.setOnAction((event) -> {
			if (startButton.getText().equals("Start")) {
				startButton.setText("Pause");
				System.out.println("Persons: " + spMgr.getPersons());
				System.out.println("simulationRoom: " + simulationRoom);
				simulationManager.start(time);
			} else {
				startButton.setText("Start");
				simulationManager.getSimulationThread().interrupt();
			}
			startButton.setDisable(false);
		});

		resetButton.setOnAction((event -> {
//	        implement a reset.
			simulationRoom.getChildren().removeIf(item -> (item instanceof Person));
			spawnButton.setDisable(false);
			startButton.setText("Start");
			startButton.setDisable(true);
		}));
	}


	private int sliderSum() {
		return (int) Math.round(sliderYoung.getValue() + sliderMidage.getValue() + sliderOld.getValue() + sliderHandicap.getValue());
	}


	private void calculateSlider() {
		int sum = sliderSum();
		if (sum != 100) {
			int diff = sum - 100;
			sliderYoung.setValue(Math.round(sliderYoung.getValue() - diff / 4));
			sliderMidage.setValue(Math.round(sliderMidage.getValue() - diff / 4));
			sliderOld.setValue(Math.round(sliderOld.getValue() - diff / 4));
			sliderHandicap.setValue(Math.round(sliderHandicap.getValue() - diff / 4));
		}
	}


	public Boolean getIsWeighted() {
		return this.isWeighted.isSelected();
	}


	private TextField doubleToTextfield(Double value) {
		return new TextField("" + value);
	}


	private double textFieldToDouble(TextField text) {
		return new Double(text.getText());
	}


	private int textFieldToInt(TextField text) {
		return new Integer(text.getText());
	}


	private TextField intToTextfield(int value) {
		return new TextField("" + value);
	}


	public double getTotalPersons() {
		return textFieldToDouble(this.totalPersons);
	}


	public void setTotalPersons(double totalPersons) {
		this.totalPersons = doubleToTextfield(totalPersons);
	}


	public double getSliderYoungValue() {
		return this.sliderYoung.getValue();
	}


	public void setSliderYoungValue(double sliderYoung) {
		this.sliderYoung.setValue(sliderYoung);
	}


	public double getSliderMidageValue() {
		return this.sliderMidage.getValue();
	}


	public void setSliderMidageValue(double sliderMidage) {
		this.sliderMidage.setValue(sliderMidage);
	}


	public double getSliderOldValue() {
		return this.sliderOld.getValue();
	}


	public void setSliderOldValue(double sliderOld) {
		this.sliderOld.setValue(sliderOld);
	}


	public double getSliderHandicapValue() {
		return this.sliderHandicap.getValue();
	}


	public void setSliderHandicapValue(double sliderHandicap) {
		this.sliderHandicap.setValue(sliderHandicap);
	}


	public double getWeightYoung() {
		return textFieldToInt(this.weightYoung);
	}


	public void setWeightYoung(int weightYoung) {
		this.weightYoung = intToTextfield(weightYoung);
	}


	public int getWeightMidage() {
		return textFieldToInt(this.weightMidage);
	}


	public void setWeightMidage(int weightMidage) {
		this.weightMidage = intToTextfield(weightMidage);
	}


	public int getWeightOld() {
		return textFieldToInt(this.weightOld);
	}


	public void setWeightOld(int weightOld) {
		this.weightOld = intToTextfield(weightOld);
	}


	public int getWeightHandicap() {
		return textFieldToInt(this.weightHandicap);
	}


	public void setWeightHandicap(int weightHandicap) {
		this.weightHandicap = intToTextfield(weightHandicap);
	}


	public Button getSpawnButton() {
		return this.spawnButton;
	}

}
