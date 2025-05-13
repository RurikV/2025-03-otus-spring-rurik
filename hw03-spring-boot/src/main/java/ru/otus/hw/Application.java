package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.service.TestRunnerService;

import java.util.Locale;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class Application {
    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(Application.class, args);

        // Print system's default locale short name
        System.out.println("System's default locale: " + Locale.getDefault().toLanguageTag());

        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();

    }
}
