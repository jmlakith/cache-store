package tech.solutions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TimeBasedCache<V> implements CacheStore<V>, CacheCleaner {
    private final Logger logger = Logger.getLogger(TimeBasedCache.class.getName());
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
    private final TreeMap<LocalDateTime, String> timeBasedMap = new TreeMap<>();
    private final Map<String, V> contentMap = new ConcurrentHashMap<>();
    private int duration;

    private static final long REFRESH_INTERVAL = 500;
    private static final long INITIAL_DELAY = 500;

    @Override
    public void setTTL(int duration) {
        this.duration = duration;
    }

    @Override
    public void add(String key, V value) {
        var now = LocalDateTime.now();
        var expiryTime = now.plus(Duration.ofSeconds(duration));
        contentMap.put(key, value);
        synchronized (this) {
            timeBasedMap.put(expiryTime, key);
        }
    }

    @Override
    public Optional<V> get(String key) {
        return Optional.ofNullable(contentMap.get(key));
    }

    private void removeContents(Map<LocalDateTime, String> expiredRecords) {
        expiredRecords.forEach((key, value) -> contentMap.remove(value));
    }

    @Override
    public void clean() {
        executorService.scheduleAtFixedRate(() -> {
            synchronized (this) {
                if (!timeBasedMap.isEmpty()) {
                    var now = LocalDateTime.now();
                    var expiredRecords = timeBasedMap.headMap(now);
                    removeContents(expiredRecords);
                    expiredRecords.clear();
                    logger.info("cleaner executed: " + now);
                }
            }
        }, INITIAL_DELAY, REFRESH_INTERVAL, TimeUnit.MICROSECONDS);
    }
}
