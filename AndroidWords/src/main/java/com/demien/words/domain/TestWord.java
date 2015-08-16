package com.demien.words.domain;

/**
 * Created by dmitry on 14.03.15.
 */
public class TestWord extends Word {

    public enum TestWordType {ORIGINAL, TRANSLATION}

    private boolean isLearned;
    private TestWordType type;


    public TestWord(String value, String translation, String example, TestWordType type) {
        super(value, translation, example);
        isLearned = false;
        this.type = type;
    }

    public static TestWord getOriginalWordObject(Word word) {
        TestWord testWord = new TestWord(word.getValue(), word.getTranslation(), word.getExample(), TestWordType.ORIGINAL);
        return testWord;
    }

    public static TestWord getTranslationWordObject(Word word) {
        TestWord testWord = new TestWord(word.getTranslation(), word.getValue(), word.getExample(), TestWordType.TRANSLATION);
        return testWord;
    }

    public Word toWord() {
        return new Word(getValue(), getTranslation(), getExample());
    }

    public Word toOriginalWord() {
        if (getType().equals(TestWordType.ORIGINAL)) {
            return new Word(getValue(), getTranslation(), getExample());
        } else {
            return new Word(getTranslation(), getValue(), getExample());
        }

    }

    public TestWordType getType() {
        return type;
    }

}
