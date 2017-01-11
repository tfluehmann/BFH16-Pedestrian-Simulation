package events;

import controller.MainController;
import manager.SimulationManager;

/**
 * Created by tgdflto1 on 11/01/17.
 */
public class FinishedEvent implements SimulationEvent {
    private SimulationManager simulationManager = SimulationManager.getInstance();
    private MainController mc;

    public FinishedEvent(MainController mc) {
        this.mc = mc;
        this.simulationManager.addEventListener(this);
    }

    @Override
    public void finished() {
        mc.stopPressed();

    }
}
