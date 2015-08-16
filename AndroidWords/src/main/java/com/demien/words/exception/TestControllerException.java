package com.demien.words.exception;

/**
 * Created by dmitry on 15.03.15.
 */
public class TestControllerException extends BaseException {

    public final static String LESSON_IS_LEARNED = "LESSON_IS_LEARNED";

    public TestControllerException(String message) {
        super(message);
    }
}
