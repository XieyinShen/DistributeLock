package com.js.lock.sample;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Now service init is inside the loop, which is to simulate multi-thread to multi-server (cluster).
 * It's tested on a distributed.
 * 
 * @author I073287
 *
 */
public class ConcurrentTestDistributeDemo2 {

	public static void main(String[] args) {
		
//		int currency = 5;
//		CyclicBarrier cb = new CyclicBarrier(currency);
//		
//		for(int i=0; i<currency; i++) {
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					try {
//						cb.await();
//					} catch (InterruptedException | BrokenBarrierException e) {
//						
//						e.printStackTrace();
//					}
//					runUnderCluster();
//				}
//			}).start();
//		}
	}

	public static void runUnderCluster() {
		int currency = 20;

		CyclicBarrier cb = new CyclicBarrier(currency);

		for (int i = 0; i < currency; i++) {
			new Thread(new Runnable() {
				public void run() {
					
					/**
					 * Thread-48 =============>2019-05-12-14-24-34-20
Thread-32 =============>2019-05-12-14-24-34-19
Thread-36 =============>2019-05-12-14-24-34-18 still wrong even if it has lock here! why?
Thread-2 =============>2019-05-12-14-24-34-18
Thread-17 =============>2019-05-12-14-24-34-17
Thread-35 =============>2019-05-12-14-24-34-16
Thread-6 =============>2019-05-12-14-24-34-15

					 */
					OrderService orderService = new OrderServiceImplWithLock();
					
					// Improve with distribute-lock to try.
//					OrderService orderService = new OrderServiceImplWithDisLock();

					System.out.println(Thread.currentThread().getName() + "---------I am ready--------------");
		
					try {
						cb.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
					orderService.createOrder();
				}
			}).start();

		}
	}
}
