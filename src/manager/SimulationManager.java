package manager;

import EventListener.SimulationFinishedListener;
import factory.SimulationFactory;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

import java.util.ArrayList;

/**
 * Created by tgdflto1 on 26/10/16.
 * runs the simulation and notifies event listeners on finish
 */
public class SimulationManager {
    private static SimulationManager instance;
    private static Task task;
    private LongProperty speedProperty = new SimpleLongProperty();

    private static ArrayList<SimulationFinishedListener> finishedListeners = new ArrayList<>();

    private SimulationManager() {
    }

    public static SimulationManager getInstance() {
        if (instance == null) instance = new SimulationManager();
        return instance;
    }

    /**
     * shuffle before every run because there might be
     * unsolvable issues if it is always the same order
     */
    public void start(Label time, int oldTime) {
        task = new SimulationFactory(oldTime, speedProperty);
        task.setOnSucceeded((event) -> notifyFinishedListeners(false));
        task.setOnFailed((event) -> notifyFinishedListeners(false));
        task.setOnCancelled((event) -> notifyFinishedListeners(true));
        time.textProperty().bind(task.messageProperty());
        new Thread(task).start();
    }

    private void notifyFinishedListeners(boolean cancelled) {
        for (SimulationFinishedListener sfl : finishedListeners)
            sfl.simulationFinished(cancelled);
    }

    public Task getSimulationTask() {
        return task;
    }

    public void addSimulationFinishedListener(SimulationFinishedListener sfl) {
        finishedListeners.add(sfl);
    }

    public LongProperty getSpeedProperty() {
        return speedProperty;
    }
}
