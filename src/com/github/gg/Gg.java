package com.github.gg;

import com.github.gg.compiler.tres.Parser;
import com.github.gg.compiler.tres.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Gg {

    public static void main(String[] args) throws FileNotFoundException, Exception {

//        String pathfile = "src/com/github/gg/tres/test/file";
//
//        Scanner s = new Scanner(new FileInputStream(pathfile));
//        Parser p = new Parser(s);
//
//        boolean b = ! (3 > 1) || true && false;
//        System.err.println(b);
//
//        p.parse();
        
        int[][] a = new int[2][3];
        
        for (int i = 0; i < a.length; i++) {
            int[] is = a[i];
            System.err.println(i);
        }
        
        
    }

}
