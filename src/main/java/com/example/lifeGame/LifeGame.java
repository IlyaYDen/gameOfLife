package com.example.lifeGame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.net.ssl.SSLEngineResult;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LifeGame extends Application {
    public static MainView mainView;
    public static int START = 0;
    static final int SIZE = 80;
    static final int SCREEN_SIZE_X = 800;
    static final int SCREEN_SIZE_Y = 820;

   // private static final Map<Coords, Byte> HashMap = new HashMap<>();
   // static Map<Coords,Byte> gameZone = Collections.synchronizedMap(HashMap);
    static ConcurrentHashMap<Coords,Byte> gameZone = new ConcurrentHashMap<>();
    //private static Map<Coords,Byte> gameZone = new HashMap<>();

    public static final Object lock = 1;
    public static Map<Coords,Byte> getGameZone(){
        //synchronized (lock) {
            return gameZone;
        //}
    }
    public static void setGameZone(Map<Coords,Byte> map){
        //synchronized (lock) {
            gameZone.clear();
            gameZone.putAll(map);
        //}
    }

    public static void addGameZone(Coords add) {
        //synchronized (lock) {
            gameZone.put(add, (byte) 1);
       // }
    }

    public static void addallgameZone(Map<Coords, Byte> newP) {
        //synchronized (lock) {
            gameZone.putAll(newP);
        //}
    }

    public static void removeGameZone(Coords c) {
        //synchronized (lock) {
            gameZone.remove(c);
        //}
    }

    public static void clearGameZone() {
        //synchronized (lock) {
            gameZone.clear();
        //}
    }

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

        Thread t = new Thread(() -> mainView = new MainView(c));
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        GameLogic gl = new GameLogic(mainView);
        gl.start();

        launch();
        /*
        System.out.println();

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        DataFlavor flavor = DataFlavor.stringFlavor;

        if (clipboard.isDataFlavorAvailable(flavor)) {
            try {
                GameParser.gameZoneParser(clipboard.getData(flavor).toString());
            } catch (UnsupportedFlavorException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.exit(1);*/
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