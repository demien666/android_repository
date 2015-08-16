package com.demien.words.controller;

import android.app.Activity;
import com.demien.words.R;
import com.demien.words.AppConst;
import com.demien.words.activity.BaseActivity;
import com.demien.words.domain.Word;
import com.demien.words.exception.LearnControllerException;
import com.demien.words.util.DialogHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitry on 14.02.15.
 */
public class LearnController extends BaseController {

    private List<Word> words;
    private int currentLessonNumber = 1;
    private int wordInLessonPosition = 0;
    private int wordsInLessonCount;

    public LearnController(BaseActivity activity) {
        super(activity);
        words = new ArrayList<Word>();
        loadWordsFromResource();
        wordsInLessonCount = settings.getIntValue(AppConst.SETTINGS_WORDS_IN_TEST, AppConst.DEF_WORDS_IN_TEST);
    }

    public LearnController(BaseActivity activity, List<Word> words, int wordsInLessonCount) {
        super(activity);
        this.words = words;
        this.wordsInLessonCount = wordsInLessonCount;
    }


    public void loadWordsFromResource() {
        /*
        for (int i = 0; i < 100; i++) {
            String index = Integer.toString(i);
            Word word = new Word(index + "-value", index + "-translation", index + "-example");
            words.add(word);
        }
        */
        InputStream is = activity.getResources().openRawResource(R.raw.words);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String readLine = null;

        try {
            while ((readLine = br.readLine()) != null) {
                String arr[]=readLine.split("#");
                if (arr.length>1) {
                    String value=arr[0];
                    String translation=arr[1];
                    String example="";
                    if (arr.length>2) {
                        example=arr[2];
                    }
                    Word word=new Word(value, translation, example);
                    words.add(word);
                }

            }
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public int getWordsCount() {
        return words.size();
    }

    public int getCurrentWordInLessonPosition() {
        return wordInLessonPosition;
    }

    public int getMaxLessonNumber() {
        return (words.size() / getWordsInLessonCount()) + 1;
    }

    public int getWordsInLessonCount() {
        return wordsInLessonCount;
    }

    public void moveToLessonWithNumber(int n) {

        if (n < 1) {
            DialogHelper.showErrorMessage(activity, R.string.const_error_lesson_number_too_low);
            return;
        }
        if (n>getMaxLessonNumber()) {
            DialogHelper.showErrorMessage(activity, R.string.const_error_lesson_number_too_hight);
            return;
        }
        currentLessonNumber = n;
        wordInLessonPosition = 0;
        settings.putValue(AppConst.SETTINGS_CURRENT_LESSON_NUMBER, Integer.toString(currentLessonNumber));
    }

    public void moveToNextLesson() {
        moveToLessonWithNumber(currentLessonNumber + 1);
    }

    public void moveToPrevLesson() {
        moveToLessonWithNumber(currentLessonNumber - 1);
    }

    public void moveToNextWord() {
        wordInLessonPosition++;
        if (wordInLessonPosition >= wordsInLessonCount || getCurrentWordPosition()==words.size()) {
            wordInLessonPosition = 0;
        }
    }

    public void moveToPrevWord() {
        wordInLessonPosition--;
        if (wordInLessonPosition < 0) {
            wordInLessonPosition = wordsInLessonCount - 1;
        }
    }

    public int getCurrentWordPosition() {
        return wordInLessonPosition + ((currentLessonNumber - 1) * wordsInLessonCount);
    }

    public int getCurrentLessonNumber() {
        return currentLessonNumber;
    }


    public List<Word> getWordsForCurrentLesson() throws LearnControllerException {
        if ((words == null) || (words.size() == 0)) {
            throw new LearnControllerException(LearnControllerException.DATABASE_IS_EMPTY);
        }
        List<Word> result = new ArrayList<Word>();
        int startPosition = (getCurrentLessonNumber() - 1) * wordsInLessonCount;
        for (int i = startPosition; i < startPosition + wordsInLessonCount && i < words.size(); i++) {
            result.add(words.get(i));
        }

        return result;
    }

    public Word getCurrentWord() {
        return words.get(getCurrentWordPosition());
    }

}
