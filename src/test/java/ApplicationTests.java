import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ApiConnector.class, Application.class, Thread.class})
public class ApplicationTests extends BaseTest {

    private static final int INVALID_SWITCH_CASE_NUMBER = 0;
    private ApiConnector apiConnector;

    @Before
    public void setUp() throws Exception {
        apiConnector = mock(ApiConnector.class);
        whenNew(ApiConnector.class).withAnyArguments().thenReturn(apiConnector);
        when(ApiConnector.getInstance()).thenReturn(apiConnector);
    }

    @After
    public void cleanUp() {
        Whitebox.setInternalState(ApiConnector.class, "instance", (Object[]) null);
    }

    @Test
    public void testApplicationSendGetApiRequestViaConnector() throws Exception {
        //given
        when(apiConnector.performGetRequest(anyString())).thenReturn(HttpStatus.SC_OK);

        //when
        Application application = new Application();
        application.runMethod(1);

        //then
        verify(apiConnector, times(1)).performGetRequest(anyString());
    }

    @Test
    public void testApplicationsendPostRequestViaConnector() throws Exception {
        //given
        doNothing().when(apiConnector).performPostRequest(anyString(), anyString(), anyString());

        //when
        Application application = new Application();
        application.runMethod(2);

        //then
        verify(apiConnector, times(1))
                .performPostRequest(anyString(), anyString(), anyString());
    }

    @Test
    public void testApplicationRunPostIfGet200Code200() throws Exception {
        //given
        when(apiConnector.performGetRequest(anyString())).thenReturn(HttpStatus.SC_OK);
        doNothing().when(apiConnector).performPostRequest(anyString(), anyString(), anyString());

        //when
        Application application = new Application();
        application.runMethod(4);

        //then
        verify(apiConnector, times(1))
                .performPostRequest(anyString(), anyString(), anyString());
    }

    @Test(expected = Exception.class)
    public void testApplicationRunPostIfGet200CodeNot200() throws Exception {
        //given
        when(apiConnector.performGetRequest(anyString())).thenReturn(HttpStatus.SC_BAD_GATEWAY);

        //when
        Application application = new Application();
        application.runMethod(4);
    }

    @Test
    public void testApplicationDefaultCaseForRunMethod() throws Exception {

        //when
        Application application = new Application();
        application.runMethod(INVALID_SWITCH_CASE_NUMBER);

        //then
        // verifying that runMethod doesn't call any method
        // from Application class with calling ApiConnector methods
        verifyNoMoreInteractions(apiConnector);
    }

    @Test
    public void testApplicationRunGetRequestsCreateAnotherThread() throws Exception {
        ArgumentCaptor<Runnable> runnables = ArgumentCaptor.forClass(Runnable.class);
        Thread mockedThread = mock(Thread.class);

        //when
        whenNew(Thread.class).withParameterTypes(Runnable.class).withArguments(runnables.capture())
                .thenReturn(mockedThread);
        when(apiConnector.performGetRequest(anyString())).thenReturn(HttpStatus.SC_OK);
        Application application = new Application();
        application.runMethod(3);
        runnables.getValue().run();

        //then
        verify(apiConnector, times(1)).performGetRequest(anyString());
    }
}
