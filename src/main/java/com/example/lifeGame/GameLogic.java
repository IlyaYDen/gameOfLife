package com.example.lifeGame;

import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.*;

public class GameLogic extends TimerTask {

    static int time = 10;
    int timer = 0;
    MainView mainView;
    //private short[][] gameZone = new short[LifeGame.SIZE+1][LifeGame.SIZE+1];
    ExecutorService tp = Executors.newScheduledThreadPool(10);


    public GameLogic(MainView mainView) {
        this.mainView = mainView;
    }



    final Object lock1 = 1;
    public void start() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(this, 0,1);
    }
    @Override
    public void run() {
        synchronized(lock1) {
            if (timer < time) timer++;
            else timer = 0;
            if(MainView.insertTrue){
                MainView.insertTrue = false;
                for(Coords c : MainView.insert.keySet()){
                    LifeGame.addGameZone(Coords.add(c,MainView.insertCoords));
                }
                MainView.insert.clear();
                MainView.insertCoords = null;
            }
            if ((LifeGame.START == 1 && timer == time) || LifeGame.START == 2) {


                //for (int[] a : mainView.getNewPoints())
                //    gameZone[a[0]][a[1]] = (byte) a[2];
                //mainView.clearNewPoints();
                LifeGame.addallgameZone(MainView.newP);
                MainView.newP.clear();
                for(Coords c : MainView.remP) LifeGame.removeGameZone(c);
                MainView.remP.clear();


                //short[][] gameZone2 = new short[LifeGame.SIZE + 1][LifeGame.SIZE + 1];
                Map<Coords,Byte> gz2 = new ConcurrentHashMap<>();
                //Set<Coords> gzrem = new HashSet<>();


                List<Future<?>> futures = new ArrayList<>();


                int count = 0;
                for (Coords c : LifeGame.getGameZone().keySet()) {
                    count++;
                    Future<?> f = tp.submit(() -> {

                        System.out.println(Thread.currentThread().getName() + " GL");

                        int g = logic(c.getx(),c.gety());

                        if (g >= 2 && g <= 3) {
                            gz2.put(c, (byte) 1);
                        }

                        //if (g < 2 || g > 3) {
                        //} else{
                        //    gz2.put(c, (byte) 1);
                        //}

                        for(int a = -1; a < 2; a ++)
                            for(int b = -1; b < 2; b ++) {
                                int x = c.getx() + a;
                                int y = c.gety() + b;


                                if (!LifeGame.getGameZone().containsKey(new Coords(x,y))) {
                                    g = logic(x, y);
                                    if (g == 3) {
                                        gz2.put(new Coords(x, y), (byte) 1);
                                        //System.out.println("test " + x + " " + y);
                                    }
                                }
                            }
                    });
                    futures.add(f);
                }
                if(count != futures.size()) return;
                try {
                    for(Future f : futures) {
                            f.get();
                    }


                    LifeGame.clearGameZone();
                    LifeGame.addallgameZone(gz2);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
                //LifeGame.gameZone = gz2;
                //for(Coords c : gzrem) LifeGame.gameZone.remove(c);
//
                //for(byte c : LifeGame.gameZone.values()) System.out.println(" - - :" + c);
                //LifeGame.gameZone.replaceAll();
                //gameZone = gameZone2;
                //mainView.setGameZone(gameZone2);
                //mainView.draw();
            }
            if (LifeGame.START == 2) {
                LifeGame.START = 0;
                timer = 0;
            } else if (LifeGame.START == 3) {
                //gameZone = new short[LifeGame.SIZE + 1][LifeGame.SIZE + 1];
                mainView.clearNewPoints();
                LifeGame.START = 2;
            }
        }
    }

    private int logic(int x, int y) {

        int g = 0;
        if (LifeGame.getGameZone().containsKey(new Coords(x - 1, y)))
            g += LifeGame.getGameZone().get(new Coords(x - 1, y)) == 0 ? 0 : 1;
        if (LifeGame.getGameZone().containsKey(new Coords(x - 1, y - 1)))
            g += LifeGame.getGameZone().get(new Coords(x - 1, y - 1)) == 0 ? 0 : 1;
        if (LifeGame.getGameZone().containsKey(new Coords(x, y - 1)))
            g += LifeGame.getGameZone().get(new Coords(x, y - 1)) == 0 ? 0 : 1;
        if (LifeGame.getGameZone().containsKey(new Coords(x + 1, y - 1)))
            g += LifeGame.getGameZone().get(new Coords(x + 1, y - 1)) == 0 ? 0 : 1;
        if (LifeGame.getGameZone().containsKey(new Coords(x + 1, y)))
            g += LifeGame.getGameZone().get(new Coords(x + 1, y)) == 0 ? 0 : 1;
        if (LifeGame.getGameZone().containsKey(new Coords(x + 1, y + 1)))
            g += LifeGame.getGameZone().get(new Coords(x + 1, y + 1)) == 0 ? 0 : 1;
        if (LifeGame.getGameZone().containsKey(new Coords(x, y + 1)))
            g += LifeGame.getGameZone().get(new Coords(x, y + 1)) == 0 ? 0 : 1;
        if (LifeGame.getGameZone().containsKey(new Coords(x - 1, y + 1)))
            g += LifeGame.getGameZone().get(new Coords(x - 1, y + 1)) == 0 ? 0 : 1;
        return g;
    }

    private boolean[][] gameZoneView(byte[][] gz) {
        boolean[][] f = new boolean[LifeGame.SIZE+1][LifeGame.SIZE+1];
        for(int a = 0; a < gz.length; a++)
            for(int b = 0; b < gz[a].length; b++)
                f[a][b] = gz[a][b]>0;
        return f;
    }
}
