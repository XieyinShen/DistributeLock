package com.study.distribute.lock.sample;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ConcurrentTestDemo {

	public static void main(String[] args) {

		// Currency # to simulate.
		int currency = 50;

		// 循环屏障
		CyclicBarrier cb = new CyclicBarrier(currency);

		OrderService orderService;
		
		// Test without/with lock, e.g.
		/**
		 * Thread-6 =============>2019-05-12-14-00-57-3
Thread-11 =============>2019-05-12-14-00-57-7
Thread-12 =============>2019-05-12-14-00-57-8
Thread-7 =============>2019-05-12-14-00-57-9
Thread-3 =============>2019-05-12-14-00-57-11
Thread-49 =============>2019-05-12-14-00-57-6
Thread-30 =============>2019-05-12-14-00-57-14
Thread-15 =============>2019-05-12-14-00-57-16
Thread-44 =============>2019-05-12-14-00-57-5
Thread-36 =============>2019-05-12-14-00-57-19
Thread-18 =============>2019-05-12-14-00-57-4
Thread-37 =============>2019-05-12-14-00-57-23
Thread-46 =============>2019-05-12-14-00-57-2
Thread-38 =============>2019-05-12-14-00-57-27
Thread-21 =============>2019-05-12-14-00-57-29
Thread-4 =============>2019-05-12-14-00-57-33
Thread-35 =============>2019-05-12-14-00-57-36
Thread-45 =============>2019-05-12-14-00-57-1
Thread-48 =============>2019-05-12-14-00-57-46
Thread-2 =============>2019-05-12-14-00-57-48
Thread-23 =============>2019-05-12-14-00-57-47
Thread-22 =============>2019-05-12-14-00-57-45
Thread-26 =============>2019-05-12-14-00-57-44
Thread-43 =============>2019-05-12-14-00-57-43
Thread-14 =============>2019-05-12-14-00-57-42
Thread-33 =============>2019-05-12-14-00-57-40
Thread-32 =============>2019-05-12-14-00-57-41
Thread-25 =============>2019-05-12-14-00-57-39
Thread-20 =============>2019-05-12-14-00-57-38
Thread-5 =============>2019-05-12-14-00-57-37
Thread-24 =============>2019-05-12-14-00-57-34
Thread-41 =============>2019-05-12-14-00-57-35
Thread-13 =============>2019-05-12-14-00-57-32
Thread-0 =============>2019-05-12-14-00-57-31
Thread-19 =============>2019-05-12-14-00-57-30
Thread-16 =============>2019-05-12-14-00-57-28
Thread-31 =============>2019-05-12-14-00-57-26
Thread-27 =============>2019-05-12-14-00-57-25
Thread-28 =============>2019-05-12-14-00-57-24
Thread-42 =============>2019-05-12-14-00-57-22
Thread-8 =============>2019-05-12-14-00-57-21
Thread-17 =============>2019-05-12-14-00-57-20
Thread-29 =============>2019-05-12-14-00-57-18
Thread-47 =============>2019-05-12-14-00-57-17
Thread-40 =============>2019-05-12-14-00-57-15
Thread-1 =============>2019-05-12-14-00-57-13
Thread-9 =============>2019-05-12-14-00-57-13
Thread-39 =============>2019-05-12-14-00-57-12
Thread-34 =============>2019-05-12-14-00-57-12 same order id here, wrong!!!
Thread-10 =============>2019-05-12-14-00-57-10
		 */
//		orderService = new OrderServiceImpl();
		
		// Test with JUC lock.
		orderService = new OrderServiceImplWithLock();

		for (int i = 0; i < currency; i++) {
			new Thread(new Runnable() {
				public void run() {

					System.out.println(Thread.currentThread().getName() + "---------I am ready---------------");

					try {
						cb.await(); // JUC programming here, all thread will wait for each each then start together!
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
					
					orderService.createOrder();
				}
			}).start();
		}
	}

}
