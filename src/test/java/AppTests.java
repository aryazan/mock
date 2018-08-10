import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito.*;

import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.*;


@RunWith(PowerMockRunner.class)
public class AppTests {

    @Mock
    private Application application;

    @Test
    public void testMethod1() throws Exception {
        Application application = mock(Application.class);
     //   when(application.runMethod(1))
        when(application.ggg()).thenReturn(22);

        System.out.println(application.ggg());

        when(application.ggg()).thenReturn(22).thenCallRealMethod();

        System.out.println(application.ggg());
        System.out.println(application.ggg());
        System.out.println(application.ggg());
//        Application application = new Application();
//        application.runMethod(1);
    }
}
