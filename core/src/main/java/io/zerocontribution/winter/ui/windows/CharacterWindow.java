package io.zerocontribution.winter.ui.windows;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import io.zerocontribution.winter.components.Player;
import io.zerocontribution.winter.components.Stats;
import io.zerocontribution.winter.ui.UIManager;
import io.zerocontribution.winter.utils.ClientGlobals;

public class CharacterWindow extends Window {

    private final static float FONT_SMALL = 0.5f;
    private final static float FONT_MEDIUM = 0.8f;
    private final static int STAT_LABEL_WIDTH = 120;

    Skin skin;
    Stats statsComponent;

    public CharacterWindow() {
        super(ClientGlobals.getPlayerComponent(Player.class).name, UIManager.getInstance().getSkin());
        skin = UIManager.getInstance().getSkin();
        statsComponent = ClientGlobals.getPlayerComponent(Stats.class);

        debug();
        setSize(400, 600);

        Label characterClass = new Label(ClientGlobals.getPlayerComponent(Player.class).getGameClass(), skin);
        characterClass.setFontScale(FONT_SMALL);
        add(characterClass).align(Align.center).height(20).colspan(2).expandX();
        row();

        addLeftStatsTable();
        addRightStatsTable();
        row();

        addEquipmentTable();
        row();

        Label credits = new Label("credits", skin);
        credits.setFontScale(FONT_SMALL);
        add(credits).colspan(2);
    }

    private void addLeftStatsTable() {
        Table leftStatsTable = new Table(skin);
        leftStatsTable.debug();
        leftStatsTable.row().height(20);
        add(leftStatsTable).left();

        Label staminaLabel = new Label("Stamina", skin);
        staminaLabel.setFontScale(FONT_SMALL);
        Label staminaStat = new Label(String.valueOf(statsComponent.stamina), skin);
        staminaStat.setFontScale(FONT_SMALL);
        leftStatsTable.add(staminaLabel).right().width(STAT_LABEL_WIDTH);
        leftStatsTable.add(staminaStat).left().expandX();
        leftStatsTable.row().height(20);

        Label strengthLabel = new Label("Body", skin);
        strengthLabel.setFontScale(FONT_SMALL);
        Label strengthStat = new Label(String.valueOf(statsComponent.body), skin);
        strengthStat.setFontScale(FONT_SMALL);
        leftStatsTable.add(strengthLabel).right().width(STAT_LABEL_WIDTH);
        leftStatsTable.add(strengthStat).left().expandX();
        leftStatsTable.row().height(20);

        Label intellectLabel = new Label("Smarts", skin);
        intellectLabel.setFontScale(FONT_SMALL);
        Label intellectStat = new Label(String.valueOf(statsComponent.smarts), skin);
        intellectStat.setFontScale(FONT_SMALL);
        leftStatsTable.add(intellectLabel).right().width(STAT_LABEL_WIDTH);
        leftStatsTable.add(intellectStat).left().expandX();
    }

    private void addRightStatsTable() {
        Table rightStatsTable = new Table(skin);
        rightStatsTable.debug();
        rightStatsTable.row().height(20);
        add(rightStatsTable).right();

        Label precisionLabel = new Label("Precision", skin);
        precisionLabel.setFontScale(FONT_SMALL);
        Label precisionStat = new Label(String.valueOf(statsComponent.precision), skin);
        precisionStat.setFontScale(FONT_SMALL);
        rightStatsTable.add(precisionStat).right().expandX();
        rightStatsTable.add(precisionLabel).left().width(STAT_LABEL_WIDTH);
        rightStatsTable.row().height(20);

        Label toughnessLabel = new Label("Toughness", skin);
        toughnessLabel.setFontScale(FONT_SMALL);
        Label toughnessStat = new Label(String.valueOf(statsComponent.toughness), skin);
        toughnessStat.setFontScale(FONT_SMALL);
        rightStatsTable.add(toughnessStat).right().expandX();
        rightStatsTable.add(toughnessLabel).left().width(STAT_LABEL_WIDTH);
        rightStatsTable.row().height(20);

        Label willpowerLabel = new Label("Attractiveness", skin);
        willpowerLabel.setFontScale(FONT_SMALL);
        Label willpowerStat = new Label(String.valueOf(statsComponent.attractiveness), skin);
        willpowerStat.setFontScale(FONT_SMALL);
        rightStatsTable.add(willpowerStat).right().expandX();
        rightStatsTable.add(willpowerLabel).left().width(STAT_LABEL_WIDTH);
    }

    private void addEquipmentTable() {
        Table equipmentTable = new Table(skin);
        equipmentTable.debug();
        equipmentTable.setWidth(400);
        add(equipmentTable).colspan(2).expand();

        Label placeholder = new Label("equipment placeholder", skin);
        placeholder.setFontScale(FONT_MEDIUM);
        equipmentTable.add(placeholder).align(Align.center).expand();
    }
}
