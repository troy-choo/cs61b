package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.awt.Font;

public class MainMenu {
    private final int width;
    private final int height;
    private boolean createNewGame;
    private boolean createName;
    private TETile currentAvatarTile;



    public static void main(String[] args) {
        MainMenu game = new MainMenu(40, 40);
        game.showMenu();
    }

    public MainMenu(int width, int height) {
        this.width = width;
        this.height = height;
        setupCanvas();
    }

    private void setupCanvas() {
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public void drawFrame(String s, String n) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        Font fontSmall = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontBig);
        StdDraw.text(20, this.height - 10, "CS61B: THE GAME" );
        StdDraw.setFont(fontSmall);
        StdDraw.text(20, this.height - 20, "New Game (N)" );
        StdDraw.text(20, this.height - 22, "Load Game (L)" );
        StdDraw.text(20, this.height - 24, "Quit (Q)" );


        if (createNewGame || createName) {
            StdDraw.text(20, this.height - 28, "Seed: " + s);
            StdDraw.text(20, this.height - 30, "Type 's' to generate world");
        }
        if (createName) {
            StdDraw.text(20, this.height - 34, "Your name: " + n);
            StdDraw.text(20, this.height - 36, "press 'enter' to start world");
        }
        StdDraw.show();
    }

    public String[] showMenu() {
        StringBuilder input = new StringBuilder();
        StringBuilder name = new StringBuilder();

        while (true) {
            drawFrame(input.toString(), name.toString());
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            String[] result = handleKeyTyped(input, name);
            if (result != null) {
                return result;
            }
        }
    }


    private String[] handleKeyTyped(StringBuilder input, StringBuilder name) {
        char type = StdDraw.nextKeyTyped();

        if (input.length() == 0) {
            if (type == 'n') {
                input.append('n');
                createNewGame = true;
            } else if (type == 'l') {
                input.append(type);
                return new String[]{input.toString(), name.toString()};
            } else if (type == 'q') {
                System.exit(0);
            }
        } else if (createNewGame) {
            if (Character.isDigit(type)) {
                input.append(type);
            } else if (type == 's' && input.length() > 1) { // a seed should have at least one digit
                input.append(type);
                createName = true;
                createNewGame = false;
            }
        } else if (createName) {
            name.append(type);
            if (type == '\n') {
                return new String[]{input.toString(), name.toString()};
            }
        }

        return null;
    }



    public void gameOver(String round) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        Font fontSmall = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(fontBig);
        StdDraw.text(20, this.height - 10, "Game Over!" );
        StdDraw.setFont(fontSmall);
        StdDraw.text(20, this.height - 20, "Your round: " + round );
        StdDraw.text(20, this.height - 22, "Go Back (B)" );
        StdDraw.text(20, this.height - 24, "Quit (Q)" );
        StdDraw.show();
    }
}