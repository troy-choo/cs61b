package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Engine {
    TERenderer ter = new TERenderer();
    private double mouseX;
    private double mouseY;
    private String avatarName;
    private int GAME_STATUS;
    private boolean DIED;
    private final int IN_GAME = 1;
    private final int LOSE = 3;
    private final boolean DEAD = true;
    private WorldGenerator world;
    private Avatar avatar;
    private PutAvatarToWorld PAW;
    private MainMenu menu;
    private double remainingTime;
    private long seed;
    private int round;
    private RecordController rc;
    private boolean isDark;
    private int avatarIndex = 0;


    public Engine() {
        avatarName = "";
        GAME_STATUS = IN_GAME;
        DIED = false;
        isDark = false;
    }


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        showMenu();
        startGame();
    }

    private void showMenu() {
        menu = new MainMenu(EngineUtils.MENU_LENGTH, EngineUtils.MENU_LENGTH);
        String[] inputAndName = menu.showMenu();
        avatarName = inputAndName[1];
        rc = new RecordController(inputAndName[0]);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        rc = new RecordController(input);
        setSeed();
        TETile[][] finalWorldFrame = PAW.getTeTiles();
        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }

    private void initialWorldAndAvatar(long s, String movement, String name) {
        world = new WorldGenerator(s, EngineUtils.WIDTH, EngineUtils.HEIGHT - EngineUtils.UHD_OFFSET);
        avatar = new Avatar(s, world, name, this);
        for (char d : movement.toCharArray()) {
            avatar.walk(d);
        }
        Random random = new Random(s);
        if (rc.getRemainingTime() != 0) {
            remainingTime = rc.getRemainingTime();
        } else {
            remainingTime = EngineUtils.MINIMUM_TIME + random.nextDouble() * EngineUtils.MINIMUM_TIME;
            // Random time between 30 and 60 seconds
        }
        System.out.println(world);
    }
    public void changeAvatar() {
        avatarIndex = (avatarIndex + 1) % 3; // Will rotate between 0, 1, and 2
    }





    private void drawUHD() {
        mouseX = StdDraw.mouseX();
        mouseY = StdDraw.mouseY();
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTimeString = now.format(formatter);

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textLeft(EngineUtils.UHD_INDENT, EngineUtils.HEIGHT - 1, PAW.getMouseHangName(mouseX, mouseY));
        StdDraw.textRight(EngineUtils.WIDTH - EngineUtils.UHD_INDENT,
                EngineUtils.HEIGHT - 1, "Press o to light");
        StdDraw.text(EngineUtils.WIDTH / 2, EngineUtils.HEIGHT - 1,
                String.format("Time remaining: %.1f", remainingTime));
        StdDraw.text(EngineUtils.WIDTH / EngineUtils.THREE, EngineUtils.HEIGHT - 1,
                String.format("Round: %d", round));
        StdDraw.text((EngineUtils.WIDTH / EngineUtils.THREE) * 2, EngineUtils.HEIGHT - 1,
                String.format("Steps: %d", rc.getRemainingSteps()));

        StdDraw.text(EngineUtils.WIDTH / 2, EngineUtils.HEIGHT - 2, dateTimeString);

        StdDraw.show();
    }

    public void startGame() {
        setSeed();
        ter.renderFrame(PAW.getTeTiles());
        isDark = true;

        gameLoop();

        if (GAME_STATUS == LOSE || DIED == DEAD) {
            gameOver();
        }
    }

    private void gameLoop() {
        double startTime = System.currentTimeMillis();
        while (GAME_STATUS == IN_GAME) {
            startTime = updateTime(startTime); // Update  startTime here
            handleInput();

            if (avatar.isAtDoor()) {
                nextRound();
            }

            PAW = new PutAvatarToWorld(world, avatar, isDark);
            ter.renderFrame(PAW.getTeTiles());
            drawUHD();

            if (rc.getRemainingSteps() <= 0) {
                GAME_STATUS = LOSE;
                return;
            }
        }
    }

    private double updateTime(double startTime) {
        double currentTime = System.currentTimeMillis();
        double elapsedTime = (currentTime - startTime) / EngineUtils.MILLIS;
        remainingTime -= elapsedTime;

        if (remainingTime <= 0) {
            GAME_STATUS = LOSE;
        }

        return currentTime; // Return  current time to update the startTime
    }

    private void handleInput() {
        if (StdDraw.hasNextKeyTyped()) {
            char c = StdDraw.nextKeyTyped();
            avatar.walk(c);
            rc.addLetterToRecord(c);
            rc.setRemainingTime(remainingTime);
            toggleLight(c);
        }
    }

    private void toggleLight(char c) {
        if (c == 'o') {
            isDark = !isDark;
        }
    }

    private void nextRound() {
        seed += seed;
        round++;
        rc.setSeed(seed);
        rc.setRound(round);
        rc.clearMovement();
        rc.setRemainingTime(0);
        startGame();
    }

    private void setSeed() {
        seed = rc.getSeed();
        ter.initialize(EngineUtils.WIDTH, EngineUtils.HEIGHT, 0, 2);

        if (avatarName == null || avatarName.equals("")) {
            rc.setAvatarName(avatarName);
        }

        round = rc.getRound();
        initialWorldAndAvatar(seed, rc.getMovementRecord(), avatarName);
        PAW = new PutAvatarToWorld(world, avatar, isDark);
    }


    private void gameOver() {
        while (GAME_STATUS == LOSE || DIED == DEAD) {
            menu.gameOver(Integer.toString(round));
            if (StdDraw.hasNextKeyTyped()) {
                char type = StdDraw.nextKeyTyped();
                if (type == 'b') {
                    GAME_STATUS = IN_GAME;
                    interactWithKeyboard();
                } else if (type == 'q') {
                    System.exit(0);
                }
            }
        }
    }

    public void setGameStatus(boolean status) {
        if (status) {
            GAME_STATUS = LOSE;
            DIED = true;
        }
    }

    @Override
    public String toString() {
        return PAW.toString();
    }

}
