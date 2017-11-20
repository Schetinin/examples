package com.schetinin.examples;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Юрий on 15.11.2017.
 */
public class FibonatchiRecursion extends Fibonatchi {

    public static int calc(int n){
        if(n<2){
            return returnIfLess2(n);
        }
        // 0 1 1 2 3 4 7 11
        return calc(n-1) + calc(n-2);

    }

}
