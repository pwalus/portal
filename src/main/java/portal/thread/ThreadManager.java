package portal.thread;

import java.util.*;
import java.util.concurrent.*;
import org.springframework.stereotype.*;

@Service
public class ThreadManager {

    private List<Runnable> tasks = new ArrayList<>();

    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        tasks.forEach(executorService::submit);
        executorService.shutdown();
    }

    public void addTask(Runnable runnable) {
        tasks.add(runnable);
    }
}
