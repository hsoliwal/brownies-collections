/*
 * Copyright (c) 2026 Hitesh Soliwal and contributors.
 * SPDX-License-Identifier: Apache-2.0
 */
package org.magicwerk.brownies.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.magicwerk.brownies.collections.primitive.BooleanGapList;
import org.magicwerk.brownies.collections.primitive.ByteGapList;
import org.magicwerk.brownies.collections.primitive.CharGapList;
import org.magicwerk.brownies.collections.primitive.DoubleGapList;
import org.magicwerk.brownies.collections.primitive.FloatGapList;
import org.magicwerk.brownies.collections.primitive.IntGapList;
import org.magicwerk.brownies.collections.primitive.LongGapList;
import org.magicwerk.brownies.collections.primitive.ShortGapList;

/** Regression: growing a wrapped list retains values and the physical offset mapping. */
public class GapListOffsetGrowthTest {
    @Test
    public void objectArrayGrowth() {
        GapList<Object> list = new GapList<>(4);
        Object a = new Object(), b = new Object(), c = new Object();
        list.add(b);
        list.add(c);
        list.add(0, a);
        list.ensureCapacity(65);
        assertTrue(list.capacity() >= 65);
        assertEquals(3, list.size());
        assertSame(a, list.get(0));
        assertSame(b, list.get(1));
        assertSame(c, list.get(2));
    }

    @Test
    public void booleanArrayGrowth() {
        BooleanGapList list = new BooleanGapList(4);
        list.add(false);
        list.add(true);
        list.add(0, true);
        list.ensureCapacity(65);
        assertTrue(list.capacity() >= 65);
        assertTrue(list.get(0));
        assertTrue(!list.get(1));
        assertTrue(list.get(2));
    }

    @Test
    public void byteArrayGrowth() {
        ByteGapList list = new ByteGapList(4);
        list.add((byte) 10);
        list.add((byte) -7);
        list.add(0, (byte) 3);
        list.ensureCapacity(65);
        assertTrue(list.capacity() >= 65);
        assertEquals(3, list.get(0));
        assertEquals(10, list.get(1));
        assertEquals(-7, list.get(2));
    }

    @Test
    public void charArrayGrowth() {
        CharGapList list = new CharGapList(4);
        list.add('b');
        list.add('\u03bb');
        list.add(0, 'a');
        list.ensureCapacity(65);
        assertTrue(list.capacity() >= 65);
        assertEquals('a', list.get(0));
        assertEquals('b', list.get(1));
        assertEquals('\u03bb', list.get(2));
    }

    @Test
    public void doubleArrayGrowthPreservesNanPayload() {
        DoubleGapList list = new DoubleGapList(4);
        double nan = Double.longBitsToDouble(0x7ff8000000000001L);
        list.add(-0.0d);
        list.add(nan);
        list.add(0, 1.25d);
        list.ensureCapacity(65);
        assertTrue(list.capacity() >= 65);
        assertEquals(1.25d, list.get(0), 0d);
        assertEquals(Double.doubleToRawLongBits(-0.0d), Double.doubleToRawLongBits(list.get(1)));
        assertEquals(Double.doubleToRawLongBits(nan), Double.doubleToRawLongBits(list.get(2)));
    }

    @Test
    public void floatArrayGrowthPreservesNanPayload() {
        FloatGapList list = new FloatGapList(4);
        float nan = Float.intBitsToFloat(0x7fc00001);
        list.add(-0.0f);
        list.add(nan);
        list.add(0, 1.25f);
        list.ensureCapacity(65);
        assertTrue(list.capacity() >= 65);
        assertEquals(1.25f, list.get(0), 0f);
        assertEquals(Float.floatToRawIntBits(-0.0f), Float.floatToRawIntBits(list.get(1)));
        assertEquals(Float.floatToRawIntBits(nan), Float.floatToRawIntBits(list.get(2)));
    }

    @Test
    public void intArrayGrowth() {
        IntGapList list = new IntGapList(4);
        list.add(Integer.MIN_VALUE);
        list.add(Integer.MAX_VALUE);
        list.add(0, 17);
        list.ensureCapacity(65);
        assertTrue(list.capacity() >= 65);
        assertEquals(17, list.get(0));
        assertEquals(Integer.MIN_VALUE, list.get(1));
        assertEquals(Integer.MAX_VALUE, list.get(2));
    }

    @Test
    public void longArrayGrowth() {
        LongGapList list = new LongGapList(4);
        list.add(Long.MIN_VALUE);
        list.add(Long.MAX_VALUE);
        list.add(0, 17L);
        list.ensureCapacity(65);
        assertTrue(list.capacity() >= 65);
        assertEquals(17L, list.get(0));
        assertEquals(Long.MIN_VALUE, list.get(1));
        assertEquals(Long.MAX_VALUE, list.get(2));
    }

    @Test
    public void shortArrayGrowth() {
        ShortGapList list = new ShortGapList(4);
        list.add((short) 8);
        list.add((short) -3);
        list.add(0, (short) 17);
        list.ensureCapacity(65);
        assertTrue(list.capacity() >= 65);
        assertEquals(17, list.get(0));
        assertEquals(8, list.get(1));
        assertEquals(-3, list.get(2));
    }
}
