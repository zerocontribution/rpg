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

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.NetJavaImpl;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.awt.Desktop;

public class ServerNet implements Net {

	NetJavaImpl netJavaImpl = new NetJavaImpl();

	@Override
	public void sendHttpRequest(HttpRequest httpRequest, HttpResponseListener httpResponseListener) {
		netJavaImpl.sendHttpRequest(httpRequest, httpResponseListener);
	}

    @Override
    public void cancelHttpRequest(HttpRequest httpRequest) {
        netJavaImpl.cancelHttpRequest(httpRequest);
    }

    @Override
	public ServerSocket newServerSocket(Protocol protocol, int port, ServerSocketHints hints) {
		return new ServerApplicationSocket(protocol, port, hints);
	}

	@Override
	public Socket newClientSocket(Protocol protocol, String host, int port, SocketHints hints) {
		return new ClientSocket(protocol, host, port, hints);
	}

	@Override
	public void openURI(String URI) {
		if (!Desktop.isDesktopSupported()) return;

		Desktop desktop = Desktop.getDesktop();

		if (!desktop.isSupported(Desktop.Action.BROWSE)) return;

		try {
			desktop.browse(new java.net.URI(URI));
		} catch (Exception e) {
			throw new GdxRuntimeException(e);
		}
	}

}
