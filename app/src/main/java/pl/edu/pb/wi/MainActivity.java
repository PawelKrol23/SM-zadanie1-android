package pl.edu.pb.wi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button trueButton, falseButton, nextButton;
    private TextView questionTextView;
    private int correctAnswers = 0, wrongAnswers = 0;

    private final Question[] questions = new Question[] {
            new Question(R.string.q_activity, true),
            new Question(R.string.q_find_resources, false),
            new Question(R.string.q_listener, true),
            new Question(R.string.q_resources, true),
            new Question(R.string.q_version, false),
    };
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        questionTextView = findViewById(R.id.question_text_view);
        setQuestionText();

        trueButton.setOnClickListener(v -> checkAnswerCorrectness(true));
        falseButton.setOnClickListener(v -> checkAnswerCorrectness(false));
        nextButton.setOnClickListener(v -> setNextQuestion());
    }

    private void checkAnswerCorrectness(boolean userAnswer) {
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId;
        if(userAnswer == correctAnswer) {
            resultMessageId = R.string.correct_answer;
            correctAnswers++;
        } else {
            wrongAnswers++;
            resultMessageId = R.string.incorrect_answer;
        }
        Toast.makeText(this, resultMessageId, Toast.LENGTH_SHORT).show();
        setNextQuestion();
    }

    private void setQuestionText() {
        questionTextView.setText(questions[currentIndex].getQuestionId());
    }

    private void setNextQuestion() {
        currentIndex = currentIndex + 1;
        if(currentIndex == questions.length) {
            endQuiz();
        } else {
            setQuestionText();
        }
    }

    private void endQuiz() {
        trueButton.setVisibility(View.GONE);
        falseButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);

        String text = getString(R.string.result_text, correctAnswers, wrongAnswers);
        questionTextView.setText(text);
    }
}