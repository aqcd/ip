import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Duke {
    private static List<Task> tasks;

    private enum Commands {
        bye, list, delete, done, todo, deadline, event, invalid
    }

    private enum TaskTypes {
        todo, deadline, event
    }

    private enum TaskSymbols {
        T, E, D
    }

    public static void main(String[] args) {
        tasks = new ArrayList<>();
        greet();
        retrieveTaskList();
        Scanner sc = new Scanner(System.in);
        converse(sc);
    }

    private static void greet() {
        System.out.println(">> Beep Boop. I am Aq-bot.\n>> How can I help?");
    }

    private static void retrieveTaskList() {
        try {
            File directory = new File("data");
            File file = new File("data/duke.txt");
            if (!directory.exists()) {
                boolean success = directory.mkdir();
                if (!success) {
                    System.out.println(">> Something went wrong creating the data directory!");
                }
            }
            if (file.exists()) {
                Scanner fr = new Scanner(file);
                while (fr.hasNextLine()) {
                    String ln = fr.nextLine();
                    String[] taskInfo = ln.split(Task.ESCAPED_SAVE_DELIMITER);
                    TaskSymbols type = TaskSymbols.valueOf(taskInfo[0]);
                    switch(type) {
                        case T:
                            addTask(new Todo(taskInfo[1], Boolean.parseBoolean(taskInfo[2])));
                            break;
                        case E:
                            addTask(new Event(taskInfo[1], Boolean.parseBoolean(taskInfo[2]), taskInfo[3]));
                            break;
                        case D:
                            addTask(new Deadline(taskInfo[1], Boolean.parseBoolean(taskInfo[2]), taskInfo[3]));
                            break;
                    }
                }
            } else {
                boolean success = file.createNewFile();
                if (!success) {
                    System.out.println(">> Something went wrong creating the data file!");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(">> Oh no! I can't find your file :(");
        } catch (IOException e) {
            System.out.println(">> Oh no! The file couldn't be created for some reason!");
        }
    }

    private static void saveTasksToFile() {
        try {
            FileWriter fw = new FileWriter("data/duke.txt");
            for (Task t : tasks) {
                fw.write(t.format() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println(">> Oh no! Your tasks could not be saved!");
        }
    }

    private static void addTask(Task task) {
        tasks.add(task);
        saveTasksToFile();
        System.out.println(">> Added the task:\n>> " + task + "\n>> You now have " + tasks.size() + " tasks to do!");
    }

    private static void deleteTask(int idx) {
        Task task = tasks.get(idx);
        tasks.remove(idx);
        saveTasksToFile();
        System.out.println(">> I've eradicated the task:\n>> " + task + "\n>> You now have " + tasks.size() + " tasks to do!");
    }

    private static void completeTask(int idx) {
        tasks.get(idx).complete();
        saveTasksToFile();
        System.out.println(">> Yay! The following task is marked as done:\n>> " + tasks.get(idx));
    }

    private static void descriptionError(TaskTypes type) {
        System.out.println(">> Oh no!!! A " + type + " must have a description!");
    }

    private static void conditionError(TaskTypes type) {
        System.out.println(">> Oh no!!! A " + type + " must have an associated date!");
    }

    private static void deleteError() {
        System.out.println(">> Oh no!!! That task does not exist!");
    }

    private static void converse(Scanner sc) {
        boolean running = true;
        while(running) {
            String input = sc.nextLine();
            String[] chunks = input.split(" ", 2);
            String action = chunks[0];
            Commands command = Commands.invalid;
            try {
                command = Commands.valueOf(action);
            } catch (IllegalArgumentException e) {
                System.out.println(">> Oh no!!! I don't understand this input :(" );
            }
            switch(command) {
                case bye:
                    System.out.println(">> Bye! Hope I helped!");
                    running = false;
                    break;
                case list:
                    int i = 1;
                    for (Task task : tasks) {
                        System.out.println(">> " + i++ + ". " + task);
                    }
                    break;
                case delete:
                    int deleteIndex = Integer.parseInt(chunks[1]) - 1;
                    if (deleteIndex >= tasks.size()) {
                        deleteError();
                        break;
                    }
                    deleteTask(deleteIndex);
                    break;
                case done:
                    int index = Integer.parseInt(chunks[1]) - 1;
                    completeTask(index);
                    break;
                case todo:
                    if (chunks.length < 2) {
                        descriptionError(TaskTypes.todo);
                        break;
                    }
                    Task todo = new Todo(chunks[1]);
                    addTask(todo);
                    break;
                case deadline:
                    if (chunks.length < 2) {
                        descriptionError(TaskTypes.deadline);
                        break;
                    }
                    String[] deadlineInfo = chunks[1].split(" /by ", 2); // [name, deadline]
                    if (deadlineInfo.length < 2) {
                        conditionError(TaskTypes.deadline);
                        break;
                    }
                    Task deadline = new Deadline(deadlineInfo[0], deadlineInfo[1]);
                    addTask(deadline);
                    break;
                case event:
                    if (chunks.length < 2) {
                        descriptionError(TaskTypes.event);
                        break;
                    }
                    String[] eventInfo = chunks[1].split(" /at ", 2); // [name, date]
                    if (eventInfo.length < 2) {
                        conditionError(TaskTypes.event);
                        break;
                    }
                    Task event = new Event(eventInfo[0], eventInfo[1]);
                    addTask(event);
                    break;
                default:
                    break;
            }
        }
    }
}
