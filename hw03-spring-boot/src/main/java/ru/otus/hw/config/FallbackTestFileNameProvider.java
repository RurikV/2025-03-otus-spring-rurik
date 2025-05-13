package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class FallbackTestFileNameProvider implements TestFileNameProvider {

    private final LocaleConfig localeConfig;

    private final AppProperties appProperties;

    @Override
    public String getTestFileName() {
        // If fallbackToSystemLocale is true, always return "questions.csv"
        if (appProperties.isFallbackToSystemLocale()) {
            return "questions.csv";
        }

        // If fallbackToSystemLocale is false, try to find a file for the provided locale
        var locale = localeConfig.getLocale();
        if (locale == null) {
            return "questions.csv";
        }

        String fileName = appProperties.getFileNameByLocaleTag().get(locale.toLanguageTag());
        if (fileName != null) {
            return fileName;
        }

        // If no file is found for the provided locale, return "questions.csv"
        return "questions.csv";
    }
}
