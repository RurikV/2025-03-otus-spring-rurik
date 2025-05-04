package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.mockito.Mockito.*;

class TestServiceImplTest {

    private TestService testService;
    private IOService ioService;
    private QuestionDao questionDao;

    @BeforeEach
    void setUp() {
        ioService = mock(IOService.class);
        questionDao = mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    void shouldCorrectlyExecuteTestAndCalculateScore() {
        // Prepare test data
        List<Answer> answers1 = List.of(
                new Answer("Answer 1", true),
                new Answer("Answer 2", false)
        );
        List<Answer> answers2 = List.of(
                new Answer("Answer 3", false),
                new Answer("Answer 4", true)
        );

        List<Question> questions = List.of(
                new Question("Question 1", answers1),
                new Question("Question 2", answers2)
        );

        // Configure mock behavior
        when(questionDao.findAll()).thenReturn(questions);
        // First answer is correct (1), second answer is correct (2)
        when(ioService.readStringWithPrompt("Please enter the number of your answer:"))
                .thenReturn("1")  // Correct answer for question 1
                .thenReturn("2"); // Correct answer for question 2

        // Execute the method being tested
        testService.executeTest();

        // Verify interactions
        verify(ioService, times(3)).printLine("");
        verify(ioService).printFormattedLine("Please answer the questions below%n");

        // Verify question 1 output
        verify(ioService).printFormattedLine("Question %d: %s", 1, "Question 1");
        verify(ioService).printFormattedLine("  %d. %s", 1, "Answer 1");
        verify(ioService).printFormattedLine("  %d. %s", 2, "Answer 2");
        verify(ioService, times(2)).readStringWithPrompt("Please enter the number of your answer:");
        verify(ioService, times(2)).printLine("Correct!");

        // Verify question 2 output
        verify(ioService).printFormattedLine("Question %d: %s", 2, "Question 2");
        verify(ioService).printFormattedLine("  %d. %s", 1, "Answer 3");
        verify(ioService).printFormattedLine("  %d. %s", 2, "Answer 4");

        // Verify final score
        verify(ioService).printFormattedLine("Test completed! Your score: %d out of %d", 2, 2);
    }

    @Test
    void shouldHandleIncorrectAnswers() {
        // Prepare test data
        List<Answer> answers1 = List.of(
                new Answer("Answer 1", true),
                new Answer("Answer 2", false)
        );

        List<Question> questions = List.of(
                new Question("Question 1", answers1)
        );

        // Configure mock behavior
        when(questionDao.findAll()).thenReturn(questions);
        // User selects the incorrect answer (2)
        when(ioService.readStringWithPrompt("Please enter the number of your answer:"))
                .thenReturn("2");  // Incorrect answer

        // Execute the method being tested
        testService.executeTest();

        // Verify feedback for incorrect answer
        verify(ioService).printLine("Incorrect!");

        // Verify final score
        verify(ioService).printFormattedLine("Test completed! Your score: %d out of %d", 0, 1);
    }

    @Test
    void shouldHandleInvalidInputAndRetry() {
        // Prepare test data
        List<Answer> answers1 = List.of(
                new Answer("Answer 1", true),
                new Answer("Answer 2", false)
        );

        List<Question> questions = List.of(
                new Question("Question 1", answers1)
        );

        // Configure mock behavior
        when(questionDao.findAll()).thenReturn(questions);
        // First input is invalid, second is out of range, third is valid
        when(ioService.readStringWithPrompt("Please enter the number of your answer:"))
                .thenReturn("abc")  // Invalid input
                .thenReturn("3")    // Out of range
                .thenReturn("1");   // Valid input (correct answer)

        // Execute the method being tested
        testService.executeTest();

        // Verify error messages
        verify(ioService).printLine("Please enter a valid number");
        verify(ioService).printFormattedLine("Please enter a number between 1 and %d", 2);

        // Verify correct answer feedback
        verify(ioService).printLine("Correct!");

        // Verify final score
        verify(ioService).printFormattedLine("Test completed! Your score: %d out of %d", 1, 1);
    }
}
