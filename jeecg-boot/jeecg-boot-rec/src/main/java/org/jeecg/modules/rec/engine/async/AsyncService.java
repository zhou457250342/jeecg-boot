package org.jeecg.modules.rec.engine.async;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: zhou x
 * @Date: 2022/2/16 17:10
 */
@Service
public class AsyncService {

    private final int fn_execMaxPoolCount = 30;
    @Autowired
    private AsyncWorker asyncWorker;

    public <T extends AsyncTagOperation> void asyncWork(List<T> list, Consumer<T> action) throws Exception {
        if (CollectionUtils.isEmpty(list)) return;
        CountDownLatch latch = new CountDownLatch(list.size());
        for (T entity : list) asyncWorker.async(latch, entity, action);
        latch.await();
    }

    public <T, R> List<R> asyncWorkFunc(List<T> list, Function<T, R> action) throws Exception {
        if (CollectionUtils.isEmpty(list)) return null;
        ConcurrentLinkedQueue<R> queues = new ConcurrentLinkedQueue();
        CountDownLatch latch = new CountDownLatch(list.size());
        for (T entity : list) asyncWorker.asyncWorkFunc(latch, entity, action, queues);
        latch.await();
        return queues.stream().collect(Collectors.toList());
    }

    public <T> void asyncWorkBatch(List<T> list, Consumer<List<T>> action) throws Exception {
        if (CollectionUtils.isEmpty(list)) return;
        List<List<T>> split = splitList(list);
        CountDownLatch latch = new CountDownLatch(split.size());
        for (List<T> entity : split) asyncWorker.asyncBatch(latch, entity, action);
        latch.await();
    }

    private <T> List<List<T>> splitList(List<T> list) {
        List<List<T>> execList = new ArrayList<>();
        int interval = list.size() >= fn_execMaxPoolCount ? list.size() / fn_execMaxPoolCount : list.size();
        int start = 0;
        do {
            execList.add(list.subList(start, start + interval < list.size() ? start + interval : list.size()));
            start += interval;
        } while (start < list.size());
        return execList;
    }
}
