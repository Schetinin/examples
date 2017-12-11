package com.schetinin.examples;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created by Юрий on 11.12.2017.
 */
public class FactorialParallelTree extends RecursiveTask<BigInteger> {
    private final int MIN_INTERVAL = 5;
    private int from;
    private int to;

    private FactorialParallelTree(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public static BigInteger calc(int n) {
        ForkJoinPool pool = new ForkJoinPool();
        Collection<FactorialParallelTree> tasks = new ArrayList<>();
        if (n < 0) {
            throw new ArithmeticException("Not defined");
        }
        if (n == 0 || n == 1) {
            return BigInteger.valueOf(1);
        }

        FactorialParallelTree res = new FactorialParallelTree(1, n);
        System.out.println(String.format("Start calc for %d", n));
        pool.execute(res);
        return res.join();
    }

    @Override
    protected BigInteger compute() {
        if (to - from > MIN_INTERVAL) {

            FactorialParallelTree left = new FactorialParallelTree(from, (from + to) / 2);
            FactorialParallelTree right = new FactorialParallelTree((from + to) / 2 + 1, to);
            System.out.println(String.format("Start splitting to: (%d,%d) and (%d,%d)",
                    left.from,
                    left.to,
                    right.from,
                    right.to));
            left.fork();
            right.fork();
            return left.join().multiply(right.join());
        }
        BigInteger result = BigInteger.valueOf(from);
        System.out.println(String.format("%s Calc from %d to %d", Thread.currentThread().getName(), from, to));
        for (int i = from + 1; i <= to; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

}
