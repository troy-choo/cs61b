package PlusWorld;
import org.junit.Test;
import static org.junit.Assert.*;

import byowTools.TileEngine.TERenderer;
import byowTools.TileEngine.TETile;
import byowTools.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of plus shaped regions.
 */
public class PlusWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    // Setting at the center.
    private static int midWidth = WIDTH / 2;
    private static int midHeight = HEIGHT / 2;


    public static void main(String[] args) {
        addPlus(5);
    }

    public static void addPlus(int size) {
        int out = size + (size / 2);
        int in = size / 2;
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        for (int x = midHeight - out; x < midHeight + out; x++) {
            for (int y = midHeight - in; y < midHeight + in; y++) {
                world[x][y] = Tileset.WALL;
            }
        }
        for (int x = midWidth - in; x < midWidth + in; x++) {
            for (int y = midWidth - out; y < midWidth + out; y++) {
                world[x][y] = Tileset.WALL;
            }
        }

        ter.renderFrame(world);
    }
}
