package com.example.currency.util;

import com.example.currency.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyHelper {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyHelper.class);

    public static String createQuery (Query query) {

        logger.info("CurrencyHelper.createQuery - query: " + query);

        StringBuilder builder = new StringBuilder();

        if (query.getPeriod().isPresent() && query.getDate().isPresent()) {
            logger.warn("Both time period and date criteria are supplied. Time period takes priority and date will be ignored");
        }

        if (query.getPeriod().isPresent()) {
            TimePeriod period = query.getPeriod().get();
            if(period.getStartAt().isPresent() && period.getEndAt().isPresent())
                builder.append("?start_at=" + period.getStartAt().get()).append("&end_at=" + period.getEndAt().get());
            else
                logger.warn("Both start_at and end_at criteria should be supplied");
        } else if (query.getDate().isPresent()) {
            builder.append("?date=" + query.getDate().get());
        }

        if (query.getBase().isPresent())
            builder.append("&base=" + query.getBase().get());

        if (query.getSymbols().isPresent())
            builder.append("&symbols=" + String.join(",", query.getSymbols().get()));

        if (builder.length() > 0 && builder.charAt(0) == '&') {
            builder.replace(0, 1, "?");
        }

        logger.info("CurrencyHelper.createQuery - response: " + builder.toString());

        return builder.toString();

    }

}
