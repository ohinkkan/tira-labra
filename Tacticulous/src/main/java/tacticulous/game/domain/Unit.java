/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tacticulous.game.domain;

import java.util.Random;

/**
 *
 * @author O
 */
public class Unit {

    private int speed;
    private int defense;
    private int attack;
    private int range;
    private int hitPoints;
    private String name;
    private int x;
    private int y;
    private boolean notAttacked;
    private boolean notMoved;
    private Player player;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Unit(int speed, int defense, int attack, int hitPoints, String name) {
        if (speed < 0 || hitPoints < 1) {
            throw new IllegalArgumentException();
        }
        this.speed = speed;
        this.defense = defense;
        this.attack = attack;
        this.hitPoints = hitPoints;
        this.name = name;
        this.range = 1;
        this.notAttacked = true;
        this.notMoved = true;
        this.player = null;
    }

    public Player getPlayer() {
        return player;
    }

    public Unit(int speed, int defense, int attack, int range, int hitPoints, String name, Player player) {
        if (speed < 0 || hitPoints < 1) {
            throw new IllegalArgumentException();
        }
        this.speed = speed;
        this.defense = defense;
        this.attack = attack;
        this.hitPoints = hitPoints;
        this.name = name;
        this.range = range;
        this.notAttacked = true;
        this.notMoved = true;
        this.player = player;
    }

    public int getRange() {
        return range;
    }

    public boolean hasNotAttacked() {
        return notAttacked;
    }

    public void attacks() {
        this.notAttacked = false;
    }

    public boolean hasNotMoved() {
        return notMoved;
    }

    public void moves() {
        this.notMoved = false;
    }

    public void newRound() {
        this.notAttacked = true;
        this.notMoved = true;
    }

    public int getAttack() {
        return attack;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public boolean isHitAndDies() {
        hitPoints--;
        if (hitPoints > 0) {
            return false;
        }
        return true;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public String getNimi() {
        return name;
    }
}
