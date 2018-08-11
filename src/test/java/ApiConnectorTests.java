 import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpStatus;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.IOException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ApiConnector.class)
public class ApiConnectorTests {

    private HttpClient client;
    private GetMethod get;
    private PostMethod post;

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
        PowerMockito.whenNew(PostMethod.class).withAnyArguments().thenReturn(post);
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
        PowerMockito.whenNew(PostMethod.class).withAnyArguments().thenReturn(post);
        PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(client);

        //when
        ApiConnector apiConnector = ApiConnector.getInstance();
        when(client.executeMethod(get)).thenThrow(IOException.class);

        //then
        assertEquals(0, apiConnector.performGetRequest("test message"));
    }

    @Test
    public void test_ApiConnector_performPostRequest() throws Exception {
        //given
        PowerMockito.whenNew(GetMethod.class).withAnyArguments().thenReturn(get);
        PowerMockito.whenNew(PostMethod.class).withAnyArguments()
                .thenReturn(post);
        PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(client);
        //when
        ApiConnector apiConnector = ApiConnector.getInstance();

        doNothing().when(post).addParameter("parN", "parV");
        when(client.executeMethod(post)).thenReturn(HttpStatus.SC_OK);
        apiConnector.performPostRequest("Message for post msg", "parN", "parV");

        PowerMockito.verifyNoMoreInteractions();
     //   Mockito.verify(client.executeMethod(post), Mockito.times(1));
        //then
      //  assertEquals(0, apiConnector.performGetRequest("test post method"));
    }
}
