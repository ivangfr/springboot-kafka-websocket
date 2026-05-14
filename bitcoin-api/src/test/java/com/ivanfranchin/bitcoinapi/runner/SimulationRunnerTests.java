package com.ivanfranchin.bitcoinapi.runner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.ivanfranchin.bitcoinapi.price.PriceService;
import com.ivanfranchin.bitcoinapi.price.model.Price;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(SimulationRunner.class)
class SimulationRunnerTests {

  @MockitoBean private PriceService priceService;

  @Autowired private SimulationRunner simulationRunner;

  @Test
  void testRunSavesInitialPriceOf37000() throws Exception {
    when(priceService.getLastPrice()).thenReturn(null);
    when(priceService.savePrice(any(Price.class))).thenAnswer(inv -> inv.getArgument(0));

    simulationRunner.run();

    ArgumentCaptor<Price> captor = ArgumentCaptor.forClass(Price.class);
    verify(priceService).savePrice(captor.capture());

    Price saved = captor.getValue();
    assertThat(saved.getValue()).isEqualByComparingTo(BigDecimal.valueOf(37000));
    assertThat(saved.getTimestamp()).isNotNull();
  }

  @Test
  void testRunDoesNotSavePriceWhenPricesAlreadyExist() throws Exception {
    when(priceService.getLastPrice())
        .thenReturn(new Price(BigDecimal.valueOf(37000), LocalDateTime.now()));

    simulationRunner.run();

    verify(priceService).getLastPrice();
    verifyNoMoreInteractions(priceService);
  }

  @Test
  void testRunInvokesSavePriceExactlyOnce() throws Exception {
    when(priceService.getLastPrice()).thenReturn(null);
    when(priceService.savePrice(any(Price.class))).thenAnswer(inv -> inv.getArgument(0));

    simulationRunner.run();

    verify(priceService).getLastPrice();
    verify(priceService).savePrice(any(Price.class));
    verifyNoMoreInteractions(priceService);
  }
}
