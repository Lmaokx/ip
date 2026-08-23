# Cracker User Guide

// Product screenshot goes here

Cracker is a command-line chatbot. Running `Cracker.main()` displays its banner and greeting. Enter any task text to add it, enter `list` to display the saved tasks, and enter `bye` to exit. Cracker stores up to 100 tasks in memory for the current run only.

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
 1. read book
 2. return book
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

Enter `list` to display every task that has been added during the current run, numbered from 1.

## Exiting

Enter `bye` to end the program. Tasks are not saved to disk, so they will not be available the next time Cracker runs.
