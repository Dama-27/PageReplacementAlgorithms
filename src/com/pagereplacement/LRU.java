package com.pagereplacement;

import java.util.*;

public class LRU {
    public static Map<String, Object> lru(int[] numbers, int numFrames) {
        // Validate input
        if (numFrames <= 0) {
            throw new IllegalArgumentException("Number of frames must be positive.");
        }
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Input array cannot be null or empty.");
        }

        LinkedHashSet<Integer> frames = new LinkedHashSet<>();
        int hitCount = 0, faultCount = 0;
        List<Map<String, Object>> result = new ArrayList<>();

        for (int num : numbers) {
            Map<String, Object> step = new HashMap<>();
            step.put("page", num);

            if (frames.contains(num)) { // Hit
                hitCount++;
                frames.remove(num); // Remove and re-add to update recency
                frames.add(num);
                step.put("hit_miss", "Hit");
            } else { // Miss
                if (frames.size() == numFrames) {
                    Iterator<Integer> iterator = frames.iterator();
                    frames.remove(iterator.next()); // Remove the oldest page
                }
                frames.add(num); // Add new page
                faultCount++;
                step.put("hit_miss", "Miss");
            }

            // Store state of frames
            step.put("frames", new ArrayList<>(frames));
            result.add(step);
        }

        // Construct final output
        Map<String, Object> output = new HashMap<>();
        output.put("result", result);
        output.put("total_faults", faultCount);
        return output;
    }
}