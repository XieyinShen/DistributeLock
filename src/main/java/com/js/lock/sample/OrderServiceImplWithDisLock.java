package com.js.lock.sample;

import java.util.concurrent.locks.Lock;

import com.js.lock.zk.ZKDistributeLock;

public class OrderServiceImplWithDisLock implements OrderService {

	private static OrderCodeGenerator ocg = new OrderCodeGenerator();

	@Override
	public void createOrder() {
		String orderCode = null;
		
		Lock lock = new ZKDistributeLock("/improvedzk");
		try {
			lock.lock();
			orderCode = ocg.getOrderCode();

		} finally {
			lock.unlock();
		}

		System.out.println(Thread.currentThread().getName() + " =============>" + orderCode);

		// Business goes here
		// ...

	}

}
