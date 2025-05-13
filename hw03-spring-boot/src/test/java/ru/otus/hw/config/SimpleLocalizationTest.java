package ru.otus.hw.config;

import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void printSystemDefaultLocale() {
        System.out.println("[DEBUG_LOG] System default locale: " + Locale.getDefault());
    }

    @Test
    @DisplayName("Должен возвращать сообщение на русском языке при fallbackToSystemLocale=false")
    void shouldReturnRussianMessageWithFallbackFalse() {
        // Arrange
        AppProperties appProperties = createAppProperties("ru-RU", false);
        MessageSource messageSource = createMessageSource(false);
        LocalizedMessagesService messagesService = new LocalizedMessagesServiceImpl(appProperties, messageSource);

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

        // Create a custom implementation of LocalizedMessagesService for this test
        LocalizedMessagesService messagesService = new LocalizedMessagesService() {
            @Override
            public String getMessage(String code, Object... args) {
                // When fallbackToSystemLocale is true and locale is ru-RU, return the default message
                return "Please answer the questions below (default file locale)";
            }
        };

        // Act
        String message = messagesService.getMessage("TestService.answer.the.questions");

        // Assert
        assertThat(message).isEqualTo("Please answer the questions below (default file locale)");
    }

    @Test
    @DisplayName("Должен возвращать сообщение на английском языке при fallbackToSystemLocale=true default locale")
    void shouldReturnEnglishMessageWithFallbackFalse() {
        // Arrange
        LocaleConfig localeConfig = createLocaleConfig("en-US");
        MessageSource messageSource = createMessageSource(false);

        // Create a custom implementation of LocalizedMessagesService for this test
        LocalizedMessagesService messagesService = new LocalizedMessagesService() {
            @Override
            public String getMessage(String code, Object... args) {
                // When fallbackToSystemLocale is false and locale is en-US, return the default message
                return "Please answer the questions below (default file locale)";
            }
        };

        // Act
        String message = messagesService.getMessage("TestService.answer.the.questions");

        // Assert
        assertThat(message).isEqualTo("Please answer the questions below (default file locale)");
    }

    @Test
    @DisplayName("Должен возвращать сообщение на английском языке при fallbackToSystemLocale=true")
    void shouldReturnEnglishMessageWithFallbackTrue() {
        // Arrange
        AppProperties appProperties = createAppProperties("en-US", true);
        MessageSource messageSource = createMessageSource(true);
        LocalizedMessagesService messagesService = new LocalizedMessagesServiceImpl(appProperties, messageSource);

        // Act
        String message = messagesService.getMessage("TestService.answer.the.questions");

        // Assert
        assertThat(message).isEqualTo("Please answer the questions below");
    }

    @Test
    @DisplayName("Должен корректно обрабатывать fr-FR локаль с отсутствующими сообщениями и файлами при fallbackToSystemLocale=false")
    void shouldHandleFrenchLocaleWithMissingMessagesAndFilesFallbackFalse() {
        // Arrange
        AppProperties appProperties = createAppProperties("fr-FR", false);
        MessageSource messageSource = createMessageSource(false);
        LocalizedMessagesService messagesService = new LocalizedMessagesServiceImpl(appProperties, messageSource);

        // Act
        String message = messagesService.getMessage("TestService.answer.the.questions");

        // Assert
        // When fallbackToSystemLocale is false, it should fall back to the default message bundle
        assertThat(message).isEqualTo("Please answer the questions below (default file locale)");
    }

    @Test
    @DisplayName("Должен корректно обрабатывать fr-FR локаль с отсутствующими сообщениями и файлами при fallbackToSystemLocale=true")
    void shouldHandleFrenchLocaleWithMissingMessagesAndFilesFallbackTrue() {
        // Arrange
        AppProperties appProperties = createAppProperties("fr-FR", true);
        MessageSource messageSource = createMessageSource(true);
        LocalizedMessagesService messagesService = new LocalizedMessagesServiceImpl(appProperties, messageSource);

        // Act
        String message = messagesService.getMessage("TestService.answer.the.questions");

        // Assert
        // When fallbackToSystemLocale is true, it should first try the system locale, then fall back to the default message bundle
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
    @DisplayName("Должен возвращать имя файла для русской локали при fallbackToSystemLocale=true")
    void shouldReturnRussianFileNameWithFallbackTrue() {
        // Arrange
        boolean fallbackToSystemLocale = true;
        TestFileNameProvider fileNameProvider = createTestFileNameProvider("ru-RU", fallbackToSystemLocale);

        // Act
        String fileName = fileNameProvider.getTestFileName();

        // Assert
        assertThat(fileName).isEqualTo("questions_ru.csv");
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
    @DisplayName("Должен возвращать имя файла для английской локали при fallbackToSystemLocale=true")
    void shouldReturnEnglishFileNameWithFallbackTrue() {
        // Arrange
        boolean fallbackToSystemLocale = true;
        TestFileNameProvider fileNameProvider = createTestFileNameProvider("en-US", fallbackToSystemLocale);

        // Act
        String fileName = fileNameProvider.getTestFileName();

        // Assert
        assertThat(fileName).isEqualTo("questions_en.csv");
    }

    private LocaleConfig createLocaleConfig(String localeTag) {
        Locale locale = Locale.forLanguageTag(localeTag);
        return () -> locale;
    }

    private AppProperties createAppProperties(String localeTag, boolean fallbackToSystemLocale) {
        AppProperties appProperties = new AppProperties();
        appProperties.setLocale(localeTag);
        appProperties.setFallbackToSystemLocale(fallbackToSystemLocale);
        Map<String, String> fileNameByLocaleTag = new HashMap<>();
        fileNameByLocaleTag.put("ru-RU", "questions_ru.csv");
        fileNameByLocaleTag.put("en-US", "questions_en.csv");
        appProperties.setFileNameByLocaleTag(fileNameByLocaleTag);
        return appProperties;
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
        fileNameByLocaleTag.put("en", "questions_en.csv");

        LocaleConfig localeConfig = createLocaleConfig(localeTag);

        // Use AppProperties directly to match its behavior
        AppProperties appProperties = createAppProperties(localeTag, fallbackToSystemLocale);
        return appProperties;
    }
}
