package gitlet;

import jdk.jshell.execution.Util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Troy Choo
 */
public class Repository implements Serializable {
    /**
     *
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    static final File BLOBS = Utils.join(GITLET_DIR, "/blobs");
    static final File COMMITS = Utils.join(GITLET_DIR, "/commits");

    private HashSet<String> allCommit = new HashSet<String>();
    private HashMap<String, String> branches = new HashMap<>();
    private HashMap<String, Blob> staging = new HashMap<>();
    private HashMap<String, Blob> removed = new HashMap<>();
    private String currBranch;
    private String head;

    public void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        } else {
            GITLET_DIR.mkdir();
            BLOBS.mkdir();
            COMMITS.mkdir();

            java.util.Date initDate = new java.util.Date(0);
            Commit initCommit = new Commit("initial commit", initDate, null, null, new HashMap<>());
            initCommit.save();

            currBranch = "main";
            head = initCommit.getId();
            allCommit.add(head);
            branches.put(currBranch, head);

            File allCommitFile = Utils.join(GITLET_DIR, "allCommit");
            Utils.writeObject(allCommitFile, allCommit);

            File branchesFile = Utils.join(GITLET_DIR, "branches");
            Utils.writeObject(branchesFile, branches);

            File stagingFile = Utils.join(GITLET_DIR, "staging");
            Utils.writeObject(stagingFile, staging);

            File removedFile = Utils.join(GITLET_DIR, "removed");
            Utils.writeObject(removedFile, removed);

            File currBranchFile = Utils.join(GITLET_DIR, "currBranch");
            Utils.writeContents(currBranchFile, currBranch);

            File headFile = Utils.join(GITLET_DIR, "head");
            Utils.writeContents(headFile, head);
        }
    }

    public void add(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        Blob blob = new Blob(file);
        String currHead = Utils.readContentsAsString(Utils.join(GITLET_DIR, "head"));
        Commit currCommit = Utils.readObject(Utils.join(COMMITS, currHead), Commit.class);
        HashMap<String, Blob> currStage = Utils.readObject(Utils.join(GITLET_DIR, "staging"), HashMap.class);

        if (currCommit.getDirect().containsKey(fileName) && blob.SHA1.equals(currCommit.getDirect().get(fileName).SHA1)) {
            currStage.remove(fileName);
        } else {
            currStage.put(fileName, blob);
            blob.save();
        }

        HashMap<String, String> currRemove = Utils.readObject(Utils.join(GITLET_DIR, "removed"), HashMap.class);
        currRemove.remove(fileName);

        Utils.writeObject(Utils.join(GITLET_DIR, "removed"), currRemove);
        Utils.writeObject(Utils.join(GITLET_DIR, "staging"), currStage);
    }

    public void commit(String message) {
        String currHead = Utils.readContentsAsString(Utils.join(GITLET_DIR, "head"));
        Commit previous = Utils.readObject(Utils.join(COMMITS, currHead), Commit.class);
        HashMap<String, Blob> commitTree = new HashMap<>(previous.getDirect());
        HashMap<String, Blob> currStage = Utils.readObject(Utils.join(GITLET_DIR, "staging"), HashMap.class);
        HashMap<String, String> currRemove = Utils.readObject(Utils.join(GITLET_DIR, "removed"), HashMap.class);

        if (currStage.isEmpty() && currRemove.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            return;
        }

        // Remove files marked for removal from the commit tree
        for (String fileName : currRemove.keySet()) {
            commitTree.remove(fileName);
        }

        // Add or update files from the staging area in the commit tree
        for (String fileName : currStage.keySet()) {
            Blob blob = currStage.get(fileName);
            commitTree.put(fileName, blob);
            blob.save();
        }

        Commit currCommit = new Commit(
                message, previous.getTimestamp(), previous.getId(), null, commitTree);
        currCommit.save();

        head = currCommit.getId();
        HashSet<String> allCommit = Utils.readObject(Utils.join(GITLET_DIR, "allCommit"), HashSet.class);
        HashMap<String, String> branches = Utils.readObject(Utils.join(GITLET_DIR, "branches"), HashMap.class);
        String currBranch = Utils.readContentsAsString(Utils.join(GITLET_DIR, "currBranch"));

        Utils.writeContents(Utils.join(GITLET_DIR, "head"), head);
        allCommit.add(currCommit.getId());
        Utils.writeObject(Utils.join(GITLET_DIR, "allCommit"), allCommit);
        currStage.clear();
        currRemove.clear();
        Utils.writeObject(Utils.join(GITLET_DIR, "staging"), currStage);
        Utils.writeObject(Utils.join(GITLET_DIR, "removed"), currRemove);
        branches.replace(currBranch, currCommit.getId());
        Utils.writeObject(Utils.join(GITLET_DIR, "branches"), branches);
    }

    public void log() {
        String currHead = readContentsAsString(join(GITLET_DIR, "head"));
        Commit currCommit = readObject(join(COMMITS, currHead), Commit.class);

        while (currCommit.getParent() != null) {
            System.out.println("===");
            System.out.println("commit " + currCommit.getId());
            System.out.println(formatTimestamp(currCommit.getTimestamp()));
            System.out.println(currCommit.getMessage());
            System.out.println("");

            if (currCommit.getSecondParent() != null) {
                String firstParentId = currCommit.getParent();
                String secondParentId = currCommit.getSecondParent();
                System.out.println("Merge: " + firstParentId.substring(0, 7) + " " + secondParentId.substring(0, 7));
            }

            currCommit = currCommit.fromFile(currCommit.getParent());
        }
        System.out.println("===");
        System.out.println("commit " + currCommit.getId());
        System.out.println(formatTimestamp(currCommit.getTimestamp()));
        System.out.println(currCommit.getMessage());
        System.out.println("");
    }

    private String formatTimestamp(Date timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        return "Date: " + dateFormat.format(timestamp);
    }

    public void restore(String... args) {
        if (args.length < 2 || !args[args.length - 2].equals("--")) {
            System.out.println("Incorrect operands.");
            return;
        }

        String fileName = args[args.length - 1];

        if (args.length >= 3) {
            // If a commit id is provided.
            String commitId = args[args.length - 3];
            restoreFromCommit(commitId, fileName);
        } else {
            // If no commit id is provided.
            restoreFromHeadCommit(fileName);
        }
    }

    private String findFullCommitId(String shortId) {
        HashSet<String> allCommit = readObject(join(GITLET_DIR, "allCommit"), HashSet.class);
        for (String commitId : allCommit) {
            if (commitId.startsWith(shortId)) {
                return commitId;
            }
        }
        return null;
    }

    private void restoreFromHeadCommit(String fileName) {
        String currHead = readContentsAsString(join(GITLET_DIR, "head"));
        Commit currCommit = readObject(join(COMMITS, currHead), Commit.class);
        HashMap<String, Blob> commitTree = currCommit.getDirect();

        if (!commitTree.containsKey(fileName)) {
            System.out.println("File does not exist in the current commit.");
            return;
        }

        Blob blob = commitTree.get(fileName);
        writeContents(new File(fileName), blob.getContent());
    }

    private void restoreFromCommit(String commitId, String fileName) {
        HashSet<String> allCommit = readObject(join(GITLET_DIR, "allCommit"), HashSet.class);

        String fullCommitId = findFullCommitId(commitId);

        if (!allCommit.contains(fullCommitId)) {
            System.out.println("No commit with that id exists.");
            return;
        }

        Commit commit = readObject(join(COMMITS, fullCommitId), Commit.class);
        HashMap<String, Blob> commitTree = commit.getDirect();

        if (!commitTree.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        Blob blob = commitTree.get(fileName);
        writeContents(new File(fileName), blob.getContent());
    }

    public void status() {
        // Load files
        String currBranch = Utils.readContentsAsString(Utils.join(GITLET_DIR, "currBranch"));
        HashMap<String, String> branches = Utils.readObject(Utils.join(GITLET_DIR, "branches"), HashMap.class);
        HashMap<String, Blob> currStage = Utils.readObject(Utils.join(GITLET_DIR, "staging"), HashMap.class);
        HashMap<String, Blob> currRemove = Utils.readObject(Utils.join(GITLET_DIR, "removed"), HashMap.class);

        //branches
        List<String> branchesStatus = new ArrayList<>();
        System.out.println("=== Branches ===");
        for (String branch : branches.keySet()) {
            branchesStatus.add(branch);
        }
        Collections.sort(branchesStatus);
        for (String branchOrder : branchesStatus) {
            if (branchOrder.equals(currBranch)) {
                System.out.println("*" + branchOrder);
            } else {
                System.out.println(branchOrder);
            }
        }

        System.out.println();

        //staged files
        List<String> stagedStatus = new ArrayList<>();
        System.out.println("=== Staged Files ===");
        for (String f : currStage.keySet()) {
            stagedStatus.add(f);
        }

        stagedStatus.sort(String::compareTo);

        for (String f : stagedStatus) {
            System.out.println(f);
        }

        System.out.println();

        //removed files
        System.out.println("=== Removed Files ===");
        for (String f : currRemove.keySet()) {
            System.out.println(f);
        }

        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");

        System.out.println();
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    public static void rm(String fileName) {
        String currHead = Utils.readContentsAsString(Utils.join(GITLET_DIR, "head"));
        Commit currCommit = Utils.readObject(Utils.join(COMMITS, currHead), Commit.class);
        HashMap<String, Blob> currStage = Utils.readObject(Utils.join(GITLET_DIR, "staging"), HashMap.class);
        HashMap<String, Blob> currRemove = Utils.readObject(Utils.join(GITLET_DIR, "removed"), HashMap.class);

        if (!currStage.containsKey(fileName) && !currCommit.getDirect().containsKey(fileName)) {
            System.out.println("No reason to remove the file.");
            return;
        }

        currStage.remove(fileName);

        if (currCommit.getDirect().containsKey(fileName)) {
            currRemove.put(fileName, currCommit.getDirect().get(fileName));
            Utils.restrictedDelete(fileName);
        }

        Utils.writeObject(Utils.join(GITLET_DIR, "staging"), currStage);
        Utils.writeObject(Utils.join(GITLET_DIR, "removed"), currRemove);
    }

    public void globalLog() {
        HashSet<String> allCommit = Utils.readObject(Utils.join(GITLET_DIR, "allCommit"), HashSet.class);
        for (String commitId : allCommit) {
            Commit currCommit = Utils.readObject(Utils.join(COMMITS, commitId), Commit.class);

            System.out.println("===");
            System.out.println("commit " + currCommit.getId());
            System.out.println(formatTimestamp(currCommit.getTimestamp()));
            System.out.println(currCommit.getMessage());
            System.out.println("");
        }
    }

    public static void find(String commitMessage) {
        HashSet<String> allCommit = Utils.readObject(Utils.join(GITLET_DIR, "allCommit"), HashSet.class);
        boolean found = false;

        for (String commitId : allCommit) {
            Commit currCommit = Utils.readObject(Utils.join(COMMITS, commitId), Commit.class);
            if (currCommit.getMessage().equals(commitMessage)) {
                System.out.println(currCommit.getId());
                found = true;
            }
        }
        if (!found) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void branch(String newPointer) {
        String currHead = Utils.readContentsAsString(Utils.join(GITLET_DIR, "head"));
        HashMap<String, String> branches = Utils.readObject(Utils.join(GITLET_DIR, "branches"), HashMap.class);

        if (branches.containsKey(newPointer)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        branches.put(newPointer, currHead);
        Utils.writeObject(Utils.join(GITLET_DIR, "branches"), branches);
    }

    public static void switchBranch(String branchName) {
        String currHead = Utils.readContentsAsString(Utils.join(GITLET_DIR, "head"));
        HashMap<String, String> branches = Utils.readObject(Utils.join(GITLET_DIR, "branches"), HashMap.class);
        HashMap<String, String> currStage = Utils.readObject(Utils.join(GITLET_DIR, "staging"), HashMap.class);

        Commit currCommit = Utils.readObject(Utils.join(COMMITS, currHead), Commit.class);
        List<String> plainFiles = Utils.plainFilenamesIn(CWD);
        HashMap<String, Blob> currDirect = currCommit.getDirect();

        if (!branches.containsKey(branchName)) {
            System.out.println("No such branch exists.");
            return;
        }
        if (currHead.equals(branchName)) {
            System.out.println("No need to switch to the current branch.");
            return;
        }
        if (!plainFiles.isEmpty()) {
            for (String fileName : plainFiles) {
                if (!currDirect.containsKey(fileName) && !currStage.containsKey(fileName)) {
                    Commit targetCommit = Utils.readObject(Utils.join(COMMITS, branches.get(branchName)), Commit.class);
                    if (targetCommit.getDirect().containsKey(fileName)) {
                        System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                        return;
                    }
                }
            }
        }

        currStage.clear();
        Utils.writeObject(Utils.join(GITLET_DIR, "staging"), currStage);

        String newBranchCommit = branches.get(branchName);

        Commit newBranchHead = Utils.readObject(Utils.join(COMMITS, newBranchCommit), Commit.class);
        HashMap<String, Blob> newBranchFiles = newBranchHead.getDirect();

        // Clear working directory (except .gitlet directory)
        File[] files = CWD.listFiles();
        for (File file : files) {
            if (!file.getName().equals(".gitlet")) {
                Utils.restrictedDelete(file);
            }
        }

        for (String fileName : newBranchFiles.keySet()) {
            Blob blob = newBranchFiles.get(fileName);
            Utils.writeContents(new File(fileName), blob.getContent());
        }

        Utils.writeContents(Utils.join(GITLET_DIR, "head"), newBranchCommit);
        Utils.writeContents(Utils.join(GITLET_DIR, "currBranch"), branchName);
    }

    public static void rmBranch(String branchName) {
        String currBranch = Utils.readContentsAsString(Utils.join(GITLET_DIR, "currBranch"));
        HashMap<String, String> branches = Utils.readObject(Utils.join(GITLET_DIR, "branches"), HashMap.class);

        if (!branches.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
        }
        if (currBranch.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
        }
        branches.remove(branchName, branches.get(branchName));
        Utils.writeObject(Utils.join(GITLET_DIR, "branches"), branches);
    }

    public static void reset(String id) {
        String currHead = Utils.readContentsAsString(Utils.join(GITLET_DIR, "head"));
        Commit currCommit = Utils.readObject(Utils.join(COMMITS, currHead), Commit.class);

        HashSet<String> allCommit = Utils.readObject(Utils.join(GITLET_DIR, "allCommit"), HashSet.class);

        if (!allCommit.contains(id)) {
            System.out.println("No commit with that id exists.");
            return;
        }

        Commit targetCommit = Utils.readObject(Utils.join(COMMITS, id), Commit.class);

        // Check for untracked files in the working directory
        List<String> files = Utils.plainFilenamesIn(CWD);
        for (String fileName : files) {
            if (!currCommit.getDirect().containsKey(fileName) && targetCommit.getDirect().containsKey(fileName)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }

        // Reset the working directory to match the target commit
        for (String fileName : targetCommit.getDirect().keySet()) {
            Blob blob = targetCommit.getDirect().get(fileName);
            byte[] content = blob.getContent();
            File file = new File(fileName);
            Utils.writeContents(file, content);
        }

        // Update the current branch's head to the target commit
        String currBranch = Utils.readContentsAsString(Utils.join(GITLET_DIR, "currBranch"));
        HashMap<String, String> branches = Utils.readObject(Utils.join(GITLET_DIR, "branches"), HashMap.class);
        branches.put(currBranch, id);
        Utils.writeObject(Utils.join(GITLET_DIR, "branches"), branches);

        // Clear the staging area
        HashMap<String, Blob> currStage = new HashMap<>();
        Utils.writeObject(Utils.join(GITLET_DIR, "staging"), currStage);

        // Update the head pointer
        Utils.writeContents(Utils.join(GITLET_DIR, "head"), id);
    }

    public static void merge(String branchName) {
        
    }
}
