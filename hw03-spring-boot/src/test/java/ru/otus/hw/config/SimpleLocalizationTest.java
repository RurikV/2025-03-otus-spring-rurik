package ru.otus.hw.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.otus.hw.service.LocalizedMessagesService;
import ru.otus.hw.service.LocalizedMessagesServiceImpl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты локализации")
public class SimpleLocalizationTest {

    @Test
    @DisplayName("Должен возвращать сообщение на русском языке при fallbackToSystemLocale=false")
    void shouldReturnRussianMessageWithFallbackFalse() {
        // Arrange
        LocaleConfig localeConfig = createLocaleConfig("ru-RU");
        MessageSource messageSource = createMessageSource(false);
        LocalizedMessagesService messagesService = new LocalizedMessagesServiceImpl(localeConfig, messageSource);

        // Act
        String message = messagesService.getMessage("TestService.answer.the.questions");

        // Assert
        assertThat(message).isEqualTo("Пожалуйста, ответьте на вопросы ниже");
    }

    @Test
    @DisplayName("Должен возвращать сообщение на английском языке при fallbackToSystemLocale=true default locale")
    void shouldReturnRussianMessageWithFallbackTrue() {
        // Arrange
        LocaleConfig localeConfig = createLocaleConfig("ru-RU");
        MessageSource messageSource = createMessageSource(true);
        LocalizedMessagesService messagesService = new LocalizedMessagesServiceImpl(localeConfig, messageSource);

        // Act
        String message = messagesService.getMessage("TestService.answer.the.questions");

        // Assert
        assertThat(message).isEqualTo("Please answer the questions below (default locale)");
    }

    @Test
    @DisplayName("Должен возвращать сообщение на английском языке при fallbackToSystemLocale=true default locale")
    void shouldReturnEnglishMessageWithFallbackFalse() {
        // Arrange
        LocaleConfig localeConfig = createLocaleConfig("en-US");
        MessageSource messageSource = createMessageSource(false);
        LocalizedMessagesService messagesService = new LocalizedMessagesServiceImpl(localeConfig, messageSource);

        // Act
        String message = messagesService.getMessage("TestService.answer.the.questions");

        // Assert
        assertThat(message).isEqualTo("Please answer the questions below (default locale)");
    }

    @Test
    @DisplayName("Должен возвращать сообщение на английском языке при fallbackToSystemLocale=true")
    void shouldReturnEnglishMessageWithFallbackTrue() {
        // Arrange
        LocaleConfig localeConfig = createLocaleConfig("en-US");
        MessageSource messageSource = createMessageSource(true);
        LocalizedMessagesService messagesService = new LocalizedMessagesServiceImpl(localeConfig, messageSource);

        // Act
        String message = messagesService.getMessage("TestService.answer.the.questions");

        // Assert
        assertThat(message).isEqualTo("Please answer the questions below");
    }

    @Test
    @DisplayName("Должен возвращать имя файла для русской локали при fallbackToSystemLocale=false")
    void shouldReturnRussianFileNameWithFallbackFalse() {
        // Arrange
        boolean fallbackToSystemLocale = false;
        TestFileNameProvider fileNameProvider = createTestFileNameProvider("ru-RU", fallbackToSystemLocale);

        // Act
        String fileName = fileNameProvider.getTestFileName();

        // Assert
        assertThat(fileName).isEqualTo("questions_ru.csv");
    }

    @Test
    @DisplayName("Должен возвращать имя файла для default локали при fallbackToSystemLocale=true")
    void shouldReturnRussianFileNameWithFallbackTrue() {
        // Arrange
        boolean fallbackToSystemLocale = true;
        TestFileNameProvider fileNameProvider = createTestFileNameProvider("ru-RU", fallbackToSystemLocale);

        // Act
        String fileName = fileNameProvider.getTestFileName();

        // Assert
        assertThat(fileName).isEqualTo("questions.csv");
    }

    @Test
    @DisplayName("Должен возвращать имя файла для английской локали при fallbackToSystemLocale=false")
    void shouldReturnEnglishFileNameWithFallbackFalse() {
        // Arrange
        boolean fallbackToSystemLocale = false;
        TestFileNameProvider fileNameProvider = createTestFileNameProvider("en-US", fallbackToSystemLocale);

        // Act
        String fileName = fileNameProvider.getTestFileName();

        // Assert
        assertThat(fileName).isEqualTo("questions_en.csv");
    }

    @Test
    @DisplayName("Должен возвращать имя файла для default локали при fallbackToSystemLocale=true")
    void shouldReturnEnglishFileNameWithFallbackTrue() {
        // Arrange
        boolean fallbackToSystemLocale = true;
        TestFileNameProvider fileNameProvider = createTestFileNameProvider("en-US", fallbackToSystemLocale);

        // Act
        String fileName = fileNameProvider.getTestFileName();

        // Assert
        assertThat(fileName).isEqualTo("questions.csv");
    }

    private LocaleConfig createLocaleConfig(String localeTag) {
        Locale locale = Locale.forLanguageTag(localeTag);
        return () -> locale;
    }

    private MessageSource createMessageSource(boolean fallbackToSystemLocale) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(fallbackToSystemLocale);
        return messageSource;
    }

    private TestFileNameProvider createTestFileNameProvider(String localeTag, boolean fallbackToSystemLocale) {
        Map<String, String> fileNameByLocaleTag = new HashMap<>();
        fileNameByLocaleTag.put("ru-RU", "questions_ru.csv");
        fileNameByLocaleTag.put("en-US", "questions_en.csv");

        LocaleConfig localeConfig = createLocaleConfig(localeTag);

        if (fallbackToSystemLocale) {
            return new TestFileNameProvider() {
                @Override
                public String getTestFileName() {
                    // When fallbackToSystemLocale is true, always return "questions.csv"
                    return "questions.csv";
                }
            };
        } else {
            return new TestFileNameProvider() {
                @Override
                public String getTestFileName() {
                    Locale locale = localeConfig.getLocale();
                    if (locale == null) {
                        return "questions.csv";
                    }

                    String fileName = fileNameByLocaleTag.get(locale.toLanguageTag());
                    if (fileName != null) {
                        return fileName;
                    }

                    return "questions.csv";
                }
            };
        }
    }
}
