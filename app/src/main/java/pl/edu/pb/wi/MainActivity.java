package pl.edu.pb.wi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_CURRENT_INDEX = "currentIndex";
    private static final int REQUEST_CODE_PROMPT = 0;
    public static final String KEY_EXTRA_ANSWER = "pl.edu.wi.quiz.correctAnswer";

    private Button trueButton, falseButton, nextButton, promptButton;
    private TextView questionTextView;
    private int correctAnswers = 0, wrongAnswers = 0;
    private int currentIndex = 0;
    private boolean answerWasShown;

    private final Question[] questions = new Question[] {
            new Question(R.string.q_activity, true),
            new Question(R.string.q_find_resources, false),
            new Question(R.string.q_listener, true),
            new Question(R.string.q_resources, true),
            new Question(R.string.q_version, false),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG ,"Wywołano onCreate");
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        questionTextView = findViewById(R.id.question_text_view);
        promptButton = findViewById(R.id.prompt_button);
        setQuestionText();

        trueButton.setOnClickListener(v -> checkAnswerCorrectness(true));
        falseButton.setOnClickListener(v -> checkAnswerCorrectness(false));
        nextButton.setOnClickListener(v -> setNextQuestion());
        promptButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PromptActivity.class);
            boolean correctAnswer = questions[currentIndex].isTrueAnswer();
            intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
            startActivityForResult(intent, REQUEST_CODE_PROMPT);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG ,"Wywołano onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "Wywołano onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "Wywołano onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Wywołano onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Wywołano onDestroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "Wywołano onSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) { return; }
        if (requestCode == REQUEST_CODE_PROMPT) {
            if (data == null) { return; }
            answerWasShown = data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN, false);
        }
    }

    private void checkAnswerCorrectness(boolean userAnswer) {
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessageId;
        if(answerWasShown) {
            resultMessageId = R.string.answer_was_shown;
        }
        else if(userAnswer == correctAnswer) {
            resultMessageId = R.string.correct_answer;
            correctAnswers++;
        }
        else {
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
        answerWasShown = false;
        if(currentIndex == questions.length) {
            endQuiz();
            currentIndex = 0;
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