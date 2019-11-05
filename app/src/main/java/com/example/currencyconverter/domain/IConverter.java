package com.example.currencyconverter.domain;

import androidx.annotation.NonNull;

/**
 * Конвертер из одной произвольной сущности в другую
 *
 * @author Evgeny Chumak
 **/
public interface IConverter<From, To> {

    /**
     * Выполняет конвертацию
     */
    @NonNull
    To convert(@NonNull From from);
}
