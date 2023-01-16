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
package com.jcstress_tests.tests.atomics.booleans;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Description;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.IntResult1;

import java.util.concurrent.atomic.AtomicBoolean;

@JCStressTest
@Description("Tests the visibility of AtomicBoolean initial value.")
@Outcome(id = "[-1]", expect = Expect.ACCEPTABLE, desc = "Seeing null AtomicX, this is a legal race.")
@Outcome(id = "[1]", expect = Expect.ACCEPTABLE, desc = "Acceptable to see the initial value.")
@Outcome(id = "[0]", expect = Expect.ACCEPTABLE, desc = "Acceptable to see a default value!")
@State
public class AtomicBooleanInitialValueTest {

    public AtomicBoolean ai;

    @Actor
    public void actor1() {
        ai = new AtomicBoolean(true);
    }

    @Actor
    public void actor2(IntResult1 r) {
        AtomicBoolean ai = this.ai;
        r.r1 = (ai == null) ? -1 : (ai.get() ? 1 : 0);
    }

}
