package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;
    private final IOService ioService;

    @Override
    public void run() {
        String firstName = ioService.readStringWithPrompt("Please enter your first name:");
        String lastName = ioService.readStringWithPrompt("Please enter your last name:");

        testService.executeTestForStudent(firstName, lastName);
    }
}
