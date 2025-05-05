package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.exceptions.QuestionReadException;

@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;
    private final IOService ioService;

    @Override
    public void run() {
        try {
            String firstName = ioService.readStringWithPrompt("Please enter your first name:");
            String lastName = ioService.readStringWithPrompt("Please enter your last name:");

            testService.executeTestForStudent(firstName, lastName);
        } catch (QuestionReadException e) {
            ioService.printLine("Error: Unable to read test questions.");
            ioService.printLine("Please check that the questions file exists and is properly formatted.");
            ioService.printFormattedLine("Technical details: %s", e.getMessage());
        } catch (Exception e) {
            ioService.printLine("Error: An unexpected error occurred while running the test.");
            ioService.printLine("Please contact the administrator for assistance.");
            ioService.printFormattedLine("Technical details: %s", e.getMessage());
        }
    }
}
