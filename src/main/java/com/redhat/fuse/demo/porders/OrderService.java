/*
 * Copyright 2005-2016 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.redhat.fuse.demo.porders;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OrderService {

    private final AtomicInteger counter = new AtomicInteger();

    private final Random qty = new Random();

    public PurchaseOrder generateOrder() {
        PurchaseOrder order = new PurchaseOrder();
        // order.setId(counter.incrementAndGet());
	counter.incrementAndGet();
        order.setItem(counter.get() % 2 == 0 ? "1/4-Inch" : "1/2-Inch");
	order.setPrice(counter.get() % 2 == 0 ? new Float(1.23) : new Float(1.44));
        order.setQuantity(qty.nextInt(10) + 1);
        order.setDescription("Flat-Head Screw");
   	order.setCname(counter.get() % 2 == 0 ? "Assembly-line-1" : "Assembly-line-2");
   	order.setDcode(counter.get() % 2 == 0 ? "OAB" : "B01");
	order.setOrigin("SAP ERP");
        return order;
    }

/*
    public Order rowToOrder(Map<String, Object> row) {
        Order order = new Order();
        order.setId((Integer) row.get("id"));
        order.setItem((String) row.get("item"));
        order.setAmount((Integer) row.get("amount"));
        order.setDescription((String) row.get("description"));
        order.setProcessed((Boolean) row.get("processed"));
        return order;
    }
*/
}
