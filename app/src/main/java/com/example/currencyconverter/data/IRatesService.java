package com.example.currencyconverter.data;

import com.example.currencyconverter.data.model.CurrenciesData;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author Evgeny Chumak
 **/
public interface IRatesService {

    @GET("scripts/XML_daily.asp")
    Call<CurrenciesData> loadCurrencies();
}
