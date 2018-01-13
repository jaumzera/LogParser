package com.ef;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

public class DurationTest {

	@Test
	public void shoudCreateADailyDuration() {
		LocalDateTime begin = LocalDateTime.of(2018, 1, 10, 20, 44);
		LocalDateTime end = begin.plusDays(1);
		Duration duration = Duration.daily(begin);
		assertEquals(duration.getBegin(), begin);
		assertEquals(duration.getEnd(), end);
		Duration parsedDuration = Duration.parse(begin, "daily");
		assertEquals(duration, parsedDuration);
	}
	
	@Test
	public void shoudCreateAHourlyDuration() {
		LocalDateTime begin = LocalDateTime.of(2018, 1, 10, 20, 44);
		LocalDateTime end = begin.plusHours(1);
		Duration duration = Duration.hourly(begin);
		assertEquals(duration.getBegin(), begin);
		assertEquals(duration.getEnd(), end);
		Duration parsedDuration = Duration.parse(begin, "hourly");
		assertEquals(duration, parsedDuration);
	}
}
