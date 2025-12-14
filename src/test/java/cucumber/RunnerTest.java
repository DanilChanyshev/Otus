package cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("scenarious")
@ConfigurationParameter(key = "cucumber.glue", value = "cucumber.steps,cucumber.hooks")
public class RunnerTest {
}
