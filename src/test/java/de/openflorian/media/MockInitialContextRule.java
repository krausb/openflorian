package de.openflorian.media;

import javax.naming.Context;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * {@link TestRule} for JNDI Application Server Context Emulation<br/>
 * <br/>
 * Initializes the default context.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
public class MockInitialContextRule implements TestRule {

    private final Context context;

    public MockInitialContextRule(Context context) {
        this.context = context;        
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                System.setProperty(Context.INITIAL_CONTEXT_FACTORY, MockInitialContextFactory.class.getName());
                MockInitialContextFactory.setCurrentContext(context);
                try {
                    base.evaluate();
                } finally {
                    System.clearProperty(Context.INITIAL_CONTEXT_FACTORY);
                    MockInitialContextFactory.clearCurrentContext();
                }
            }
        };
    }
}
