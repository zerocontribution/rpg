package io.zerocontribution.winter.server.maps.tiled;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import net.pevnostgames.lwjglserver.ServerFiles;

public class TmxMapLoader {

    protected static final int FLAG_FLIP_HORIZONTALLY = 0x80000000;
    protected static final int FLAG_FLIP_VERTICALLY = 0x40000000;
    protected static final int FLAG_FLIP_DIAGONALLY = 0x20000000;
    protected static final int MASK_CLEAR = 0xE0000000;

    ServerFiles serverFiles;

    protected XmlReader xml = new XmlReader();
    protected Element root;

    protected int mapTileWidth;
    protected int mapTileHeight;
    protected int mapWidthInPixels;
    protected int mapHeightInPixels;

    protected TiledMap map;

    public TmxMapLoader() {
        serverFiles = new ServerFiles();
    }

    public TmxMapLoader(ServerFiles serverFiles) {
        this.serverFiles = serverFiles;
    }

    public TiledMap load(String fileName) {
        try {
            FileHandle tmxFile = serverFiles.internal(fileName);
            root = xml.parse(tmxFile);
            return loadTilemap(root, tmxFile);
        } catch (Exception e) {
            throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
        }
    }

    protected TiledMap loadTilemap(Element root, FileHandle tmxFile) {
        TiledMap map = new TiledMap();

        String mapOrientation = root.getAttribute("orientation", null);
        int mapWidth = root.getIntAttribute("width", 0);
        int mapHeight = root.getIntAttribute("height", 0);
        int tileWidth = root.getIntAttribute("tilewidth", 0);
        int tileHeight = root.getIntAttribute("tileheight", 0);

        MapProperties mapProperties = map.getProperties();
        if (mapOrientation != null) {
            mapProperties.put("orientation", mapOrientation);
        }
        mapProperties.put("width", mapWidth);
        mapProperties.put("height", mapHeight);
        mapProperties.put("tilewidth", tileWidth);
        mapProperties.put("tileheight", tileHeight);
        mapTileWidth = tileWidth;
        mapTileHeight = tileHeight;
        mapWidthInPixels = mapWidth * tileWidth;
        mapHeightInPixels = mapHeight * tileHeight;

        Element properties = root.getChildByName("properties");
        if (properties != null) {
            loadProperties(map.getProperties(), properties);
        }

        Array<Element> tilesets = root.getChildrenByName("tileset");
        for (Element element : tilesets) {
            loadTileSet(map, element, tmxFile);
            root.removeChild(element);
        }

        for (int i = 0, j = root.getChildCount(); i < j; i++) {
            Element element = root.getChild(i);
            String name = element.getName();
            if (name.equals("layer")) {
                loadTileLayer(map, element);
            } else if (name.equals("objectgroup")) {
                loadObjectGroup(map, element);
            }
        }

        return map;
    }

    protected void loadTileSet(TiledMap map, Element element, FileHandle tmxFile) {
        if (element.getName().equals("tileset")) {
            String name = element.get("name", null);
            int firstgid = element.getIntAttribute("firstgid", 1);
            int tileWidth = element.getIntAttribute("tilewidth", 0);
            int tileHeight = element.getIntAttribute("tileheight", 0);
            int spacing = element.getIntAttribute("spacing", 0);
            int margin = element.getIntAttribute("margin", 0);
            String source = element.getAttribute("source", null);

            int imageWidth = 0, imageHeight = 0;

            if (source != null) {
                throw new IllegalStateException("Server TmxMapLoader does not support tileset source attribute yet");
            } else {
                imageWidth = element.getChildByName("image").getIntAttribute("width", 0);
                imageHeight = element.getChildByName("image").getIntAttribute("height", 0);
            }

            TiledMapTileSet tileset = new TiledMapTileSet();
            MapProperties props = tileset.getProperties();
            props.put("firstgid", firstgid);
            props.put("tilewidth", tileWidth);
            props.put("tileheight", tileHeight);
            props.put("margin", margin);
            props.put("spacing", spacing);

            int stopWidth = imageWidth - tileWidth;
            int stopHeight = imageHeight - tileHeight;

            int id = firstgid;

            for (int y = margin; y <= stopHeight; y += tileHeight + spacing) {
                for (int x = margin; x <= stopWidth; x += tileWidth + spacing) {
                    TiledMapTile tile = new StaticTiledMapTile();
                    tile.setId(id);
                    tileset.putTile(id++, tile);
                }
            }

            Array<Element> tileElements = element.getChildrenByName("tile");

            for (Element tileElement : tileElements) {
                int localtid = tileElement.getIntAttribute("id", 0);

                TiledMapTile tile = tileset.getTile(firstgid + localtid);
                if (tile != null) {
                    String terrain = tileElement.getAttribute("terrain", null);
                    if (terrain != null) {
                        tile.getProperties().put("terrain", terrain);
                    }
                    String probability = tileElement.getAttribute("probability", null);
                    if (probability != null) {
                        tile.getProperties().put("probability", probability);
                    }
                    Element properties = tileElement.getChildByName("properties");
                    if (properties != null) {
                        loadProperties(tile.getProperties(), properties);
                    }
                }
            }

            Element properties = element.getChildByName("properties");
            if (properties != null) {
                loadProperties(tileset.getProperties(), properties);
            }
            map.getTileSets().addTileSet(tileset);
        }
    }

