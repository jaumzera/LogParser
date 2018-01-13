package com.ef;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

@Value
@EqualsAndHashCode
public class Duration {

	public static Duration daily(LocalDateTime begin) {
		return new Duration(begin, begin.plusDays(1));
	}

	public static Duration hourly(LocalDateTime begin) {
		return new Duration(begin, begin.plusHours(1));
	}

	public static Duration parse(LocalDateTime begin, String format) {
		switch (format) {
		case "daily":
			return Duration.daily(begin);
		case "hourly" : 
			return Duration.hourly(begin);
		default :
			throw new IllegalArgumentException("'" + format + "' must be 'daily' or 'hourly'");
		}
	}

	@Getter
	private final LocalDateTime begin;

	@Getter
	private final LocalDateTime end;

	public Duration(LocalDateTime begin, LocalDateTime end) {
		this.begin = begin;
		this.end = end;
	}

	public boolean isRangeOf(LocalDateTime date) {
		return date.isAfter(begin) && date.isBefore(end);
	}
}
