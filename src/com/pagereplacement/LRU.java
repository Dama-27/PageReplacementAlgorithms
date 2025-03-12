package com.pagereplacement;

import java.util.*;

public class LRU {
    public static Map<String, Object> lru(int[] numbers, int numFrames) {
        List<Integer> frames = new ArrayList<>();
        int hits = 0;
        int misses = 0;
        List<Map<String, Object>> result = new ArrayList<>();
        List<Integer> tempFrames = new ArrayList<>();

        for (int num : numbers) {
            Map<String, Object> step = new HashMap<>();
            step.put("page", num);

            if (frames.contains(num)) {
                hits++;
                frames.remove((Integer) num); // Remove the number and add it to the end
                frames.add(num);
                step.put("hit_miss", "Hit");
            } else {
                if (frames.size() < numFrames) {
                    frames.add(num);
                    tempFrames.add(num);
                } else {
                    if (tempFrames.contains(frames.get(0))) {
                        int index = tempFrames.indexOf(frames.get(0));
                        tempFrames.set(index, num);
                    }
                    frames.remove(0);
                    frames.add(num);
                }
                misses++;
                step.put("hit_miss", "Miss");
            }

            step.put("frames", new ArrayList<>(tempFrames));
            result.add(step);
        }

        Map<String, Object> output = new HashMap<>();
        output.put("result", result);
        output.put("total_faults", misses);
        return output;
    }
}