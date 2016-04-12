/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author doanvanthien
 */
public class testClass {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String result = "123456";
        for (int i = 0; i < 100; i++) {
            SecureRandom random = new SecureRandom();
            result = new BigInteger(16, random).toString(10);
            System.out.println(result);
        }

    }
}
