package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class PutAvatarToWorld {

    private TETile[][] teTile;

    private Avatar avatar;

    public PutAvatarToWorld(WorldGenerator world, Avatar avatar) {
        this.avatar = avatar;
        WorldGenerator w = new WorldGenerator(world);
        teTile = w.getTiles();

        putAvatar();
//        System.out.println(this);
//        teTile[avatar.getY()][avatar.getX()] = avatar.getT();
//        System.out.println(this);
    }

    public PutAvatarToWorld(WorldGenerator world, Avatar avatar, boolean isDark) {
        this.avatar = avatar;
        WorldGenerator w = new WorldGenerator(world);
        teTile = w.getTiles();
        putAvatar();
//        System.out.println(this);
        if (isDark) {
            makeDark();
        }
//        teTile[avatar.getY()][avatar.getX()] = avatar.getT();
//        System.out.println(this);
    }

    //below changed
    public void putAvatar() {
        teTile[avatar.getY()][avatar.getX()] = avatar.getT();
    }

    public TETile[][] getTeTiles() {
        return teTile;
    }

    public String getMouseHangName(double x, double y) {
        int intX = (int) x;
        int intY = (int) y - 1;
        return isValidPosition(intX, intY) ? teTile[intX][intY].description() : " ";
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < teTile.length && y >= 0 && y < teTile[0].length;
    }

    public void makeDark() {
        int radius = 1;
        int avatarX = avatar.getY();
        int avatarY = avatar.getX();

        for (int x = 0; x < teTile[0].length; x++) {
            for (int y = 0; y < teTile.length; y++) {
                if (Math.abs(x - avatarY) > radius || Math.abs(y - avatarX) > radius) {
                    teTile[y][x] = Tileset.NOTHING;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int col = teTile[0].length - 1; col >= 0; col--) {
            for (int row = 0; row < teTile.length; row++) {
                s.append(teTile[row][col].character());
            }
            s.append('\n');
        }
        return s.toString();
    }
}
