package com.schetinin.examples;

import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * @author Igor Tyazhev (tyazhev@gmail.com) Date: 22.11.2017 17:44
 */
public class FibonacciTest {

    @Test
    public void testCalc() {
        doTest(Fibonatchi::calc);
        doTest(FibonatchiRecursion::calc);
    }

    protected void doTest(Function<Integer, Integer> calculator) {
        // 0 1 1 2 3 5 8 13
        checkNthValue(0, 0, calculator);
        checkNthValue(1, 1, calculator);
        checkNthValue(2, 1, calculator);
        checkNthValue(3, 2, calculator);
        checkNthValue(4, 3, calculator);
        checkNthValue(5, 5, calculator);
        checkNthValue(6, 8, calculator);
        checkNthValue(7, 13, calculator);
    }

    protected void checkNthValue(int i, int expected, Function<Integer, Integer> calculator) {
        assertEquals(String.format("Incorrect value at %d", i), expected, (int)calculator.apply(i));
    }
}
