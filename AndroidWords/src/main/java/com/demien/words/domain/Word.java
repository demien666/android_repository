package com.demien.words.domain;

import java.io.Serializable;

/**
 * Created by dmitry on 14.02.15.
 */
public class Word implements Serializable {
    protected String value;
    protected String translation;
    protected String example;
    private WordState state = WordState.DEFAULT;

    public Word(String value, String translation) {
        this.value = value;
        this.translation = translation;
    }

    public Word(String value, String translation, String example) {
        this.value = value;
        this.translation = translation;
        this.example = example;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public WordState getState() {
        return state;
    }

    public void setState(WordState state) {
        this.state = state;
    }
}
