package manager;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import model.persons.Person;

import java.util.Collections;

/**
 * Created by tgdflto1 on 26/10/16.
 */
public class SimulationManager {
	private static SimulationManager instance;
	private static SpawnManager spawnManager = SpawnManager.getInstance();
	private static Thread simulation;


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
	public static void start(Label time) {
		Task task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
				int i = 0;
				while (!isSimulationFinished()) {
					Collections.shuffle(spawnManager.getPersons());
					spawnManager.handlePersonsInTargetRange();
					Platform.runLater(() -> {
						for (Person p : spawnManager.getPersons())
							p.calculateStep();
					});
					this.updateMessage(++i + " seconds");
					Thread.sleep(20);
				}
				System.out.println("finished simulation");
				return null;
			}
		};
		time.textProperty().bind(task.messageProperty());

		simulation = new Thread(task);
		simulation.start();
	}


	private static boolean isSimulationFinished() {
		for (Person p : spawnManager.getPersons()) {
			if (!p.isInGoalArea())
				return false;
		}
		return true;
	}


	public Thread getSimulationThread() {
		return simulation;
	}
}
