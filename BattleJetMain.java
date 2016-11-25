package projetEphec;


import java.util.Random;

import projetEphec.Board.Cell;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BattleJetMain extends Application {

    private boolean running = false;  //apres avoir placer tous les jets
    private Board enemyBoard, playerBoard;

    private int jetsToPlace = 5; //le nombre des jets qui tu dois le placer 

    private boolean enemyTurn = false;

    private Random random = new Random();

    private Parent createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(600, 800);

        root.setRight(new Text("RIGHT SIDEBAR - CONTROLS"));

        enemyBoard = new Board(true, event -> {
            if (!running)
                return;

            Cell cell = (Cell) event.getSource();
            if (cell.wasShot)   // si le cellule a ete frappe 
                return;

            enemyTurn = !cell.shoot(); // un tour pour l'ordinateur seulement 
                                       // si le shot est true et puis ! le metre en false "comme le pc a rate donc c'est notre tour mtn"

            if (enemyBoard.jets == 0) { // si il reste rien comme jets por l'enemy
                System.out.println("YOU WIN");
                System.exit(0);  // pour sortir mais on peut demander "Voulez vous jouer encore  ?"
            }

            if (enemyTurn)
                enemyMove();
        });

        playerBoard = new Board(false, event -> {  // false ca veut dire que c'est pas emeny Board
            if (running)
                return;

            Cell cell = (Cell) event.getSource();
            if (playerBoard.placeJet(new Jet(jetsToPlace, event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {
                if (--jetsToPlace == 0) {  // si on place le jet alors mtn on tire un de stock et quand ce sera 0 alors on peut demarre le jeu
                    startGame();
                }
            }
        });

        VBox vbox = new VBox(50, enemyBoard, playerBoard); // l'espace entre les deux fenetre
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        return root;
    }

    private void enemyMove() {
        while (enemyTurn) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Cell cell = playerBoard.getCell(x, y);
            if (cell.wasShot)
                continue;

            enemyTurn = cell.shoot();  // si le pc a tire sur mon jet donc il peut jouer encore 

            if (playerBoard.jets == 0) {
                System.out.println("YOU LOSE");
                System.exit(0);
            }
        }
    }

    private void startGame() {
        // place enemy Jets
        int type = 5;

        while (type > 0) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if (enemyBoard.placeJet(new Jet(type, Math.random() < 0.5), x, y)) {
                type--;
            }
        }

        running = true;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Battleship");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // bloquer l'utilisateur de changer le size de la fenetere 
        primaryStage.show();
    }
    /*
     * la methode qui va lancer l'application 
     */
    public static void main(String[] args) {
        launch(args);
    }
}
