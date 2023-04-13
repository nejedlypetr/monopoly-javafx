package cz.cvut.fel.pvj.nejedly.monopoly.model.die;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.Random;

public class Die {
    private final Random random = new Random();
    private final SimpleIntegerProperty dieOneRoll;
    private final SimpleIntegerProperty dieTwoRoll;
    private final int dieMinValue = 1;
    private final int dieMaxValue = 6;

    public Die() {
        dieOneRoll = new SimpleIntegerProperty(0);
        dieTwoRoll = new SimpleIntegerProperty(0);
    }

    public int roll() {
        dieOneRoll.set(random.nextInt(dieMinValue, dieMaxValue + 1));
        dieTwoRoll.set(random.nextInt(dieMinValue, dieMaxValue + 1));
        return getDieRollTotal();
    }

    public SimpleIntegerProperty getDieOneRoll() {
        return dieOneRoll;
    }

    public SimpleIntegerProperty getDieTwoRoll() {
        return dieTwoRoll;
    }

    public int getDieRollTotal() {
        return dieOneRoll.add(dieTwoRoll).intValue();
    }

    public boolean isDoubles() {
        return dieOneRoll.getValue().equals(dieTwoRoll.getValue());
    }

    @Override
    public String toString() {
        return dieOneRoll.getValue()+" + "+dieTwoRoll.getValue()+" = "+getDieRollTotal();
    }
}
