package com.example.currencyconverter.data;

import androidx.annotation.NonNull;

import com.example.currencyconverter.data.model.CurrenciesData;
import com.example.currencyconverter.data.model.CurrencyData;
import com.example.currencyconverter.domain.ICurrenciesRepository;
import com.example.currencyconverter.domain.model.Currency;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Реализация репозитория для загрузки списка валют
 *
 * @author Evgeny Chumak
 **/
public class CurrenciesRepository implements ICurrenciesRepository {

    private static final String BASE_URL = "http://www.cbr.ru";
    private final IRatesService mRatesApi;
    private final CurrencyConverter mCurrencyConverter;

    /**
     * Constructor
     *
     * @param currencyConverter используется для конвертации загруженных валют в  domain entity
     */
    public CurrenciesRepository(@NonNull CurrencyConverter currencyConverter) {
        mCurrencyConverter = currencyConverter;
        Strategy strategy = new AnnotationStrategy();
        Serializer serializer = new Persister(strategy);
        // noinspection deprecation
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
                .build();
        mRatesApi = retrofit.create(IRatesService.class);
    }

    @NonNull
    @Override
    public List<Currency> loadCurrencies() throws IOException {
        Call<CurrenciesData> listCall = mRatesApi.loadCurrencies();
        Response<CurrenciesData> response = listCall.execute();
        if (response.body() == null || response.errorBody() != null) {
            throw new IOException("Не удалось загрузить список валют");
        }
        List<CurrencyData> currencies = response.body().getCurrencies();
        return mCurrencyConverter.convert(currencies);
    }
}
