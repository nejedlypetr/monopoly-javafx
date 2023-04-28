module cz.cvut.fel.pvj.nejedly.monopoly {
    requires javafx.controls;
    requires json.simple;
    exports cz.cvut.fel.pvj.nejedly.monopoly;
    exports cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

    opens images;
    opens sprites;
    opens stylesheets;
}