package com.github.skyousuke.gdxutils;

public class CompareUtils {

    private static final double FLOAT_EPSILON = 1.1920928955078125E-7;
    private static final double DOUBLE_EPSILON = 2.220446049250313E-16;

    private CompareUtils() {
    }

    public static boolean equals(float a, float b){
        return Math.abs(a - b) < FLOAT_EPSILON;
    }

    public static boolean equals(double a, double b){
        return Math.abs(a - b) < DOUBLE_EPSILON;
    }

}
