package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/** Represents a gitlet commit object.
 *  It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Troy Choo
 */
public class Commit implements Serializable {
    /**
     *
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private Date timestamp;
    private String parent;
    private String secondParent;
    private String SHA1;
    private HashMap<String, Blob> direct;
    private HashMap<String, String> toCommit;
    private HashMap<String, String> toRemove;

    public Commit(String message, Date timestamp, String parent, String secondParent, HashMap<String, Blob> direct) {
        this.message = message;
        this.timestamp = timestamp;
        this.parent = parent;
        this.secondParent = secondParent;
        this.SHA1 = Utils.sha1(Utils.serialize(this));
        this.direct = direct;
    }

    public void save() {
        File commitFile = Utils.join(Repository.COMMITS, this.SHA1);
        Utils.writeObject(commitFile, this);
    }

    public static Commit fromFile(String id) {
        File commitFile = Utils.join(Repository.COMMITS, id);
        return Utils.readObject(commitFile, Commit.class);
    }

    public String getMessage() {
        return this.message;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public String getParent() {
        return this.parent;
    }

    public String getSecondParent() {
        return this.secondParent;
    }

    public String getId() {
        return this.SHA1;
    }

    public HashMap<String, Blob> getDirect() {
        return this.direct;
    }
}
