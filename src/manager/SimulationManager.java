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
 */
public class SimulationManager {
    private static SimulationManager instance;
    private static SpawnManager spawnManager = SpawnManager.getInstance();
    private static Thread simulation;
    public static LongProperty speedProperty = new SimpleLongProperty();

    private static ArrayList<SimulationFinishedListener> finishedListeners = new ArrayList();

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
    public static void start(Label time, int oldTime) {
        Task task = new SimulationFactory(time, oldTime, speedProperty);

        task.setOnSucceeded((event) -> {
            for (SimulationFinishedListener sfl : finishedListeners)
                sfl.simulationFinished();
        });
        task.setOnFailed((event) -> {
            for (SimulationFinishedListener sfl : finishedListeners)
                sfl.simulationFinished();
        });

        time.textProperty().bind(task.messageProperty());
        simulation = new Thread(task);
        simulation.start();
    }

    public Thread getSimulationThread() {
        return simulation;
    }

    public static void addSimulationFinishedListener(SimulationFinishedListener sfl) {
        finishedListeners.add(sfl);
    }
}
