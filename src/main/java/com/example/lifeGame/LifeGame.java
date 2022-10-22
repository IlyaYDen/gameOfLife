package com.example.lifeGame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LifeGame extends Application {

    public static int START = 0;
    static final int SIZE = 50;
    @Override
    public void start(Stage stage) {

        MainView mainView = new MainView();
        GameLogic gl = new GameLogic(mainView);
        gl.start();

        StackPane pane = new StackPane();
        pane.setAlignment(Pos.BOTTOM_CENTER);
        pane.getChildren().add(mainView);
        Scene scene = new Scene(pane,800,820);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);


        //mainView.draw();

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