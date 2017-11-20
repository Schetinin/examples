package com.schetinin.examples;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Юрий on 15.11.2017.
 */
public class HitStatistics {

    private static class PeriodInfo{
        final Period period;
        final AtomicInteger counter = new AtomicInteger(0);
        private AtomicReference<Date> nextPeriodDate;

        public void clearIfExpired(){
            Date now = new Date();
            //If period expired, then clear stat
            if(!now.before(nextPeriodDate.get())){
                counter.set(0);
                nextPeriodDate.set(getNextPeriodDate(now));
            }
        }

        public PeriodInfo(Period period) {
            this.period = period;
            nextPeriodDate = new AtomicReference<>(getNextPeriodDate());
        }

        private Date getNextPeriodDate(){
            return getNextPeriodDate(new Date());
        }

        private Date getNextPeriodDate(Date date){
            return DateUtils.ceiling(date, period.calendarPeriod);
        }

    }

    private static HitStatistics instance = new HitStatistics();

    public enum Period{
        SECOND(Calendar.SECOND),
        MINUTE(Calendar.MINUTE),
        HOUR(Calendar.HOUR),
        DAY(Calendar.DATE);

        Period(int calendarPeriod) {
            this.calendarPeriod = calendarPeriod;
        }

        int calendarPeriod;
    };

    private Map<Period,PeriodInfo> stats;


    private HitStatistics() {
        stats = new ConcurrentHashMap<Period,PeriodInfo>(Period.values().length);
        for (Period period : Period.values()) {
            stats.put(period,new PeriodInfo(period));
        }
    }

    public static HitStatistics getInstance(){
        return instance;
    }



    public void hit(){
        for (Period period : Period.values()) {
            prepare(period);
            stats.get(period).counter.incrementAndGet();
        }

    }

    public int getCountPerPeriod(Period period){
        prepare(period);
        return stats.get(period).counter.intValue();
    }

    private void prepare(Period period){
        PeriodInfo info = stats.get(period);
        info.clearIfExpired();
    }

}
