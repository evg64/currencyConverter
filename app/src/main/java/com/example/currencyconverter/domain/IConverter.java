package com.example.currencyconverter.domain;

import androidx.annotation.NonNull;

/**
 * @author Evgeny Chumak
 **/
public interface IConverter<From, To> {

    @NonNull
    To convert(@NonNull From from);
}
