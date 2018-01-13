package com.ef;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

import lombok.extern.java.Log;

@Log
public class Parser {

	private final String outputMessage;

	private final File file;

	private final Duration duration;

	private final int threshold;

	private List<LogEntry> logEntries;
	
	private Map<String, Long> requestsByIp;

	private Set<BlockedIp> blockeds = new LinkedHashSet<>();

	public Parser(File file, Duration duration, int threshold) {
		this.file = file;
		this.duration = duration;
		this.threshold = threshold;
		this.outputMessage = "\n%s was blocked because it exceeded the limit of "
				+ String.format("%d requests between the period of %s and %s",
						threshold, 
						LogEntry.DATE_FORMATER.format(duration.getBegin()),
						LogEntry.DATE_FORMATER.format(duration.getEnd()));
	}

	public void parse() {
		log.info("Reading log file...");
		loadLogEntries(); 

		log.info("Counting requests by ips...");
		countRequestsByIps();

		log.info("Loading blockeds...");
		loadBlockeds();

		log.info("Persisting...");
		AccessLogDao.instance().save(logEntries);
		AccessLogDao.instance().save(blockeds);
		AccessLogDao.instance().dispose();
		log.info("Done.");
	}

	private void loadLogEntries() {
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			logEntries = br.lines().map(LogEntry::of).collect(Collectors.toList());
		} catch (IOException ex) { 
			log.log(Level.SEVERE, null, ex);
		}
	}

	private void countRequestsByIps() {
		requestsByIp = logEntries.parallelStream()
				.filter(entry -> duration.isRangeOf(entry.getDate()))
				.map(entry -> entry.getIp())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
				.entrySet().stream().filter(entry -> entry.getValue() >= threshold)
				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
	}

	private void loadBlockeds() {
		blockeds = requestsByIp.entrySet().stream().map(entry -> {
			BlockedIp blocked = BlockedIp.of(
					entry.getKey(),
					entry.getValue(),
					String.format(outputMessage, entry.getKey())); 
			System.out.println(blocked.toString());
			return blocked;
		}).collect(Collectors.toSet());
	}

	public static void main(String[] args) {
		try {
			ArgsParser argsParser = ArgsParser.of(args);
			
			File logfile;
			if(argsParser.contains("file")) {
				logfile = argsParser.getArgAsFile("file");
			} else {
				logfile = new File("./accesslog.log");
			}
			
			if(!logfile.exists()) {
				System.out.println("Log file not found.");
				System.exit(1);
			}
	
			LocalDateTime begin = argsParser.getArgAsLocalDateTime("startDate");
			Duration duration = Duration.hourly(begin);
	
			int threshold = argsParser.getArgAsInt("threshold");
	
			Parser parser = new Parser(logfile, duration, threshold);
			parser.parse();
		} catch(IllegalArgumentException ex) {
			log.log(Level.SEVERE,  null,  ex);
			System.out.println(ex.getMessage());
		}
	}
}
