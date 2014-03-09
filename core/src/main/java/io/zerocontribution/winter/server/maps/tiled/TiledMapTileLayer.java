package io.zerocontribution.winter.server.maps.tiled;

import com.badlogic.gdx.maps.MapLayer;

public class TiledMapTileLayer extends MapLayer {

    private int width;
    private int height;

    private float tileWidth;
    private float tileHeight;

    private Cell[][] cells;

    public TiledMapTileLayer(int width, int height, int tileWidth, int tileHeight) {
        super();
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.cells = new Cell[width][height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getTileWidth() {
        return tileWidth;
    }

    public float getTileHeight() {
        return tileHeight;
    }

    public Cell getCell(int x, int y) {
        if (x < 0 || x >= width) return null;
        if (y < 0 || y >= height) return null;
        return cells[x][y];
    }

    public void setCell(int x, int y, Cell cell) {
        if (x < 0 || x >= width) return;
        if (y < 0 || y >= height) return;
        cells[x][y] = cell;
    }

    public static class Cell {

        public final static int ROTATE_0 = 0;
        public final static int ROTATE_90 = 1;
        public final static int ROTATE_180 = 2;
        public final static int ROTATE_270 = 3;

        private TiledMapTile tile;
        private boolean flipHorizontally;
        private boolean flipVertically;
        private int rotation;

        public TiledMapTile getTile() {
            return tile;
        }

        public void setTile(TiledMapTile tile) {
            this.tile = tile;
        }

        public boolean isFlipHorizontally() {
            return flipHorizontally;
        }

        public void setFlipHorizontally(boolean flipHorizontally) {
            this.flipHorizontally = flipHorizontally;
        }

        public boolean isFlipVertically() {
            return flipVertically;
        }

        public void setFlipVertically(boolean flipVertically) {
            this.flipVertically = flipVertically;
        }

        public int getRotation() {
            return rotation;
        }

        public void setRotation(int rotation) {
            this.rotation = rotation;
        }

    }

}
