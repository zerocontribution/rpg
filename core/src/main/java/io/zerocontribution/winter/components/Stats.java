package io.zerocontribution.winter.components;

import io.zerocontribution.winter.assets.EnemyAsset;

import java.util.Map;

public class Stats extends EntityComponent {

    public int health = 100;
    public int power = 100;

    public int maxHealth = 100;
    public int maxPower = 100;

    // TODO: Remove experience & level system?
    public int experience = 0;
    public int maxExperience = 3000;
    public int level = 1;
    public int maxLevel = 20;

    public int stamina = 10;
    public int smarts = 10;
    public int precision = 10;
    public int toughness = 10;
    public int attractiveness = 10;
    public int body = 10;

    public int kills = 0;

    public static Stats buildFromAsset(EnemyAsset asset) {
        Stats stats = new Stats();

        stats.health = applyStatValue(asset.baseStats, "health", 100);
        stats.power = applyStatValue(asset.baseStats, "power", 100);
        stats.experience = applyStatValue(asset.baseStats, "experience", 0);
        stats.level = applyStatValue(asset.baseStats, "level", 1);
        stats.stamina = applyStatValue(asset.baseStats, "stamina", 10);
        stats.smarts = applyStatValue(asset.baseStats, "smarts", 10);
        stats.precision = applyStatValue(asset.baseStats, "precision", 10);
        stats.toughness = applyStatValue(asset.baseStats, "toughness", 10);
        stats.attractiveness = applyStatValue(asset.baseStats, "attractiveness", 10);
        stats.body = applyStatValue(asset.baseStats, "body", 10);

        return stats;
    }

    private static int applyStatValue(Map<String, Integer> stats, String key, int defaultValue) {
        return stats.get(key) == null ? defaultValue : stats.get(key);
    }

    public Stats() {}

    public void levelUp() {
        level++;
        experience = experience - maxExperience;
        maxExperience *= 0.1;

        attractiveness += 1;
        body += 1;

        maxHealth *= 0.1;
        maxPower *= 0.1;

        health = maxHealth;
        power = maxPower;
    }

}
