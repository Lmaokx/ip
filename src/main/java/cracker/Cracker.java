package cracker;

import java.util.Scanner;

/**
 * Entry point for the Cracker chatbot application.
 */
public class Cracker {
    /** Maximum number of tasks stored during one run of the application. */
    private static final int MAX_TASKS = 100;

    /**
     * Starts the chatbot, responding to commands until the user enters {@code bye}.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        String banner = " ██████╗██████╗  █████╗  ██████╗██╗  ██╗███████╗██████╗ \n"
                + "██╔════╝██╔══██╗██╔══██╗██╔════╝██║ ██╔╝██╔════╝██╔══██╗\n"
                + "██║     ██████╔╝███████║██║     █████╔╝ █████╗  ██████╔╝\n"
                + "██║     ██╔══██╗██╔══██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗\n"
                + "╚██████╗██║  ██║██║  ██║╚██████╗██║  ██╗███████╗██║  ██║\n"
                + " ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝\n";
        System.out.println("____________________________________________________________");
        System.out.print(banner);
        System.out.println("Hello! I'm Cracker.");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");

        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                break;
            }

            if (command.equals("list")) {
                System.out.println(" Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                String taskNumberText = command.substring("mark ".length()).trim();
                try {
                    int taskNumber = Integer.parseInt(taskNumberText);
                    if (taskNumber < 1 || taskNumber > taskCount) {
                        System.out.println(" Please provide the number of a task in your list.");
                    } else {
                        int taskIndex = taskNumber - 1;
                        tasks[taskIndex].markAsDone();
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("   " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Please provide the number of a task in your list.");
                }
            } else if (command.startsWith("unmark ")) {
                String taskNumberText = command.substring("unmark ".length()).trim();
                try {
                    int taskNumber = Integer.parseInt(taskNumberText);
                    if (taskNumber < 1 || taskNumber > taskCount) {
                        System.out.println(" Please provide the number of a task in your list.");
                    } else {
                        int taskIndex = taskNumber - 1;
                        tasks[taskIndex].markAsNotDone();
                        System.out.println(" OK, I've marked this task as not done yet:");
                        System.out.println("   " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Please provide the number of a task in your list.");
                }
            } else if (command.startsWith("todo ") && taskCount < MAX_TASKS) {
                tasks[taskCount] = new Todo(command.substring("todo ".length()).trim());
                taskCount = addTask(tasks, taskCount);
            } else if (command.startsWith("deadline ") && taskCount < MAX_TASKS) {
                String deadlineDetails = command.substring("deadline ".length()).trim();
                int byIndex = deadlineDetails.indexOf(" /by ");
                if (byIndex < 0) {
                    System.out.println(" Please specify a deadline using /by.");
                } else {
                    String description = deadlineDetails.substring(0, byIndex).trim();
                    String by = deadlineDetails.substring(byIndex + " /by ".length()).trim();
                    tasks[taskCount] = new Deadline(description, by);
                    taskCount = addTask(tasks, taskCount);
                }
            } else if (command.startsWith("event ") && taskCount < MAX_TASKS) {
                String eventDetails = command.substring("event ".length()).trim();
                int fromIndex = eventDetails.indexOf(" /from ");
                int toIndex = eventDetails.indexOf(" /to ");
                if (fromIndex < 0 || toIndex < 0 || toIndex < fromIndex) {
                    System.out.println(" Please specify an event using /from and /to.");
                } else {
                    String description = eventDetails.substring(0, fromIndex).trim();
                    String from = eventDetails.substring(fromIndex + " /from ".length(), toIndex).trim();
                    String to = eventDetails.substring(toIndex + " /to ".length()).trim();
                    tasks[taskCount] = new Event(description, from, to);
                    taskCount = addTask(tasks, taskCount);
                }
            } else {
                System.out.println(" Don't understand the command ... yet.");
            }
            System.out.println("____________________________________________________________");
        }

        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
        scanner.close();
    }

    /**
     * Adds the task at the current index to the list and prints its confirmation.
     *
     * @param tasks tasks stored by the chatbot
     * @param taskCount index of the newly added task
     * @return the updated number of tasks
     */
    private static int addTask(Task[] tasks, int taskCount) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + tasks[taskCount]);
        taskCount++;
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        return taskCount;
    }
}
