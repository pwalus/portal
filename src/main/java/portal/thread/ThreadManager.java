package portal.thread;

import java.util.*;
import java.util.concurrent.*;
import org.springframework.stereotype.*;

@Service
public class ThreadManager {

    private List<Runnable> tasks = new ArrayList<>();

    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        try {
            tasks.forEach(executorService::submit);
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            System.out.println("System shutdown...");
        }
    }

    public void addTask(Runnable runnable) {
        tasks.add(runnable);
    }
}
