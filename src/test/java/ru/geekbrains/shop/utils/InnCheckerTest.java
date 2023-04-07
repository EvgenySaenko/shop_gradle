package ru.geekbrains.shop.utils;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InnCheckerTest {

    @Test
    public void mustGiveCorrectInn(){
        assertTrue(InnChecker.innIsCorrect("0000055555"));
    }

    @Test
    @Ignore
    public void mustGiveCorrectValue(){
        assertEquals(4,InnChecker.doSomething(2));
    }
}
