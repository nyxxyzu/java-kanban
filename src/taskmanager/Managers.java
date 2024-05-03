package taskmanager;

public final class Managers {

	private static TaskManager manager;

	private Managers() {

	}

	public static TaskManager getDefault() {
		if (manager == null) {
			manager = new InMemoryTaskManager();
		}
		return manager;
	}

	public static HistoryManager getDefaultHistory() {
		return new InMemoryHistoryManager();

	}

}
