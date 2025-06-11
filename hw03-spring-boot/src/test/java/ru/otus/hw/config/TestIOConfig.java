package ru.otus.hw.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.StreamsIOService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@TestConfiguration
public class TestIOConfig {

    @Bean
    @Primary
    public IOService ioService() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("1\n".getBytes());
        return new StreamsIOService(new PrintStream(outputStream), inputStream);
    }
}