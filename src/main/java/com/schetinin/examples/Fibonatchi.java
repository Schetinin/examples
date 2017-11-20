package com.schetinin.examples;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Юрий on 15.11.2017.
 */
public class Fibonatchi {

    public static Integer returnIfLess2(int n){
        if(n<0){
            throw new IllegalArgumentException("n must be more or equals then 0");
        }
        switch (n){
            case 0: return 0;
            case 1: return 1;
        }
        return null;
    }


    public static int calc(int n){
        if(n<2){
            return returnIfLess2(n);
        }
        // 0 1 1 2 3 4 7 11
        Queue<Integer> queue = new LinkedList<Integer>();
        queue.add(0);
        queue.add(1);
        int result=0;
        for(int i=2;i<=n;i++){
            Integer im1 = queue.poll();
            Integer im2 = queue.peek();
            result = im1+im2;
            queue.add(result);
        }
        return result;
    }

}
