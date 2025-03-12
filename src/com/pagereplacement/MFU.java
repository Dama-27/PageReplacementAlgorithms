package com.pagereplacement;

import java.util.*;

public class MFU {
    public static Map<String, Object> mfu(int[] numbers, int numFrames) {
        List<Integer> frames = new ArrayList<>(); // Current pages in memory
        int hits = 0;
        int misses = 0;
        List<Map<String, Object>> result = new ArrayList<>(); // Steps for visualization
        List<Integer> tempNumbers = new ArrayList<>(); // To track frequency

        for (int num : numbers) {
            Map<String, Object> step = new HashMap<>();
            step.put("page", num);
            tempNumbers.add(num); // Add the current page to the reference string history

            if (frames.contains(num)) {
                hits++;
                step.put("hit_miss", "Hit");
            } else {
                if (frames.size() < numFrames) {
                    frames.add(num); // Add the page if there's space
                    misses++;
                    step.put("hit_miss", "Miss");
                } else {
                    // Find the page with the highest frequency
                    int maxFreq = -1;
                    int maxFreqNum = -1;
                    int earliestIndex = -1;

                    for (int frame : frames) {
                        int freq = Collections.frequency(tempNumbers, frame);
                        if (freq > maxFreq) {
                            maxFreq = freq;
                            maxFreqNum = frame;
                            earliestIndex = tempNumbers.indexOf(frame); // Track the earliest occurrence
                        } else if (freq == maxFreq) {
                            // If frequencies are equal, choose the one that came first
                            int currentIndex = tempNumbers.indexOf(frame);
                            if (currentIndex < earliestIndex) {
                                maxFreqNum = frame;
                                earliestIndex = currentIndex;
                            }
                        }
                    }

                    // Replace the page with the highest frequency (or the earliest if tied)
                    frames.set(frames.indexOf(maxFreqNum), num);
                    misses++;
                    step.put("hit_miss", "Miss");
                }
            }

            step.put("frames", new ArrayList<>(frames)); // Add current frames to the step
            result.add(step); // Add step to the result
        }

        Map<String, Object> output = new HashMap<>();
        output.put("result", result);
        output.put("total_faults", misses);
        return output;
    }
}