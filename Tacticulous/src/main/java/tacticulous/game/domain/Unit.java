package tacticulous.game.domain;

/**
 * The brave bits and pixels who doth battle, shed bitblood ,etc.
 *
 * @author O
 */
public class Unit {

    protected int speed;
    protected int defense;
    protected int attack;
    protected int range;
    protected int startHitPoints;
    protected int hitPoints;
    protected String name;
    protected int x;
    protected int y;
    protected boolean notAttacked;
    protected boolean notMoved;
    protected boolean notDelayed;
    protected Player player;
    protected boolean leader = false;

    /**
     * Basic constructor.
     *
     * @param speed how fast unit can move.
     * @param defense defense modifier
     * @param attack attack modifier
     * @param range attack range
     * @param hitPoints how much damage unit can survive
     * @param name name of the unit
     * @param player owner of unit
     */
    public Unit(int speed, int defense, int attack, int range, int hitPoints, String name, Player player) {
        if (speed < 0 || hitPoints < 1) {
            throw new IllegalArgumentException();
        }
        this.speed = speed;
        this.defense = defense;
        this.attack = attack;
        this.startHitPoints = hitPoints;
        this.hitPoints = hitPoints;
        this.name = name;
        this.range = range;
        this.notAttacked = true;
        this.notMoved = true;
        this.notDelayed = true;
        this.player = player;
    }

    /**
     * Shorter constructor for testing purposes.
     *
     * @param speed
     * @param defense
     * @param attack
     * @param hitPoints
     * @param name
     */
    public Unit(int speed, int defense, int attack, int hitPoints, String name) {
        if (speed < 0 || hitPoints < 1) {
            throw new IllegalArgumentException();
        }
        this.speed = speed;
        this.defense = defense;
        this.attack = attack;
        this.startHitPoints = hitPoints;
        this.hitPoints = hitPoints;
        this.name = name;
        this.range = 1;
        this.notAttacked = true;
        this.notMoved = true;
        this.notDelayed = true;
        this.player = null;
    }

    /**
     * Basic constructor for Commander unit in kill commander game type.
     *
     * @param player owner of unit
     */
    public Unit(Player player) {
        this.speed = 4;
        this.defense = 5;
        this.attack = 1;
        this.startHitPoints = 3;
        this.hitPoints = 3;
        this.name = "Commander";
        this.range = 3;
        this.notAttacked = true;
        this.notMoved = true;
        this.notDelayed = true;
        this.player = player;
        this.leader = true;
    }

    public boolean isLeader() {
        return leader;
    }

    /**
     * Sets the unit's location coordinates.
     *
     * @param x vertical coordinate, 0 = top of map
     * @param y horizontal coordinate, 0 = left side of map
     */
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Checks if unit can still move or attack this round.
     *
     * @return true is unit has moved and attacked (or ended turn).
     */
    public boolean doneForTheRound() {
        return !this.notAttacked && !this.notMoved;
    }

    /**
     * Checks if unit dies when hit.
     *
     * @return true if hitpoints are 0 (or less).
     */
    public boolean isHitAndDies() {
        hitPoints--;
        return hitPoints <= 0;
    }

    /**
     * Sets notAttacked to false.
     */
    public void attacks() {
        this.notAttacked = false;
    }

    /**
     * Sets notMoved to false.
     */
    public void moves() {
        this.notMoved = false;
    }

    /**
     * Sets notDelayed to false.
     */
    public void delays() {
        this.notDelayed = false;
    }

    /**
     * Resets notAttacked, notMoved and notDelayed to true.
     */
    public void newRound() {
        this.notAttacked = true;
        this.notMoved = true;
        this.notDelayed = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAttack() {
        return attack;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    public int getRange() {
        return range;
    }

    public boolean hasNotAttacked() {
        return notAttacked;
    }

    public boolean hasNotMoved() {
        return notMoved;
    }

    public boolean hasNotDelayed() {
        return notDelayed;
    }

    /**
     * Surprisingly, returns unit information in a string. Used by unit
     * displays.
     *
     * @return information printed on active unit and target tile displays.
     */
    @Override
    public String toString() {
        return "owner=" + player.getName()
                + "\nname=" + name
                + "\nspeed=" + speed
                + "\ndefense=" + defense
                + "\nattack=" + attack
                + "\nrange=" + range
                + "\nhitpoints=" + hitPoints + " / " + startHitPoints
                + "\nhas not attacked=" + notAttacked
                + "\nhas not moved=" + notMoved
                + "\nhas not delayed=" + notDelayed;
    }

}
