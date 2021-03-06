/*
 * Copyright 2017 Otavio Santana and others
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jnosql.artemis.cassandra.converters;

import org.jnosql.artemis.AttributeConverter;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;


public class TimestampConverterTest {

    private ZoneId defaultZoneId = ZoneId.systemDefault();

    private AttributeConverter<Object, Date> converter;

    @Before
    public void setUp() {
        converter = new TimestampConverter();
    }

    @Test
    public void shouldConvertoNumber() {
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Number number = date.getTime();
        java.time.LocalDate localDate = converter.convertToDatabaseColumn(number).toInstant()
                .atZone(defaultZoneId).toLocalDate();

        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), localDate.getDayOfMonth());
        assertEquals(calendar.get(Calendar.YEAR), localDate.getYear());
        assertEquals(calendar.get(Calendar.MONTH) + 1, localDate.getMonthValue());
    }

    @Test
    public void shouldConvertoDate() {
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        java.time.LocalDate localDate = converter.convertToDatabaseColumn(date).toInstant()
                .atZone(defaultZoneId).toLocalDate();
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), localDate.getDayOfMonth());
        assertEquals(calendar.get(Calendar.YEAR), localDate.getYear());
        assertEquals(calendar.get(Calendar.MONTH) + 1, localDate.getMonthValue());
    }

    @Test
    public void shouldConvertoCalendar() {

        Calendar calendar = Calendar.getInstance();
        java.time.LocalDate localDate = converter.convertToDatabaseColumn(calendar).toInstant()
                .atZone(defaultZoneId).toLocalDate();
        assertEquals(calendar.get(Calendar.DAY_OF_MONTH), localDate.getDayOfMonth());
        assertEquals(calendar.get(Calendar.YEAR), localDate.getYear());
        assertEquals(calendar.get(Calendar.MONTH) + 1, localDate.getMonthValue());
    }

    @Test
    public void shouldConvertoLocalDate() {

        java.time.LocalDate date = java.time.LocalDate.now();
        java.time.LocalDate localDate = converter.convertToDatabaseColumn(date).toInstant()
                .atZone(defaultZoneId).toLocalDate();
        assertEquals(date.getDayOfMonth(), localDate.getDayOfMonth());
        assertEquals(date.getYear(), localDate.getYear());
        assertEquals(date.getMonthValue(), localDate.getMonthValue());
    }


    @Test
    public void shouldConvertLocalDateTime() {

        LocalDateTime date = LocalDateTime.now();
        java.time.LocalDate localDate = converter.convertToDatabaseColumn(date).toInstant()
                .atZone(defaultZoneId).toLocalDate();
        assertEquals(date.getDayOfMonth(), localDate.getDayOfMonth());
        assertEquals(date.getYear(), localDate.getYear());
        assertEquals(date.getMonthValue(), localDate.getMonthValue());
    }

    @Test
    public void shouldConvertZonedDateTime() {

        ZonedDateTime date = ZonedDateTime.now();
        java.time.LocalDate localDate = converter.convertToDatabaseColumn(date).toInstant()
                .atZone(defaultZoneId).toLocalDate();
        assertEquals(date.getDayOfMonth(), localDate.getDayOfMonth());
        assertEquals(date.getYear(), localDate.getYear());
        assertEquals(date.getMonthValue(), localDate.getMonthValue());
    }
}