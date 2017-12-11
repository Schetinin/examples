package com.schetinin.examples;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created by Юрий on 11.12.2017.
 */
public class FactorialParallel {
    private final int INTERVAL = 4;

    public BigInteger calc(int n) {
        ForkJoinPool pool = new ForkJoinPool();
        Collection<CalcTask> tasks = new ArrayList<>();
        if (n < 0) {
            throw new ArithmeticException("Not defined");
        }
        if (n == 0 || n == 1) {
            return BigInteger.valueOf(1);
        }
        int i;
        for (i = 1; i <= n ; i += INTERVAL + 1) {
            int i1 = i + INTERVAL;
            CalcTask task = new CalcTask(i, i1>n?n:i1);
            tasks.add(task);
            pool.execute(task);
        }


        BigInteger result = BigInteger.ONE;
        for (CalcTask task : tasks) {
            BigInteger joinRes = (BigInteger) task.join();
            System.out.println(String.format("join res:%s", joinRes));
            result = result.multiply(joinRes);
        }

        return result;
    }

    private class CalcTask extends RecursiveTask {

        private int from;
        private int to;

        public CalcTask(int from, int to) {

            this.from = from;
            this.to = to;
        }


        @Override
        protected Object compute() {
            BigInteger result = BigInteger.valueOf(from);
            System.out.println(String.format("%s Calc from %d to %d",Thread.currentThread().getName(), from, to));
            for (int i = from + 1; i <= to; i++) {
                result = result.multiply(BigInteger.valueOf(i));
            }
            return result;
        }
    }

}
