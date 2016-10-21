package manager;

/**
 * Created by suter1 on 21.10.2016.
 */
public class SpawnManager {

	private static SpawnManager instance = null;

	public SpawnManager(){
	}

	private static SpawnManager getInstance(){
		if (instance == null){
			instance = new SpawnManager();
		}
		return instance;
	}



}
