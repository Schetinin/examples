package com.schetinin.examples;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Hello world!
 */
public class App {

    public static final int HITTER_POOL_SIZE = 10;
    public static final int STATS_POOL_SIZE = 30;
    private static final int FAKE_POOL_SIZE = 5;
    private static ExecutorService hittersPool;
    private static ExecutorService statsPool;
    private static ExecutorService fakePool;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static void sout(String str){

        System.out.println(String.format("%s [%s] - %s",dateFormat.format(new Date()),Thread.currentThread().getName(),str));
    }

    public static void main(String[] args) throws InterruptedException {
        sout("On queue");
        for (int i = 0; i <= 21; i++) {
            System.out.print(String.format("%d, ", Fibonatchi.calc(i)));
        }
        sout("\nrecursion");
        for (int i = 0; i <= 21; i++) {
            System.out.print(String.format("%d, ", FibonatchiRecursion.calc(i)));
        }
        System.out.print("\n\nSorting\n\tBefore\n\t\t");
        int[] arr = {7, 4, 76, 78, 3, 6, 7, 8, 42, 324, 346, 68786, 21, 0};
        for (int i = 0; i < arr.length; i++) {
            System.out.print(String.format("%d, ", arr[i]));
        }
        BubbleSort.sort(arr);
        System.out.print("\n\tAfter\n\t\t");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(String.format("%d, ", arr[i]));
        }

        sout("\nStat test begin");

        runHitStatisticTest();
        sout("\nAll threads made. Press any key to exit");
        Thread.sleep(100000);
        pauseWhileKeyPress();
        StatConsoleReader.work.set(false);
        Hitter.work.set(false);
        FakeRunner.work.set(false);

        hittersPool.shutdown();
        statsPool.shutdown();
        fakePool.shutdown();
    }

    private static void pauseWhileKeyPress() {
        try {
            //System.in.read();
            Thread.sleep(3000);
        } catch (Exception e) {
        }
    }

    private static class Hitter extends BaseRunner{

        @Override
        protected void process() {
            try {
                HitStatistics.getInstance().hit();
                //Thread.sleep((long) (Math.random() * 1000));
            } catch (Exception e) {
                work.set(false);
            }
        }
    }

    private static class StatConsoleReader extends BaseRunner {

        private HitStatistics.Period period;

        public StatConsoleReader(HitStatistics.Period period) {
            this.period = period;
        }

        @Override
        protected void process() {
            try {
                sout(String.format(" Period:%s\t-\tstat:%d", period.toString(), HitStatistics.getInstance().getCountPerPeriod(period)));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                work.set(false);
            }
        }

    }

    private static class StatFakeReader extends BaseRunner{
        static AtomicBoolean work = new AtomicBoolean(true);
        private HitStatistics.Period period;

        public StatFakeReader(HitStatistics.Period period) {
            this.period = period;
        }

        @Override
        protected void process() {
            try {
                HitStatistics.getInstance().getCountPerPeriod(period);
            } catch (Exception e) {
                work.set(false);
            }
        }
    }

    private static class FakeRunner extends BaseRunner {
        @Override
        protected void process() {

        }
    }

    private static void runHitStatisticTest() {

        hittersPool = Executors.newFixedThreadPool(HITTER_POOL_SIZE);
        statsPool = Executors.newFixedThreadPool(HitStatistics.Period.values().length+STATS_POOL_SIZE);
        fakePool = Executors.newFixedThreadPool(FAKE_POOL_SIZE);
        for (int i = 0; i < HITTER_POOL_SIZE; i++) {
            hittersPool.execute(new Hitter());
        }

        /*for (HitStatistics.Period period : HitStatistics.Period.values()) {
            statsPool.execute(new StatConsoleReader(period));
        }*/
        statsPool.execute(new StatConsoleReader(HitStatistics.Period.SECOND));

        for (int i = 0; i < FAKE_POOL_SIZE; i++) {
            pauseWhileKeyPress();
            try {
                statsPool.execute(new FakeRunner());
                sout(String.format("Add %d reader:FAKE",i+1));
            } catch (Exception e) {
                return;
            }
        }


        List<HitStatistics.Period> periods = Arrays.asList(HitStatistics.Period.values());
        Iterator<HitStatistics.Period> periodIterator = periods.iterator();
        sout("Press any key to add fake reader");
        for (int i = 0; i < STATS_POOL_SIZE; i++) {
            pauseWhileKeyPress();
            try {
                if(!periodIterator.hasNext()){
                    periodIterator = periods.iterator();
                }
                HitStatistics.Period currPeriod = periodIterator.next();
                statsPool.execute(new StatFakeReader(currPeriod));
                sout(String.format("Add %d reader:%s",i+1,currPeriod.toString()));


            } catch (Exception e) {
                return;
            }
        }


    }

}
