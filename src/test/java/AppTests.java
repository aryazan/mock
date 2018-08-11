import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
    @Mock
    private HttpClient httpClient;

    @InjectMocks
    GetMethod getMethod;
    PostMethod postMethod;


    @Test
    public void testMethod1() throws Exception {
      //  Application application = mock(Application.class);
     //   when(application.runMethod(1))

        when(application.ggg()).thenReturn(22);

        System.out.println(application.ggg());

        when(application.ggg()).thenReturn(22).thenCallRealMethod();

//        Application application = new Application();
//        application.runMethod(1);
    }
}
