package com.askmesmth.designtraining;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class QuestionCategory {
    //temporary
    public static final QuestionCategory[] categories = {
            new QuestionCategory("Начало диалога", R.drawable.dialog_begining),
            new QuestionCategory("Случайные вопросы", R.drawable.random_questions),
            new QuestionCategory("Личные вопросы", R.drawable.personal_questions),
            new QuestionCategory("Еда", R.drawable.food),
            new QuestionCategory("Спорт", R.drawable.sport),
            new QuestionCategory("Отдых", R.drawable.relax),
            new QuestionCategory("Привычки", R.drawable.habits),
            new QuestionCategory("Детство", R.drawable.childhood),
            new QuestionCategory("Семья", R.drawable.family),
            new QuestionCategory("Для разговора с детьми", R.drawable.chat_with_children),
            new QuestionCategory("Учеба", R.drawable.education),
            new QuestionCategory("Работа", R.drawable.work),
            new QuestionCategory("Путешествия", R.drawable.travel),
            new QuestionCategory("Развлечения", R.drawable.entertaiment),
            new QuestionCategory("Отношения", R.drawable.relationship),
            new QuestionCategory("Прошлое", R.drawable.past),
            new QuestionCategory("Настоящее", R.drawable.now),
            new QuestionCategory("Будущее", R.drawable.future)
    };
    private final String categoryName;
    private final int imageResourceId;
}

