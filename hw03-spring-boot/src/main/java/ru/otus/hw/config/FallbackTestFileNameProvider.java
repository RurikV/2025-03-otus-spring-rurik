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
        var locale = localeConfig.getLocale();
        if (locale == null) {
            return "questions.csv";
        }

        String fileName = appProperties.getFileNameByLocaleTag().get(locale.toLanguageTag());
        if (fileName != null) {
            return fileName;
        }

        fileName = appProperties.getFileNameByLocaleTag().get(Locale.getDefault().toLanguageTag());
        if (fileName != null) {
            return fileName;
        }

        return "questions.csv";
    }
}
