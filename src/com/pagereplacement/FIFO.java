package com.pagereplacement;

import java.util.*;

public class FIFO {
    public static Map<String, Object> fifo(int[] numbers, int numFrames) {
        // Validate input
        if (numFrames <= 0) {
            throw new IllegalArgumentException("Number of frames must be positive.");
        }
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Input array cannot be null or empty.");
        }

        Queue<Integer> frames = new LinkedList<>(); // FIFO queue for frames
        int hitCount = 0, faultCount = 0;
        List<Map<String, Object>> result = new ArrayList<>();

        for (int num : numbers) {
            Map<String, Object> step = new HashMap<>();
            step.put("page", num);

            if (frames.contains(num)) { // Hit
                hitCount++;
                step.put("hit_miss", "Hit");
            } else { // Miss
                if (frames.size() == numFrames) {
                    frames.poll(); // Remove oldest page (FIFO)
                }
                frames.add(num); // Add new page to queue
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