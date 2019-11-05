package com.example.currencyconverter.domain;

import com.example.currencyconverter.domain.model.Currency;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Юнит тесты на {@link CurrenciesInteractor}
 *
 * @author Evgeny Chumak
 **/
public class CurrenciesInteractorTest {

    private CurrenciesInteractor mCurrenciesInteractor;
    private ICurrenciesRepository mRepository;

    @Before
    public void setUp() {
        mRepository = mock(ICurrenciesRepository.class);
        mCurrenciesInteractor = new CurrenciesInteractor(mRepository);
    }

    @Test
    public void testLoadCurrencies_happyCase() throws IOException, LoadCurrenciesException {
        // arrange
        // noinspection unchecked
        List<Currency> currencies = mock(List.class);
        when(mRepository.loadCurrencies()).thenReturn(currencies);

        // act
        List<Currency> output = mCurrenciesInteractor.loadCurrencies();

        // assert
        assertThat(output, is(currencies));
    }

    @Test(expected = LoadCurrenciesException.class)
    public void testLoadCurrencies_repositoryThrowsException_exceptionIsWrappedIntoLoadCurrenciesException()
            throws IOException, LoadCurrenciesException {
        // arrange
        when(mRepository.loadCurrencies()).thenThrow(new IOException("mock"));

        // act
        mCurrenciesInteractor.loadCurrencies();
    }
}