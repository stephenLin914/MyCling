package com.lzh.cling;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;



public class ClingExecutor extends ThreadPoolExecutor{

    private static Logger log = Logger.getLogger(ClingExecutor.class.getName());
    public ClingExecutor() {
        this(new ClingThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy() {
                    // The pool is unbounded but rejections will happen during shutdown
                    @Override
                    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                        // Log and discard
                        log.info("Thread pool rejected execution of " + runnable.getClass());
                        super.rejectedExecution(runnable, threadPoolExecutor);
                    }
                }
        );
    }

    public ClingExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedHandler) {
        // This is the same as Executors.newCachedThreadPool
        super(0,
                Integer.MAX_VALUE,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                threadFactory,
                rejectedHandler
        );
    }

    @Override
    protected void afterExecute(Runnable runnable, Throwable throwable) {
        super.afterExecute(runnable, throwable);
        if (throwable != null) {
            Throwable cause = throwable.getCause();
            if (cause instanceof InterruptedException) {
                // Ignore this, might happen when we shutdownNow() the executor. We can't
                // log at this point as the logging system might be stopped already (e.g.
                // if it's a CDI component).
                return;
            }
            // Log only
            log.warning("Thread terminated " + runnable + " abruptly with exception: " + throwable);
            log.warning("Root cause: " + cause);
        }
    }

    public static class ClingThreadFactory implements ThreadFactory {

        protected final ThreadGroup group;
        protected final AtomicInteger threadNumber = new AtomicInteger(1);
        protected final String namePrefix = "cling-";

        public ClingThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(
                    group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0
            );
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);

            return t;
        }
    }
}
