import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.lang.reflect.Constructor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ApiConnector.class)
public class ApiConnectorTests extends BaseTest {

    private HttpClient mockedClient;
    private GetMethod mockedGet;
    private PostMethod mockedPost;
    private static final String POST_PARAM_NAME = "paramName";
    private static final String POST_PARAM_VALUE = "paramValue";
    private static final String TEXT_MESSAGE_TEMPLATE_FOR_TEST = "This is a template message for the test";

    @Before
    public void setUp() {
        mockedClient = mock(HttpClient.class);
        mockedGet = mock(GetMethod.class);
        mockedPost = mock(PostMethod.class);
    }


    @Test
    public void testApiConnectorPerformGetRequestReturnCodeIs200IfOk() throws Exception {
        //given
        whenNew(GetMethod.class).withAnyArguments().thenReturn(mockedGet);
        whenNew(HttpClient.class).withAnyArguments().thenReturn(mockedClient);
        ApiConnector apiConnector = ApiConnector.getInstance();

        //when
        when(mockedGet.getStatusCode()).thenReturn(HttpStatus.SC_OK);

        //then
        assertEquals(HttpStatus.SC_OK, apiConnector.performGetRequest(TEXT_MESSAGE_TEMPLATE_FOR_TEST));
    }

    @Test
    public void testApiConnectorPerformGetRequestReturnCode0IfException() throws Exception {
        //given
        whenNew(GetMethod.class).withAnyArguments().thenReturn(mockedGet);
        whenNew(HttpClient.class).withAnyArguments().thenReturn(mockedClient);
        ApiConnector apiConnector = ApiConnector.getInstance();

        //when
        when(mockedClient.executeMethod(mockedGet)).thenThrow(IOException.class);

        //then
        assertEquals(0, apiConnector.performGetRequest(TEXT_MESSAGE_TEMPLATE_FOR_TEST));
    }

    @Test()
    public void testApiConnectorPerformGetRequestExceptionCatching() throws Exception {
        //given
        whenNew(GetMethod.class).withAnyArguments().thenReturn(mockedGet);
        whenNew(HttpClient.class).withAnyArguments().thenReturn(mockedClient);
        ApiConnector apiConnector = ApiConnector.getInstance();

        //when
        when(mockedClient.executeMethod(mockedGet)).thenThrow(IOException.class);
        apiConnector.performGetRequest(TEXT_MESSAGE_TEMPLATE_FOR_TEST);

        //then
        verify(mockedClient, times(1)).executeMethod(mockedGet);
    }

    @Test
    public void testApiConnectorPerformPostRequestValidParams() throws Exception {
        //given
        whenNew(PostMethod.class).withAnyArguments().thenReturn(mockedPost);
        whenNew(HttpClient.class).withAnyArguments().thenReturn(mockedClient);
        ApiConnector apiConnector = ApiConnector.getInstance();

        //when
        doNothing().when(mockedPost).addParameter(POST_PARAM_NAME, POST_PARAM_VALUE);
        when(mockedClient.executeMethod(mockedPost)).thenReturn(HttpStatus.SC_OK);
        apiConnector.performPostRequest(TEXT_MESSAGE_TEMPLATE_FOR_TEST, POST_PARAM_NAME, POST_PARAM_VALUE);

        //then
        verify(mockedClient, times(1)).executeMethod(mockedPost);
    }

    @Test
    public void testApiConnectorPerformPostRequestParametersSetting() throws Exception {
        //given
        whenNew(PostMethod.class).withAnyArguments().thenReturn(mockedPost);
        whenNew(HttpClient.class).withAnyArguments().thenReturn(mockedClient);
        ApiConnector apiConnector = ApiConnector.getInstance();

        //when
        doNothing().when(mockedPost).addParameter(POST_PARAM_NAME, POST_PARAM_VALUE);
        apiConnector.performPostRequest(TEXT_MESSAGE_TEMPLATE_FOR_TEST, POST_PARAM_NAME, POST_PARAM_VALUE);

        //then
        verify(mockedPost, times(1)).addParameter(POST_PARAM_NAME, POST_PARAM_VALUE);
    }

    @Test
    public void testApiConnectorPerformPostRequestExceptionCatching() throws Exception {
        //given
        whenNew(PostMethod.class).withAnyArguments().thenReturn(mockedPost);
        whenNew(HttpClient.class).withAnyArguments().thenReturn(mockedClient);
        ApiConnector apiConnector = ApiConnector.getInstance();

        //when
        when(mockedClient.executeMethod(mockedPost)).thenThrow(IOException.class);
        apiConnector.performPostRequest(TEXT_MESSAGE_TEMPLATE_FOR_TEST, POST_PARAM_NAME, POST_PARAM_VALUE);

        //then
        verify(mockedClient, times(1)).executeMethod(mockedPost);
    }

    @Test
    public void testApiConnectorGetInstanceIsNotNull() {
        //when
        ApiConnector apiConnector = ApiConnector.getInstance();

        //then
        assertNotNull("Instance wasn't recieved", apiConnector);
    }

    @Test
    public void testApiConnectorGetInstanceSingleton(){
        //when
        ApiConnector firstCallSingleton = ApiConnector.getInstance();
        ApiConnector secondCallSingleton = ApiConnector.getInstance();

        //then
        assertEquals("Objects are not equal", firstCallSingleton, secondCallSingleton);
    }

    @Test
    public void testApiConnectorConstructorExecution() throws Exception {
        //This test just checking that consturctor from class ApiConnector is working without exceptions via
        // Reflection API
        //given
        Constructor<ApiConnector> constructor = ApiConnector.class.getDeclaredConstructor();

        //when
        assertFalse(constructor.isAccessible());
        constructor.setAccessible(true);

        //then
        constructor.newInstance((Object[]) null);
    }
}
