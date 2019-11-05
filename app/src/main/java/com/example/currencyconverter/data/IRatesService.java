package com.example.currencyconverter.data;

import com.example.currencyconverter.data.model.CurrenciesData;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Web-api для списка курсов валют
 *
 * @author Evgeny Chumak
 **/
public interface IRatesService {

    /**
     * Загружает курсы валют
     */
    @GET("scripts/XML_daily.asp")
    Call<CurrenciesData> loadCurrencies();
}
