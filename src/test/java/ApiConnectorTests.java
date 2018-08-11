import javafx.geometry.Pos;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.IOException;

import static org.powermock.api.mockito.PowerMockito.when;

    @RunWith(PowerMockRunner.class)
    @PrepareForTest(ApiConnector.class)
    public class ApiConnectorTests {

        @Mock
        private HttpClient client;
        @Mock
        private GetMethod get;
        @Mock
        private PostMethod post;


        @Test
        public void exceptionWhenPost() throws Exception

        {
            PowerMockito.whenNew(GetMethod.class).withAnyArguments().thenReturn(get);
            PowerMockito.whenNew(PostMethod.class).withAnyArguments().thenReturn(post);
            PowerMockito.whenNew(HttpClient.class).withAnyArguments().thenReturn(client);

            ApiConnector apiConnector = ApiConnector.getInstance();
            when(client.executeMethod(get)).thenReturn(1);
            //when(get.getStatusCode()).thenReturn(202);
            when(get.getStatusCode()).thenThrow(IOException.class);
            System.out.println(apiConnector.performGetRequest("123"));

        }
}
