/*
 * Copyright (c) 2014, 2015, Oracle and/or its affiliates. All rights reserved.
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
package com.jcstress_tests.tests.vjug;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Description;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.BooleanResult2;

import java.util.BitSet;

@JCStressTest
@Description("Tests if racy update to BitSet experiences word tearing.")
@Outcome(id = "[true, false]", expect = Expect.ACCEPTABLE_INTERESTING, desc = "Thread 1 has intervened, word tearing occurred.")
@Outcome(id = "[false, true]", expect = Expect.ACCEPTABLE_INTERESTING, desc = "Thread 2 has intervened, word tearing occurred.")
@Outcome(id = "[true, true]", expect = Expect.ACCEPTABLE_INTERESTING, desc = "Observed both thread writes.")
@State
public class BitSetTest {

    final BitSet o = new BitSet();

    @Actor
    public void thread1() {
        o.set(0);
    }

    @Actor
    public void thread2() {
        o.set(1);
    }

    @Arbiter
    public void observe(BooleanResult2 res) {
        res.r1 = o.get(0);
        res.r2 = o.get(1);
    }

}
