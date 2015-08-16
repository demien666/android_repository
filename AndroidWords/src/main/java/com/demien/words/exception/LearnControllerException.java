package com.demien.words.exception;

/**
 * Created by dmitry on 21.02.15.
 */
public class LearnControllerException extends BaseException {


    public final static String DATABASE_IS_EMPTY = "DATABASE_IS_EMPTY";

    public final static String LESSON_NUMBER_IS_TOO_LOW = "LESSON_NUMBER_ERROR_TOO_LOW";
    public final static String LESSON_NUMBER_IS_TOO_BIG = "LESSON_NUMBER_ERROR_TOO_MUCH";


    public LearnControllerException(String message) {
        super(message);
    }
}
