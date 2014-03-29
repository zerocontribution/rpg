package io.zerocontribution.winter.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import io.zerocontribution.winter.WinterGame;
import io.zerocontribution.winter.event.GameEvent;
import io.zerocontribution.winter.ui.UIManager;

import java.util.concurrent.CopyOnWriteArrayList;

public class ScrollingCombatText extends Actor {

    private CopyOnWriteArrayList<CombatText> combatTexts = new CopyOnWriteArrayList<CombatText>();

    private Pool<CombatText> combatTextPool = new Pool<CombatText>(16, 32) {
        protected CombatText newObject() {
            return new CombatText();
        }
    };

    private Pool<BitmapFont> fontsPool = new Pool<BitmapFont>(16, 32) {
        protected BitmapFont newObject() {
            BitmapFont defaultFont = UIManager.getInstance().getSkin().getFont("default-font");
            return new BitmapFont(defaultFont.getData(), defaultFont.getRegion(), true);
        }
    };
    private Array<BitmapFont> obtainedFonts = new Array<BitmapFont>(32);

    public ScrollingCombatText() {
        WinterGame.events.listen(ScrollingCombatTextEvent.class, this);
    }

    public void draw(SpriteBatch batch, float parentAlpha) {
        if (combatTexts.size() > 0) {
            fontsPool.freeAll(obtainedFonts);

            for (CombatText combatText : combatTexts) {
                BitmapFont font = fontsPool.obtain();
                combatText.y += 1;
                combatText.color.a -= 0.01f;

                font.setColor(combatText.color);
                font.draw(batch, combatText.text, combatText.x, combatText.y);

                if (combatText.color.a <= 0) {
                    combatTexts.remove(combatText);
                    combatTextPool.free(combatText);
                }

                obtainedFonts.add(font);
            }
        }
    }

    public void displayText(String text, Color color) {
        CombatText combatText = combatTextPool.obtain();
        combatText.text = text;
        combatText.color = color.cpy();
        combatText.color.a = 1; // Just in case.
        combatText.y = (Gdx.graphics.getHeight() / 2) + 115;
        combatText.x = (Gdx.graphics.getWidth() / 2) - (text.length() * 12 / 2);

        combatTexts.add(combatText);
    }

    public static class ScrollingCombatTextEvent implements GameEvent<ScrollingCombatText> {

        private String text;
        private Color color;

        public ScrollingCombatTextEvent(String text) {
            this(text, Color.YELLOW);
        }

        public ScrollingCombatTextEvent(String text, Color color) {
            this.text = text;
            this.color = color;
        }

        public void notify(ScrollingCombatText listener) {
            listener.displayText(text, color);
        }
    }

    public static class CombatText {
        String text;
        Color color;
        float y, x;

        public void reset() {
            text = null;
            color = null;
            y = 0f;
            x = 0f;
        }
    }
}
