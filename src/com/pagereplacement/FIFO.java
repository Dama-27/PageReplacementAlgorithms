package com.pagereplacement;

import java.util.*;

public class FIFO {
    public static Map<String, Object> fifo(int[] numbers, int numFrames) {
        if (numFrames <= 0) {
            throw new IllegalArgumentException("Number of frames must be positive.");
        }
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Input array cannot be null or empty.");
        }

        Queue<Integer> frames = new LinkedList<>();
        int faultCount = 0;
        List<Map<String, Object>> result = new ArrayList<>();

        for (int num : numbers) {
            Map<String, Object> step = new HashMap<>();
            step.put("page", num);

            if (frames.contains(num)) {
                step.put("hit_miss", "Hit");
            } else {
                if (frames.size() == numFrames) {
                    frames.poll();
                }
                frames.add(num);
                faultCount++;
                step.put("hit_miss", "Miss");
            }

            step.put("frames", new ArrayList<>(frames));
            result.add(step);
        }

        Map<String, Object> output = new HashMap<>();
        output.put("result", result);
        output.put("total_faults", faultCount);
        return output;
    }
}