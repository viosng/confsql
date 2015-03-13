package com.viosng.confsql.semantic.model;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: vio
 * Date: 13.03.2015
 * Time: 12:29
 */
public class StreamTest {
    @Test
    public void testStream() throws Exception {
        List<String> words = Lists.newArrayList("aaaaa", "bbbbbbb", "ccccccc");
        int target = 0;
        String[] preps = {"в", "и", "с", "по", "на", "под", "над", "от", "из",
                "через", "перед", "за", "до", "о", "не", "или", "у", "про", "для"};
        List<String> infices = Stream.concat(Stream.of(" "), Arrays.stream(preps).map(p -> ' ' + p + ' '))
                .collect(Collectors.toList());
        Map<Integer, Map<Integer, List<String>>> lenHashSuffix = words.stream()
                .flatMap(s -> infices.stream().map((String infix) -> infix + s))
                .collect(Collectors.groupingBy(String::length, Collectors.groupingBy(String::hashCode)));
        words.stream()
                .map(s -> Character.toTitleCase(s.charAt(0)) + s.substring(1))
                .flatMap(s -> lenHashSuffix.entrySet().stream()
                        .flatMap(entry -> entry.getValue().getOrDefault(
                                target - IntStream.range(0, entry.getKey()).reduce(s.hashCode(), (a, i) -> a * 31),
                                Collections.emptyList()).stream().map(suffix -> s + suffix)))
                .sorted().forEach(System.out::println);
    }
}
