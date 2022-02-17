package org.jeecg.modules.rec.engine.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 异步处理
 *
 * @Author: zhou x
 * @Date: 2022/2/16 17:01
 */
@Component
public class AsyncWorker {
    @Async
    public <T extends AsyncTagOperation> void async(CountDownLatch latch, T t, Consumer<T> action) {
        action.accept(t);
        latch.countDown();
    }

    @Async
    public <T> void asyncBatch(CountDownLatch latch, List<T> t, Consumer<List<T>> action) {
        action.accept(t);
        latch.countDown();
    }

    @Async
    public <T, R> void asyncWorkFunc(CountDownLatch latch, T t, Function<T, R> action, ConcurrentLinkedQueue<R> queue) {
        R r = action.apply(t);
        if (r != null) queue.add(r);
        latch.countDown();
    }
}
