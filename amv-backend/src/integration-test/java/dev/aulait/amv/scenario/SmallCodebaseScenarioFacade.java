package dev.aulait.amv.scenario;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.aulait.amv.arch.async.AsyncExecStatus;
import dev.aulait.amv.arch.async.AsyncExecWsClient;
import dev.aulait.amv.interfaces.project.CodebaseClient;
import dev.aulait.amv.interfaces.project.CodebaseController.CodebaseSearchResultDto;
import dev.aulait.amv.interfaces.project.CodebaseDto;
import dev.aulait.amv.interfaces.project.CodebaseSearchCriteriaDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class SmallCodebaseScenarioFacade {

  private AsyncExecWsClient asyncExecWsClient = new AsyncExecWsClient();
  private CodebaseClient codebaseClient = new CodebaseClient();

  private static final SmallCodebaseScenarioFacade SINGLETON = new SmallCodebaseScenarioFacade();

  public static SmallCodebaseScenarioFacade getInstance() {
    return SINGLETON;
  }

  public synchronized void runIfNotLoaded() {
    if (isLoaded()) {
      log.info("Simple codebase is already loaded.");
    } else {
      log.info("Loading simple codebase...");
      run();
    }
  }

  public boolean isLoaded() {
    CodebaseSearchCriteriaDto request = new CodebaseSearchCriteriaDto();
    request.setText("amv-test-repository");
    CodebaseSearchResultDto result = codebaseClient.search(request);
    return !result.getList().isEmpty();
  }

  public void run() {
    CodebaseDto codebaseDto =
        CodebaseDto.builder()
            .url("https://github.com/project-au-lait/amv-test-repository.git")
            .build();

    String codebaseId = codebaseClient.save(codebaseDto);

    codebaseClient.analyze(codebaseId);

    assertEquals(AsyncExecStatus.COMPLETED, asyncExecWsClient.waitUntilFinished(codebaseId));
  }
}
