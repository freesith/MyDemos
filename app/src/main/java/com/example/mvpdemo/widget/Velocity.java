package com.example.mvpdemo.widget;

/**
 * Created by Tish on 2016/7/27.
 */
public class Velocity {

    public double x;
    public double y;

    public Velocity(double x, double y) {

        this.x = x;
        this.y = y;
    }


    public Velocity multi(double d) {
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
