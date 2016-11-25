package projetEphec;

import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Board extends Parent {
    private VBox rows = new VBox();
    private boolean enemy = false; //c'est les lignes dans les deux boits
    public int jets = 5; // c'est le nombre de jet

    /**
     * C'est un Constructeur qui gere le tapant de la souris
     * @param enemy
     * @param handler
     */
    public Board(boolean enemy, EventHandler<? super MouseEvent> handler) {
        this.enemy = enemy;
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) {
                Cell c = new Cell(x, y, this);
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }

            rows.getChildren().add(row);
        }

        getChildren().add(rows);
    }
    /**
     * C'est une method qui va placer le bateau dans le box si oui il renvoie true sinon false
     * @param ship
     * @param x
     * @param y
     * @return
     */
    public boolean placeJet(Jet jet, int x, int y) {
        if (canPlaceJet(jet, x, y)) {   // si on reussi a placer le jet alors on prend le length de jet
            int length = jet.type;

            if (jet.vertical) { // si c'est vertical alors on verifie le Y values
                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(x, i); //on a 10 cellule
                    cell.jet = jet;
                    if (!enemy) { // si c'est pas l'enemie alors on va colorer notre box 
                        cell.setFill(Color.WHITE);
                        cell.setStroke(Color.GREEN);
                    }
                }
            }
            else {
                for (int i = x; i < x + length; i++) {
                    Cell cell = getCell(i, y);
                    cell.jet = jet;
                    if (!enemy) {
                        cell.setFill(Color.WHITE);
                        cell.setStroke(Color.GREEN);
                    }
                }
            }

            return true;
        }

        return false;
    }

    public Cell getCell(int x, int y) {
        return (Cell)((HBox)rows.getChildren().get(y)).getChildren().get(x);
    }

    private Cell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[] {
                new Point2D(x - 1, y),    // des cellule ( 00 et 10 et 01 )
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };

        List<Cell> neighbors = new ArrayList<Cell>(); // on cree une list 

        for (Point2D p : points) {  // pour les quatres points qu'on a mis dans la list Array ' points ' 
            if (isValidPoint(p)) {   // on verifie si c'est valider alors on rajoute des point
                neighbors.add(getCell((int)p.getX(), (int)p.getY()));
            }
        }

        return neighbors.toArray(new Cell[0]);
    }

    private boolean canPlaceJet(Jet jet, int x, int y) {
        int length = jet.type;

        if (jet.vertical) {         // si tu veux mettre les jets en vertical alors on va verifier les Y values
            for (int i = y; i < y + length; i++) {
                if (!isValidPoint(x, i)) //si c'est pas valider alors on renvoie False (on ne peut pas la placer ici)
                    return false;

                Cell cell = getCell(x, i);
                if (cell.jet != null) // si il y en a deja une partie de jet ssur un cellule alors on ne peut pas placer un deuxieme jet sur 
                	                   // une cellue qui a ete deja utilise (Verification)
                    return false;

                for (Cell neighbor : getNeighbors(x, i)) { // pour eviter de ne pas placer des jet un à cote de l'autre
                    if (!isValidPoint(x, i))
                        return false;    

                    if (neighbor.jet != null)
                        return false;
                }
            }
        }
        else {
            for (int i = x; i < x + length; i++) {
                if (!isValidPoint(i, y))
                    return false;

                Cell cell = getCell(i, y);
                if (cell.jet != null)
                    return false;

                for (Cell neighbor : getNeighbors(i, y)) {
                    if (!isValidPoint(i, y))
                        return false;

                    if (neighbor.jet != null)
                        return false;
                }
            }
        }

        return true;
    }

    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    public class Cell extends Rectangle {
        public int x, y;
        public Jet jet = null; // si la cellule n'a pas une partie de jet alors ca va etre null
        public boolean wasShot = false; // si on a pas touche le jet alors false (raté)
        private Board board;
        /**
         * C'est le constructeur 
         * @param x
         * @param y
         * @param board
         */
        public Cell(int x, int y, Board board) {
            super(30, 30); // c'est le width and height
            this.x = x;
            this.y = y;
            this.board = board;
            setFill(Color.LIGHTGRAY);
            setStroke(Color.BLACK);
        }
        /**
         * C'est la methode 
         * pour voir si le bateau a été touche on va le mentionne en rouge 
         * si le bateau est mort on renvoie ture sinon false
         * @return
         */
        public boolean shoot() {
            wasShot = true;
            setFill(Color.BLUE);

            if (jet != null) { // si le jet a été toucheé et on le color en rouge 
                jet.hit();
                setFill(Color.RED);
                if (!jet.isAlive()) { // si le jet est mort 
                    board.jets--;
                }
                return true;
            }

            return false;
        }
    }
}
