package duke;

/**
 * Handles searching of tasks given a search phrase.
 */

public class FindCommand extends Command {
    /** duke.Command details */
    private final String[] instructions;

    /**
     * Constructor for duke.FindCommand.
     * @param instructions Contains search phrase.
     */
    public FindCommand(String[] instructions) {
        super();
        this.instructions = instructions;
    }

    /**
     * Executes the Find duke.Command, displaying a list of tasks that have the search phrase.
     * @param tasks duke.TaskList to be searched.
     * @param ui For user interaction.
     * @param storage Unused.
     */
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        StringBuilder response = new StringBuilder();
        response.append(">> Your matching tasks:");
        boolean hasResult = false;
        int i = 1;
        for (Task task : tasks.getTasks()) {
            if (task.name.contains(instructions[1])) {
                response.append("\n>> " + i++ + ". " + task);
                hasResult = true;
            }
        }
        if (!hasResult) {
            return ">> No results!";
        }
        return response.toString();
    }
}

