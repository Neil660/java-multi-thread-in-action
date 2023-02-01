package com.mtia.ch6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @Decription 清单6-12　保障对外包装对象的遍历操作的线程安全
 * @Author NEIL
 * @Date 2023/2/1 16:03
 * @Version 1.0
 */
public class SyncCollectionSafeTraversal {
    final List<String> syncList = Collections.synchronizedList(new ArrayList<>());

    //...

    public void dump() {
        Iterator<String> iterator = syncList.iterator();
        synchronized (syncList) {
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        }
    }
}
