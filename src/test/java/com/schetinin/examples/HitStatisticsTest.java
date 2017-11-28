package com.schetinin.examples;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Юрий on 28.11.2017.
 */
public class HitStatisticsTest {

    HitStatistics hitStatistics = HitStatistics.getInstance();

    @Test
    public void hit10() throws Exception {
        hitStatistics.clearPeriodManually(HitStatistics.Period.DAY);
        for (int i = 0; i < 10; i++) {
            hitStatistics.hit();
        }
        assertEquals(10,hitStatistics.getCountPerPeriod(HitStatistics.Period.DAY));
    }

}