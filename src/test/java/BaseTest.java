import org.junit.After;
import org.powermock.reflect.Whitebox;

public abstract class BaseTest {

    @After
    public void cleanUp() {
        Whitebox.setInternalState(ApiConnector.class, "instance", (Object[]) null);
    }
}
