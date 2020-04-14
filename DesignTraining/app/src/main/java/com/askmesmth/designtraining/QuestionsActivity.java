package com.askmesmth.designtraining;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

public class QuestionsActivity extends AppCompatActivity {
    public static String QUESTION_NUMB = "question number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        int categoryNumb = (Integer) Objects.requireNonNull(getIntent().getExtras()).get(QUESTION_NUMB);
        QuestionCategory questionCategory = QuestionCategory.categories[categoryNumb];
        String categoryName = questionCategory.getCategoryName();

        TextView categoryNameView = findViewById(R.id.category_name_in_question);
        categoryNameView.setText(categoryName);

        TextView questionView = findViewById(R.id.question_text);
        questionView.setText("Здесь будет вопрос?Большой тестовый вопрос для интерфейса");

        RelativeLayout activityLayout = findViewById(R.id.activity_questions);
        activityLayout.setBackground(getResources().getDrawable(R.drawable.question,
                null));
        activityLayout.getBackground().setAlpha(70);
    }
}
