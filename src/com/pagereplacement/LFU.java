package com.pagereplacement;

import java.util.*;

public class LFU {
    public static Map<String, Object> lfu(int[] numbers, int numFrames) {
        if (numFrames <= 0) {
            throw new IllegalArgumentException("Number of frames must be positive.");
        }
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Input array cannot be null or empty.");
        }

        Set<Integer> frames = new LinkedHashSet<>();
        Map<Integer, Integer> freqMap = new HashMap<>();
        int faultCount = 0;
        List<Map<String, Object>> result = new ArrayList<>();

        for (int num : numbers) {
            Map<String, Object> step = new HashMap<>();
            step.put("page", num);

            if (frames.contains(num)) {
                freqMap.put(num, freqMap.get(num) + 1);
                step.put("hit_miss", "Hit");
            } else {
                if (frames.size() == numFrames) {
                    int minFreq = Integer.MAX_VALUE, replace = -1;
                    for (int frame : frames) {
                        if (freqMap.get(frame) < minFreq) {
                            minFreq = freqMap.get(frame);
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

            step.put("frames", new ArrayList<>(frames));
            result.add(step);
        }

        Map<String, Object> output = new HashMap<>();
        output.put("result", result);
        output.put("total_faults", faultCount);
        return output;
    }
}