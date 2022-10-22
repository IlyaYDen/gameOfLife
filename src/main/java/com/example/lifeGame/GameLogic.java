package com.example.lifeGame;

import java.util.Timer;
import java.util.TimerTask;

public class GameLogic extends TimerTask {

    static int time = 1000;
    int timer = 0;
    MainView mainView;
    private byte[][] gameZone = new byte[LifeGame.SIZE+1][LifeGame.SIZE+1];
    public GameLogic(MainView mainView) {
        this.mainView = mainView;
    }


    public void start() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(this, 0,1);
    }
    @Override
    public void run() {
        if(timer<time) timer++;
        else timer=0;
        if((LifeGame.START==1 && timer==time)|| LifeGame.START==2){


            for(Byte[] a : mainView.getNewPoints())
                gameZone[a[0]][a[1]]= a[2];
            mainView.clearNewPoints();
            System.out.println("test");

            byte[][] gameZone2 = new byte[LifeGame.SIZE+1][LifeGame.SIZE+1];
            for(int a = 0; a < gameZone.length; a++)
                for(int b = 0; b < gameZone[a].length; b++){
                    if(gameZone[a][b]==0){
                        int g=0;
                        if(a>0) g+= gameZone[a-1][b]==0 ? 0:1;
                        if(a>0 && b>0) g+= gameZone[a-1][b-1]==0 ? 0:1;
                        if(b>0) g+= gameZone[a][b-1]==0 ? 0:1;
                        if(a<50 && b>0) g+= gameZone[a+1][b-1]==0 ? 0:1;
                        if(a<50) g+= gameZone[a+1][b]==0 ? 0:1;
                        if(a<50 && b<50) g+= gameZone[a+1][b+1]==0 ? 0:1;
                        if(b<50) g+= gameZone[a][b+1]==0 ? 0:1;
                        if(a>0 && b<50) g+= gameZone[a-1][b+1]==0 ? 0:1;
                        if(g==3) gameZone2[a][b]+=1;
                        else gameZone2[a][b]=0;
                    }
                    else if(gameZone[a][b]==1){
                        int g=0;
                        if(a>0) g+= gameZone[a-1][b]==0 ? 0:1;
                        if(a>0 && b>0) g+= gameZone[a-1][b-1]==0 ? 0:1;
                        if(b>0) g+= gameZone[a][b-1]==0 ? 0:1;
                        if(a<50 && b>0) g+= gameZone[a+1][b-1]==0 ? 0:1;
                        if(a<50) g+= gameZone[a+1][b]==0 ? 0:1;
                        if(a<50 && b<50) g+= gameZone[a+1][b+1]==0 ? 0:1;
                        if(b<50) g+= gameZone[a][b+1]==0 ? 0:1;
                        if(a>0 && b<50) g+= gameZone[a-1][b+1]==0 ? 0:1;
                        System.out.println(g);
                        if(g<2 || g>3) gameZone2[a][b]=0;
                        else gameZone2[a][b]+=1;
                    }
                    else gameZone2[a][b]=gameZone[a][b];
                    //if(gameZone[a][b]>=1) gameZone[a][b]+=10;
                }

            gameZone=gameZone2;
            mainView.setGameZone(gameZoneView(gameZone2));
            mainView.draw();
        }
        if(LifeGame.START==2) {
            LifeGame.START = 0;
            timer=0;
        }
        else if(LifeGame.START==3) {
            gameZone = new byte[LifeGame.SIZE+1][LifeGame.SIZE+1];
            mainView.clearNewPoints();
            LifeGame.START = 2;
        }
    }

    private boolean[][] gameZoneView(byte[][] gz) {
        boolean[][] f = new boolean[LifeGame.SIZE+1][LifeGame.SIZE+1];
        for(int a = 0; a < gz.length; a++)
            for(int b = 0; b < gz[a].length; b++)
                f[a][b] = gz[a][b]>0;
        return f;
    }
}
