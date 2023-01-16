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
package com.jcstress_tests.tests.init.arrays.volatiles;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.JCStressMeta;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.ShortResult4;
import com.jcstress_tests.tests.init.Grading_AllZeroes;

@JCStressTest
@JCStressMeta(Grading_AllZeroes.class)
@State
public class ShortArrayInitTest {

    volatile short[] arr;

    @Actor
    public void actor1() {
        arr = new short[4];
    }

    @Actor
    public void actor2(ShortResult4 r) {
        short[] arr = this.arr;
        if (arr == null) {
            r.r1 = r.r2 = r.r3 = r.r4 = -1;
        }
        else {
            r.r1 = arr[0];
            r.r2 = arr[1];
            r.r3 = arr[2];
            r.r4 = arr[3];
        }
    }

}
