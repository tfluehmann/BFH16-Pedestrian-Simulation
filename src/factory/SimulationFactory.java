package factory;

import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.concurrent.Task;
import manager.SpawnManager;
import model.persons.Person;

import java.util.Collections;

/**
 * Created by fluht1 on 11/01/17.
 * Creates a new Simulation task
 */
public class SimulationFactory extends Task {
    private static SpawnManager spawnManager = SpawnManager.getInstance();
    private final LongProperty speedProperty;
    private int oldTime;

    public SimulationFactory(int oldTime, LongProperty speedProperty) {
        this.oldTime = oldTime;
        this.speedProperty = speedProperty;
    }

    @Override
    protected Object call() throws Exception {
        int i = oldTime;
        while (!isSimulationFinished()) {
            Collections.shuffle(spawnManager.getPersons());
            spawnManager.handlePersonsInTargetRange();
            Platform.runLater(() -> {
                for (Person p : spawnManager.getPersons())
                    p.calculateStep();
            });
            this.updateMessage(++i + " s");
            Thread.sleep(speedProperty.getValue());
        }
        return null;
    }

    private static boolean isSimulationFinished() {
        for (Person p : spawnManager.getPersons()) {
            if (!p.isInGoalArea()) return false;
        }
        return true;
    }
}
