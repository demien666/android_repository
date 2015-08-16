package com.demien.words.util;

import java.util.List;

/**
 * Created by dmitry on 14.03.15.
 */
public class ArrayShuffler<T> {
    public int getRandomNumber(int maxNumber) {
        int result = (int) Math.round(Math.random() * maxNumber);
        return result;
    }

    public void shuffleElementsInList(List<T> listForShuffling) {
        for (int i = 0; i < listForShuffling.size(); i++) {
            int leftPosition = getRandomNumber(listForShuffling.size() - 1);
            int rightPosition = getRandomNumber(listForShuffling.size() - 1);

            T leftElement = listForShuffling.get(leftPosition);
            listForShuffling.set(leftPosition, listForShuffling.get(rightPosition));
            listForShuffling.set(rightPosition, leftElement);
        }

    }
}
