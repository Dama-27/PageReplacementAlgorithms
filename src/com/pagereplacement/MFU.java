package com.pagereplacement;

import java.util.*;

public class MFU {
    public static Map<String, Object> mfu(int[] numbers, int numFrames) {
        // Validate input
        if (numFrames <= 0) {
            throw new IllegalArgumentException("Number of frames must be positive.");
        }
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Input array cannot be null or empty.");
        }

        Set<Integer> frames = new LinkedHashSet<>();
        Map<Integer, Integer> freqMap = new HashMap<>();
        int hitCount = 0, faultCount = 0;
        List<Map<String, Object>> result = new ArrayList<>();

        for (int num : numbers) {
            Map<String, Object> step = new HashMap<>();
            step.put("page", num);

            if (frames.contains(num)) { // Hit
                hitCount++;
                freqMap.put(num, freqMap.get(num) + 1);
                step.put("hit_miss", "Hit");
            } else { // Miss
                if (frames.size() == numFrames) {
                    // Find the page with the highest frequency
                    int maxFreq = -1;
                    int replace = -1;
                    for (int frame : frames) {
                        if (freqMap.get(frame) > maxFreq) {
                            maxFreq = freqMap.get(frame);
                            replace = frame;
                        }
                    }
                    frames.remove(replace);
                    freqMap.remove(replace);
                }
                frames.add(num);
                freqMap.put(num, 1);
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