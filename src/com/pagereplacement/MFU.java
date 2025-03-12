package com.pagereplacement;

import java.util.*;

public class MFU {
    public static Map<String, Object> mfu(int[] numbers, int numFrames) {
        List<Integer> frames = new ArrayList<>();
        int hits = 0;
        int misses = 0;
        List<Map<String, Object>> result = new ArrayList<>();
        List<Integer> tempNumbers = new ArrayList<>();

        for (int num : numbers) {
            Map<String, Object> step = new HashMap<>();
            step.put("page", num);
            tempNumbers.add(num);

            if (frames.contains(num)) {
                hits++;
                step.put("hit_miss", "Hit");
            } else {
                if (frames.size() < numFrames) {
                    frames.add(num);
                    misses++;
                    step.put("hit_miss", "Miss");
                } else {
                    int maxFreq = -1;
                    int maxFreqNum = -1;
                    int earliestIndex = -1;

                    for (int frame : frames) {
                        int freq = Collections.frequency(tempNumbers, frame);
                        if (freq > maxFreq) {
                            maxFreq = freq;
                            maxFreqNum = frame;
                            earliestIndex = tempNumbers.indexOf(frame);
                        } else if (freq == maxFreq) {
                            int currentIndex = tempNumbers.indexOf(frame);
                            if (currentIndex < earliestIndex) {
                                maxFreqNum = frame;
                                earliestIndex = currentIndex;
                            }
                        }
                    }

                    frames.set(frames.indexOf(maxFreqNum), num);
                    misses++;
                    step.put("hit_miss", "Miss");
                }
            }

            step.put("frames", new ArrayList<>(frames));
            result.add(step);
        }

        Map<String, Object> output = new HashMap<>();
        output.put("result", result);
        output.put("total_faults", misses);
        return output;
    }
}