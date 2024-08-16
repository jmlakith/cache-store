package tech.solutions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TimeBasedCacheTest {
    private final TimeBasedCache<String> cacheStore = new TimeBasedCache<>();

    private TimeBasedCacheTest() {
        cacheStore.clean();
    }

    @Test
    @DisplayName("GIVEN entries, WHEN get, THEN return successfully")
    void testGet_whenAddSomeEntries_thenGetThemBeforeExpiation() {
        cacheStore.setTTL(5);
        cacheStore.add("key1", "value1");

        var value = cacheStore.get("key1");

        Assertions.assertTrue(value.isPresent());
        Assertions.assertEquals("value1", value.get());
    }

    @Test
    @DisplayName("GIVEN a key, WHEN expired after some period, THEN won't return a value")
    void testClean_whenExpiredEntriesFound_thenRemoveThem() {
        cacheStore.setTTL(2);
        cacheStore.add("key1", "value1");
        await(2000);

        Assertions.assertTrue(cacheStore.get("key1").isEmpty());
    }

    private void await(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}