package ru.otus.hw.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.LocaleConfig;

import java.util.Locale;

@Service
public class LocalizedMessagesServiceImpl implements LocalizedMessagesService {

    private final LocaleConfig localeConfig;

    private final MessageSource messageSource;

    private final boolean fallbackToSystemLocale;

    private final Locale configLocale;

    public LocalizedMessagesServiceImpl(LocaleConfig localeConfig, MessageSource messageSource,
                                       @Value("${spring.messages.fallbackToSystemLocale:false}")
                                        boolean fallbackToSystemLocale,
                                       @Value("${test.locale}") String localeTag) {
        this.localeConfig = localeConfig;
        this.messageSource = messageSource;
        this.fallbackToSystemLocale = fallbackToSystemLocale;
        this.configLocale = Locale.forLanguageTag(localeTag);
    }

    @Override
    public String getMessage(String code, Object... args) {
        // Use the locale from application.yml directly
        if (configLocale != null) {
            return messageSource.getMessage(code, args, configLocale);
        }

        // Fallback to the locale from LocaleConfig if configLocale is null
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
