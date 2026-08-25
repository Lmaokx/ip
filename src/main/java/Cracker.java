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

        String[] tasks = new String[MAX_TASKS];
        boolean[] isDone = new boolean[MAX_TASKS];
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
                    String status = isDone[i] ? "[X]" : "[ ]";
                    System.out.println(" " + (i + 1) + "." + status + " " + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                String taskNumberText = command.substring("mark ".length()).trim();
                try {
                    int taskNumber = Integer.parseInt(taskNumberText);
                    if (taskNumber < 1 || taskNumber > taskCount) {
                        System.out.println(" Please provide the number of a task in your list.");
                    } else {
                        int taskIndex = taskNumber - 1;
                        isDone[taskIndex] = true;
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("   [X] " + tasks[taskIndex]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" Please provide the number of a task in your list.");
                }
            } else if (taskCount < MAX_TASKS) {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println(" added: " + command);
            }
            System.out.println("____________________________________________________________");
        }

        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
        scanner.close();
    }
}