    protected void loadTileLayer(TiledMap map, Element element) {
        if (element.getName().equals("layer")) {
            String name = element.getAttribute("name", null);
            int width = element.getIntAttribute("width", 0);
            int height = element.getIntAttribute("height", 0);
            int tileWidth = element.getParent().getIntAttribute("tilewidth", 0);
            int tileHeight = element.getParent().getIntAttribute("tileheight", 0);
            boolean visible = element.getIntAttribute("visible", 1) == 1;
            float opacity = element.getFloatAttribute("opacity", 1.0f);
            TiledMapTileLayer layer = new TiledMapTileLayer(width, height, tileWidth, tileHeight);
            layer.setVisible(visible);
            layer.setOpacity(opacity);
            layer.setName(name);

            int[] ids = TmxMapHelper.getTileIds(element, width, height);
            TiledMapTileSets tilesets = map.getTileSets();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int id = ids[y * width + x];
                    boolean flipHorizontally = ((id & FLAG_FLIP_HORIZONTALLY) != 0);
                    boolean flipVertically = ((id & FLAG_FLIP_VERTICALLY) != 0);
                    boolean flipDiagonally = ((id & FLAG_FLIP_DIAGONALLY) != 0);

                    TiledMapTile tile = tilesets.getTile(id & ~MASK_CLEAR);
                    if (tile != null) {
                        TiledMapTileLayer.Cell cell = createTileLayerCell(flipHorizontally, flipVertically, flipDiagonally);
                        cell.setTile(tile);
                        layer.setCell(x, height - 1 - y, cell);
                    }
                }
            }

            Element properties = element.getChildByName("properties");
            if (properties != null) {
                loadProperties(layer.getProperties(), properties);
            }
            map.getLayers().add(layer);
        }
    }

    protected void loadObjectGroup(TiledMap map, Element element) {
        if (element.getName().equals("objectgroup")) {
            String name = element.getAttribute("name", null);
            MapLayer layer = new MapLayer();
            layer.setName(name);
            Element properties = element.getChildByName("properties");
            if (properties != null) {
                loadProperties(layer.getProperties(), properties);
            }

            for (Element objectElement : element.getChildrenByName("object")) {
                loadObject(layer, objectElement);
            }

            map.getLayers().add(layer);
        }
    }

    protected void loadObject(MapLayer layer, Element element) {
        if (element.getName().equals("object")) {
            MapObject object = null;

            float x = element.getIntAttribute("x", 0);
            float y = mapHeightInPixels - element.getIntAttribute("y", 0);
            float width = element.getIntAttribute("width", 0);
            float height = element.getIntAttribute("height", 0);

            if (element.getChildCount() > 0) {
                Element child = null;
                if ((child = element.getChildByName("polygon")) != null) {
                    String[] points = child.getAttribute("points").split(" ");
                    float[] vertices = new float[points.length * 2];
                    for (int i = 0; i < points.length; i++) {
                        String[] point = points[i].split(",");
                        vertices[i * 2] = Integer.valueOf(point[0]);
                        vertices[i * 2 + 1] = Integer.valueOf(point[1]) * -1;
                    }
                    Polygon polygon = new Polygon(vertices);
                    polygon.setPosition(x, y);
                    object = new PolygonMapObject(polygon);
                } else if ((child = element.getChildByName("polyline")) != null) {
                    String[] points = child.getAttribute("points").split(" ");
                    float[] vertices = new float[points.length * 2];
                    for (int i = 0; i < points.length; i++) {
                        String[] point = points[i].split(",");
                        vertices[i * 2] = Integer.parseInt(point[0]);
                        vertices[i * 2 + 1] = Integer.parseInt(point[1]) * -1;
                    }
                    Polyline polyline = new Polyline(vertices);
                    polyline.setPosition(x, y);
                    object = new PolylineMapObject(polyline);
                } else if ((child = element.getChildByName("ellipse")) != null) {
                    object = new EllipseMapObject(x, y - height, width, height);
                }
            }
            if (object == null) {
                object = new RectangleMapObject(x, y - height, width, height);
            }
            object.setName(element.getAttribute("name", null));
            String type = element.getAttribute("type", null);
            if (type != null) {
                object.getProperties().put("type", type);
            }
            int gid = element.getIntAttribute("gid", -1);
            if (gid != -1) {
                object.getProperties().put("gid", gid);
            }
            object.getProperties().put("x", x);
            object.getProperties().put("y", y - height);
            object.setVisible(element.getIntAttribute("visible", 1) == 1);
            Element properties = element.getChildByName("properties");
            if (properties != null) {
                loadProperties(object.getProperties(), properties);
            }
            layer.getObjects().add(object);
        }
    }

    protected void loadProperties(MapProperties properties, Element element) {
        if (element.getName().equals("properties")) {
            for (Element property : element.getChildrenByName("property")) {
                String name = property.getAttribute("name", null);
                String value = property.getAttribute("value", null);
                if (value == null) {
                    value = property.getText();
                }
                properties.put(name, value);
            }
        }
    }

    protected TiledMapTileLayer.Cell createTileLayerCell(boolean flipHorizontally, boolean flipVertically, boolean flipDiagonally) {
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        if (flipDiagonally) {
            if (flipHorizontally && flipVertically) {
                cell.setFlipHorizontally(true);
                cell.setRotation(TiledMapTileLayer.Cell.ROTATE_270);
            } else if (flipHorizontally) {
                cell.setRotation(TiledMapTileLayer.Cell.ROTATE_270);
            } else if (flipVertically) {
                cell.setRotation(TiledMapTileLayer.Cell.ROTATE_90);
            } else {
                cell.setFlipVertically(true);
                cell.setRotation(TiledMapTileLayer.Cell.ROTATE_270);
            }
        } else {
            cell.setFlipHorizontally(flipHorizontally);
            cell.setFlipVertically(flipVertically);
        }
        return cell;
    }

}
