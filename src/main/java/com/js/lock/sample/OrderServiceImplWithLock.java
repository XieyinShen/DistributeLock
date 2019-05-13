package com.js.lock.sample;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderServiceImplWithLock implements OrderService {

	private static OrderCodeGenerator ocg = new OrderCodeGenerator(); // static is to simulate multi-tomcat uses 1 generator service

	private static Lock lock = new ReentrantLock(); // no static means every service has a lock, try add a static?

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
