import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ApiConnector.class, Application.class, Thread.class})
public class ApplicationTests extends BaseTest {

    private static final int INVALID_SWITCH_CASE_NUMBER = 0;
    private static final int SEND_GET_CASE_NUMBER = 1;
    private static final int SEND_POST_CASE_NUMBER = 2;
    private static final int SEND_GET_IN_ANOTHER_THREAD_CASE_NUMBER = 3;
    private static final int SEND_POST_IF_GET_200_CASE_NUMBER = 4;
    private ApiConnector apiConnector;

    @Before
    public void setUp() throws Exception {
        apiConnector = mock(ApiConnector.class);
        whenNew(ApiConnector.class).withAnyArguments().thenReturn(apiConnector);
        when(ApiConnector.getInstance()).thenReturn(apiConnector);
    }

    @Test
    public void testApplicationSendGetApiRequestViaConnector() throws Exception {
        //given
        when(apiConnector.performGetRequest(anyString())).thenReturn(HttpStatus.SC_OK);
        Application application = new Application();

        //when
        application.runMethod(SEND_GET_CASE_NUMBER);

        //then
        verify(apiConnector, times(1)).performGetRequest(anyString());
    }

    @Test
    public void testApplicationsendPostRequestViaConnector() throws Exception {
        //given
        doNothing().when(apiConnector).performPostRequest(anyString(), anyString(), anyString());
        Application application = new Application();

        //when
        application.runMethod(SEND_POST_CASE_NUMBER);

        //then
        verify(apiConnector, times(1))
                .performPostRequest(anyString(), anyString(), anyString());
    }

    @Test
    public void testApplicationRunPostIfGet200Code200() throws Exception {
        //given
        when(apiConnector.performGetRequest(anyString())).thenReturn(HttpStatus.SC_OK);
        doNothing().when(apiConnector).performPostRequest(anyString(), anyString(), anyString());
        Application application = new Application();

        //when
        application.runMethod(SEND_POST_IF_GET_200_CASE_NUMBER);

        //then
        verify(apiConnector, times(1))
                .performPostRequest(anyString(), anyString(), anyString());
    }

    @Test(expected = Exception.class)
    public void testApplicationRunPostIfGet200CodeNot200() throws Exception {
        //given
        when(apiConnector.performGetRequest(anyString())).thenReturn(HttpStatus.SC_BAD_GATEWAY);
        Application application = new Application();

        //when
        application.runMethod(SEND_POST_IF_GET_200_CASE_NUMBER);
    }

    @Test
    public void testApplicationDefaultCaseForRunMethod() throws Exception {
        //given
        Application application = new Application();

        //when
        application.runMethod(INVALID_SWITCH_CASE_NUMBER);

        //then
        // verifying that runMethod doesn't call any method
        // from Application class with calling ApiConnector methods
        verifyNoMoreInteractions(apiConnector);
    }

    @Test
    public void testApplicationRunGetRequestsCreateAnotherThread() throws Exception {
        //given
        ArgumentCaptor<Runnable> runnables = ArgumentCaptor.forClass(Runnable.class);
        Thread mockedThread = mock(Thread.class);

        //when
        whenNew(Thread.class).withParameterTypes(Runnable.class).withArguments(runnables.capture())
                .thenReturn(mockedThread);
        doNothing().when(mockedThread).start();

        when(apiConnector.performGetRequest(anyString())).thenReturn(HttpStatus.SC_OK);
        Application application = new Application();
        application.runMethod(SEND_GET_IN_ANOTHER_THREAD_CASE_NUMBER);
        runnables.getValue().run();

        //then
        verify(apiConnector, times(1)).performGetRequest(anyString());
        verify(mockedThread, times(1)).start();

    }

    @Test
    public void testApplicationRunGetRequestInAnotherThreadThreadStart() throws Exception {
        //given
        Thread mockedThread = mock(Thread.class);
        whenNew(Thread.class).withParameterTypes(Runnable.class).withArguments(anyString())
                .thenReturn(mockedThread);

        //when
        doNothing().when(mockedThread).start();
        Application application = new Application();
        application.runMethod(SEND_GET_IN_ANOTHER_THREAD_CASE_NUMBER);

        //then
        verify(mockedThread, times(1)).start();
    }
}
