package config;

import model.Position;

/**
 * Created by suter1 on 07.10.2016.
 */
public class ConfigModel {

	public static final double ROOM_WIDTH_ORIGIN = 1280.0;
	public static final double ROOM_HEIGHT_ORIGIN = 720.0;
	private static ConfigModel instance = null;
	// Room size ratio= 16:10
	private double pixelPerMeter;
	private double roomWidth;
	private double roomHeight;
	private double roomWidthMeter;
	private double roomHeightMeter;
	private double totalPersons;
	private double personRadius = 0.4;
	private double weightedYoungPersons;
	private double weigthedMidagePersons;
	private double weightedOldPersons;
	private double weightedHandicappedPersons;
	private String spawnAreaPosition;
	private Position spawnPosition;
	private double spawnWidth;
	private double spawnHeight;
	private String goalAreaPosition;
	private Position goalPosition;
	private double goalWidth;
	private double goalHeight;
	private boolean isWeighted;


	private ConfigModel() {
	}


	public static ConfigModel getInstance() {
		if (instance == null) {
			instance = new ConfigModel();
		}
		return instance;
	}


	public double getPersonRadius() {
		return this.personRadius * getPixelPerMeter();
	}


	public void calculateRoomSize() {

		double h = ROOM_HEIGHT_ORIGIN / getRoomHeightMeter();
		double w = ROOM_WIDTH_ORIGIN / getRoomWidthMeter();

		setPixelPerMeter(Math.min((ROOM_HEIGHT_ORIGIN / getRoomHeightMeter()), (ROOM_WIDTH_ORIGIN / getRoomWidthMeter())));

		setRoomHeight(getRoomHeightMeter() * getPixelPerMeter());
		setRoomWidth(getRoomWidthMeter() * getPixelPerMeter());
	}


	public double getRoomWidth() {
		return this.roomWidth;
	}


	public void setRoomWidth(double roomWidth) {
		this.roomWidth = roomWidth;
	}


	public double getRoomHeight() {
		return this.roomHeight;
	}


	public void setRoomHeight(double roomHeight) {
		this.roomHeight = roomHeight;
	}


	public double getTotalPersons() {
		return this.totalPersons;
	}


	public void setTotalPersons(double totalPersons) {
		this.totalPersons = totalPersons;
	}


	public double getWeightedYoungPersons() {
		return this.weightedYoungPersons;
	}


	public void setWeightedYoungPersons(double weightedYoungPersons) {
		this.weightedYoungPersons = weightedYoungPersons;
	}


	public double getWeigthedMidagePersons() {
		return this.weigthedMidagePersons;
	}


	public void setWeigthedMidagePersons(double weigthedMidagePersons) {
		this.weigthedMidagePersons = weigthedMidagePersons;
	}


	public double getWeightedOldPersons() {
		return this.weightedOldPersons;
	}


	public void setWeightedOldPersons(double weightedOldPersons) {
		this.weightedOldPersons = weightedOldPersons;
	}


	public double getWeightedHandicappedPersons() {
		return this.weightedHandicappedPersons;
	}


	public void setWeightedHandicappedPersons(double weightedHandicappedPersons) {
		this.weightedHandicappedPersons = weightedHandicappedPersons;
	}


	/**
	 * Pixel -> Meter: Pixel / getPixelPerMeter
	 * Meter -> Pixel: Meter * getPixelPerMeter
	 *
	 * @return pixelPerMeter ratio as double.
	 */
	public double getPixelPerMeter() {
		return this.pixelPerMeter;
	}


	public void setPixelPerMeter(double pixelPerMeter) {
		this.pixelPerMeter = pixelPerMeter;
	}


	public double getRoomWidthMeter() {
		return this.roomWidthMeter;
	}


	public void setRoomWidthMeter(double roomWidthMeder) {
		this.roomWidthMeter = roomWidthMeder;
	}


	public double getRoomHeightMeter() {
		return this.roomHeightMeter;
	}


	public void setRoomHeightMeter(double roomHeightMeter) {
		this.roomHeightMeter = roomHeightMeter;
	}


	public Position getSpawnPosition() {
		return this.spawnPosition;
	}


	public void setSpawnPosition(String spawnAreaPosition) {
		if (spawnAreaPosition.equals("TOP_LEFT")) {
			this.spawnPosition = new Position(0, 0);
		} else if (spawnAreaPosition.equals("TOP_CENTER")) {
			this.spawnPosition = new Position((getRoomWidth() / 2) - (getSpawnWidth() / 2), 0);
		} else {
			this.spawnPosition = new Position(getRoomWidth() - getSpawnWidth(), 0);
		}
	}


	public double getSpawnWidth() {
		return this.spawnWidth;
	}


	public void setSpawnWidth(double spawnWidth) {
		this.spawnWidth = spawnWidth * getPixelPerMeter();
	}


	public double getSpawnHeight() {
		return this.spawnHeight;
	}


	public void setSpawnHeight(double spawnHeight) {
		this.spawnHeight = spawnHeight * getPixelPerMeter();
	}


	public Position getGoalPosition() {
		return this.goalPosition;
	}


	public void setGoalPosition(String goalAreaPosition) {
		if (goalAreaPosition.equals("BOTTOM_LEFT")) {
			this.goalPosition = new Position(0, (getRoomHeight() - getGoalHeight()));
		} else if (goalAreaPosition.equals("BOTTOM_CENTER")) {
			this.goalPosition = new Position((getRoomWidth() / 2) - (getGoalWidth() / 2), (getRoomHeight() - getGoalHeight()));
		} else {
			this.goalPosition = new Position((getRoomWidth() - getGoalWidth()), (getRoomHeight() - getGoalHeight()));
		}
	}


	public double getGoalWidth() {
		return this.goalWidth;
	}


	public void setGoalWidth(double goalWidth) {
		this.goalWidth = goalWidth * getPixelPerMeter();
	}


	public double getGoalHeight() {
		return this.goalHeight;
	}


	public void setGoalHeight(double goalHeight) {
		this.goalHeight = goalHeight * getPixelPerMeter();
	}


	public boolean isWeighted() {
		return this.isWeighted;
	}


	public void setWeighted(Boolean weighted) {
		this.isWeighted = weighted;
	}

}
