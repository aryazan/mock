import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ApiConnector.class)
public class ApiConnectorTests {

    private HttpClient client;
    private GetMethod get;
    private PostMethod post;
    private static final String POST_PARAM_NAME = "paramName";
    private static final String POST_PARAM_VALUE = "paramValue";
    private static final String TEXT_MESSAGE_TEMPLATE_FOR_TEST = "This is a template message for the test: [%s]";

    @Before
    public void setUp() {
        client = mock(HttpClient.class);
        get = mock(GetMethod.class);
        post = mock(PostMethod.class);
        Whitebox.setInternalState(ApiConnector.class, "instance", (Object[]) null);
    }


    @Test
    public void test_ApiConnector_performGetRequest_return_code_200_if_ok() throws Exception {
        //given
        PowerMockito.whenNew(GetMethod.class).withAnyArguments().thenReturn(get);
        PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(client);

        //when
        ApiConnector apiConnector = ApiConnector.getInstance();
        when(get.getStatusCode()).thenReturn(HttpStatus.SC_OK);

        //then
        assertEquals(HttpStatus.SC_OK, apiConnector.performGetRequest("test message"));
    }

    @Test
    public void test_ApiConnector_performGetRequest_return_code_0_if_exception() throws Exception {
        //given
        PowerMockito.whenNew(GetMethod.class).withAnyArguments().thenReturn(get);
        PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(client);

        //when
        ApiConnector apiConnector = ApiConnector.getInstance();
        when(client.executeMethod(get)).thenThrow(IOException.class);

        //then
        assertEquals(0, apiConnector.performGetRequest("test message"));
    }

    @Test()
    public void testApiConnectorPerformGetRequest() throws Exception {
        //given
        PowerMockito.whenNew(GetMethod.class).withAnyArguments().thenReturn(get);
        PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(client);

        //when
        ApiConnector apiConnector = ApiConnector.getInstance();
        when(client.executeMethod(get)).thenThrow(IOException.class);
        apiConnector.performGetRequest("test");
        Mockito.verify(client, Mockito.times(1)).executeMethod(get);
    }

    @Test
    public void test_ApiConnector_performPostRequest() throws Exception {
        //given
        PowerMockito.whenNew(PostMethod.class).withAnyArguments()
                .thenReturn(post);
        PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(client);
        //when
        ApiConnector apiConnector = ApiConnector.getInstance();

        doNothing().when(post).addParameter(POST_PARAM_NAME, POST_PARAM_VALUE);
        when(client.executeMethod(post)).thenReturn(HttpStatus.SC_OK);
        apiConnector.performPostRequest("Message for post msg", POST_PARAM_NAME, POST_PARAM_VALUE);

        //then
        Mockito.verify(client, Mockito.times(1)).executeMethod(post);
    }

    @Test
    public void test_ApiConnector_performPostRequest_parameters_set() throws Exception {
        //given
        PowerMockito.whenNew(PostMethod.class).withAnyArguments().thenReturn(post);
        PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(client);

        //when
        ApiConnector apiConnector = ApiConnector.getInstance();
        doNothing().when(post).addParameter(POST_PARAM_NAME, POST_PARAM_VALUE);
        apiConnector.performPostRequest("Message for post msg", POST_PARAM_NAME, POST_PARAM_VALUE);

        //then
        Mockito.verify(post, Mockito.times(1)).addParameter(POST_PARAM_NAME, POST_PARAM_VALUE);
    }

    @Test
    public void testApiConnectorGetInstanceNotNull() {
        ApiConnector apiConnector = ApiConnector.getInstance();
        assertNotNull("Instance wasn't recieved", apiConnector);
    }

    @After
    public void cleanUp() {
        Mockito.reset(post);
        Mockito.reset(get);
        Mockito.reset(client);
    }
}
