package com.demien.words.domain;

import com.demien.words.util.ArrayShuffler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmitry on 14.03.15.
 */
public class TestWordStorage {
    private List<TestWord> testWords = new LinkedList<TestWord>();
    private List<Word> words;
    private int translationOptionCount = 5;

    private ArrayShuffler<TestWord> testWordArrayShuffler = new ArrayShuffler<TestWord>();
    private ArrayShuffler<String> stringArrayShuffler = new ArrayShuffler<String>();
    private int position = -1;


    public TestWordStorage(List<Word> words, int translationOptionCount) {
        this.words = words;
        this.translationOptionCount = translationOptionCount;
        for (Word word : words) {
            testWords.add(TestWord.getOriginalWordObject(word));
            testWords.add(TestWord.getTranslationWordObject(word));
        }
        testWordArrayShuffler.shuffleElementsInList(testWords);
    }

    public boolean isEmpty() {
        if (testWords.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void removeCurrentWord() {
        testWords.remove(position);
        position--;
    }

    public Word getNextWord() {
        position++;
        if (position > testWords.size() - 1) {
            position = 0;
        }

        TestWord testWord = testWords.get(position);
        return testWord.toWord();
    }

    public TestWord getCurrentTestWord() {
        return testWords.get(position);
    }

    public String getTranslation(Word word) {
        TestWord currentTestWord=getCurrentTestWord();
        if (currentTestWord.getType() == TestWord.TestWordType.ORIGINAL) {
            return word.getTranslation();
        } else {
            return word.getValue();
        }
    }

    public List<String> getTranslationList() {
        List<String> translations = new ArrayList<String>();
        // base word
        TestWord testWord = getCurrentTestWord();
        translations.add(getTranslation(testWord.toOriginalWord()));

        // optional transaltions
        int addedTranslations = 1;
        do {
            int randomNumber = testWordArrayShuffler.getRandomNumber(words.size()-1);
            Word randomWord = words.get(randomNumber);
            if ((!randomWord.getValue().equals(testWord.getValue())) && (!randomWord.getValue().equals(testWord.getTranslation()))) {

                String translation=getTranslation(randomWord);
                if (!translations.contains(translation)) {
                    translations.add(translation);
                    addedTranslations++;
                }
            }
        } while (addedTranslations <= translationOptionCount);
        stringArrayShuffler.shuffleElementsInList(translations);


        return translations;
    }

    public List<TestWord> getTestWords() {
        return testWords;
    }

}
