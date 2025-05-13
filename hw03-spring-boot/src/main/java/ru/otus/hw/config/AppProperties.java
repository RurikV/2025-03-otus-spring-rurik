package ru.otus.hw.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;
import java.util.Map;

@Setter
@ConfigurationProperties(prefix = "test")
public class AppProperties implements TestConfig, TestFileNameProvider, LocaleConfig {

    @Getter
    private int rightAnswersCountToPass;

    @Getter
    private Locale locale;

    @Getter
    private Map<String, String> fileNameByLocaleTag;

    @Getter
    private final boolean fallbackToSystemLocale;

    public AppProperties(@Value("${spring.messages.fallbackToSystemLocale:false}") boolean fallbackToSystemLocale) {
        this.fallbackToSystemLocale = fallbackToSystemLocale;
    }

    public void setLocale(String locale) {
        this.locale = Locale.forLanguageTag(locale);
    }

    @Override
    public String getTestFileName() {
        // Check if locale is null to avoid NullPointerException
        if (locale == null) {
            // If locale is null, use the default file
            return "questions.csv";
        }

        String fileName = fileNameByLocaleTag.get(locale.toLanguageTag());
        if (fileName == null) {
            // If no file is found for the current locale, use the default file (questions.csv)
            fileName = "questions.csv";
        }
        return fileName;
    }
}
