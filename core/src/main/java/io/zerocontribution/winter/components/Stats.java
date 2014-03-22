package io.zerocontribution.winter.components;

public class Stats extends EntityComponent {

    public int health;
    public int power;

    public int maxHealth;
    public int maxPower;

    public int experience;
    public int maxExperience;
    public int level;
    public int maxLevel;

    public int technicalAbility;
    public int cool;
    public int attractiveness;
    public int body;

    public int kills = 0;

    public Stats() {}
    public Stats(int health, int power, int maxHealth, int maxPower, int experience, int level,
                 int technicalAbility, int cool, int attractiveness, int body) {

        this.health = health > maxHealth ? maxHealth : health;
        this.power = power > maxPower ? maxPower : power;
        this.maxHealth = maxHealth;
        this.maxPower = maxPower;

        this.experience = experience;
        this.level = level;

        this.maxLevel = 20; // TODO
        this.maxExperience = 3000; // TODO

        this.technicalAbility = technicalAbility;
        this.cool = cool;
        this.attractiveness = attractiveness;
        this.body = body;
    }

}
