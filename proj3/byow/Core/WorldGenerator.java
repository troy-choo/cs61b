package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;


public class WorldGenerator {
    private final int WIDTH;
    private final int HEIGHT;
    private final int NOTHING = 0;
    private final int HALLWAY = 1;
    private final int WALL = 2;
    private final int TRAP = 4;


    private final int LOCKED_DOOR = 3;

    private int[][] world;

    private TETile[][] tiles;

    private List<Room> rooms = new ArrayList<>();

    public WorldGenerator(long seed, int width, int height) {
        WIDTH = height;
        HEIGHT = width;
        world = new int[HEIGHT][WIDTH];
        tiles = new TETile[HEIGHT][WIDTH];
        rooms = new ArrayList<>();
        Random random = new Random(seed);
        generateRooms(random);
        placeTraps(random);
        connectRooms();
        generateDoor(random);
        printWorld(world);
    }

    public WorldGenerator(WorldGenerator w) {
        this(w.HEIGHT, w.WIDTH, w.tiles, w.getWorld());
    }

    private WorldGenerator(int height, int width, TETile[][] t, int[][] worldMap) {
        HEIGHT = height;
        WIDTH = width;
        tiles = t;
        printWorld(worldMap);
    }



    public int[][] getWorld() {
        return world;
    }

