package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Primary
public class NoFallbackTestFileNameProvider implements TestFileNameProvider {

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

        return "questions.csv";
    }
}
