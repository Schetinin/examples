package com.schetinin.examples;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Юрий on 17.11.2017.
 */
public abstract class BaseRunner implements Runnable {
    static AtomicBoolean work = new AtomicBoolean(true);

    protected abstract void process();

    @Override
    public void run() {
        App.sout(String.format("%s started",getClass().getName()));
        while (work.get()) {
            process();
        }
        App.sout(String.format("%s stopped",getClass().getName()));
    }
}
