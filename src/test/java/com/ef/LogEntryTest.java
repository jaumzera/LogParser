package com.ef;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

public class LogEntryTest {

	@Test
	public void shouldParseALogLine() {
		LogEntry entry = LogEntry.of("2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200"
				+ "|\"swcd (unknown ) CFNetwork/808.2.16 Darwin/15.6.0\"");
		assertEquals(entry.getDate(), LocalDateTime.of(2017, 1, 1, 0, 0, 11, 763000000));
		assertEquals(entry.getIp(), "192.168.234.82");
		assertEquals(entry.getRequest(), "\"GET / HTTP/1.1\"");
		assertEquals(entry.getResponseCode(), 200);
		assertEquals(entry.getUserInfo(), "\"swcd (unknown ) CFNetwork/808.2.16 Darwin/15.6.0\"");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionForInvalidDate() {
		LogEntry.of("2017-13-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200"
				+ "|\"swcd (unknown ) CFNetwork/808.2.16 Darwin/15.6.0\"");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionForInvalidIp() {
		LogEntry.of("2017-01-01 00:00:11.763|192.xxx.234.82|\"GET / HTTP/1.1\"|200"
				+ "|\"swcd (unknown ) CFNetwork/808.2.16 Darwin/15.6.0\"");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionForInvalidResponseCode() {
		LogEntry.of("2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|2a0"
				+ "|\"swcd (unknown ) CFNetwork/808.2.16 Darwin/15.6.0\"");
	}

}
