package cz.cvut.fel.pvj.nejedly.monopoly.model.die;

import java.util.Random;

public class Die {
    private final Random random = new Random();
    private int dieOneRoll;
    private int dieTwoRoll;
    private final int dieMinValue = 1;
    private final int dieMaxValue = 6;

    public void roll() {
        dieOneRoll = random.nextInt(dieMinValue, dieMaxValue + 1);
        dieTwoRoll = random.nextInt(dieMinValue, dieMaxValue + 1);
    }

    public int getDieOneRoll() {
        return dieOneRoll;
    }

    public int getDieTwoRoll() {
        return dieTwoRoll;
    }

    public int getDieRollTotal() {
        return dieOneRoll + dieTwoRoll;
    }

    public boolean isDoubles() {
        return dieOneRoll == dieTwoRoll;
    }
}
