
package com.example.lifeGame;

import java.util.Objects;

public class Coords {
    private int x,y;

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getx() {
        return x;
    }
    public int gety() {
        return y;
    }

    @Override
    public String toString() {
        return "Coords{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(x,y);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Coords c) {
            return this.x == c.getx() && this.y == c.gety();
        }
        return false;
    }
}
