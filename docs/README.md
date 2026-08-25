# Cracker User Guide

// Product screenshot goes here

Cracker is a command-line chatbot. Running `Cracker.main()` displays its banner and greeting. Enter any task text to add it, enter `list` to display the saved tasks, enter `mark TASK_NUMBER` to mark a task as done, and enter `bye` to exit. Cracker stores up to 100 tasks in memory for the current run only.

```
____________________________________________________________
 ██████╗██████╗  █████╗  ██████╗██╗  ██╗███████╗██████╗
██╔════╝██╔══██╗██╔══██╗██╔════╝██║ ██╔╝██╔════╝██╔══██╗
██║     ██████╔╝███████║██║     █████╔╝ █████╗  ██████╔╝
██║     ██╔══██╗██╔══██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗
╚██████╗██║  ██║██║  ██║╚██████╗██║  ██╗███████╗██║  ██║
 ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝
Hello! I'm Cracker.
What can I do for you?
____________________________________________________________
read book
 added: read book
____________________________________________________________
return book
 added: return book
____________________________________________________________
list
 Here are the tasks in your list:
 1.[ ] read book
 2.[ ] return book
____________________________________________________________
mark 2
 Nice! I've marked this task as done:
   [X] return book
____________________________________________________________
bye
 Bye. Hope to see you again soon!
____________________________________________________________
```

## Adding tasks

Enter a line of text that is not `list` or `bye`. Cracker confirms that the task was added.

Example: `read book`

```
 added: read book
```

## Listing tasks

Enter `list` to display every task that has been added during the current run, numbered from 1. A completed task is shown with `[X]`; an incomplete task is shown with `[ ]`.

## Marking tasks as done

Enter `mark TASK_NUMBER` to mark the numbered task as done. For example, `mark 2` marks the second task in the list.

## Exiting

Enter `bye` to end the program. Tasks are not saved to disk, so they will not be available the next time Cracker runs.
