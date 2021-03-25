package com.viettel.campaign.utils;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;

@Plugin(name = "thread", category = StrLookup.CATEGORY)
public class Log4j2ThreadLookup implements StrLookup {
    @Override
    public String lookup(String s) {
        return Thread.currentThread().getName();
    }

    @Override
    public String lookup(LogEvent event, String key) {
        // Check event first:
        if (event.getThreadName() != null) {
            return event.getThreadName();
        }
        // Fallback to key if event doesn't define a threadName:
        return this.lookup(key);
    }
}