package PlusWorld;

import byowTools.TileEngine.TERenderer;
import byowTools.TileEngine.TETile;
import byowTools.TileEngine.Tileset;

/**
 *  Draws a world that is mostly empty except for a small region.
 */
public class BoringWorldDemo {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 30;

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // fills in a block 14 tiles wide by 4 tiles tall
        for (int x = 20; x < 35; x += 1) {
            for (int y = 5; y < 10; y += 1) {
                world[x][y] = Tileset.WALL;
            }
        }

        // Grass
        for (int x = 20; x < 22; x++) {
            for (int y = 5; y < 7; y++) {
                world[x][y] = Tileset.GRASS;
            }
        }

        // Water
        for (int x = 22; x < 24; x++) {
            for (int y = 7; y < 9; y++) {
                world[x][y] = Tileset.WATER;
            }
        }

        // Sand
        for (int x = 24; x < 26; x++) {
            for (int y = 5; y < 7; y++) {
                world[x][y] = Tileset.SAND;
            }
        }

        // Mountain
        for (int x = 26; x < 34; x++){
            for (int y = 5; y < 7; y++) {
                world[x][y] = Tileset.MOUNTAIN;
            }
        }

        // Tree
        for (int x = 26; x < 34; x++) {
            for (int y = 7; y < 9; y++) {
                world[x][y] = Tileset.TREE;
            }
        }


        // draws the world to the screen
        ter.renderFrame(world);
    }


}
