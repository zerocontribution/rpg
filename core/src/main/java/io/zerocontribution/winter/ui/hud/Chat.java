package io.zerocontribution.winter.ui.hud;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.minlog.Log;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.network.ChatMessage;
import io.zerocontribution.winter.ui.UIManager;

public class Chat extends Group implements Disposable {

    public final static int PREF_WIDTH = 400;
    private final static int MAX_MESSAGES = 3;

    private Skin skin;
    private ScrollPane scrollPane;
    private Table messages;

    public Chat() {
        skin = UIManager.getInstance().getSkin();

        messages = new Table(skin);
        messages.debug();
        messages.setHeight(200);
        messages.setWidth(PREF_WIDTH + 50);
        messages.defaults().align(Align.left).height(20);
        messages.bottom();

        scrollPane = new ScrollPane(messages, skin);
        scrollPane.setPosition(0, 50);
        scrollPane.setWidth(PREF_WIDTH + 50);
        addActor(scrollPane);

        final TextField input = new TextField("", skin);
        input.setWidth(PREF_WIDTH);
        input.addListener(new InputListener() {
            public boolean keyUp(InputEvent event, int keycode) {
                if (input.getText().length() > 0) {
                    if (keycode == Input.Keys.ENTER) {
                        WinterGame.gameClient.sendChatMessage(input.getText());
                        input.setText("");
                        return true;
                    } else if (keycode == Input.Keys.ESCAPE) {
                        input.setText("");
                        return true;
                    }
                }
                return false;
            }
        });
        addActor(input);

        WinterGame.events.listen(ChatMessage.ChatMessageEvent.class, this);
    }

    synchronized public void displayMessage(String name, String message) {
//        Log.info("Client", "cells: " + messages.getCells().size());
//        while (messages.getCells().size() >= MAX_MESSAGES) {
//            Log.info("Client", "Chat: Pruned 1 chat message");
//        }

        messages.row();

        Label nameLabel = new Label(name + ":", skin);
        nameLabel.setWidth(50);
        nameLabel.setFontScale(0.5f);
        messages.add(nameLabel);

        Label msgLabel = new Label(message, skin);
        msgLabel.setWrap(true);
        msgLabel.setWidth(PREF_WIDTH);
        msgLabel.setFontScale(0.5f);
        msgLabel.setAlignment(Align.left);
        messages.add(msgLabel).expandX();

        scrollPane.setScrollY(messages.getHeight() + 20);
    }

    public void dispose() {
        WinterGame.events.mute(ChatMessage.ChatMessageEvent.class, this);
    }

}
