package io.zerocontribution.winter.combat.abilities;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import io.zerocontribution.winter.combat.processors.CombatProcessor;
import io.zerocontribution.winter.components.Position;
import io.zerocontribution.winter.utils.GdxLogHelper;

public abstract class Ability implements Json.Serializable {

    public int id;
    public String name;
    public String description;
    public CombatProcessor processor;
    public int cost;
    public float cooldown;

    abstract public Entity create(World world, Entity source, String group, Position position);

    abstract public CombatProcessor getCombatProcessor();

    public void write(Json json) {
        json.writeValue("id", id);
        json.writeValue("name", name);
        json.writeValue("description", description);
        json.writeValue("processorClass", processor.getClass().getName());
        json.writeValue("cost", cost);
        json.writeValue("cooldown", cooldown);
    }

    public void read(Json json, JsonValue jsonData) {
        id = jsonData.getInt("id");
        name = jsonData.getString("name");
        description = jsonData.getString("description");
        cost = jsonData.getInt("cost");
        cooldown = jsonData.getFloat("cooldown");

        try {
            processor = (CombatProcessor) Class.forName(jsonData.getString("processorClass")).newInstance();
        } catch (ClassNotFoundException e) {
            GdxLogHelper.log("ability", "Processor not found " + jsonData.getString("processorClass"));
            e.printStackTrace();
        } catch (InstantiationException e) {
            GdxLogHelper.log("ability", "Could not initialize processor " + jsonData.getString("processorClass"));
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            GdxLogHelper.log("ability", "Could not access processor " + jsonData.getString("processorClass"));
            e.printStackTrace();
        }
    }

}
