package manager.areamanagers;

import model.areas.GoalArea;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tgdflto1 on 09/11/16.
 */
public class GoalAreaManager extends AreaManager {
    private static GoalAreaManager instance;
    private List<GoalArea> goalAreas = new ArrayList<>();

    private GoalAreaManager() {
    }

    public static GoalAreaManager getInstance() {
        if (instance == null) instance = new GoalAreaManager();
        return instance;
    }

    public List<GoalArea> getGoalAreas() {
        return goalAreas;
    }

    public void add(GoalArea o) {
        super.add(o);
        this.getGoalAreas().add(o);
    }

    public void clear() {
        this.goalAreas.clear();
    }


}
