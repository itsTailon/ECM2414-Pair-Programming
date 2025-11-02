package tests;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.PrintWriter;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class TestRunner {

    /**
     * Runs all unit tests
     * @param args
     */
    public static void main(String[] args) {
        LauncherDiscoveryRequest req = LauncherDiscoveryRequestBuilder.request().selectors(selectPackage("tests")).filters(includeClassNamePatterns(".*Test")).build();
        Launcher launcher = LauncherFactory.create();
        TestPlan testPlan = launcher.discover(req);
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(req);

        // Output test results to console
        TestExecutionSummary summary = listener.getSummary();
        summary.printTo(new PrintWriter(System.out));
    }
}
