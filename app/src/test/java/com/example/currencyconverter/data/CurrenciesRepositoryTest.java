package com.example.currencyconverter.data;

import com.example.currencyconverter.data.model.CurrenciesData;
import com.example.currencyconverter.data.model.CurrencyData;
import com.example.currencyconverter.domain.model.Currency;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.FieldSetter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Юнит тесты на {@link CurrenciesRepository}
 *
 * @author Evgeny Chumak
 **/
public class CurrenciesRepositoryTest {

    private CurrenciesRepository mCurrenciesRepository;
    private CurrencyConverter mCurrencyConverter;
    private IRatesService mRatesApi;

    @Before
    public void setUp() throws Exception {
        mCurrencyConverter = mock(CurrencyConverter.class);
        mRatesApi = mock(IRatesService.class);
        mCurrenciesRepository = new CurrenciesRepository(mCurrencyConverter);
        FieldSetter.setField(mCurrenciesRepository, CurrenciesRepository.class.getDeclaredField("mRatesApi"), mRatesApi);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testLoadCurrencies_happyCase() throws IOException {
        // arrange
        List<CurrencyData> currencies = Arrays.asList(mock(CurrencyData.class), mock(CurrencyData.class));
        CurrenciesData currenciesData = mock(CurrenciesData.class);
        when(currenciesData.getCurrencies()).thenReturn(currencies);
        Response<CurrenciesData> response = Response.success(currenciesData);
        Call<CurrenciesData> call = mock(Call.class);
        when(call.execute()).thenReturn(response);
        when(mRatesApi.loadCurrencies()).thenReturn(call);
        List<Currency> expectedOutput = Arrays.asList(mock(Currency.class), mock(Currency.class));
        when(mCurrencyConverter.convert(currencies)).thenReturn(expectedOutput);

        // act
        List<Currency> output = mCurrenciesRepository.loadCurrencies();

        // assert
        assertThat(output, is(expectedOutput));
    }

    @Test(expected = IOException.class)
    @SuppressWarnings("unchecked")
    public void testLoadCurrencies_bodyIsNull_throwsException() throws IOException {
        // arrange
        List<CurrencyData> currencies = Arrays.asList(mock(CurrencyData.class), mock(CurrencyData.class));
        CurrenciesData currenciesData = mock(CurrenciesData.class);
        when(currenciesData.getCurrencies()).thenReturn(currencies);
        Response<CurrenciesData> response = Response.success(null);
        Call<CurrenciesData> call = mock(Call.class);
        when(call.execute()).thenReturn(response);
        when(mRatesApi.loadCurrencies()).thenReturn(call);

        // act
        mCurrenciesRepository.loadCurrencies();
    }

    @Test(expected = IOException.class)
    @SuppressWarnings("unchecked")
    public void testLoadCurrencies_errorBodyIsNotNull_throwsException() throws IOException {
        // arrange
        List<CurrencyData> currencies = Arrays.asList(mock(CurrencyData.class), mock(CurrencyData.class));
        CurrenciesData currenciesData = mock(CurrenciesData.class);
        when(currenciesData.getCurrencies()).thenReturn(currencies);
        ResponseBody errorBody = mock(ResponseBody.class);
        Response<CurrenciesData> response = Response.error(404, errorBody);
        Call<CurrenciesData> call = mock(Call.class);
        when(call.execute()).thenReturn(response);
        when(mRatesApi.loadCurrencies()).thenReturn(call);

        // act
        mCurrenciesRepository.loadCurrencies();
    }

    // Call<CurrenciesData> listCall = mRatesApi.loadCurrencies();
    // Response<CurrenciesData> response = listCall.execute();
    //     if (response.body() == null) {
    //     throw new IOException("Не удалось загрузить список валют");
    // }
    // List<CurrencyData> currencies = response.body().getCurrencies();
    //     return mCurrencyConverter.convert(currencies);
}