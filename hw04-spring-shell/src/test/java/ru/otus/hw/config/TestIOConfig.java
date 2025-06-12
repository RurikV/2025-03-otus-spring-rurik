package ru.otus.hw.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import ru.otus.hw.service.IOService;

@SpringBootTest(
        properties = {
                "spring.shell.interactive.enabled=false",
                "spring.shell.script.enabled=false"
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@TestConfiguration
public class TestIOConfig {

    @Bean
    @Primary
    public IOService ioService() {
        return new TestIOService();
    }

    // Simple implementation of IOService for testing
    private static class TestIOService implements IOService {
        @Override
        public void printLine(String s) {
            // Do nothing in tests
        }

        @Override
        public void printFormattedLine(String s, Object... args) {
            // Do nothing in tests
        }

        @Override
        public String readString() {
            return "1"; // Always return "1" for tests
        }

        @Override
        public String readStringWithPrompt(String prompt) {
            return "1"; // Always return "1" for tests
        }

        @Override
        public int readIntForRange(int min, int max, String errorMessage) {
            return 1; // Always return 1 for tests
        }

        @Override
        public int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage) {
            return 1; // Always return 1 for tests
        }
    }
}
