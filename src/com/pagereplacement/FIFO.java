package com.pagereplacement;

import java.util.*;

public class FIFO {
    public static Map<String, Object> fifo(int[] numbers, int numFrames) {
        List<Integer> frames = new ArrayList<>(); // List of numbers
        int hits = 0;
        int misses = 0;
        List<Map<String, Object>> result = new ArrayList<>(); // List of steps
        List<Integer> tempFrames = new ArrayList<>(); // Temporary frames for display

        for (int num : numbers) {
            Map<String, Object> step = new HashMap<>(); // Step is a map
            step.put("page", num);

            if (frames.contains(num)) { // If num is in frames, it's a hit
                hits++;
                step.put("hit_miss", "Hit");
            } else {
                if (frames.size() < numFrames) { // If frames are not full, add the number
                    frames.add(num);
                    tempFrames.add(num);
                } else { // Replace the oldest number
                    if (tempFrames.contains(frames.get(0))) {
                        int index = tempFrames.indexOf(frames.get(0));
                        tempFrames.set(index, num);
                    }
                    frames.remove(0); // Remove the oldest number
                    frames.add(num); // Add the new number
                }
                misses++;
                step.put("hit_miss", "Miss");
            }

            step.put("frames", new ArrayList<>(tempFrames)); // Add frames to the step
            result.add(step); // Add step to the result
        }

        Map<String, Object> output = new HashMap<>();
        output.put("result", result);
        output.put("total_faults", misses);
        return output;
    }
}