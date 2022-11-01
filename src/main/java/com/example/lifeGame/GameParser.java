package com.example.lifeGame;

import java.util.*;

public class GameParser {
    public static Map<Coords, Byte> gameZoneParser(String str) {
        int x,y;
        String[] a = str.split("\n");
        if(a[0] ==null
                || !a[0].contains("x = ")
                || !a[0].contains("y = ")
                || !a[0].contains("rule = ")
        )
            return null;

        String[] b = a[0].split(", ");
        String[] c1 = b[0].split(" = ");
        String[] c2 = b[1].split(" = ");


        x = Integer.parseInt(c1[1]);
        y = Integer.parseInt(c2[1]);
        System.out.println(x+y);


        Map<Coords,Byte> gz = new HashMap<>();
        List<Map<Integer,Boolean>> i = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for(int st =1; st < a.length; st ++) {
            sb.append(a[st]);
        }
        String[] newline = sb.toString().split("\\$");

        int ay = 0;
        boolean bl = true;

        Map<Coords,Byte> n = new HashMap<>();
        for(String st : newline){
            int xx = 0;
            int ax = 0;
            for(Character c : st.toCharArray()){
                if(bl) {
                    xx +=ax;
                    ax = 0;
                }
                if(Character.isDigit(c)){
                    ax *= 10;
                    ax += c-48;
                    bl = false;
                }
                else if(c == 'b') {
                    if(ax==0) {
                        ax = 1;
                        //xx ++;
                    }
                    //for(int xt = xx; xt < ax+xx; xt ++) {
                    //    n.put(new Coords(ay, xt), (byte) 0);
                    //} x = 3, y = 1, rule = B3/S23
                    //obo!
                    bl = true;
                }
                else if(c== 'o') {
                    if(ax==0) {
                        ax = 1;
                        //xx ++;
                    }
                    for(int xt = xx; xt < ax+xx; xt ++) {
                        n.put(new Coords(xt, ay), (byte) 1);
                    }
                    bl = true;
                }
            }
            ay ++;
        }
        return n;

    }
}
