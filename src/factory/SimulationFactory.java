package factory;

import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import manager.SpawnManager;
import model.persons.Person;

import java.util.Collections;

/**
 * Created by tgdflto1 on 11/01/17.
 */
public class SimulationFactory extends Task {
    private static SpawnManager spawnManager = SpawnManager.getInstance();
    private final LongProperty speedProperty;
    private int oldTime;
    private Label time;

    public SimulationFactory(Label time, int oldTime, LongProperty speedProperty) {
        this.time = time;
        this.oldTime = oldTime;
        this.speedProperty = speedProperty;
    }

    @Override
    protected Object call() throws Exception {
        String t = time.getText();
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
