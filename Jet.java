package projetEphec;

/**
 * Cette classe gère toutes les opérations de graphe de scène hiérarchiques, y compris l'ajout / suppression des nœuds enfants,
 */
import javafx.scene.Parent; 



public class Jet extends Parent{
    public int type; // c'est le length de jet
    public boolean vertical = true;

    private int health; // la vie d'un jet

    public Jet(int type, boolean vertical) {
        this.type = type;
        this.vertical = vertical;
        health = type; // si le jet est de 5 boite alors ca vie est de 5 aussi 

       /* VBox vbox = new VBox();
        for (int i = 0; i < type; i++) {
            Rectangle square = new Rectangle(30, 30);
            square.setFill(null);
            square.setStroke(Color.BLACK);
            vbox.getChildren().add(square);
        }
        getChildren().add(vbox); */
    }
 
    /**
	 * Will Will decrease the health of jet by 1
	 */
    public void hit() {
        health--; 
    }
   /**
    * 
    * @return the health of a Jet 
    */
    public boolean isAlive() {
        return health > 0;
    }
}
