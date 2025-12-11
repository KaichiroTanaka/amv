package dev.aulait.amv.scenario;

import org.junit.jupiter.api.Test;

class SmallCodebaseScenarioIT {

  @Test
  void test() {
    SmallCodebaseScenarioFacade.getInstance().runIfNotLoaded();
  }
}
