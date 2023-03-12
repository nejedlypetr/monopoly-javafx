package cz.cvut.fel.pvj.nejedly.monopoly.model;

public class Player {
    private final String name;
    private int position;
    private int money;

    public Player(String name) {
        this.name = name;
        position = 0;
        money = 1_500;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void advancePositionBy(int steps) {
        this.position += steps;
    }

    public int getMoney() {
        return money;
    }
}
