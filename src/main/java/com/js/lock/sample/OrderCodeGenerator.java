package com.js.lock.sample;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderCodeGenerator {

	private int i = 0;

	// Generate the order code by "year-month-day-hour-minute-second".
	public String getOrderCode() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-");
		return sdf.format(now) + (++i);
	}
}
