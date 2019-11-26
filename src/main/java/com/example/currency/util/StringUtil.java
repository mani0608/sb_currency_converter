package com.example.currency.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtil {

    public static String convertToCamelCase(String toConvert) {

        toConvert = lowerFirstLetter(toConvert);

        List<String> sections = Arrays.asList(toConvert.split("\\s|_|-|,"));

        StringBuilder builder = new StringBuilder(sections.get(0));

        builder.append(sections.subList(1, sections.size())
                .stream().map(item -> upperFirstLetter(item)).collect(Collectors.joining()));

        return builder.toString();

    }

    public static String lowerFirstLetter(String input) {
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }

    public static String upperFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
