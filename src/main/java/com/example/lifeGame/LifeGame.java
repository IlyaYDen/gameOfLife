package com.example.lifeGame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.net.ssl.SSLEngineResult;
import java.util.HashMap;
import java.util.Map;

public class LifeGame extends Application {
    public static MainView mainView;
    public static int START = 0;
    static final int SIZE = 80;
    static final int SCREEN_SIZE_X = 800;
    static final int SCREEN_SIZE_Y = 820;

    static Map<Coords,Byte> gameZone = new HashMap<>();
    @Override
    public void start(Stage stage) {

        StackPane pane = new StackPane();
        pane.setAlignment(Pos.BOTTOM_CENTER);
        pane.getChildren().add(mainView);
        Scene scene = new Scene(pane,SCREEN_SIZE_X,SCREEN_SIZE_Y);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        stage.setOnCloseRequest(windowEvent -> {
            stage.close();
            });


        //mainView.draw();

    }

    public static void main(String[] args) {

        Controller c = new Controller();

        mainView = new MainView(c);
        GameLogic gl = new GameLogic(mainView);
        gl.start();

        launch();
    }

}

/*

        AnimationTimer animator = new AnimationTimer(){

            @Override
            public void handle(long arg0){

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // UPDATE
                ballX += xSpeed;

                if (ballX + ballRadius >= WIDTH)
                {
                    ballX = WIDTH - ballRadius;
                    xSpeed *= -1;
                }
                else if (ballX - ballRadius < 0)
                {
                    ballX = 0 + ballRadius;
                    xSpeed *= -1;
                }

                // RENDER
                r.setX(ballX);
            }
        };
        animator.start();
 */