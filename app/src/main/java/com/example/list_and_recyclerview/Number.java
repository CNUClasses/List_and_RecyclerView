package com.example.list_and_recyclerview;

public class Number {
    private static final int UNDEFINED = -1;
    private int numbtosquare;

    //something is going to take this
    // number and square it
    public int result = UNDEFINED;

    public Number(int numbtodouble) {
        this.numbtosquare = numbtodouble;
    }

    public int  squareit(){
        numbtosquare = numbtosquare*numbtosquare;
        return numbtosquare;
    }
}
