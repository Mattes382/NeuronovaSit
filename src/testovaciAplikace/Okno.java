/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testovaciAplikace;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import neuronovasit.NeuronovaSit;

/**
 *
 * @author Matej
 * jednoducha neuronova sit ktera dokaze vyresit XOR problem pomoci 1 hidden vrstvy
 */
public class Okno extends Application {

    static int rychlostSimulace = 10000;
    static int sirka = 30;
    static int vyska = 30;
    static int velikostHada = 20;
    static Random random = new Random();
    static AnimationTimer animace;
    static double[][] testovaciPole = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
    static double[][] spravnyVysledek = {{0}, {1}, {1}, {0}};
    Button OR;
    Button AND;
    Button XOR;
    Button NOR;

    static NeuronovaSit nn = new NeuronovaSit(2, 4, 1);

    public void start(Stage primaryStage) {

        try {

            VBox root = new VBox();
            HBox buttons = new HBox();
            Canvas c = new Canvas(sirka * velikostHada, vyska * velikostHada);
            GraphicsContext gc = c.getGraphicsContext2D();
            OR = new Button();
            OR.setStyle("-fx-background-color: red;");
            OR.setTextFill(javafx.scene.paint.Color.WHITE);
            OR.setText("OR");
            OR.setFont(new Font("Forte", 26.0));
            AND = new Button();
            AND.setStyle("-fx-background-color: red;");
            AND.setTextFill(javafx.scene.paint.Color.WHITE);
            AND.setText("AND");
            AND.setFont(new Font("Forte", 26.0));

            XOR = new Button();
            XOR.setStyle("-fx-background-color: red;");
            XOR.setTextFill(javafx.scene.paint.Color.WHITE);
            XOR.setText("XOR");
            XOR.setFont(new Font("Forte", 26.0));

            NOR = new Button();
            NOR.setStyle("-fx-background-color: red;");
            NOR.setTextFill(javafx.scene.paint.Color.WHITE);
            NOR.setText("NOR");
            NOR.setFont(new Font("Forte", 26.0));

            buttons.getChildren().add(OR);
            buttons.getChildren().add(AND);
            buttons.getChildren().add(XOR);
            buttons.getChildren().add(NOR);
            root.getChildren().add(buttons);
            root.getChildren().add(c);

            OR.setOnAction(e -> {
                double[][] novyspravnyVysledek = {{0}, {1}, {1}, {1}};
                spravnyVysledek = novyspravnyVysledek;
            });

            AND.setOnAction(e -> {
                double[][] novyspravnyVysledek = {{0}, {0}, {0}, {1}};
                spravnyVysledek = novyspravnyVysledek;
            });

            XOR.setOnAction(e -> {
                double[][] novyspravnyVysledek = {{0}, {1}, {1}, {0}};
                spravnyVysledek = novyspravnyVysledek;
            });

            NOR.setOnAction(e -> {
                double[][] novyspravnyVysledek = {{1}, {0}, {0}, {0}};
                spravnyVysledek = novyspravnyVysledek;
            });

            animace = new AnimationTimer() {
                long posledniTik = 0;

                public void handle(long now) {
                    if (posledniTik == 0) {
                        posledniTik = now;
                        tik(gc);
                        return;
                    }

                    if (now - posledniTik > 1000000000 / rychlostSimulace) {
                        posledniTik = now;
                        tik(gc);
                    }
                }

            };
            animace.start();

            Scene scene = new Scene(root, sirka * velikostHada, vyska * velikostHada + 47);

            scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
                if (key.getCode() == KeyCode.W || key.getCode() == KeyCode.UP) {
                    nn.setLearningRate(nn.getLearningRate() - 0.1);
                    System.out.println(nn.getLearningRate());
                }
                if (key.getCode() == KeyCode.A || key.getCode() == KeyCode.LEFT) {
                    nn.setLearningRate(nn.getLearningRate() + 0.1);
                    System.out.println(nn.getLearningRate());
                }
            });
            primaryStage.setScene(scene);
            primaryStage.setTitle("XOR");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void tik(GraphicsContext gc) {
        int random = 0;
        for (int i = 0; i < 100; i++) {
//            int random = new Random().nextInt(spravnyVysledek.length);

            nn.trenujMozek(testovaciPole[random], spravnyVysledek[random]);
            if(random < 3){
                random++;
            } else {
                random = 0;
            }
        }
//        nn.mutujNeurony(10);
        double cols = (double) sirka;
        double rows = (double) vyska;
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                double jd = (double) j;
                double ji = (double) i;
                double x1 = ji / cols;
                double x2 = jd / rows;
                double[] inputs = {x1, x2};
                double[] y = nn.aktivujMozek(inputs);

                int barva = (int) Math.round(y[0] * 255);
//                int barva = (int)Math.round(Math.random() *255);                
                gc.setFill(Color.rgb(barva, barva, barva));
                gc.fillRect(i * velikostHada, j * velikostHada, velikostHada, velikostHada);
//
//                gc.setFill(Color.GREEN);
//                gc.setFont(new Font("Calibri", 5));
//                gc.fillText(x1 + "x" +x2, i * velikostHada, j * velikostHada);

            }
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
