/*
 * Copyright 2026 Hitesh Soliwal and contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.magicwerk.brownies.collections;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Random;
import org.junit.Test;

/** Differential checks for one-reservation gap growth and retired-slot clearing. */
public class GapListArrayGrowthTest {
    @Test
    public void randomizedGapMovesAndCapacityGrowthPreserveLogicalOrder() {
        for (int initial : new int[] {0, 1, 2, 4, 7, 16}) {
            GapList<Integer> actual = new GapList<>(initial);
            ArrayList<Integer> reference = new ArrayList<>();
            Random random = new Random(0x4d334741504cL ^ initial);
            for (int iteration = 0; iteration < 2600; iteration++) {
                int operation = random.nextInt(100);
                if (reference.isEmpty() || operation < 52) {
                    int index = random.nextInt(reference.size() + 1);
                    Integer value = random.nextInt(7) == 0 ? null : Integer.valueOf(random.nextInt(300));
                    actual.add(index, value);
                    reference.add(index, value);
                } else if (operation < 80) {
                    int index = random.nextInt(reference.size());
                    assertEquals(reference.remove(index), actual.remove(index));
                } else if (operation < 92) {
                    int desired = reference.size() + random.nextInt(60);
                    actual.ensureCapacity(desired);
                } else {
                    actual.trimToSize();
                }
                assertEquals(reference.size(), actual.size());
                assertArrayEquals(reference.toArray(), actual.toArray());
                if (!reference.isEmpty()) {
                    int index = random.nextInt(reference.size());
                    assertEquals(reference.get(index), actual.get(index));
                }
            }
        }
    }

    @Test
    public void growthAfterFrontRemovalsPreservesWrappedEncounterOrder() {
        GapList<Integer> actual = new GapList<>(4);
        ArrayList<Integer> reference = new ArrayList<>();
        for (int i = 0; i < 800; i++) {
            actual.add(i);
            reference.add(i);
            if (i % 3 == 0 && !reference.isEmpty()) {
                assertEquals(reference.remove(0), actual.remove(0));
            }
            if (i % 7 == 0) actual.ensureCapacity(actual.size() + 25);
            assertArrayEquals(reference.toArray(), actual.toArray());
        }
    }
}
