package byow.Core;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.Stack;

import java.io.File;

public class RecordController {

    private StringBuilder record;

    private StringBuilder movementRecord;

    private long seed;

    private boolean readyToSave;

    static File file = new File("byow/Core/saveFile.txt");

    private String avatarName;

    private int round;

    private double remainingTime;
    private int stepsRemaining;

    public RecordController(String input) {
        this.record = new StringBuilder();
        movementRecord = new StringBuilder();
        extractInput(input);
        this.stepsRemaining = 200;
    }

    public static void main(String[] args) {
        RecordController rc = new RecordController("LDDD:Q");
        System.out.println(rc.seed);
        System.out.println(rc.getMovementRecord());
//        rc.save();
    }

    private void save() {
        Out out = new Out(String.valueOf(file));
        out.println(seed);
        out.println(movementRecord.toString());
        out.println(avatarName);
        out.println(round);
        out.println(remainingTime);
        out.close();
    }

    public void addLetterToRecord(char c) {
        if (!readyToSave) {
            if (c == ':') {
                readyToSave = true;
            } else if (validDirection(c)) {
                movementRecord.append(c);
                decreaseSteps();
            }
            return;
        }

        if (c == 'q') {
            save();
            System.exit(0);
        } else if (validDirection(c)) {
            movementRecord.append(c);
            decreaseSteps();
        }

        readyToSave = false;
    }

    private void decreaseSteps() {
        stepsRemaining--;
        if (stepsRemaining <= 0) {
            // trigger game over or any other appropriate action
        }
    }

    public int getRemainingSteps() {
        return stepsRemaining;
    }

    private void endGame() {
        System.out.println("Game Over! You've run out of steps.");

    }


    private long load() {
        In in = new In(file);
        if (in.hasNextLine()) {
            seed = Long.parseLong(in.readLine());
            movementRecord.append(in.readLine());
            avatarName = in.readLine();
            round = in.readInt();
            remainingTime = in.readDouble();
        } else {
            System.exit(0);
        }

        return getSeed();
    }

    public void extractInput(String input) {
        seed = 0;
        int seedStart = -1;
        int seedEnd = -1;

        for (int i = 0; i < input.length(); i++) {
            char currentChar = Character.toLowerCase(input.charAt(i));

            if (currentChar == 'l') {
                seed = load();
                seedEnd = 0;
                continue;
            }

            if (seedStart == -1 && currentChar == 'n') {
                seedStart = i + 1;
                continue;
            }

            if (seedStart != -1 && seedEnd == -1 && currentChar == 's') {
                seedEnd = i;
                seed = Long.parseLong(input.substring(seedStart, seedEnd));
                record.append(input.substring(seedStart - 1, seedEnd + 1));
                continue;
            }

            if (seedEnd != -1) {
                if (currentChar == ':' && i + 1 < input.length() && Character.toLowerCase(input.charAt(i + 1)) == 'q') {
                    save();
                    break;
                }
                if (validDirection(currentChar)) {
                    movementRecord.append(currentChar);
                }
            }
        }
    }


    private boolean validDirection(char currentChar) {
        return (currentChar == 'w' || currentChar == 's' || currentChar == 'a' || currentChar == 'd');
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }

    public String getMovementRecord() {
        return movementRecord.toString();
    }

    public void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }

    public void clearMovement() {
        movementRecord.setLength(0);
    }

    public double getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(double remainingTime) {
        this.remainingTime = remainingTime;
    }
}