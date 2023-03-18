package cz.cvut.fel.pvj.nejedly.monopoly.model.board;

import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.*;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.CardType;

public class Board {
    private final Square[] boardSquares;

    public Board() {
        boardSquares = new Square[]{
            new Go(0, 200),
            new Property("Mediterranean Avenue", 1, PropertyGroup.BROWN, 2, 60),
            new Cards("Community Chest", 2, CardType.COMMUNITY_CHEST),
            new Property("Baltic Avenue", 3, PropertyGroup.BROWN, 4, 60),
            new Tax("Income Tax", 4, 200),
            new Railroad("Reading Railroad", 5),
            new Property("Oriental Avenue", 6, PropertyGroup.LIGHTBLUE, 6, 100),
            new Cards("Chance", 7, CardType.CHANCE),
            new Property("Vermont Avenue", 8, PropertyGroup.LIGHTBLUE, 6, 100),
            new Property("Connecticut Avenue", 9, PropertyGroup.LIGHTBLUE, 8, 120),
            new Jail("Jail", 10),
            new Property("St. Charles Place", 11, PropertyGroup.PINK, 10, 140),
            new Utility("Electric Company", 12),
            new Property("States Avenue", 13, PropertyGroup.PINK, 10, 140),
            new Property("Virginia Avenue", 14, PropertyGroup.PINK, 12, 160),
            new Railroad("Pennsylvania Railroad", 15),
            new Property("St. James Place", 16, PropertyGroup.ORANGE, 14, 180),
            new Cards("Community Chest", 17, CardType.COMMUNITY_CHEST),
            new Property("Tennessee Avenue", 18, PropertyGroup.ORANGE, 14, 180),
            new Property("New York Avenue", 19, PropertyGroup.ORANGE, 16, 200),
            new FreeParking(20),
            new Property("Kentucky Avenue", 21, PropertyGroup.RED, 18, 220),
            new Cards("Chance", 22, CardType.CHANCE),
            new Property("Indiana Avenue", 23, PropertyGroup.RED, 18, 220),
            new Property("Illinois Avenue", 24, PropertyGroup.RED, 20, 240),
            new Railroad("B. & O. Railroad", 25),
            new Property("Atlantic Avenue", 26, PropertyGroup.YELLOW, 22, 260),
            new Property("Ventnor Avenue", 27, PropertyGroup.YELLOW, 22, 260),
            new Utility("Water Works", 28),
            new Property("Marvin Gardens", 29, PropertyGroup.YELLOW, 24, 280),
            new GoToJail(30, 10),
            new Property("Pacific Avenue", 31, PropertyGroup.GREEN, 26, 300),
            new Property("North Carolina Avenue", 32, PropertyGroup.GREEN, 26, 300),
            new Cards("Community Chest", 33, CardType.COMMUNITY_CHEST),
            new Property("Pennsylvania Avenue", 34, PropertyGroup.GREEN, 28, 320),
            new Railroad("Short Line", 35),
            new Cards("Chance", 36, CardType.CHANCE),
            new Property("Park Place", 37, PropertyGroup.BLUE, 35, 350),
            new Tax("Luxury Tax", 38, 100),
            new Property("Boardwalk", 39, PropertyGroup.BLUE, 50, 400)
        };
    }
}
