package model;

import javafx.stage.Screen;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by suter1 on 07.10.2016.
 */
public class ConfigModel {

    private double roomWidthOrigin;
    private double roomHeightOrigin;
    private double perimeterSize;
    private double edgeExtender;
    private double anchorRadius;
    private int anchorColorRed;
    private int anchorColorGreen;
    private int anchorColorBlue;
    private double anchorColorOpacity;
    private int anchorStrokeWidth;
    private double handicappedPersonMaxSpeed;
    private double handicappedPersonMinSpeed;
    private double youngPersonMaxSpeed;
    private double youngPersonMinSpeed;
    private double midAgePersonMaxSpeed;
    private double midAgePersonMinSpeed;
    private double oldPersonMaxSpeed;
    private double oldPersonMinSpeed;
    private static ConfigModel instance = null;
    private double pixelPerMeter;
    private double roomWidth;
    private double roomHeight;
    private double roomWidthMeter;
    private double roomHeightMeter;
    private double totalPersons;
    private double personRadius;
    private double weightedYoungPersons;
    private double weightedMidAgePersons;
    private double weightedOldPersons;
    private double weightedHandicappedPersons;
    private boolean isWeighted;
    private double epsilon;

    private Properties props;


    private ConfigModel() {
        props = new Properties();
        InputStream is = null;
        // First try loading from the current directory
        try {
            File f = new File("simulation.properties");
            is = new FileInputStream(f);
        } catch (Exception e) {
            is = null;
        }
        try {
            if (is == null) {
                // Try loading from classpath
                is = getClass().getResourceAsStream("simulation.properties");
            }
            // Try loading properties from the file (if found)
            props.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefaults();
    }

    private void setDefaults() {
        roomWidthOrigin = Screen.getPrimary().getVisualBounds().getWidth() + Double.parseDouble(props.getProperty("Room.origin.padding.width", "-570"));
        roomHeightOrigin = Screen.getPrimary().getVisualBounds().getHeight() + Double.parseDouble(props.getProperty("Room.origin.padding.height", "-40"));
        personRadius = Double.parseDouble(props.getProperty("Person.radius"));
        perimeterSize = Double.parseDouble(props.getProperty("Perimeter.size"));
        edgeExtender = Double.parseDouble(props.getProperty("Edge.extender"));
        anchorRadius = Double.parseDouble(props.getProperty("Anchor.radius"));
        anchorColorRed = Integer.parseInt(props.getProperty("Anchor.color.red"));
        anchorColorGreen = Integer.parseInt(props.getProperty("Anchor.color.green"));
        anchorColorBlue = Integer.parseInt(props.getProperty("Anchor.color.blue"));
        anchorColorOpacity = Double.parseDouble(props.getProperty("Anchor.color.opacity"));
        anchorStrokeWidth = Integer.parseInt(props.getProperty("Anchor.stroke.width"));
        handicappedPersonMaxSpeed = Integer.parseInt(props.getProperty("Person.handicapped.speed.max"));
        handicappedPersonMinSpeed = Integer.parseInt(props.getProperty("Person.handicapped.speed.min"));
        midAgePersonMaxSpeed = Integer.parseInt(props.getProperty("Person.midAge.speed.max"));
        midAgePersonMinSpeed = Integer.parseInt(props.getProperty("Person.midAge.speed.min"));
        oldPersonMaxSpeed = Integer.parseInt(props.getProperty("Person.old.speed.max"));
        oldPersonMinSpeed = Integer.parseInt(props.getProperty("Person.old.speed.min"));
        youngPersonMaxSpeed = Integer.parseInt(props.getProperty("Person.young.speed.max"));
        youngPersonMinSpeed = Integer.parseInt(props.getProperty("Person.young.speed.min"));
        epsilon = Double.parseDouble(props.getProperty("Math.epsilon"));
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
        double h = roomHeightOrigin / getRoomHeightMeter();
        double w = roomWidthOrigin / getRoomWidthMeter();
        setPixelPerMeter(Math.min(h, w));
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

    public double getWeightedMidAgePersons() {
        return this.weightedMidAgePersons;
    }

    public void setWeightedMidAgePersons(double weightedMidAgePersons) {
        this.weightedMidAgePersons = weightedMidAgePersons;
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

    public void setRoomWidthMeter(double roomWidthMeter) {
        this.roomWidthMeter = roomWidthMeter;
    }

    public double getRoomHeightMeter() {
        return this.roomHeightMeter;
    }

    public void setRoomHeightMeter(double roomHeightMeter) {
        this.roomHeightMeter = roomHeightMeter;
    }


    public boolean isWeighted() {
        return this.isWeighted;
    }

    public void setWeighted(Boolean weighted) {
        this.isWeighted = weighted;
    }

    public double getAnchorRadius() {
        return anchorRadius;
    }

    public double getRoomWidthOrigin() {
        return roomWidthOrigin;
    }

    public double getRoomHeightOrigin() {
        return roomHeightOrigin;
    }

    public double getPerimeterSize() {
        return perimeterSize;
    }

    public double getEdgeExtender() {
        return edgeExtender;
    }

    public int getAnchorColorRed() {
        return anchorColorRed;
    }

    public int getAnchorColorGreen() {
        return anchorColorGreen;
    }

    public int getAnchorColorBlue() {
        return anchorColorBlue;
    }

    public double getAnchorColorOpacity() {
        return anchorColorOpacity;
    }

    public int getAnchorStrokeWidth() {
        return anchorStrokeWidth;
    }

    public double getHandicappedPersonMaxSpeed() {
        return handicappedPersonMaxSpeed;
    }

    public double getHandicappedPersonMinSpeed() {
        return handicappedPersonMinSpeed;
    }

    public double getYoungPersonMaxSpeed() {
        return youngPersonMaxSpeed;
    }

    public double getYoungPersonMinSpeed() {
        return youngPersonMinSpeed;
    }

    public double getMidAgePersonMaxSpeed() {
        return midAgePersonMaxSpeed;
    }

    public double getMidAgePersonMinSpeed() {
        return midAgePersonMinSpeed;
    }

    public double getOldPersonMaxSpeed() {
        return oldPersonMaxSpeed;
    }

    public double getOldPersonMinSpeed() {
        return oldPersonMinSpeed;
    }

    public double getEpsilon() {
        return epsilon;
    }
}
