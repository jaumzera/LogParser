package com.ef;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ef.jpa.LocalDateTimeConverter;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "log_entries")
public class LogEntry {
	private static final String SEPARATOR = "\\|";

	public static final DateTimeFormatter DATE_FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	private static final String IP_PATTERN = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";

	public static LogEntry of(String logLine) {
		try {
			String[] parts = logLine.split(SEPARATOR);
			if(parts.length != 5) {
				throw new IllegalArgumentException("Invalid log entry format: " + logLine);
			}
			
			LocalDateTime date = LocalDateTime.parse(parts[0], DATE_FORMATER);

			if(!Pattern.matches(IP_PATTERN, parts[1])) {
				throw new IllegalArgumentException("Can't parse IP");
			}

			String ip = parts[1];
			String request = parts[2];
			int responseCode = Integer.parseInt(parts[3]);
			String userInfo = parts[4];
			return new LogEntry(date, ip, request, responseCode, userInfo);
		} catch(DateTimeParseException ex ) {
			throw new IllegalArgumentException("Can't parse date", ex);
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException("Can't parse response code", ex);
		} catch(Exception ex) {
			throw new IllegalArgumentException("Can't parse log line: " + logLine, ex);
		}
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Integer id;
	
	private String ip;

	@Convert(converter = LocalDateTimeConverter.class)
	private LocalDateTime date;

	private String request;

	@Column(name = "response_code")
	private int responseCode;

	@Column(name = "user_info")
	private String userInfo;
	
	private LogEntry() {
		// Required by Hibernate
	}

	private LogEntry(LocalDateTime date, String ip, String request, int responseCode, String userInfo) {
		this.date = date;
		this.ip = ip;
		this.request = request;
		this.responseCode = responseCode;
		this.userInfo = userInfo;
	}
}
