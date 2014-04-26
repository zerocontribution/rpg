package io.zerocontribution.winter.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

/**
 * @see http://siondream.com/blog/games/managing-groups-of-assets/
 */
public class Assets implements Disposable, AssetErrorListener {

    private AssetManager manager;
    private ObjectMap<String, Array<Asset>> groups;

    public Assets(String assetFile) {
        manager = new AssetManager();
        manager.setErrorListener(this);

        loadGroups(assetFile);
    }

    public void loadGroup(String groupName) {
        Array<Asset> assets = groups.get(groupName, null);

        if (assets != null) {
            for (Asset asset : assets) {
                manager.load(asset.path, asset.type);
            }
        } else {
            Log.info("AssetManager", "Error loading group '" + groupName + "', not found.");
        }
    }

    public void unloadGroup(String groupName) {
        Array<Asset> assets = groups.get(groupName, null);

        if (assets != null) {
            for (Asset asset : assets) {
                if (manager.isLoaded(asset.path, asset.type)) {
                    manager.unload(asset.path);
                }
            }
        } else {
            Log.info("AssetManager", "Error unloading group '" + groupName + "', not found");
        }
    }

    public synchronized <T> T get(String fileName) {
        return manager.get(fileName);
    }

    public synchronized <T> T get(String fileName, Class<T> type) {
        return manager.get(fileName, type);
    }

    public boolean update() {
        return manager.update();
    }

    public void finishLoading() {
        manager.finishLoading();
    }

    public float getProgress() {
        return manager.getProgress();
    }

    public void dispose() {
        manager.dispose();
    }

    public void error(AssetDescriptor asset, Throwable throwable) {
        Log.error("AssetManager", "Error loading" + asset.fileName);
    }

    private void loadGroups(String assetFile) {
        groups = new ObjectMap<String, Array<Asset>>();

        XmlReader reader = new XmlReader();
        XmlReader.Element root;
        try {
            FileHandle file = Gdx.files.internal(assetFile);
            root = reader.parse(file);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        for (XmlReader.Element groupElement : root.getChildrenByName("group")) {
                String groupName = groupElement.getAttribute("name", "base");

                if (groups.containsKey(groupName)) {
                    continue;
                }

                Array<Asset> assets = new Array<Asset>();

                for (XmlReader.Element assetElement : groupElement.getChildrenByName("asset")) {
                    assets.add(new Asset(
                            assetElement.getAttribute("type", ""),
                            assetElement.getAttribute("path", "")));
                }

                groups.put(groupName, assets);
            }
    }

    private class Asset {
        public Class<?> type;
        public String path;

        public Asset(String type, String path) {
            try {
                this.type = Class.forName(type);
                this.path = path;
            } catch (ClassNotFoundException e) {
                Log.error("AssetManager", "Asset type '" + type + "' not found");
            }
        }
    }

}
