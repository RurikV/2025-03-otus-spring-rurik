package ru.otus.hw.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@TestPropertySource(properties = {
        "spring.shell.interactive.enabled=false",
        "spring.shell.script.enabled=false"
})
public class TestShellConfig {
}