package com.schetinin.examples;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class for counting hits
 */
public class HitStatistics {

    private static class PeriodInfo{
        private static class CurrValues{
            final private AtomicInteger counter = new AtomicInteger(0);
            final private Date nextPeriodDate;

            CurrValues(Date nextPeriodDate) {
                this.nextPeriodDate = nextPeriodDate;
            }

            int getCount() {
                return counter.get();
            }

            Date getNextPeriodDate() {
                return nextPeriodDate;
            }

            void inc(){
                counter.incrementAndGet();
            }
        }
        final private Period period;
        private volatile CurrValues currValues;



        private PeriodInfo(Period period, Date now) {
            this.period = period;
            setNext(now);
        }

        private void clearIfExpired(){
            Date now = new Date();
            //If period expired, then clear stat
            if(!now.before(currValues.getNextPeriodDate())){
                setNext(now);
            }
        }

        private void setNext(Date now){
            currValues = new CurrValues(getNextPeriodDate(now));
        }

        private void inc(){
            currValues.inc();
        }

        private int getCount(){
            return currValues.getCount();
        }

        private PeriodInfo(Period period) {
            this(period, new Date());
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
    }

    private Map<Period,PeriodInfo> stats;


    private HitStatistics() {
        stats = new ConcurrentHashMap<>(Period.values().length);
        for (Period period : Period.values()) {
            stats.put(period,new PeriodInfo(period));
        }
    }

    public static HitStatistics getInstance(){
        return instance;
    }



    public void hit(){
        for (Period period : Period.values()) {
            prepare(period).inc();
        }

    }

    public int getCountPerPeriod(Period period){
        return prepare(period).getCount();
    }

    void clearPeriodManually(Period period){
        PeriodInfo info = stats.get(period);
        info.setNext(new Date());

    }

    private PeriodInfo prepare(Period period){
        PeriodInfo info = stats.get(period);
        info.clearIfExpired();
        return info;
    }

}
