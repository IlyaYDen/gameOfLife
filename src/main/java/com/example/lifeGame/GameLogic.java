package com.example.lifeGame;

import java.util.*;

public class GameLogic extends TimerTask {

    static int time = 10;
    int timer = 0;
    MainView mainView;
    //private short[][] gameZone = new short[LifeGame.SIZE+1][LifeGame.SIZE+1];

    public GameLogic(MainView mainView) {
        this.mainView = mainView;
    }


    public void start() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(this, 0,50);
    }
    @Override
    public void run() {
        synchronized(this) {
            if (timer < time) timer++;
            else timer = 0;
            if ((LifeGame.START == 1 && timer == time) || LifeGame.START == 2) {


                //for (int[] a : mainView.getNewPoints())
                //    gameZone[a[0]][a[1]] = (byte) a[2];
                //mainView.clearNewPoints();
                LifeGame.gameZone.putAll(MainView.newP);
                MainView.newP.clear();
                for(Coords c : MainView.remP) LifeGame.gameZone.remove(c);
                MainView.remP.clear();

                //short[][] gameZone2 = new short[LifeGame.SIZE + 1][LifeGame.SIZE + 1];
                Map<Coords,Byte> gz2 = new HashMap<>();
                //Set<Coords> gzrem = new HashSet<>();

                for (Coords c : LifeGame.gameZone.keySet()) {

                    int g = logic(c.getx(),c.gety());

                    if (g < 2 || g > 3) {
                        //gzrem.add(c);
                        //System.out.println("REMOVE  " + c);
                    } else{
                        gz2.put(c, (byte) 1);
                        //System.out.println("NOT REM  " + c);
                    }

                    for(int a = -1; a < 2; a ++)
                        for(int b = -1; b < 2; b ++) {
                            int x = c.getx() + a;
                            int y = c.gety() + b;


                            if (!LifeGame.gameZone.containsKey(new Coords(x,y))) {
                                g = logic(x, y);
                                if (g == 3) {
                                    gz2.put(new Coords(x, y), (byte) 1);
                                    //System.out.println("test " + x + " " + y);
                                }
                            }
                        }
                }
                LifeGame.gameZone.clear();
                LifeGame.gameZone.putAll(gz2);
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
        if (LifeGame.gameZone.containsKey(new Coords(x - 1, y)))
            g += LifeGame.gameZone.get(new Coords(x - 1, y)) == 0 ? 0 : 1;
        if (LifeGame.gameZone.containsKey(new Coords(x - 1, y - 1)))
            g += LifeGame.gameZone.get(new Coords(x - 1, y - 1)) == 0 ? 0 : 1;
        if (LifeGame.gameZone.containsKey(new Coords(x, y - 1)))
            g += LifeGame.gameZone.get(new Coords(x, y - 1)) == 0 ? 0 : 1;
        if (LifeGame.gameZone.containsKey(new Coords(x + 1, y - 1)))
            g += LifeGame.gameZone.get(new Coords(x + 1, y - 1)) == 0 ? 0 : 1;
        if (LifeGame.gameZone.containsKey(new Coords(x + 1, y)))
            g += LifeGame.gameZone.get(new Coords(x + 1, y)) == 0 ? 0 : 1;
        if (LifeGame.gameZone.containsKey(new Coords(x + 1, y + 1)))
            g += LifeGame.gameZone.get(new Coords(x + 1, y + 1)) == 0 ? 0 : 1;
        if (LifeGame.gameZone.containsKey(new Coords(x, y + 1)))
            g += LifeGame.gameZone.get(new Coords(x, y + 1)) == 0 ? 0 : 1;
        if (LifeGame.gameZone.containsKey(new Coords(x - 1, y + 1)))
            g += LifeGame.gameZone.get(new Coords(x - 1, y + 1)) == 0 ? 0 : 1;
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
