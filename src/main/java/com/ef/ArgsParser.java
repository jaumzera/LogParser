package com.ef;

import java.io.File;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Value;

public class ArgsParser {
	
	public static final DateTimeFormatter DATE_FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");

	public static ArgsParser of(String[] args) {
		return new ArgsParser(args);
	}

	private ArgsParser(String[] args) {
		this.args = Arrays.stream(args).map(arg -> {
			String[] keyValuePair = arg.split("=");
			String key = keyValuePair[0].replaceAll("\\-\\-", "").trim();
			String value = keyValuePair[1].trim();
			return new Pair(key, value);
		}).collect(Collectors.toMap(pair -> pair.getKey(), pair -> pair.getValue()));
	}

	private final Map<String, String> args;
	
	public String getArgAsString(String argName) {
		checkIfExists(argName);
		return args.get(argName);
	}
	
	public int getArgAsInt(String argName) {
		checkIfExists(argName);
		try {
			return Integer.parseInt(args.get(argName));
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException("Invalid int number at --" + argName + ": " 
				+ args.get(argName), ex); 
		}
	}
	
	public LocalDateTime getArgAsLocalDateTime(String argName) {
		checkIfExists(argName);
		try {
			return LocalDateTime.parse(args.get(argName), DATE_FORMATER);
		} catch(DateTimeException ex) {
			throw new IllegalArgumentException("Invalid date format at --" + argName + ": " 
					+ args.get(argName), ex);
		}
	}
	
	public File getArgAsFile(String argName) {
		checkIfExists(argName);
		File file = new File(args.get(argName));
		if(!file.exists()) {
			throw new IllegalArgumentException("File does not exist: " + args.get(argName));
		}
		return file;
	}

	private void checkIfExists(String argName) {
		if(!args.containsKey(argName)) {
			throw new IllegalArgumentException("Argument not found: --" + argName);
		}
	}

	@Value
	static class Pair {
		private String key;
		private String value;
	}

}
