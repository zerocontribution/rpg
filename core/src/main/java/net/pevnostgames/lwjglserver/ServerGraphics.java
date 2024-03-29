/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package net.pevnostgames.lwjglserver;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import net.pevnostgames.lwjglserver.exception.ServerGraphicsException;

public class ServerGraphics implements Graphics {
    @Override
    public boolean isGL30Available() {
        throw new ServerGraphicsException();
    }

    @Override
    public GL20 getGL20() {
        throw new ServerGraphicsException();
    }

    @Override
    public GL30 getGL30() {
        throw new ServerGraphicsException();
    }

    @Override
    public int getWidth() {
        throw new ServerGraphicsException();
    }

    @Override
    public int getHeight() {
        throw new ServerGraphicsException();
    }

    @Override
    public float getDeltaTime() {
        throw new ServerGraphicsException();
    }

    @Override
    public float getRawDeltaTime() {
        throw new ServerGraphicsException();
    }

    @Override
    public int getFramesPerSecond() {
        throw new ServerGraphicsException();
    }

    @Override
    public GraphicsType getType() {
        throw new ServerGraphicsException();
    }

    @Override
    public float getPpiX() {
        throw new ServerGraphicsException();
    }

    @Override
    public float getPpiY() {
        throw new ServerGraphicsException();
    }

    @Override
    public float getPpcX() {
        throw new ServerGraphicsException();
    }

    @Override
    public float getPpcY() {
        throw new ServerGraphicsException();
    }

    @Override
    public float getDensity() {
        throw new ServerGraphicsException();
    }

    @Override
    public boolean supportsDisplayModeChange() {
        throw new ServerGraphicsException();
    }

    @Override
    public DisplayMode[] getDisplayModes() {
        throw new ServerGraphicsException();
    }

    @Override
    public DisplayMode getDesktopDisplayMode() {
        throw new ServerGraphicsException();
    }

    @Override
    public boolean setDisplayMode(DisplayMode displayMode) {
        throw new ServerGraphicsException();
    }

    @Override
    public boolean setDisplayMode(int width, int height, boolean fullscreen) {
        throw new ServerGraphicsException();
    }

    @Override
    public void setTitle(String title) {
        throw new ServerGraphicsException();
    }

    @Override
    public void setVSync(boolean vsync) {
        throw new ServerGraphicsException();
    }

    @Override
    public BufferFormat getBufferFormat() {
        throw new ServerGraphicsException();
    }

    @Override
    public boolean supportsExtension(String extension) {
        throw new ServerGraphicsException();
    }

    @Override
    public void setContinuousRendering(boolean isContinuous) {
        throw new ServerGraphicsException();
    }

    @Override
    public boolean isContinuousRendering() {
        throw new ServerGraphicsException();
    }

    @Override
    public void requestRendering() {
        throw new ServerGraphicsException();
    }

    @Override
    public boolean isFullscreen() {
        throw new ServerGraphicsException();
    }
}
