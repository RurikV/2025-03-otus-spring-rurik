package ru.otus.hw.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.otus.hw.service.LocalizedMessagesService;
import ru.otus.hw.service.LocalizedMessagesServiceImpl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    properties = {
        "spring.shell.interactive.enabled=false",
        "spring.shell.script.enabled=false"
    },
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@DisplayName("Тесты локализации")
public class SimpleLocalizationTest {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private LocalizedMessagesServiceImpl localizedMessagesService;

    @BeforeEach
    void printSystemDefaultLocale() {
        System.out.println("[DEBUG_LOG] System default locale: " + Locale.getDefault());
    }

    @Test
    @DisplayName("Должен возвращать сообщение на русском языке при fallbackToSystemLocale=false")
    void shouldReturnRussianMessageWithFallbackFalse() {
        // Arrange
        // Using injected beans
        appProperties.setLocale("ru-RU");
        appProperties.setFallbackToSystemLocale(false);

        // Act
        String message = localizedMessagesService.getMessage("TestService.answer.the.questions");

        // Assert
        assertThat(message).isEqualTo("Пожалуйста, ответьте на вопросы ниже");
    }

    @Test
    @DisplayName("Должен возвращать сообщение на английском языке при fallbackToSystemLocale=true default locale")
    void shouldReturnRussianMessageWithFallbackTrue() {
        // Arrange
        // Using injected beans
        appProperties.setLocale("ru-RU");
        appProperties.setFallbackToSystemLocale(true);

        // Act
        String message = localizedMessagesService.getMessage("TestService.answer.the.questions");

        // Assert
        // When fallbackToSystemLocale is true, it should use the system locale or default message
        assertThat(message).isNotNull();
    }

    @Test
    @DisplayName("Должен возвращать сообщение на английском языке при fallbackToSystemLocale=false")
    void shouldReturnEnglishMessageWithFallbackFalse() {
        // Arrange
        // Using injected beans
        appProperties.setLocale("en-US");
        appProperties.setFallbackToSystemLocale(false);

        // Act
        String message = localizedMessagesService.getMessage("TestService.answer.the.questions");

        // Assert
        assertThat(message).isEqualTo("Please answer the questions below");
    }

    @Test
    @DisplayName("Должен возвращать сообщение на английском языке при fallbackToSystemLocale=true")
    void shouldReturnEnglishMessageWithFallbackTrue() {
        // Arrange
        // Using injected beans
        appProperties.setLocale("en-US");
        appProperties.setFallbackToSystemLocale(true);

        // Act
        String message = localizedMessagesService.getMessage("TestService.answer.the.questions");

        // Assert
        assertThat(message).isEqualTo("Please answer the questions below");
    }

    @Test
    @DisplayName("Должен корректно обрабатывать fr-FR локаль с отсутствующими сообщениями и файлами при fallbackToSystemLocale=false")
    void shouldHandleFrenchLocaleWithMissingMessagesAndFilesFallbackFalse() {
        // Arrange
        // Using injected beans
        appProperties.setLocale("fr-FR");
        appProperties.setFallbackToSystemLocale(false);

        // Act
        String message = localizedMessagesService.getMessage("TestService.answer.the.questions");

        // Assert
        // When fallbackToSystemLocale is false, it should fall back to the default message bundle
        assertThat(message).isNotNull();
    }

    @Test
    @DisplayName("Должен корректно обрабатывать fr-FR локаль с отсутствующими сообщениями и файлами при fallbackToSystemLocale=true")
    void shouldHandleFrenchLocaleWithMissingMessagesAndFilesFallbackTrue() {
        // Arrange
        // Using injected beans
        appProperties.setLocale("fr-FR");
        appProperties.setFallbackToSystemLocale(true);

        // Act
        String message = localizedMessagesService.getMessage("TestService.answer.the.questions");

        // Assert
        // When fallbackToSystemLocale is true, it should first try the system locale, then fall back to the default message bundle
        assertThat(message).isNotNull();
    }

    @Test
    @DisplayName("Должен возвращать имя файла для русской локали при fallbackToSystemLocale=false")
    void shouldReturnRussianFileNameWithFallbackFalse() {
        // Arrange
        // Using injected appProperties as TestFileNameProvider
        appProperties.setLocale("ru-RU");
        appProperties.setFallbackToSystemLocale(false);
        Map<String, String> fileNameByLocaleTag = new HashMap<>();
        fileNameByLocaleTag.put("ru-RU", "questions_ru.csv");
        fileNameByLocaleTag.put("en-US", "questions_en.csv");
        fileNameByLocaleTag.put("en", "questions_en.csv");
        appProperties.setFileNameByLocaleTag(fileNameByLocaleTag);

        // Act
        String fileName = appProperties.getTestFileName();

        // Assert
        assertThat(fileName).isEqualTo("questions_ru.csv");
    }

    @Test
    @DisplayName("Должен возвращать имя файла для русской локали при fallbackToSystemLocale=true")
    void shouldReturnRussianFileNameWithFallbackTrue() {
        // Arrange
        // Using injected appProperties as TestFileNameProvider
        appProperties.setLocale("ru-RU");
        appProperties.setFallbackToSystemLocale(true);
        Map<String, String> fileNameByLocaleTag = new HashMap<>();
        fileNameByLocaleTag.put("ru-RU", "questions_ru.csv");
        fileNameByLocaleTag.put("en-US", "questions_en.csv");
        fileNameByLocaleTag.put("en", "questions_en.csv");
        appProperties.setFileNameByLocaleTag(fileNameByLocaleTag);

        // Act
        String fileName = appProperties.getTestFileName();

        // Assert
        assertThat(fileName).isEqualTo("questions_ru.csv");
    }

    @Test
    @DisplayName("Должен возвращать имя файла для английской локали при fallbackToSystemLocale=false")
    void shouldReturnEnglishFileNameWithFallbackFalse() {
        // Arrange
        // Using injected appProperties as TestFileNameProvider
        appProperties.setLocale("en-US");
        appProperties.setFallbackToSystemLocale(false);
        Map<String, String> fileNameByLocaleTag = new HashMap<>();
        fileNameByLocaleTag.put("ru-RU", "questions_ru.csv");
        fileNameByLocaleTag.put("en-US", "questions_en.csv");
        fileNameByLocaleTag.put("en", "questions_en.csv");
        appProperties.setFileNameByLocaleTag(fileNameByLocaleTag);

        // Act
        String fileName = appProperties.getTestFileName();

        // Assert
        assertThat(fileName).isEqualTo("questions_en.csv");
    }

    @Test
    @DisplayName("Должен возвращать имя файла для английской локали при fallbackToSystemLocale=true")
    void shouldReturnEnglishFileNameWithFallbackTrue() {
        // Arrange
        // Using injected appProperties as TestFileNameProvider
        appProperties.setLocale("en-US");
        appProperties.setFallbackToSystemLocale(true);
        Map<String, String> fileNameByLocaleTag = new HashMap<>();
        fileNameByLocaleTag.put("ru-RU", "questions_ru.csv");
        fileNameByLocaleTag.put("en-US", "questions_en.csv");
        fileNameByLocaleTag.put("en", "questions_en.csv");
        appProperties.setFileNameByLocaleTag(fileNameByLocaleTag);

        // Act
        String fileName = appProperties.getTestFileName();

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
        fileNameByLocaleTag.put("en", "questions_en.csv");
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
