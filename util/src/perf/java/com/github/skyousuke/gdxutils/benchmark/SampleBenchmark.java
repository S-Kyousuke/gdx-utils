package com.github.skyousuke.gdxutils.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@Fork(2)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)

public class SampleBenchmark {

    private Map<Integer, Integer> map;

    @Param({"hashmap", "treemap"})
    private String type;

    private int begin;
    private int end;

    @Setup
    public void setup() {
        if (type.equals("hashmap")) {
            map = new HashMap<Integer, Integer>();

        } else if (type.equals("treemap")) {
            map = new TreeMap<Integer, Integer>();

        } else {
            throw new IllegalStateException("Unknown type: " + type);
        }

        begin = 1;
        end = 256;
        for (int i = begin; i < end; i++) {
            map.put(i, i);
        }
    }

    @Benchmark
    public void javaMapGet(Blackhole bh) {
        for (int i = begin; i < end; i++) {
            bh.consume(map.get(i));
        }
    }

}