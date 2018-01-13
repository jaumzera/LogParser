package com.ef;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.Test;

public class ArgsParserTest {
	
	@Test
	public void shouldReturnArgumentAsString() {
		ArgsParser args = ArgsParser.of(new String[] {"--someText=some-text-argument"});
		assertEquals("some-text-argument", args.getArgAsString("someText"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotReturnArgumentAsString() {
		ArgsParser args = ArgsParser.of(new String[0]);
		args.getArgAsFile("non-existent");
	}
	
	@Test
	public void shouldReturnArgumentAsInt() {
		ArgsParser args = ArgsParser.of(new String[] {"--someText=10"});
		assertEquals(10, args.getArgAsInt("someText"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotReturnArgumentAsInt() {
		ArgsParser args = ArgsParser.of(new String[0]);
		args.getArgAsFile("non-existent");
	}
	
	@Test
	public void shouldReturnArgumentAsLocalDateTime() {
		ArgsParser args = ArgsParser.of(new String[] {"--someText=2012-10-10.23:58:59"});
		assertEquals(LocalDateTime.of(2012, 10, 10, 23, 58, 59), args.getArgAsLocalDateTime("someText"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotReturnArgumentAsLocalDateTime() {
		ArgsParser args = ArgsParser.of(new String[0]);
		args.getArgAsFile("non-existent");
	}
	
	@Test
	public void shouldReturnArgumentAsFile() {
		File file = new File("/tmp/test");
		try {
			file.createNewFile();
		} catch (IOException e) {
			fail("Could not create the test file at /tmp/test");
		}
		ArgsParser args = ArgsParser.of(new String[] {"--someText=/tmp/test"});
		assertEquals(file.getName(), args.getArgAsFile("someText").getName());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldReturnArgumentAsFileIfFileDoesNotExist() {
		File file = new File("/tmp/" + System.currentTimeMillis() + "xxx");
		if(file.exists()) {
			fail("Delete file " + file.getName() + " to proceed the tests");
		}
		ArgsParser.of(new String[] {"--someText=" + file.getName()}).getArgAsFile("someText");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldNotReturnArgumentAsFile() {
		ArgsParser args = ArgsParser.of(new String[0]);
		args.getArgAsFile("non-existent");
	}
	
	@Test
	public void shouldCheckIfAArgWasSpecified() {
		ArgsParser args = ArgsParser.of(new String[] {"--someText=xxx"});
		assertTrue(args.contains("someText"));
		assertFalse(args.contains("noArgs"));
	}
}
