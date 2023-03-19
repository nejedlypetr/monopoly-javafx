package cz.cvut.fel.pvj.nejedly.monopoly.model.die;

import java.util.Random;

public class Die {
    private static final Random DIE = new Random();
    private static int dieOneRoll;
    private static int dieTwoRoll;
    private static final int dieMinValue = 1;
    private static final int dieMaxValue = 6;

    private Die() {}

    public static void roll() {
        dieOneRoll = DIE.nextInt(dieMinValue, dieMaxValue + 1);
        dieTwoRoll = DIE.nextInt(dieMinValue, dieMaxValue + 1);
    }

    public static int getDieOneRoll() {
        return dieOneRoll;
    }

    public static int getDieTwoRoll() {
        return dieTwoRoll;
    }

    public static int getDieRollTotal() {
        return dieOneRoll + dieTwoRoll;
    }

    public static boolean isDoubles() {
        return dieOneRoll == dieTwoRoll;
    }
}
