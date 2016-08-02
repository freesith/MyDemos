package com.example.mvpdemo.widget;

/**
 * Created by Tish on 2016/7/27.
 */
public class Velocity {

    public float x;
    public float y;

    public Velocity(float x, float y) {

        this.x = x;
        this.y = y;
    }

    public Velocity() {

    }

    public static void merge(Velocity a, Velocity b, Velocity dst) {
        dst.x = a.x + b.x;
        dst.y = a.y + b.y;
    }

    public static void aMerge(Velocity a, Velocity b, Velocity dst) {
        dst.x = a.x - b.x;
        dst.y = a.y - b.y;
    }

    public void mergeUp(float a, float b) {
        x += a;
        y += b;
    }


    public Velocity multi(float d) {
//        x *= d;
//        y *= d;
//
        return new Velocity(x * d, y * d);
    }

    public Velocity merge(Velocity v) {
        return new Velocity( x + v.x, y + v.y);
    }

    public Velocity aMerge(Velocity v) {
        return new Velocity(x - v.x, y - v.y);
    }

    public double getValue() {
        return Math.sqrt(x*x + y*y);
    }

    @Override
    public String toString() {
        return x + "x + " + y + "y";
    }
}
