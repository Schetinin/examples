package com.schetinin.examples;

/**
 * Created by Юрий on 15.11.2017.
 */
public class BubbleSort {
    public static void sort(int[] arr){
        if(arr==null||arr.length<2){
            return;
        }
        int buff;
        boolean wasSwap;
        for(int j=0;j<arr.length-1;j++){
            wasSwap = false;
            for(int i=1;i<arr.length;i++){
                if(arr[i]<arr[i-1]){
                    buff=arr[i];
                    arr[i]=arr[i-1];
                    arr[i-1]=buff;
                    wasSwap = true;
                }
            }
            if(!wasSwap){
                return;
            }
        }
    }
}
