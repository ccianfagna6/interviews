package com.cec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileProcessor {

    public static List<String> findByCountry(Reader reader, final String countryCode) {
        return findByCountry(lines(reader), countryCode).collect(Collectors.toList());
    }

    public static List<String> findByCountryAndResponseTimeAboutLimit(
            Reader reader, String countryCode, long responseTime) {
        return findByCountry(lines(reader), countryCode)
                .filter(responseTimeGreatherThan(responseTime))
                .collect(Collectors.toList());
    }

    public static List<String> findAboveAverage(Reader reader) throws IOException {
        List<String> lines = lines(reader).collect(Collectors.toList());
        Double average = lines.stream()
                .mapToLong(each -> Long.valueOf(each.split(",")[2]))
                .boxed()
                .collect(Collectors.averagingDouble(Double::valueOf));
        return lines.stream()
                .filter(each -> Long.valueOf(each.split(",")[2]).doubleValue() > average)
                .collect(Collectors.toList());
    }

    private static Stream<String> lines(Reader reader) {
        return new BufferedReader(reader).lines().filter(line -> !"NAME,COUNTRY_CODE,TIME".equals(line));
    }

    private static Stream<String> findByCountry(final Stream<String> stream, final String countryCode) {
        return stream.filter(countryCode(countryCode));
    }

    private static Predicate<String> responseTimeGreatherThan(final long responseTime) {
        return new Predicate<String>() {
            @Override
            public boolean test(String line) {
                final String[] columns = line.split(",");
                try{
                    return columns.length == 3 && Long.valueOf(columns[2]).longValue() > responseTime;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        };
    }

    private static Predicate<String> countryCode(final String countryCode) {
        return new Predicate<String>() {
            public boolean test(String line) {
                final String[] columns = line.split(",");
                return columns.length == 3 && columns[1].equalsIgnoreCase(countryCode);
            }
        };
    }
}
