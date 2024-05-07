package org.hoey.benchmark;

import org.hoey.exception.constant.Constants;
import org.hoey.network.IntMap;
import org.hoey.network.Socket;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author liuhuijun
 * @since 2024/5/6 22:36
 */
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class IntMapBenchmark {

    private static final Object o = new Object();

    @Param({"5", "20", "100", "1000", "10000"})
    private int size;

    @Benchmark
    public void testSocketMap(Blackhole bh) {
        Map<Socket, Object> m = new HashMap<>(1024);
        for(int i = 0; i < size; i++) {
            Socket socket = new Socket(i);
            m.put(socket, o);
            bh.consume(m.get(socket));
        }
    }

    @Benchmark
    public void testHashMap(Blackhole bh) {
        Map<Integer, Object> m = new HashMap<>(1024);
        for(int i = 0; i < size; i++) {
            m.put(i, o);
            bh.consume(m.get(i));
        }
    }

    @Benchmark
    public void testIntMap(Blackhole bh) {
        IntMap<Object> m = new IntMap<>(1024);
        for(int i = 0; i < size; i++) {
            m.put(i, o);
            bh.consume(m.get(i));
        }
    }
}
