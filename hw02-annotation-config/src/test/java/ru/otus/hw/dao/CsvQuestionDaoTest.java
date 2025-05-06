package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Dao для работы с вопросами")
class CsvQuestionDaoTest {

    @DisplayName("Должен корректно загружать вопросы из CSV файла")
    @Test
    void shouldCorrectlyLoadQuestionsFromCsvFile() {
        // Arrange
        TestFileNameProvider fileNameProvider = mock(TestFileNameProvider.class);
        when(fileNameProvider.getTestFileName()).thenReturn("questions.csv");
        
        QuestionDao questionDao = new CsvQuestionDao(fileNameProvider);
        
        // Act
        List<Question> questions = questionDao.findAll();
        
        // Assert
        assertThat(questions).isNotNull().isNotEmpty().hasSize(3);
        
        Question firstQuestion = questions.get(0);
        assertThat(firstQuestion.text()).isEqualTo("Is there life on Mars?");
        assertThat(firstQuestion.answers()).hasSize(3);
        
        Answer correctAnswer = firstQuestion.answers().stream()
                .filter(Answer::isCorrect)
                .findFirst()
                .orElseThrow();
        assertThat(correctAnswer.text()).isEqualTo("Science doesn't know this yet");
    }
}