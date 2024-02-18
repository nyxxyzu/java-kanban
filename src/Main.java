import TaskManager.TaskManager;
import Tasks.Epic;
import TaskManager.Status;
import Tasks.Subtask;
import Tasks.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager manager = new TaskManager();
        Task task1 = manager.createTask(new Task("Уборка", "Убрать квартиру"));
        Task task2 = manager.createTask(new Task("Магазин", "Купить продукты"));
        Epic epic1 = manager.createEpic(new Epic("Поездка", "Поездка на пляж"));
        Epic epic2 = manager.createEpic(new Epic("Стирка", "Постирать белье"));
        Subtask subtask1 = manager.createSubtask(new Subtask("Постирать белое", "Постирать белое белье", 4));
        Subtask subtask2 = manager.createSubtask(new Subtask("Постирать цветное", "Постирать цветное белье", 4));
        Subtask subtask3 = manager.createSubtask(new Subtask("Собрать вещи", "Собрать вещи в дорогу", 3));
        manager.getAllTasks().forEach(System.out::println);
        manager.getAllEpics().forEach(System.out::println);
        manager.getAllSubtasks().forEach(System.out::println);
        System.out.println("=========================================================================================");
        manager.updateTask(new Task("Уборка", "Убрать квартиру", Status.DONE,1));
        manager.updateTask(new Task("Магазин", "Купить продукты", Status.DONE,2));
        manager.updateSubtask(new Subtask("Постирать белое","Постирать белое белье",Status.IN_PROGRESS,5, 4));
        manager.updateEpic(new Epic("Стирочка","Постирать белье",4));
        manager.getAllTasks().forEach(System.out::println);
        manager.getAllEpics().forEach(System.out::println);
        manager.getAllSubtasks().forEach(System.out::println);
        System.out.println("=========================================================================================");
        manager.removeTaskById(1);
        manager.removeEpicById(3);
        manager.removeSubtaskById(5);
        manager.getAllTasks().forEach(System.out::println);
        manager.getAllEpics().forEach(System.out::println);
        manager.getAllSubtasks().forEach(System.out::println);


    }
}
