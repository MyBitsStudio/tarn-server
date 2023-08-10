package com.ruse.world.content.tbdminigame;

import com.ruse.model.GameObject;
import com.ruse.world.content.CustomObjects;

public class EvilTree {

    private GameObject gameObject;
    private final String name;
    private final int maxHealth;
    private int currentHealth;
    private boolean isDead;
    private final int index;

    public EvilTree(GameObject gameObject, String name, int maxHealth, int index) {
        this.gameObject = gameObject;
        this.name = name;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.index = index;
    }

    public void decrementHealth(int amount) {
        currentHealth -= amount;
    }

    public void die() {
        setDead(true);
        CustomObjects.deleteGlobalObject(gameObject);
    }

    public void spawn() {
        CustomObjects.spawnGlobalObject(gameObject);
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
