package io.zerocontribution.winter.server.maps.tiled;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.StreamUtils;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * Copied from libGDX: At this moment, this class is protected (public in future releases).
 * {@link https://github.com/libgdx/libgdx/blob/master/gdx/src/com/badlogic/gdx/maps/tiled/TmxMapHelper.java}
 */
public class TmxMapHelper {
    static final int FLAG_FLIP_HORIZONTALLY = 0x80000000;
    static final int FLAG_FLIP_VERTICALLY = 0x40000000;
    static final int FLAG_FLIP_DIAGONALLY = 0x20000000;
    static final int MASK_CLEAR = 0xE0000000;

    private TmxMapHelper () {

    }

    static public int[] getTileIds (Element element, int width, int height) {
        Element data = element.getChildByName("data");
        String encoding = data.getAttribute("encoding", null);
        if (encoding == null) { // no 'encoding' attribute means that the encoding is XML
            throw new GdxRuntimeException("Unsupported encoding (XML) for TMX Layer Data");
        }
        int[] ids = new int[width * height];
        if (encoding.equals("csv")) {
            String[] array = data.getText().split(",");
            for (int i = 0; i < array.length; i++)
                ids[i] = (int)Long.parseLong(array[i].trim());
        } else {
            if (true)
                if (encoding.equals("base64")) {
                    InputStream is = null;
                    try {
                        String compression = data.getAttribute("compression", null);
                        byte[] bytes = Base64Coder.decode(data.getText());
                        if (compression == null)
                            is = new ByteArrayInputStream(bytes);
                        else if (compression.equals("gzip"))
                            is = new GZIPInputStream(new ByteArrayInputStream(bytes), bytes.length);
                        else if (compression.equals("zlib"))
                            is = new InflaterInputStream(new ByteArrayInputStream(bytes));
                        else
                            throw new GdxRuntimeException("Unrecognised compression (" + compression + ") for TMX Layer Data");

                        byte[] temp = new byte[4];
                        for (int y = 0; y < height; y++) {
                            for (int x = 0; x < width; x++) {
                                int read = is.read(temp);
                                while (read < temp.length) {
                                    int curr = is.read(temp, read, temp.length - read);
                                    if (curr == -1) break;
                                    read += curr;
                                }
                                if (read != temp.length)
                                    throw new GdxRuntimeException("Error Reading TMX Layer Data: Premature end of tile data");
                                ids[y * width + x] = unsignedByteToInt(temp[0]) | unsignedByteToInt(temp[1]) << 8
                                        | unsignedByteToInt(temp[2]) << 16 | unsignedByteToInt(temp[3]) << 24;
                            }
                        }
                    } catch (IOException e) {
                        throw new GdxRuntimeException("Error Reading TMX Layer Data - IOException: " + e.getMessage());
                    } finally {
                        StreamUtils.closeQuietly(is);
                    }
                } else {
                    // any other value of 'encoding' is one we're not aware of, probably a feature of a future version of Tiled
                    // or another editor
                    throw new GdxRuntimeException("Unrecognised encoding (" + encoding + ") for TMX Layer Data");
                }
        }
        return ids;
    }

    static public int unsignedByteToInt (byte b) {
        return (int)b & 0xFF;
    }
}
