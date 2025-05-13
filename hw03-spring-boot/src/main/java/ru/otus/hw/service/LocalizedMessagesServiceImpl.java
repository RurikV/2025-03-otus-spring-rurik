package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.LocaleConfig;

import java.util.Locale;

@Service
public class LocalizedMessagesServiceImpl implements LocalizedMessagesService {

    private final LocaleConfig localeConfig;

    private final MessageSource messageSource;

    private final boolean fallbackToSystemLocale;

    public LocalizedMessagesServiceImpl(LocaleConfig localeConfig, MessageSource messageSource,
                                       @Value("${spring.messages.fallbackToSystemLocale:false}") boolean fallbackToSystemLocale) {
        this.localeConfig = localeConfig;
        this.messageSource = messageSource;
        this.fallbackToSystemLocale = fallbackToSystemLocale;
    }

    @Override
    public String getMessage(String code, Object... args) {
        Locale locale = localeConfig.getLocale();

        if (locale != null) {
            return messageSource.getMessage(code, args, locale);
        }

        if (fallbackToSystemLocale) {
            return messageSource.getMessage(code, args, Locale.getDefault());
        } else {
            return messageSource.getMessage(code, args, Locale.ROOT);
        }
    }
}
