package gitlet;

import java.io.Serializable;
import java.io.File;

public class Blob implements Serializable {
    File fileName;
    String SHA1;
    byte[] content;

    public Blob(File fileName) {
        content = Utils.readContents(fileName);
        SHA1 = Utils.sha1(content);
    }

    public Blob(byte[] content) {
        this.content = content;
        this.SHA1 = Utils.sha1(content);
    }

    public File getFileName() {
        return fileName;
    }

    public String getId() {
        return SHA1;
    }

    public byte[] getContent() {
        return content;
    }

    public void save() {
        File blobFile = Utils.join(Repository.BLOBS, SHA1);
        Utils.writeObject(blobFile, content);
    }

    public Blob fromFile(String id) {
        File blobFile = Utils.join(Repository.BLOBS, id);
        if (!blobFile.exists()) {
            return null;
        }
        byte[] content = Utils.readContents(blobFile);
        return new Blob(content);
    }
}