    /**
     * Fill the tiles[][] base on the world[][]
     */
    public void printWorld(int[][] world) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                switch (world[y][x]) {
                    case NOTHING -> tiles[y][x] = Tileset.NOTHING;
                    case HALLWAY -> tiles[y][x] = Tileset.FLOOR;
                    case WALL -> tiles[y][x] = Tileset.WALL;
                    case LOCKED_DOOR -> tiles[y][x] = Tileset.LOCKED_DOOR;
                    case TRAP -> tiles[y][x] = Tileset.TRAP;
                }
            }
        }
    }


    /**
     * Generate random rooms and avoid overlap.
     * @param r
     */
    private boolean doesRoomOverlap(Room r) {
        for (Room existing : rooms) {
            if (r.x < existing.x + existing.width &&
                    r.x + r.width > existing.x &&
                    r.y < existing.y + existing.height &&
                    r.y + r.height > existing.y) {
                return true;
            }
        }
        return false;
    }

    public void generateRooms(Random random) {
        int numberOfRooms = random.nextInt(10) + 151;

        for (int i = 0; i < numberOfRooms; i++) {
            int roomWidth = random.nextInt(10) + 5;
            int roomHeight = random.nextInt(10) + 3;
            int roomX = random.nextInt(WIDTH - roomWidth);
            int roomY = random.nextInt(HEIGHT - roomHeight);
            Room r = new Room(roomX, roomY, roomWidth, roomHeight);

            if (!doesRoomOverlap(r)) {
                rooms.add(r);
                drawRoom(r);
            }
        }
    }

    /**
     * Minimum spanning tree to connect room
     */
    public void connectRooms() {
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            for (int j = i + 1; j < rooms.size(); j++) {
                edges.add(new Edge(rooms.get(i), rooms.get(j)));
            }
        }

        Collections.sort(edges, Comparator.comparing(edge -> edge.distance));
        int[] parents = new int[rooms.size()];
        for (int i = 0; i < rooms.size(); i++) {
            parents[i] = i;
        }

        for (Edge edge : edges) {
            int root1 = find(parents, rooms.indexOf(edge.room1));
            int root2 = find(parents, rooms.indexOf(edge.room2));

            if (root1 != root2) {
                parents[root1] = root2;
                drawHallway(edge.room1, edge.room2);
            }
        }
    }

    private int find(int[] parents, int node) {
        return node == parents[node] ? node : find(parents, parents[node]);
    }

    private void drawRoom(Room room) {
        for (int y = room.y; y < room.y + room.height; y++) {
            for (int x = room.x; x < room.x + room.width; x++) {
                world[y][x] = (y == room.y || y == room.y + room.height - 1 || x == room.x || x == room.x + room.width - 1) ? WALL : HALLWAY;
            }
        }
    }
    public void placeTraps(Random random) {
        int numberOfTraps = random.nextInt(5) + 1;

        for (int i = 0; i < numberOfTraps; i++) {
            Room room = rooms.get(random.nextInt(rooms.size()));
            int x = random.nextInt(room.width - 2) + room.x + 1;
            int y = random.nextInt(room.height - 2) + room.y + 1;

            if (world[y][x] == HALLWAY) { // no traps on the wall
                world[y][x] = TRAP;
            }
        }
    }

    /**
     * Draw the hallways based from the connections.
     * @param room1
     * @param room2
     */
    public void drawHallway(Room room1, Room room2) {
        int x1 = room1.centerX();
        int y1 = room1.centerY();
        int x2 = room2.centerX();
        int y2 = room2.centerY();

        drawLine(x1, y1, x2, y1);
        drawLine(x2, y1, x2, y2);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        int currentX = x1;
        int currentY = y1;
        int dx = x2 > x1 ? 1 : -1;
        int dy = y2 > y1 ? 1 : -1;

        while (currentX != x2 || currentY != y2) {
            if (world[currentY][currentX] == NOTHING || world[currentY][currentX] == WALL) {
                world[currentY][currentX] = HALLWAY;
                drawWallsAround(currentX, currentY, dx, dy);
            }

            if (currentX != x2) currentX += dx;
            if (currentY != y2) currentY += dy;
        }

        if (world[y2][x2] == NOTHING || world[y2][x2] == WALL) {
            world[y2][x2] = HALLWAY;
            drawWallsAround(x2, y2, 0, 0);
        }
    }

    public void drawWallsAround(int x, int y, int excludeDx, int excludeDy) {
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == excludeDx && dy == excludeDy) continue;
                int newX = x + dx;
                int newY = y + dy;
                if (isValidPosition(newX, newY) && world[newY][newX] == NOTHING) {
                    world[newY][newX] = WALL;
                }
            }
        }
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }



    public TETile[][] getTiles() {
        return tiles;
    }

    /**
     * Returns a random room's coordinates within the WorldGenerator instance.
     * @param random The random instance used for generating the room coordinates.
     * @return An int array containing the x and y coordinates of the random room.
     */
    public int[] getRandomRoomCoordinates(Random random) {
        Room randomRoom = rooms.get(random.nextInt(rooms.size()));
        int x = randomRoom.centerX();
        int y = randomRoom.centerY();
        return new int[] {x, y};
    }

    /**
     * Returns whether the tile at the given x and y coordinates is a hallway.
     * @param x The x coordinate of the tile.
     * @param y The y coordinate of the tile.
     * @return A boolean value representing whether the tile is a hallway.
     */
    public boolean isHallway(int x, int y) {
        return world[y][x] == HALLWAY;
    }

    /**
     * Generate a door in a random room and a random wall within that room.
     * @param random The Random object initialized with the seed.
     */
    public void generateDoor(Random random) {
        // Choose a random room
        Room randomRoom = rooms.get(random.nextInt(rooms.size()));

        // Choose a random wall in the room (0: left, 1: top, 2: right, 3: bottom)
        int wall = random.nextInt(4);

        int doorX, doorY;

        // Find the position for the door based on the chosen wall
        switch (wall) {
            case 0: // Left wall
                doorX = randomRoom.x;
                doorY = randomRoom.y + random.nextInt(randomRoom.height - 2) + 1;
                break;
            case 1: // Top wall
                doorX = randomRoom.x + random.nextInt(randomRoom.width - 2) + 1;
                doorY = randomRoom.y + randomRoom.height - 1;
                break;
            case 2: // Right wall
                doorX = randomRoom.x + randomRoom.width - 1;
                doorY = randomRoom.y + random.nextInt(randomRoom.height - 2) + 1;
                break;
            default: // Bottom wall
                doorX = randomRoom.x + random.nextInt(randomRoom.width - 2) + 1;
                doorY = randomRoom.y;
                break;
        }

        world[doorY][doorX] = LOCKED_DOOR;
    }
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int col = WIDTH - 1; col >= 0; col--) {
            for (int row = 0; row < HEIGHT; row++) {
                s.append(tiles[row][col].character());

            }
            s.append('\n');

        }
        return s.toString();
    }

    public boolean isLockedDoor(int x, int y) {
        return world[y][x] == LOCKED_DOOR;
    }  public boolean isTrap(int x, int y) {
        return tiles[y][x] == Tileset.TRAP;
    }




    public class Room {
        int x, y, width, height;

        public Room(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public int centerX() {
            return x + width / 2;
        }

        public int centerY() {
            return y + height / 2;
        }
    }

    public class Edge {
        Room room1, room2;
        double distance;

        public Edge(Room r1, Room r2) {
            room1 = r1;
            room2 = r2;
            distance = calculateDistance();
        }

        private double calculateDistance() {
            return Math.sqrt(Math.pow(room1.centerX() - room2.centerX(), 2) + Math.pow(room1.centerY() - room2.centerY(), 2));
        }
    }


}