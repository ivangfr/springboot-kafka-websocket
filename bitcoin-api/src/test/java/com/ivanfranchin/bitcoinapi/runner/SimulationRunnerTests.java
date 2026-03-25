package com.ivanfranchin.bitcoinapi.runner;

import com.ivanfranchin.bitcoinapi.price.PriceService;
import com.ivanfranchin.bitcoinapi.price.model.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SimulationRunnerTests {

    @Mock
    private PriceService priceService;

    @InjectMocks
    private SimulationRunner simulationRunner;

    @Test
    void testRunSavesInitialPriceOf37000() throws Exception {
        given(priceService.savePrice(any(Price.class))).willAnswer(inv -> inv.getArgument(0));

        simulationRunner.run();

        ArgumentCaptor<Price> captor = ArgumentCaptor.forClass(Price.class);
        then(priceService).should().savePrice(captor.capture());

        Price saved = captor.getValue();
        assertThat(saved.getValue()).isEqualByComparingTo(BigDecimal.valueOf(37000));
        assertThat(saved.getTimestamp()).isNotNull();
    }

    @Test
    void testRunInvokesSavePriceExactlyOnce() throws Exception {
        given(priceService.savePrice(any(Price.class))).willAnswer(inv -> inv.getArgument(0));

        simulationRunner.run();

        then(priceService).should().savePrice(any(Price.class));
        then(priceService).shouldHaveNoMoreInteractions();
    }
}
