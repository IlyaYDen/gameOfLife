package com.example.lifeGame;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.*;

public class Controller {

    public void start(ActionEvent actionEvent, TextField textField) {
        if(!Objects.equals(textField.getText(), "") && Integer.parseInt(textField.getText())!=0){
            GameLogic.time=Integer.parseInt(textField.getText());
        }
        LifeGame.START = LifeGame.START == 0 ? 1 : 0;
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

    public void clear(Selection s) {
        int startx = (int) Math.floor(s.getStartx());
        int starty = (int) Math.floor(s.getStarty());
        int endx = (int) Math.floor(s.getEndx());
        int endy = (int) Math.floor(s.getEndy());
        System.out.println(new Coords(startx,starty));
        System.out.println(new Coords(endx,endy));
        for(int a = Math.min(startx,endx); a <= Math.max(startx,endx); a++)
            for(int b = Math.min(starty,endy); b <= Math.max(starty,endy); b++){
                //if(MainView.newP.containsKey(new Coords(a,b)))
                    MainView.newP.remove(new Coords(a,b));
                if(LifeGame.getGameZone().containsKey(new Coords(a,b)))
                    MainView.remP.add(new Coords(a,b));
            }
    }
    Set<KeyCode> key = new HashSet<>();
    final static KeyCode[] CONTROL_V = new KeyCode[] {KeyCode.CONTROL, KeyCode.V};

    public void keyPressedListener(KeyEvent keyEvent) {
        key.add(keyEvent.getCode());
        System.out.println(keyEvent.getCode() + " pres " + key.size());
        if(key.containsAll(List.of(CONTROL_V))){
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            DataFlavor flavor = DataFlavor.stringFlavor;
            if(clipboard.isDataFlavorAvailable(flavor)){
                try {
                    String str = clipboard.getData(flavor).toString();

                    MainView.insert = GameParser.gameZoneParser(str);

                } catch (UnsupportedFlavorException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else if(keyEvent.getCode().equals(KeyCode.P) && MainView.insert !=null && MainView.insertCoords!=null){
            MainView.insertTrue = true;
            System.out.println("test");
        }
        else if(keyEvent.getCode().equals(KeyCode.S)){
            LifeGame.START = 2;
        }
        //else if(keyEvent.getCode().equals(KeyCode.SPACE)){
        //    System.out.println("test space");
        //    //if(LifeGame.START>0) LifeGame.START = 0;
        //    //else LifeGame.START = 1;
        //}
    }

    public void keyReleasedListener(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode() + " relised " + key.size());
        key.remove(keyEvent.getCode());
    }
}
