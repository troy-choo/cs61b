package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.Random;

public class Avatar {
    private int x, y;

    private TETile t;
    private int avatarIndex = 0;
    private WorldGenerator world;
    private Engine engine;
    private boolean atDoor;
    private String avatarName;

    public Avatar(long seed, WorldGenerator worldGenerator, String name,Engine engine) {
        Random random = new Random(seed);
        int[] coordinates = worldGenerator.getRandomRoomCoordinates(random);
        this.x = coordinates[0];
        this.y = coordinates[1];
        this.world = worldGenerator;
        this.engine = engine;
        if (name == null) {
            avatarName =  "you";
            t = Tileset.AVATAR;
        } else {
            avatarName = name;
            t = new TETile('@', Color.white, Color.black, name);
        }
        atDoor = false;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public TETile getT() { return t; }
    public String getAvatarName() {return avatarName;}
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setT(TETile t) { this.t = t; }

    public char listen() {
        char direction;
        do {
            direction = StdDraw.hasNextKeyTyped() ? StdDraw.nextKeyTyped() : ' ';
        } while (direction == ' ');
        walk(direction);
        return direction;
    }

    public void walk(char direction) {
        int newX = x, newY = y;

        if (direction == 'w') { newX++; }
        if (direction == 's') { newX--; }
        if (direction == 'a') { newY--; }
        if (direction == 'd') { newY++; }
        if (world.isTrap(newX, newY)) {
            x = newX; // Move the avatar to the trap
            y = newY; // Move the avatar to the trap
            engine.setGameStatus(true);
        }
        else if (world.isHallway(newX, newY) || world.isLockedDoor(newX, newY)) {
            x = newX;
            y = newY;
            if (world.isLockedDoor(newX, newY)) {
                atDoor = true;
            }
        }
        System.out.print(direction);
    }

    public boolean isAtDoor() {
        return atDoor;
    }


}
