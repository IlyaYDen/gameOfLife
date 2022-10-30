package com.example.lifeGame;

public class Selection {

    private int startx,starty,endx,endy;
    public Selection(int fStartPanX, int fStartPanY, int x, int y) {
        startx = fStartPanX;
        starty = fStartPanY;
        endx = x;
        endy = y;
    }
    public Selection() {
    }

    public double getStartx() {
        return startx;
    }

    public void setStartx(int startx) {
        this.startx = startx;
    }

    public double getStarty() {
        return starty;
    }

    public void setStarty(int starty) {
        this.starty = starty;
    }

    public double getEndx() {
        return endx;
    }

    public void setEndx(int endx) {
        this.endx = endx;
    }

    public double getEndy() {
        return endy;
    }

    public void setEndy(int endy) {
        this.endy = endy;
    }
}
