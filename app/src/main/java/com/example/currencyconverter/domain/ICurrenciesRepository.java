package com.example.currencyconverter.domain;

import androidx.annotation.NonNull;

import com.example.currencyconverter.domain.model.Currency;

import java.io.IOException;
import java.util.List;

/**
 * Репозиторий для загрузки списка валют
 *
 * @author Evgeny Chumak
 **/
public interface ICurrenciesRepository {

    /**
     * Загружает список валют
     */
    @NonNull
    List<Currency> loadCurrencies() throws IOException;
}
