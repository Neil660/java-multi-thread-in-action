/*
 * Copyright (c) 2005, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.jcstress_tests.tests.fences;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IntResult3;
import sun.misc.Contended;

/**
 * Test if intervening write breaks reordering.
 *
 * @author Aleksey Shipilev (aleksey.shipilev@oracle.com)
 */
@JCStressTest
@Outcome(id = "[0, 0, 0]", expect = Expect.ACCEPTABLE, desc = "Consistent ordered reads.")
@Outcome(id = "[0, 0, 1]", expect = Expect.ACCEPTABLE, desc = "Consistent ordered reads.")
@Outcome(id = "[0, 1, 1]", expect = Expect.ACCEPTABLE, desc = "Consistent ordered reads.")
@Outcome(id = "[1, 0, 1]", expect = Expect.ACCEPTABLE, desc = "Consistent ordered reads.")
@Outcome(id = "[1, 1, 1]", expect = Expect.ACCEPTABLE, desc = "Consistent ordered reads.")
@Outcome(id = "[1, 1, 0]", expect = Expect.ACCEPTABLE_INTERESTING, desc = "Lost or reordered read of x.")
@Outcome(id = "[0, 1, 0]", expect = Expect.ACCEPTABLE_INTERESTING, desc = "Lost or reordered read of x.")
@Outcome(id = "[1, 0, 0]", expect = Expect.ACCEPTABLE_INTERESTING, desc = "Lost or reordered read of x.")
@State
public class UnfencedReadTwiceTest {

    @Contended
    int x;

    @Contended
    int y;

    @Actor
    public void actor1() {
        x = 1;
        y = 1;
    }

    @Actor
    public void actor2(IntResult3 r) {
        r.r1 = x;
        r.r2 = y;
        r.r3 = x;
    }

}
