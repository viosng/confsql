package com.viosng.confsql.semantic.model.other;

import org.junit.Test;

import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 07.03.2015
 * Time: 17:33
 */
public class ReduceTest {
    @Test
    public void testReduce() throws Exception {
        String[] a = {"a", "b", "c"};
        Integer s = Arrays.stream(a).reduce(1, new BiFunction<Integer, String, Integer>() {
            @Override
            public Integer apply(Integer integer, String s) {
                System.out.println("biFunction");
                return integer * (s.charAt(0) - 'a' + 1);
            }
        }, (integer, integer2) -> null);
        System.out.println(s);
    }
}
