public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager manager = new TaskManager();
        Task task1 = manager.createTask(new Task("Уборка", "Убрать квартиру"));
        Task task2 = manager.createTask(new Task("Магазин", "Купить продукты"));
        Epic epic1 = manager.createEpic(new Epic("Поездка", "Поездка на пляж"));
        Epic epic2 = manager.createEpic(new Epic("Стирка", "Постирать белье"));
        Subtask subtask1 = manager.createSubtask(2, new Subtask("Постирать белое", "Постирать белое белье"));
        Subtask subtask2 = manager.createSubtask(2, new Subtask("Постирать цветное", "Постирать цветное белье"));
        Subtask subtask3 = manager.createSubtask(1, new Subtask("Собрать вещи", "Собрать вещи в дорогу"));
        manager.getAllTasks().forEach(System.out::println);
        manager.getAllEpics().forEach(System.out::println);
        manager.getAllSubtasks().forEach(System.out::println);
        System.out.println("=========================================================================================");
        manager.updateTask(new Task("Уборка", "Убрать квартиру", Status.DONE,1));
        manager.updateTask(new Task("Магазин", "Купить продукты", Status.DONE,2));
        manager.updateSubtask(new Subtask("Постирать цветное","Постирать цветное белье",Status.IN_PROGRESS, 2));
        manager.getAllTasks().forEach(System.out::println);
        manager.getAllEpics().forEach(System.out::println);
        manager.getAllSubtasks().forEach(System.out::println);
        System.out.println("=========================================================================================");
        manager.removeTaskById(1);
        manager.removeEpicById(1);
        manager.removeSubtaskById(1);
        manager.getAllTasks().forEach(System.out::println);
        manager.getAllEpics().forEach(System.out::println);
        manager.getAllSubtasks().forEach(System.out::println);
        manager.getAllTasks().forEach(System.out::println);
        manager.getAllEpics().forEach(System.out::println);
        manager.getAllSubtasks().forEach(System.out::println);


    }
}
