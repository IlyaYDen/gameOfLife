package com.example.lifeGame;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Controller {

    public void start(ActionEvent actionEvent, TextField textField) {
        if(!Objects.equals(textField.getText(), "") && Integer.parseInt(textField.getText())!=0){
            GameLogic.time=Integer.parseInt(textField.getText());
        }
        LifeGame.START = 1;
    }

    public void step() {
        LifeGame.START = 2;
    }
    public void stop() {
        LifeGame.START = 0;
    }

    public void random(MainView mainView) {
        short[][] gameZone = new short[LifeGame.SIZE+1][LifeGame.SIZE+1];
        List<int[]> newPoints = new ArrayList<>();

        for(int a = 0; a < gameZone.length; a++)
            for(int b = 0; b < gameZone[a].length; b++) {
                short v = (short) (Math.random() * 2);
                gameZone[a][b] = v;
                newPoints.add(new int[] {a,b,v});
            }
        //mainView.setNewPoints(newPoints);
        //mainView.setGameZone(gameZone);
    }
}
