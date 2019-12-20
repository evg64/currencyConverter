package com.example.currencyconverter.domain;

/**
 * Ошибка доменного слоя, связанная с загрузкой списка валют
 *
 * @author Evgeny Chumak
 **/
public class LoadCurrenciesException extends Exception {

    public LoadCurrenciesException(String message, Throwable cause) {
        super(message, cause);
    }
}
