package ru.ckateptb.itlfts.schedule.api;

import org.apache.commons.lang.NotImplementedException;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public abstract class AbstractScheduledExecutorService implements ScheduledExecutorService {
    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        throw new NotImplementedException();
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        throw new NotImplementedException();
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        throw new NotImplementedException();
    }

    @Override
    public void shutdown() {
        throw new NotImplementedException();
    }

    @Override
    public List<Runnable> shutdownNow() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isShutdown() {
        throw new NotImplementedException();
    }

    @Override
    public boolean isTerminated() {
        throw new NotImplementedException();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
        throw new NotImplementedException();
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        throw new NotImplementedException();
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        throw new NotImplementedException();
    }

    @Override
    public Future<?> submit(Runnable task) {
        throw new NotImplementedException();
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
        throw new NotImplementedException();
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
        throw new NotImplementedException();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) {
        throw new NotImplementedException();
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) {
        throw new NotImplementedException();
    }

    @Override
    public void execute(Runnable command) {
        throw new NotImplementedException();
    }
}
