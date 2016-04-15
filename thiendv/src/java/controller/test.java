/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 *
 * @author doanvanthien
 */
public class test {

    public static void main(String[] args) {
        String str = "2432432a";
        System.out.println(test.isNumeric(str));
    }

    public static boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }
}
