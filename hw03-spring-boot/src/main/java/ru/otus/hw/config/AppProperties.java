package ru.otus.hw.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;
import java.util.Map;

@Setter
@Getter
@ConfigurationProperties(prefix = "test")
public class AppProperties implements TestConfig, TestFileNameProvider, LocaleConfig {

    private int rightAnswersCountToPass;

    private Locale locale;

    private Map<String, String> fileNameByLocaleTag;

    private boolean fallbackToSystemLocale;

    public AppProperties() {
        // Default constructor for Spring to use when binding properties
    }

    public void setLocale(String locale) {
        this.locale = Locale.forLanguageTag(locale);
    }

    @Override
    public Locale getLocale() {
        if (locale != null) {
            return locale;
        }

        if (fallbackToSystemLocale) {
            return Locale.getDefault();
        } else {
            return Locale.ROOT;
        }
    }

    @Override
    public String getTestFileName() {
        // Get the locale (which will never be null now)
        Locale currentLocale = getLocale();

        String fileName = fileNameByLocaleTag.get(currentLocale.toLanguageTag());
        if (fileName == null) {
            // If no file is found for the current locale, use the default file (questions.csv)
            fileName = "questions.csv";
        }
        return fileName;
    }
}
