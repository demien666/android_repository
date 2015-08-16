package com.demien.words.controller;

import com.demien.words.AppConst;
import com.demien.words.activity.TestActivity;
import com.demien.words.domain.TestWordStorage;
import com.demien.words.domain.Word;
import com.demien.words.exception.TestControllerException;

import java.util.List;

/**
 * Created by dmitry on 02.03.15.
 */
public class TestController extends BaseController {

    private TestWordStorage storage;

    public TestController(TestActivity activity) {
        super(activity);

    }

    public void setWords(List<Word> words) {
        int translationOptionCount = settings.getIntValue(AppConst.SETTINGS_TRANSLATION_OPTION_COUNT, AppConst.DEF_TRANSLATION_OPTION_COUNT);
        storage = new TestWordStorage(words, translationOptionCount);
    }

    public Word getNextWord() throws TestControllerException {
        if (storage.isEmpty()) {
            throw new TestControllerException(TestControllerException.LESSON_IS_LEARNED);
        }
        return storage.getNextWord();
    }

    public List<String> getTranslationList() {
        return storage.getTranslationList();
    }

    public int wordsToProcess() {
        return storage.getTestWords().size();
    }

    public void removeCurrentWord() {
        storage.removeCurrentWord();
    }


}
