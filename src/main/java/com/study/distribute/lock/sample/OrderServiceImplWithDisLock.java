package com.study.distribute.lock.sample;

import java.util.concurrent.locks.Lock;

import com.study.distribute.lock.zk.ZKDistributeImproveLock;

public class OrderServiceImplWithDisLock implements OrderService {

	private static OrderCodeGenerator ocg = new OrderCodeGenerator();

	@Override
	public void createOrder() {
		String orderCode = null;
		
		Lock lock = new ZKDistributeImproveLock("/improvedzk");
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
