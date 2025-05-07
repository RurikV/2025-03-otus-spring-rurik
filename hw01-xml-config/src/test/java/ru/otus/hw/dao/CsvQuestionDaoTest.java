package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("CsvQuestionDao should")
class CsvQuestionDaoTest {

    @Test
    @DisplayName("correctly read questions from existing CSV file")
    void shouldCorrectlyReadQuestionsFromExistingCsvFile() {
        // Arrange
        TestFileNameProvider fileNameProvider = mock(TestFileNameProvider.class);
        when(fileNameProvider.getTestFileName()).thenReturn("test-questions.csv");
        CsvQuestionDao questionDao = new CsvQuestionDao(fileNameProvider);

        // Act
        List<Question> questions = questionDao.findAll();

        // Assert
        assertThat(questions).hasSize(2);

        // Verify first question
        Question firstQuestion = questions.get(0);
        assertThat(firstQuestion.text()).isEqualTo("What is the capital of France?");
        assertThat(firstQuestion.answers()).hasSize(3);
        assertThat(firstQuestion.answers().get(0).text()).isEqualTo("Paris");
        assertThat(firstQuestion.answers().get(0).isCorrect()).isTrue();
        assertThat(firstQuestion.answers().get(1).text()).isEqualTo("London");
        assertThat(firstQuestion.answers().get(1).isCorrect()).isFalse();
        assertThat(firstQuestion.answers().get(2).text()).isEqualTo("Berlin");
        assertThat(firstQuestion.answers().get(2).isCorrect()).isFalse();

        // Verify second question
        Question secondQuestion = questions.get(1);
        assertThat(secondQuestion.text()).isEqualTo("What is 2+2?");
        assertThat(secondQuestion.answers()).hasSize(3);
        assertThat(secondQuestion.answers().get(0).text()).isEqualTo("3");
        assertThat(secondQuestion.answers().get(0).isCorrect()).isFalse();
        assertThat(secondQuestion.answers().get(1).text()).isEqualTo("4");
        assertThat(secondQuestion.answers().get(1).isCorrect()).isTrue();
        assertThat(secondQuestion.answers().get(2).text()).isEqualTo("5");
        assertThat(secondQuestion.answers().get(2).isCorrect()).isFalse();
    }

    @Test
    @DisplayName("throw QuestionReadException when CSV file does not exist")
    void shouldThrowQuestionReadExceptionWhenCsvFileDoesNotExist() {
        // Arrange
        TestFileNameProvider fileNameProvider = mock(TestFileNameProvider.class);
        when(fileNameProvider.getTestFileName()).thenReturn("non-existent-file.csv");
        CsvQuestionDao questionDao = new CsvQuestionDao(fileNameProvider);

        // Act & Assert
        assertThatThrownBy(questionDao::findAll)
                .isInstanceOf(QuestionReadException.class)
                .hasMessageContaining("File not found: non-existent-file.csv");
    }
}