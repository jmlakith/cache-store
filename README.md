# TimeBasedCache

## **Overview**

The TimeBasedCache is a Java implementation of a thread-safe, time-based cache system. This project demonstrates a basic caching mechanism where each cached entry has a Time-to-Live (TTL) value, after which it is automatically removed from the cache. The cache uses modern Java date-time handling (LocalDateTime and Duration) and is optimized for high ingestion scenarios.


## **Features**

- **Time-to-Live (TTL) Support**: Each cache entry is automatically removed after the specified TTL duration.

- **Thread Safety**: Uses ConcurrentHashMap and synchronized blocks to ensure thread safety in concurrent environments.

- **Periodic Cleanup**: A scheduled task periodically checks and removes expired cache entries to manage memory efficiently.

- **Modern Java Date-Time API**: Utilizes LocalDateTime and Duration for date-time handling, providing better precision and thread safety compared to older APIs like Date and Calendar.

- **Scalability**: The implementation is designed to handle high ingestion rates efficiently.


## **Usage**

### **1. Clone the Repository**

Bash




    git clone https://github.com/yourusername/TimeBasedCache.git
    cd TimeBasedCache


### **2. Running Tests**

You can run the unit tests provided to ensure everything is working as expected:

Bash




    mvn test


### **3. Implementation Details**

#### **Core Concepts**

**Data Structures:**

- ConcurrentHashMap: Used to store the actual cache content, ensuring thread-safe access.

- TreeMap\<LocalDateTime, String>: Used to maintain the expiration times of cache entries in a sorted manner.

**Time Management:**

- LocalDateTime: Used to record the current time and calculate expiration times.

- Duration: Used to define the TTL for cache entries.


#### **Cache Cleaner**

A scheduled task runs at a fixed interval (default 500ms) to remove expired cache entries. The cleaner uses the current time to identify and purge entries that have exceeded their TTL.


#### **High-Ingestion Scenario**

In scenarios where the cache ingestion rate is high, this implementation ensures that memory is managed efficiently through the use of a ScheduledExecutorService to periodically clear expired entries.


### **4. Future Improvements**

- Custom Eviction Policies: Implement additional eviction policies like LRU (Least Recently Used).

- Configuration Options: Allow more flexible configuration, such as dynamic TTLs per entry.

- SoftReference Support: Explore using SoftReference for cache values to allow the garbage collector to automatically reclaim memory under pressure.
