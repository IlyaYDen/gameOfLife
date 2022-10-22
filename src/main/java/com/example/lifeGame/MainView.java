package com.example.lifeGame;

import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainView extends VBox {

    private Button buttonStart;
    private Button buttonStep;
    private Button buttonStop;
    private Button buttonClear;
    private TextField textField;
    private Canvas canvas;
    int SIZEX = 16;
    int Screen = LifeGame.SIZE*SIZEX;
    GameLogic gl;
    private boolean[][] gameZone = new boolean[LifeGame.SIZE+1][LifeGame.SIZE+1];
    //private boolean[][] gameZone = new boolean[LifeGame.SIZE+1][LifeGame.SIZE+1];
    private List<Byte[]> newPoints = new ArrayList<>();
    public MainView() {

        this.buttonStart = new Button("Start");
        this.buttonStep = new Button("Step");
        this.buttonStop = new Button("Stop");
        this.buttonClear = new Button("Clear");
        this.textField = new TextField();
        this.canvas = new Canvas(Screen,Screen+20);
        FlowPane ap = new FlowPane(Orientation.HORIZONTAL);
        ap.getChildren().addAll(this.buttonStart,this.textField,this.buttonStep,this.buttonStop,this.buttonClear);

        this.getChildren().addAll(this.canvas,ap);
        draw();
        setOnMouseClicked(this::createPoint);
        setOnDragDetected(mouseEvent -> {System.out.println(mouseEvent.getY());});
        this.buttonStart.setOnAction(this::start);
        this.buttonStep.setOnAction(this::step);
        this.buttonStop.setOnAction(this::stop);
        this.buttonClear.setOnAction(actionEvent -> LifeGame.START = 3);
    }

    private void start(ActionEvent actionEvent) {
        if(!Objects.equals(textField.getText(), "") && Integer.parseInt(textField.getText())!=0){
            GameLogic.time=Integer.parseInt(textField.getText());
        }
        LifeGame.START = 1;
    }
    private void step(ActionEvent actionEvent) {
        LifeGame.START = 2;
    }
    private void stop(ActionEvent actionEvent) {
        LifeGame.START = 0;
    }

    private void createPoint(MouseEvent mouseEvent) {
        System.out.println(mouseEvent.getY());
        if(mouseEvent.getX()<800 & mouseEvent.getY()<824){
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                byte x = (byte) Math.floor(mouseEvent.getX() / SIZEX);
                byte y = (byte) Math.floor((mouseEvent.getY() - 12) / SIZEX);
                gameZone[x][y] = !gameZone[x][y];
                draw();
                newPoints.add(new Byte[] {x,y,((byte) (gameZone[x][y] ? 1 : 0))});
            }
        }
    }

    private void drawPoint(GraphicsContext g,int x, int y) {
        g.setFill(Color.DARKGREY);
        g.fillRect(x*SIZEX,(y-1)*SIZEX+24,SIZEX,SIZEX);
        g.setFill(Color.LIGHTBLUE);
        g.strokeLine(x*SIZEX,(y-1)*SIZEX+24,x*SIZEX,(y-1)*SIZEX+SIZEX+24);
    }


    public void draw() {
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.clearRect(0,0,Screen*2,Screen*2);

        g.setFill(Color.LIGHTBLUE);
        g.fillRect(0,0+24,Screen,Screen+24);
        for(int a = 0; a < gameZone.length; a++)
            for(int b = 0; b < gameZone[a].length; b++){
                if(gameZone[a][b]) drawPoint(g, a, b);
            }
        g.setFill(Color.GRAY);
        for(int a = 0; a < LifeGame.SIZE; a++){
            g.strokeLine(0,a*SIZEX+24,Screen,a*SIZEX+24);
            g.strokeLine(a*SIZEX,0+24,a*SIZEX,Screen+24);
        }
    }

    public void setGameZone(boolean[][] gameZone) {
        this.gameZone = gameZone;
    }
    public void setPoint(int x, int y, boolean a) {
        this.gameZone[x][y] = a;
    }

    public void clearNewPoints(){
        newPoints.clear();
    }

    public List<Byte[]> getNewPoints() {
        return newPoints;
    }

}
