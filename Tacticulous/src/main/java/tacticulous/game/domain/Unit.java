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
    private int hitPoints;
    private char nimi;

    public Unit(int speed, int defense, int attack, int hitPoints, char nimi) {
        if (speed < 0 || hitPoints < 1) {
            throw new IllegalArgumentException();
        }
        this.speed = speed;
        this.defense = defense;
        this.attack = attack;
        this.hitPoints = hitPoints;
        this.nimi = nimi;
    }

    // returns -1 if attack misses, 0 if it hits, 1 if it kills target
    public int attack(Unit target, int dieRoll) {
        int result = this.attack + dieRoll - target.getDefense();
        if (result > 0) {
            if (target.isHitAndDies()) {
                return 1;
            }
            return 0;
        }
        return -1;
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

    public char getNimi() {
        return nimi;
    }
}
