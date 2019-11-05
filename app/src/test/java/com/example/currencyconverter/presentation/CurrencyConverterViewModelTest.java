package com.example.currencyconverter.presentation;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.example.currencyconverter.R;
import com.example.currencyconverter.domain.ConversionInteractor;
import com.example.currencyconverter.domain.CurrenciesInteractor;
import com.example.currencyconverter.domain.LoadCurrenciesException;
import com.example.currencyconverter.domain.model.Currency;
import com.example.currencyconverter.presentation.utils.IResourceWrapper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Юнит тесты на {@link CurrencyConverterViewModel}
 *
 * @author Evgeny Chumak
 **/
public class CurrencyConverterViewModelTest {

    private static final String RUB = "Российский рубль";

    @Rule
    public InstantTaskExecutorRule mInstantTaskExecutorRule = new InstantTaskExecutorRule();
    private CurrencyConverterViewModel mViewModel;
    private CurrenciesInteractor mCurrenciesInteractor;
    private IResourceWrapper mResourceWrapper;
    private ConversionInteractor mConversionInteractor;
    private Currency mRub;
    private List<Currency> mCurrencies;

    @Before
    public void setUp() {
        mCurrencies = new ArrayList<>(Arrays.asList(mock(Currency.class), mock(Currency.class)));
        mCurrenciesInteractor = mock(CurrenciesInteractor.class);
        mResourceWrapper = mock(IResourceWrapper.class);
        mConversionInteractor = mock(ConversionInteractor.class);
        when(mResourceWrapper.getString(R.string.russian_ruble)).thenReturn(RUB);
        mViewModel = new CurrencyConverterViewModel(
                mCurrenciesInteractor,
                new SynchronousExecutor(),
                mResourceWrapper,
                mConversionInteractor
        );
        mRub = new Currency(
                "rub_id",
                "RUB",
                1,
                mResourceWrapper.getString(R.string.russian_ruble),
                BigDecimal.ONE
        );
    }

    @Test
    public void testLoadCurrencies_happyCase() throws LoadCurrenciesException {
        // arrange
        when(mCurrenciesInteractor.loadCurrencies()).thenReturn(mCurrencies);
        List<Currency> expectedCurrencies = new ArrayList<>(mCurrencies);
        expectedCurrencies.add(0, mRub);

        // act
        mViewModel.loadCurrencies();

        // assert
        assertThat(mViewModel.getCurrencies().getValue(), is(expectedCurrencies));
        assertThat(mViewModel.isLoading().getValue(), is(false));
    }

    @Test
    public void testLoadCurrencies_interactorThrowsException() throws LoadCurrenciesException {
        // arrange
        when(mCurrenciesInteractor.loadCurrencies()).thenThrow(new LoadCurrenciesException("message", new Throwable()));
        String errorLoadingCurrencies = "errorLoadingCurrencies";
        when(mResourceWrapper.getString(R.string.error_loading_currencies)).thenReturn(errorLoadingCurrencies);

        // act
        mViewModel.loadCurrencies();

        // assert
        assertThat(mViewModel.getErrors().getValue(), is(errorLoadingCurrencies));
        assertThat(mViewModel.isLoading().getValue(), is(false));
    }

    @Test
    public void testUpdateConversionRate_happyCase() {
        // arrange
        ((MutableLiveData<List<Currency>>) mViewModel.getCurrencies()).setValue(mCurrencies);
        String formattedString = "formattedString";
        when(mConversionInteractor.formatConversionRate(mCurrencies, 0, 1))
                .thenReturn(formattedString);

        // act
        mViewModel.updateConversionRate(0, 1);

        // assert
        assertThat(mViewModel.getConversionRate().getValue(), is(formattedString));
    }

    @Test
    public void testUpdateConversionRate_errorCase() {
        // arrange
        when(mConversionInteractor.formatConversionRate(mCurrencies, 0, 1))
                .thenReturn(null);
        ((MutableLiveData<List<Currency>>) mViewModel.getCurrencies()).setValue(mCurrencies);

        // act
        mViewModel.updateConversionRate(0, 1);

        // assert
        assertThat(mViewModel.getConversionRate().getValue(), is(nullValue()));
    }

    @Test
    public void testConvert_happyCase() {
        // arrange
        ((MutableLiveData<List<Currency>>) mViewModel.getCurrencies()).setValue(mCurrencies);
        String amount = "10";
        String formattedString = "formattedString";
        when(mConversionInteractor.convert(mCurrencies, 0, 1, amount))
                .thenReturn(formattedString);

        // act
        mViewModel.convert(0, 1, amount);

        // assert
        assertThat(mViewModel.getConvertedText().getValue(), is(formattedString));
    }

    @Test
    public void testConvert_errorCase() {
        // arrange
        ((MutableLiveData<List<Currency>>) mViewModel.getCurrencies()).setValue(mCurrencies);
        String error = "error";
        when(mResourceWrapper.getString(R.string.conversion_error)).thenReturn(error);
        String amount = "10";
        when(mConversionInteractor.convert(mCurrencies, 0, 1, amount))
                .thenReturn(null);

        // act
        mViewModel.convert(0, 1, amount);

        // assert
        assertThat(mViewModel.getErrors().getValue(), is(error));
    }
}