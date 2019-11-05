package com.example.currencyconverter.presentation;

import java.util.concurrent.Executor;

/**
 * Используется для того чтобы в юнит тесте все операции выполнялись синхронно
 *
 * @author Evgeny Chumak
 **/
class SynchronousExecutor implements Executor {

    @Override
    public void execute(Runnable command) {
        command.run();
    }

}
