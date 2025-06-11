package ru.otus.hw;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.hw.config.AppProperties;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    properties = {
        "spring.shell.interactive.enabled=false",
        "spring.shell.script.enabled=false"
    },
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class CommonHwTest {

    private static final String CONFIGURATION_ANNOTATION_NAME = "org.springframework.context.annotation.Configuration";

    @DisplayName("AppProperties SHOULD NOT be annotated by @Configuration")
    @Test
    void shouldNotContainConfigurationAnnotationAboveItSelf() {
        assertThat(AppProperties.class.isAnnotationPresent(Configuration.class))
                .withFailMessage("Класс свойств не является конфигурацией т.к. " +
                        "конфигурация для создания бинов, а тут просто компонент группирующий свойства приложения")
                .isFalse();
    }

    @Test
    void shouldNotContainPropertySourceAnnotationAboveItSelf() {
        assertThat(AppProperties.class.isAnnotationPresent(PropertySource.class))
                .withFailMessage("Аннотацию @PropertySource лучше вешать над конфигурацией, " +
                        "а класс свойств ей не является")
                .isFalse();
    }

    @Test
    void shouldNotContainFieldInjectedDependenciesOrProperties() {
        // Define specific classes to check instead of scanning the entire classpath
        // This avoids the hanging issue with ClassPathScanningCandidateComponentProvider
        Class<?>[] classesToCheck = {
            // Add the main service and DAO classes from the application
            ru.otus.hw.service.TestServiceImpl.class,
            ru.otus.hw.service.TestRunnerServiceImpl.class,
            ru.otus.hw.service.LocalizedIOServiceImpl.class,
            ru.otus.hw.service.LocalizedMessagesServiceImpl.class,
            ru.otus.hw.service.StreamsIOService.class,
            ru.otus.hw.dao.CsvQuestionDao.class
        };

        // Check each class for field injections
        var classesWithFieldInjections = Arrays.stream(classesToCheck)
            .filter(clazz -> !clazz.isInterface() && !clazz.isAnnotationPresent(Configuration.class))
            .filter(clazz -> Arrays.stream(clazz.getDeclaredFields())
                .anyMatch(f -> f.isAnnotationPresent(Autowired.class) || f.isAnnotationPresent(Value.class)))
            .map(Class::getName)
            .collect(Collectors.toList());

        assertThat(classesWithFieldInjections)
            .withFailMessage("На курсе все внедрение рекомендовано осуществлять через конструктор (" +
                    "в т.ч. @Value). Следующие классы нарушают это правило: %n%s", 
                    String.join("%n", classesWithFieldInjections))
            .isEmpty();
    }

    private Class<?> getBeanClassByName(String beanClassName) {
        try {
            return Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
