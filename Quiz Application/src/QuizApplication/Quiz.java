package QuizApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Quiz {
    private List<Question> questions;
    private int score;

    public Quiz() {
        questions = new ArrayList<>();
        score = 0;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void takeQuiz() {
        Scanner scanner = new Scanner(System.in);
        for (Question question : questions) {
            System.out.println(question.getQuestionText());
            String[] options = question.getOptions();
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ": " + options[i]);
            }
            int userAnswer = getUserInput(scanner, options.length);
            if (question.isCorrectAnswer(userAnswer - 1)) {
                score++;
            }
        }
        System.out.println("Your score is: " + score + " out of " + questions.size());
        scanner.close();
    }

    private int getUserInput(Scanner scanner, int numberOfOptions) {
        int userAnswer = -1;
        boolean validInput = false;
        while (!validInput) {
            try {
                System.out.print("Enter your answer (1-" + numberOfOptions + "): ");
                userAnswer = Integer.parseInt(scanner.nextLine());
                if (userAnswer >= 1 && userAnswer <= numberOfOptions) {
                    validInput = true;
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and " + numberOfOptions);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return userAnswer;
    }

    public static void main(String[] args) {
        Quiz quiz = new Quiz();

        String[] options1 = {"Java", "Python", "C++", "JavaScript"};
        quiz.addQuestion(new Question("Which programming language is platform-independent?", options1, 0));

        String[] options2 = {"Tesla", "Edison", "Bell", "Einstein"};
        quiz.addQuestion(new Question("Who invented the light bulb?", options2, 1));

        String[] options3 = {"Earth", "Mars", "Jupiter", "Saturn"};
        quiz.addQuestion(new Question("Which is the largest planet in our solar system?", options3, 2));

        quiz.takeQuiz();
    }
}

