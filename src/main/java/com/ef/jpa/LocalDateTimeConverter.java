package com.ef.jpa;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date>{

	@Override
	public Date convertToDatabaseColumn(LocalDateTime attribute) {
		return Date.from(attribute.atZone(ZoneId.systemDefault()).toInstant());
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Date dbData) {
		Instant instant = Instant.ofEpochMilli(dbData.getTime());
	    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

}
