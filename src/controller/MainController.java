package controller;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import manager.PathManager;
import manager.SimulationManager;
import manager.SpawnManager;
import manager.areamanagers.GoalAreaManager;
import manager.areamanagers.ObstacleManager;
import manager.areamanagers.SpawnAreaManager;
import model.*;
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

	private SimulationManager simulationManager = SimulationManager.getInstance();
	private SpawnManager spMgr = SpawnManager.getInstance();
	private ConfigModel cfg = ConfigModel.getInstance();

	private List<Slider> sliders = new ArrayList<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		obstacleManager.setRoom(simulationRoom);
		goalAreaManager.setRoom(simulationRoom);
		spawnAreaManger.setRoom(simulationRoom);

        basePane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - 40);
        basePane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() - 20);
		startButton.setDisable(true);
		resetButton.setDisable(true);
		showStats.setDisable(true);

		weightYoung.textProperty().bindBidirectional(sliderYoung.valueProperty(), NumberFormat.getNumberInstance());
		weightMidage.textProperty().bindBidirectional(sliderMidage.valueProperty(), NumberFormat.getNumberInstance());
		weightOld.textProperty().bindBidirectional(sliderOld.valueProperty(), NumberFormat.getNumberInstance());
		weightHandicap.textProperty().bindBidirectional(sliderHandicap.valueProperty(), NumberFormat.getNumberInstance());

		Slider[] slid = {sliderYoung, sliderMidage, sliderOld, sliderHandicap};
		sliders.addAll(Arrays.asList(slid));
		for (Slider slider : sliders) {
			slider.valueProperty().addListener((observable, oldValue, newValue) -> {
				slider.setValue(Math.round(newValue.doubleValue()));
				calculateSlider();
			});
		}

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
            if (spawnAreaManger.getSpawnAreas().isEmpty() || goalAreaManager.getGoalAreas().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Check your stuff");
                alert.setHeaderText("No curse words please.");
                alert.setContentText("You have not created at least a spawn and a goal area.\nPress right click and create them.");
                alert.initOwner(this.basePane.getScene().getWindow());
                alert.showAndWait();
                return;
            }

			PathManager pathManager = spMgr.getPathManager();
			Vertex goal = null;
			for (GoalArea ga : GoalAreaManager.getInstance().getGoalAreas()) {
				goal = new Vertex(ga.getPosition());
				pathManager.addTarget(goal);
			}
//			System.out.println(obstacleManager.getObstacles().size());
			for (Obstacle obstacle : obstacleManager.getObstacles()) {
                obstacle.calculateCornersAndVertices();
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

			Statistic stats = Statistic.getInstance();
			stats.calculatePersons();
			statTotalPersons.textProperty().set(""+stats.getTotalPersons());
			statYoungPersons.textProperty().set(""+stats.getNumberYoungPersons());
			statMidagePersons.textProperty().set(""+stats.getNumberMidagePersons());
			statOldPersons.textProperty().set(""+stats.getNumberOldPersons());
			statHandicappedPersons.textProperty().set(""+stats.getNumberHandicappedPersons());

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
				showStats.setDisable(true);
				simulationManager.start(statTime);
			} else {
				startButton.setText("Start");
				simulationManager.getSimulationThread().interrupt();
				enableStatsButton();
			}
			startButton.setDisable(false);
		});

		resetButton.setOnAction((event -> {
//	        implement a reset.
			simulationRoom.getChildren().removeIf(item -> (item instanceof Person || item instanceof Line));
			SpawnManager spawnManager = SpawnManager.getInstance();
			spawnManager.clear();
			spawnButton.setDisable(false);
			startButton.setText("Start");
			startButton.setDisable(true);
			showStats.setDisable(true);
		}));

		showStats.setOnAction(event -> {
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

			} catch (IOException e){
				e.printStackTrace();
			}
		});
	}

	private int sliderSum() {
		int sum = 0;
		for (Slider slider : sliders) {
			sum += Math.floor(slider.getValue() + 0.5f);
		}
		return sum;
	}

	private void calculateSlider() {
		int sum = sliderSum();
		int operator;
		operator = (sum > 100) ? -1 : 1;
		while (sum != 100)
			for (Slider slider : sliders) {
				slider.setValue(slider.getValue() + operator);
				sum += operator;
				if (sum == 100) break;
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

	public void enableStatsButton() {
		this.showStats.setDisable(false);
	}

}
