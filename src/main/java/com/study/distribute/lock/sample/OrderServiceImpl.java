package com.study.distribute.lock.sample;

public class OrderServiceImpl implements OrderService {

	private OrderCodeGenerator ocg = new OrderCodeGenerator();

	@Override
	public void createOrder() {

		// Get order code.
		String orderCode = ocg.getOrderCode();

		System.out.println(Thread.currentThread().getName() + " =============>"
				+ orderCode);

		// Business code goes here.
		// ...
		// ...

	}

}
