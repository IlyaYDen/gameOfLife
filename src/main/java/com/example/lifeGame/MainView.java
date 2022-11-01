package com.example.lifeGame;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Rectangle;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainView extends VBox {

    private final TextField textField;
    private final Canvas canvas;
    private Pane p;
    public static int SIZEX = 16;
    int Screen = LifeGame.SCREEN_SIZE_X; //LifeGame.SIZE*SIZEX;
    int worldSize = LifeGame.SIZE*SIZEX;
    GameLogic gl;
    //private  short[][] gameZone = new short[LifeGame.SIZE+1][LifeGame.SIZE+1];
    //private boolean[][] gameZone = new boolean[LifeGame.SIZE+1][LifeGame.SIZE+1];
    private List<Coords> newPoints = new ArrayList<>();
    public static Map<Coords, Byte> insert = null;
    public static Coords insertCoords = null;
    public static boolean insertTrue = false;
    public MainView(Controller c) {

        Button buttonStart = new Button("Start");
        Button buttonStep = new Button("Step");
        Button buttonStop = new Button("Stop");
        Button buttonClear = new Button("Clear");
        Button buttonRandom = new Button("Random");
        this.textField = new TextField();
        this.canvas = new Canvas(Screen,Screen+24);
        FlowPane ap = new FlowPane(Orientation.HORIZONTAL);
        //p = new Pane();
        //p.getChildren().add(new Rectangle(1,1));
        //p.getChildren().add(new Rectangle(Screen,Screen+24,1,1));

        ap.getChildren().addAll(buttonStart,this.textField, buttonStep, buttonStop, buttonClear, buttonRandom);
        this.getChildren().addAll(this.canvas,ap);
        //this.getChildren().addAll(p,ap);
        //draw();
        setOnMouseClicked(this::createPoint);
        setOnMouseDragged(this::dragScreen);
        setOnScroll(this::zoom);
        setOnKeyReleased(keyEvent -> c.keyReleasedListener(keyEvent));
        setOnKeyPressed(keyEvent -> c.keyPressedListener(keyEvent));

        buttonStart.setOnAction(actionEvent -> c.start(actionEvent, textField));
        buttonStep.setOnAction(actionEvent -> c.step());
        buttonStop.setOnAction(actionEvent -> c.stop());
        buttonClear.setOnAction(actionEvent -> c.clear(s));
        buttonRandom.setOnAction(actionEvent -> c.random(this));


        AnimationTimer frameRateMeter = new AnimationTimer() {
            @Override
            public void handle(long l) {
                draw();
                System.out.println("test " + Thread.currentThread().getName());
            }
        };

        frameRateMeter.start();
        //Timer timer = new Timer();
        //timer.scheduleAtFixedRate(new TimerTask() {
        //    @Override
        //    public void run() {
        //        draw();
        //    }
        //},0,1000/20);
    }
    double mouseXBefZoom = 0;
    double mouseYBefZoom = 0;
    private void zoom(ScrollEvent scrollEvent) {
        tp.execute(() -> {

            System.out.println("testfff " + scrollEvent.getDeltaY());

            double[] fclear1 = screenToWorld(scrollEvent.getX(), scrollEvent.getY());



            double deltaY = scrollEvent.getDeltaY();
            mouseXBefZoom = scrollEvent.getX();
            mouseYBefZoom = scrollEvent.getX();

            if(deltaY>0){
                fScaleX*=1.05f;
                fScaleY*=1.05f;

                double[] fclear2 = screenToWorld(scrollEvent.getX(), scrollEvent.getY());
                fOffsetX += (fclear1[0] - fclear2[0]);
                fOffsetY += (fclear1[1] - fclear2[1]);

            }
            else if(deltaY<0) {
                fScaleX*=0.95f;
                fScaleY*=0.95f;
                double[] fclear2 = screenToWorld(scrollEvent.getX(), scrollEvent.getY());
                fOffsetX += (fclear1[0] - fclear2[0]);
                fOffsetY += (fclear1[1] - fclear2[1]);
            }
        });
        //draw();
    }
    /*

        System.out.println("testfff " + scrollEvent.getDeltaY());

        double[] fclear1 = worldToScreen(scrollEvent.getX(), scrollEvent.getY());



        double deltaY = scrollEvent.getDeltaY();
        mouseXBefZoom = scrollEvent.getX();
        mouseYBefZoom = scrollEvent.getX();
        if(deltaY>0){
            fScaleX*=1.05f;
            fScaleY*=1.05f;

            double[] fclear2 = worldToScreen(scrollEvent.getX(), scrollEvent.getY());
            dragScreen(fclear1[0],fclear1[1],fclear2[0],fclear2[1]);

        }
        else if(deltaY<0) {
            fScaleX*=0.95f;
            fScaleY*=0.95f;
            double[] fclear2 = worldToScreen(scrollEvent.getX(), scrollEvent.getY());
            dragScreen(fclear1[0],fclear1[1],fclear2[0],fclear2[1]);
        }
     */

    int timer = 0;
    boolean drag = false;



    double fScaleX = 1.0f;
    double fScaleY = 1.0f;

    double fOffsetX = 0.0f;
    double fOffsetY = 0.0f;

    double fStartPanX = 0.0f;
    double fStartPanY = 0.0f;
    boolean selectionStart = true;
    Selection s;
    ExecutorService tp = Executors.newScheduledThreadPool(3);


    private void dragScreen(MouseEvent mouseEvent) {

        tp.execute( () -> {
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                timer++;
                drag = true;

                System.out.println("test2 " + Thread.currentThread().getName());
                if (timer == 1) {

                    fStartPanX = mouseEvent.getX();
                    fStartPanY = mouseEvent.getY();
                    return;
                }

                fOffsetX -= (mouseEvent.getX() - fStartPanX) / fScaleX;
                fOffsetY -= (mouseEvent.getY() - fStartPanY) / fScaleY;

                fStartPanX = mouseEvent.getX();
                fStartPanY = mouseEvent.getY();

                //System.out.println(fOffsetX + " - " + fOffsetY);
                //System.out.println(fStartPanX + " " + fStartPanY);

                //if (timer > 5) {
                //draw();
                //    timer = 0;
                //}
            } else {

                if(selectionStart) {
                    System.out.println("test   ");
                    s = new Selection();


                    double[] fclear2 = screenToWorld(mouseEvent.getX(), mouseEvent.getY());

                    s.setStartx((int) Math.floor(fclear2[0] / SIZEX));
                    s.setStarty((int) Math.floor((fclear2[1] - SIZEX/2) / SIZEX));


                    selectionStart = false;
                }
                //createPoint(mouseEvent.getX(),mouseEvent.getY());

                double[] fclear2 = screenToWorld(mouseEvent.getX(), mouseEvent.getY());

                s.setEndx((int) Math.floor(fclear2[0] / SIZEX));
                s.setEndy((int) Math.floor((fclear2[1] - SIZEX/2) / SIZEX));
                //s.setEndx(mouseEvent.getX()/SIZEX);
                //s.setEndx(mouseEvent.getY()/SIZEX);
            }
        });

    }
    private void createPoint(MouseEvent mouseEvent) {
        if(drag){
            timer=0;
            drag=false;
            return;
        }
        selectionStart = true;
        System.out.println(mouseEvent.getY());
            if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                double[] fclear2 = screenToWorld(mouseEvent.getX(), mouseEvent.getY());
                System.out.println(mouseEvent.getX() + " " + mouseEvent.getY());
                System.out.println(fclear2[0] + " " + fclear2[1]);

                int x = (int) Math.floor(fclear2[0] / SIZEX);
                int y = (int) Math.floor((fclear2[1] - SIZEX/2) / SIZEX);
                //if(x>LifeGame.SIZE || y>LifeGame.SIZE || x<0 || y<0) return;
                //gameZone[x][y] = (gameZone[x][y]>0) ? (byte) 0:(byte) 1;

                if(LifeGame.getGameZone().containsKey(new Coords(x,y)))
                    remP.add(new Coords(x,y));

                else {
                    newP.put(new Coords(x,y), (byte) 1);
                    drawPoint(this.canvas.getGraphicsContext2D(),new Coords(x,y), (byte) 1);
                }
                //newPoints.add(new int[] {x,y,((byte) (gameZone[x][y]>0 ? 1 : 0))});//*/
                //LifeGame.gameZone.put();

        }
            else if(mouseEvent.getButton().equals(MouseButton.SECONDARY) && insert!=null){

                double[] fclear2 = screenToWorld(mouseEvent.getX(), mouseEvent.getY());
                int x = (int) Math.floor(fclear2[0] / SIZEX);
                int y = (int) Math.floor((fclear2[1] - SIZEX/2) / SIZEX);
                insertCoords = new Coords(x,y);
            }
    }
    public static Map<Coords,Byte> newP = new HashMap<>();
    public static Set<Coords> remP = new HashSet<>();

    int fStartCreateX = 0;
    int fStartCreateY = 0;
    private void createPoint(double xf, double yf) {
        if(drag){
            timer=0;
            drag=false;
            return;
        }

        double[] fclear2 = screenToWorld(xf,yf);
        int x = (int) Math.floor(fclear2[0] / SIZEX);
        int y = (int) Math.floor((fclear2[1] - 12) / SIZEX);
        //if(x>LifeGame.SIZE || y>LifeGame.SIZE || x<0 || y<0) return;
        if(fStartCreateX != x || fStartCreateY != y) {

            fStartCreateX = x;
            fStartCreateY = y;
            //gameZone[x][y] = (gameZone[x][y]>0) ? (byte) 0:(byte) 1;

            //newP.put(new Coords(x,y),
            //        (byte) (LifeGame.gameZone.containsKey(new Coords(x,y)) && LifeGame.gameZone.get(new Coords(x,y))>0 ? 0 : 1));

            if(LifeGame.getGameZone().containsKey(new Coords(x,y)))
                remP.add(new Coords(x,y));
            else {
                newP.put(new Coords(x,y), (byte) 1);
                drawPoint(this.canvas.getGraphicsContext2D(),new Coords(x,y), (byte) 1);
            }
            draw();

            //newPoints.add(new int[] {x,y,((byte) (gameZone[x][y]>0 ? 1 : 0))});
        }
    }


    private void drawPoint(GraphicsContext g,Coords c, byte a) {

        int x = c.getx();
        int y = c.gety();

        g.setFill(Color.rgb(a, a, a));

        double[] fclear1 = worldToScreen(x*SIZEX,(y-1)*SIZEX+24);
        double[] fclear2 = worldToScreen(SIZEX,SIZEX);
        g.fillRect(fclear1[0],fclear1[1],SIZEX*fScaleX,SIZEX*fScaleY);
        g.setFill(Color.LIGHTBLUE);


        fclear1 = worldToScreen(x*SIZEX,(y-1)*SIZEX+24);
        fclear2 = worldToScreen(x*SIZEX,(y-1)*SIZEX+SIZEX+24);
        g.strokeLine(fclear1[0],fclear1[1],fclear2[0],fclear2[1]);
        //g.strokeLine(x*SIZEX,(y-1)*SIZEX+24,x*SIZEX,(y-1)*SIZEX+SIZEX+24);
    }

    private void drawPoint(GraphicsContext g,Coords c, Color a) {

        int x = c.getx();
        int y = c.gety();

        g.setFill(a);

        double[] fclear1 = worldToScreen(x*SIZEX,(y-1)*SIZEX+24);
        double[] fclear2 = worldToScreen(SIZEX,SIZEX);
        g.fillRect(fclear1[0],fclear1[1],SIZEX*fScaleX,SIZEX*fScaleY);
        g.setFill(Color.LIGHTBLUE);


        fclear1 = worldToScreen(x*SIZEX,(y-1)*SIZEX+24);
        fclear2 = worldToScreen(x*SIZEX,(y-1)*SIZEX+SIZEX+24);
        g.strokeLine(fclear1[0],fclear1[1],fclear2[0],fclear2[1]);
        //g.strokeLine(x*SIZEX,(y-1)*SIZEX+24,x*SIZEX,(y-1)*SIZEX+SIZEX+24);
    }
    Object lock = new Object();
    public void draw() {


        /*synchronized (lock) {
            List<Rectangle> rectangles = new ArrayList<>();
            rectangles.add(new Rectangle(10, 10, 1, 1));
            for (int f = 0; f < 500; f++)
                for (int ff = 0; ff < 500; ff++)
                    rectangles.add(new Rectangle(f, ff, 1, 1));
            p.getChildren().addAll(rectangles);
        }*/

        //GraphicsContext ga = this.canvas.getGraphicsContext2D();
        //ga.setFill(Color.LIGHTBLUE);
        //ga.fillPolygon(new double[] {1,200,200},new double[] {1,1,200},3);

        //if(false)
        synchronized (lock) {
            GraphicsContext g = this.canvas.getGraphicsContext2D();
            //g.set
            g.clearRect(0, 0, Screen, Screen);
            g.setFill(Color.LIGHTBLUE);
            g.fillRect(0, 0, 2 * Screen, Screen * 2);Set<Coords> coords;
            synchronized (LifeGame.lock) {
                 coords = new HashSet<>(LifeGame.getGameZone().keySet());
            }
            for (Coords c : coords) {
                //System.out.println(c.toString());
                //System.out.println(LifeGame.gameZone.get(c));
                if (remP.contains(c)) continue;
                drawPoint(g, c, (byte) 1);//LifeGame.getGameZone().get(c));
            }
            if (!newP.isEmpty())
                for (Coords c : newP.keySet()) {
                    //System.out.println(c.toString());
                    //System.out.println(LifeGame.gameZone.get(c));
                    if (remP.contains(c)) continue;
                    drawPoint(g, c, (byte) 1);
                }

            if (s != null) {
                g.setFill(Color.ORANGE);
                g.setGlobalAlpha(0.4);
                if (s.getEndx() >= s.getStartx() && s.getEndy() >= s.getStarty()) {
                    double[] fclear1 = worldToScreen(s.getStartx() * SIZEX, (s.getStarty() - 1) * SIZEX + 24);
                    g.fillRect(fclear1[0], fclear1[1], SIZEX * fScaleX * (s.getEndx() - s.getStartx() + 1), SIZEX * fScaleY * (s.getEndy() - s.getStarty() + 1));
                } else if (s.getEndx() <= s.getStartx() && s.getEndy() <= s.getStarty()) {
                    double[] fclear1 = worldToScreen(s.getEndx() * SIZEX, (s.getEndy() - 1) * SIZEX + 24);
                    g.fillRect(fclear1[0], fclear1[1], SIZEX * fScaleX * (-s.getEndx() + s.getStartx() + 1), SIZEX * fScaleY * (-s.getEndy() + s.getStarty() + 1));
                } else if (s.getEndx() <= s.getStartx() && s.getEndy() >= s.getStarty()) {
                    double[] fclear1 = worldToScreen(s.getEndx() * SIZEX, (s.getStarty() - 1) * SIZEX + 24);
                    g.fillRect(fclear1[0], fclear1[1], SIZEX * fScaleX * (-s.getEndx() + s.getStartx() + 1), SIZEX * fScaleY * (s.getEndy() - s.getStarty() + 1));
                } else if (s.getEndx() >= s.getStartx() && s.getEndy() <= s.getStarty()) {
                    double[] fclear1 = worldToScreen(s.getStartx() * SIZEX, (s.getEndy() - 1) * SIZEX + 24);
                    g.fillRect(fclear1[0], fclear1[1], SIZEX * fScaleX * (+s.getEndx() - s.getStartx() + 1), SIZEX * fScaleY * (-s.getEndy() + s.getStarty() + 1));
                }

                g.setFill(Color.GREEN);
                double[] fclear1 = worldToScreen(s.getStartx() * SIZEX, (s.getStarty()) * SIZEX + SIZEX / 2);
                g.fillRect(fclear1[0], fclear1[1], SIZEX * fScaleX, SIZEX * fScaleY);
                g.setFill(Color.RED);
                double[] fclear2 = worldToScreen(s.getEndx() * SIZEX, (s.getEndy()) * SIZEX + SIZEX / 2);
                g.fillRect(fclear2[0], fclear2[1], SIZEX * fScaleX, SIZEX * fScaleY);
                //g.fillRect(fclear2[0],fclear2[1],SIZEX*fScaleX,SIZEX*fScaleY);
            }

            g.setGlobalAlpha(1);
            if (insertCoords != null && insert != null) {
                for (Coords c : insert.keySet()) {
                    drawPoint(g, Coords.add(c, insertCoords), Color.AQUA);
                }
            }
            //System.out.println(fScaleX + " SCALE");

            g.setStroke(Color.rgb(0, 0, 0));
            g.setLineWidth(.2);

            if(!(fScaleX<0.1)) {
                for (int a = 0; a < ((Screen + SIZEX) / fScaleX); a += SIZEX)
                    g.strokeLine((((-fOffsetX) % (SIZEX)) + a) * fScaleX, 0, (((-fOffsetX) % (SIZEX)) + a) * fScaleX, Screen + 25);
                for (int a = 0; a < ((Screen) / fScaleY); a += SIZEX)
                    g.strokeLine(0, (((-fOffsetY) % (SIZEX)) + a + SIZEX / 2) * fScaleY, Screen + 25, (((-fOffsetY) % (SIZEX)) + a + SIZEX / 2) * fScaleY);

            }
            ;
            //for (int a = 0; a < ((Screen + SIZEX) / fScaleX); a += SIZEX)
            //    g.strokeLine((((-fOffsetX) % (SIZEX)) + a) * fScaleX, 0, (((-fOffsetX) % (SIZEX)) + a) * fScaleX, Screen + 25);
            //for (int a = 0; a < ((Screen) / fScaleY); a += SIZEX)
            //    g.strokeLine(0, (((-fOffsetY) % (SIZEX)) + a + SIZEX / 2) * fScaleY, Screen + 25, (((-fOffsetY) % (SIZEX)) + a + SIZEX / 2) * fScaleY);

            /*


            g.strokeLine(((-fOffsetX*fScaleX)%(SIZEX*fScaleX))+a,50,((-fOffsetX*fScaleX)%(SIZEX*fScaleX))+a,Screen+14);
        for(int a = 0; a < Screen*fScaleY; a+=SIZEX)
            g.strokeLine(50,((-fOffsetY*fScaleY)%(SIZEX*fScaleY))+a+SIZEX/2,Screen+14,((-fOffsetY*fScaleY)%(SIZEX*fScaleY))+a+SIZEX/2);




        g.strokeLine(-fOffsetX*fScaleX,50,-fOffsetX*fScaleX,Screen+14);
        g.strokeLine(10,-fOffsetY*fScaleY+ (SIZEX / 2.),Screen-14,-fOffsetY*fScaleY+ (SIZEX / 2.));


            double[] f1_1 = worldToScreen(0, SIZEX / 2);
            double[] f1_2 = worldToScreen(worldSize + SIZEX, SIZEX / 2);
            g.strokeLine(f1_1[0], f1_1[1], f1_2[0], f1_2[1]);

            double[] f2_1 = worldToScreen(worldSize + SIZEX, SIZEX / 2);
            double[] f2_2 = worldToScreen(worldSize + SIZEX, worldSize + 24);
            g.strokeLine(f2_1[0], f2_1[1], f2_2[0], f2_2[1]);


            for (int a = 0; a < LifeGame.SIZE + 1; a++) {

                f1_1 = worldToScreen(0, a * SIZEX + 24);
                f1_2 = worldToScreen(worldSize + SIZEX, a * SIZEX + 24);
                g.strokeLine(f1_1[0], f1_1[1], f1_2[0], f1_2[1]);

                f2_1 = worldToScreen(a * SIZEX, SIZEX / 2);
                f2_2 = worldToScreen(a * SIZEX, worldSize + 24);
                g.strokeLine(f2_1[0], f2_1[1], f2_2[0], f2_2[1]);
            }*/
        }
    }
    public double[] worldToScreen(double fWorldX, double fWorldY){
        double[] r = new double[2];
        r[0] = (int)(((fWorldX-fOffsetX))*fScaleX);
        r[1] = (int)(((fWorldY-fOffsetY))*fScaleY);
        return r;
    }
    public double[] screenToWorld(double nScreenX, double nScreenY){
        double[] r = new double[2];
        r[0] = (double)((nScreenX / fScaleX +fOffsetX));
        r[1] = (double)((nScreenY / fScaleY +fOffsetY));
        return r;

    }
    public void clearNewPoints(){
        newPoints.clear();
    }
    /*
    public void setGameZone(short[][] gameZone) {
        this.gameZone = gameZone;
    }

    public List<int[]> getNewPoints() {
        return newPoints;
    }

    public void setNewPoints(List<int[]> a) {
        newPoints = a;
    }*/

}
