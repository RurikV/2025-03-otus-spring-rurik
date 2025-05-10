package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.Application;
import ru.otus.hw.config.TestIOConfig;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {Application.class, TestIOConfig.class})
@DisplayName("Сервис тестирования")
class TestServiceImplTest {

    @DisplayName("Должен корректно выполнять тестирование")
    @Test
    void shouldCorrectlyExecuteTest() {
        // Arrange
        Student student = new Student("Ivan", "Ivanov");

        Answer correctAnswer = new Answer("Correct answer", true);
        Answer wrongAnswer = new Answer("Wrong answer", false);

        Question question1 = new Question("Question 1", List.of(correctAnswer, wrongAnswer));
        Question question2 = new Question("Question 2", List.of(wrongAnswer, correctAnswer));

        List<Question> questions = List.of(question1, question2);

        QuestionDao questionDao = mock(QuestionDao.class);
        when(questionDao.findAll()).thenReturn(questions);

        LocalizedIOService ioService = mock(LocalizedIOService.class);
        // Simulate user selecting the first answer for both questions
        // First question: correct answer, Second question: wrong answer
        when(ioService.readIntForRangeWithPrompt(eq(1), eq(2), anyString(), anyString()))
                .thenReturn(1);

        TestService testService = new TestServiceImpl(ioService, questionDao);

        // Act
        TestResult testResult = testService.executeTestFor(student);

        // Assert
        assertThat(testResult).isNotNull();
        assertThat(testResult.getStudent()).isEqualTo(student);
        assertThat(testResult.getAnsweredQuestions()).hasSize(2);
        assertThat(testResult.getRightAnswersCount()).isEqualTo(1);
    }
}
