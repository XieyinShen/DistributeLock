package com.study.distribute.lock.sample;

import java.util.concurrent.locks.Lock;

import com.study.distribute.lock.zk.ZKDistributeLock;

public class OrderServiceImplWithZKLock implements OrderService {

	private static OrderCodeGenerator ocg = new OrderCodeGenerator();

	private Lock lock = new ZKDistributeLock("/order_lock");

	@Override
	public void createOrder() {

		String orderCode = null;
		try {
			lock.lock();
			orderCode = ocg.getOrderCode();

		} finally {
			lock.unlock();
		}

		System.out.println(Thread.currentThread().getName() + " =============>" + orderCode);

		// Business code goes here.
		// 

	}

}
