package com.demien.words.controller;

import com.demien.words.domain.Word;
import com.demien.words.exception.LearnControllerException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by dmitry on 15.02.15.
 */
public class LearnControllerTest {

    public static List<Word> getTestWords(int wordCount) {
        List<Word> words = new ArrayList<Word>();

        for (int i = 0; i < wordCount; i++) {
            String index = Integer.toString(i);
            words.add(new Word(index + "-value", index + "-translation", index + "-example"));
        }

        return words;

    }

    public LearnController getTestLearnController(List<Word> words, int wordsInLessonCount) {
        return new LearnController(null, words, wordsInLessonCount);
    }

    @Test
    public void getMaxLessonNumberTest() {
        LearnController controller = getTestLearnController(getTestWords(5), 10);

        assertEquals(1, controller.getMaxLessonNumber());

        controller = getTestLearnController(getTestWords(15), 10);
        assertEquals(2, controller.getMaxLessonNumber());

        controller = getTestLearnController(getTestWords(20), 10);
        assertEquals(3, controller.getMaxLessonNumber());
    }

    @Test(expected = LearnControllerException.class)
    public void emptyDatabaseTest() throws LearnControllerException {
        LearnController controller = getTestLearnController(null, 10);
        controller.getWordsForCurrentLesson();
    }


}
