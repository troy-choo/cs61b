package gitlet;

import java.util.Arrays;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Troy Choo
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        Repository repository = new Repository();
        switch(firstArg) {
            case "init":
                repository.init();
                break;
            case "add":
                checkDirectory();
                repository.add(args[1]);
                break;
            case "commit":
                checkDirectory();
                repository.commit(args[1]);
                break;
            case "rm":
                checkDirectory();
                Repository.rm(args[1]);
                break;
            case "log":
                checkDirectory();
                repository.log();
                break;
            case "global-log":
                checkDirectory();
                repository.globalLog();
                break;
            case "find":
                checkDirectory();
                repository.find(args[1]);
                break;
            case "status":
                checkDirectory();
                repository.status();
                break;
            case "restore":
                checkDirectory();
                String[] argsForRestore = Arrays.copyOfRange(args, 1, args.length);
                repository.restore(argsForRestore);
                break;
            case "branch":
                checkDirectory();
                repository.branch(args[1]);
                break;
            case "switch":
                checkDirectory();
                repository.switchBranch(args[1]);
                break;
            case "rm-branch":
                checkDirectory();
                repository.rmBranch(args[1]);
                break;
            case "reset":
                checkDirectory();
                repository.reset(args[1]);
                break;
            case "merge":
                checkDirectory();
                repository.merge(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }

    public static void checkDirectory() {
        if (!Repository.GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
}
