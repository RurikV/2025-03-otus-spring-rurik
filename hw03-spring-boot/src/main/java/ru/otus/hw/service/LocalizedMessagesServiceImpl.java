package ru.otus.hw.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.AppProperties;

import java.util.Locale;

@Service
public class LocalizedMessagesServiceImpl implements LocalizedMessagesService {

    private final AppProperties appProperties;

    private final MessageSource messageSource;

    public LocalizedMessagesServiceImpl(AppProperties appProperties, MessageSource messageSource) {
        this.appProperties = appProperties;
        this.messageSource = messageSource;
    }

    @Override
    public String getMessage(String code, Object... args) {
        // Get the locale from AppProperties
        Locale locale = appProperties.getLocale();

        if (locale != null) {
            return messageSource.getMessage(code, args, locale);
        }

        if (appProperties.isFallbackToSystemLocale()) {
            return messageSource.getMessage(code, args, Locale.getDefault());
        } else {
            return messageSource.getMessage(code, args, Locale.ROOT);
        }
    }
}
