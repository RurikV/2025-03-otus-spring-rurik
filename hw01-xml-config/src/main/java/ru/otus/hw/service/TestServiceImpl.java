package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        List<Question> questions = questionDao.findAll();
        int correctAnswersCount = 0;
        int totalQuestions = questions.size();

        for (int i = 0; i < totalQuestions; i++) {
            Question question = questions.get(i);
            ioService.printFormattedLine("Question %d: %s", i + 1, question.text());

            List<Answer> answers = question.answers();
            for (int j = 0; j < answers.size(); j++) {
                Answer answer = answers.get(j);
                ioService.printFormattedLine("  %d. %s", j + 1, answer.text());
            }

            int userAnswerIndex = getUserAnswer(answers.size());
            Answer userAnswer = answers.get(userAnswerIndex - 1);

            if (userAnswer.isCorrect()) {
                correctAnswersCount++;
                ioService.printLine("Correct!");
            } else {
                ioService.printLine("Incorrect!");
            }

            ioService.printLine("");
        }

        ioService.printFormattedLine("Test completed! Your score: %d out of %d", correctAnswersCount, totalQuestions);
    }

    @Override
    public void executeTestForStudent(String firstName, String lastName) {
        ioService.printLine("");
        ioService.printFormattedLine("Student: %s %s", firstName, lastName);
        ioService.printFormattedLine("Please answer the questions below%n");

        List<Question> questions = questionDao.findAll();
        int correctAnswersCount = 0;
        int totalQuestions = questions.size();

        for (int i = 0; i < totalQuestions; i++) {
            Question question = questions.get(i);
            ioService.printFormattedLine("Question %d: %s", i + 1, question.text());

            List<Answer> answers = question.answers();
            for (int j = 0; j < answers.size(); j++) {
                Answer answer = answers.get(j);
                ioService.printFormattedLine("  %d. %s", j + 1, answer.text());
            }

            int userAnswerIndex = getUserAnswer(answers.size());
            Answer userAnswer = answers.get(userAnswerIndex - 1);

            if (userAnswer.isCorrect()) {
                correctAnswersCount++;
                ioService.printLine("Correct!");
            } else {
                ioService.printLine("Incorrect!");
            }

            ioService.printLine("");
        }

        ioService.printFormattedLine("Test completed for %s %s! Your score: %d out of %d", 
                firstName, lastName, correctAnswersCount, totalQuestions);
    }

    private int getUserAnswer(int maxAnswerNumber) {
        while (true) {
            try {
                String input = ioService.readStringWithPrompt("Please enter the number of your answer:");
                int answer = Integer.parseInt(input.trim());

                if (answer >= 1 && answer <= maxAnswerNumber) {
                    return answer;
                } else {
                    ioService.printFormattedLine("Please enter a number between 1 and %d", maxAnswerNumber);
                }
            } catch (NumberFormatException e) {
                ioService.printLine("Please enter a valid number");
            }
        }
    }
}
