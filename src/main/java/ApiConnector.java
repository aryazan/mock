import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;

public class ApiConnector {

    private static ApiConnector instance;
    private HttpClient client;
    private GetMethod get;
    private PostMethod post;

    public static ApiConnector getInstance(){
        if(instance == null){
            instance = new ApiConnector();
        }
        return instance;
    }

    private ApiConnector(){
        this.get = new GetMethod("http://192.168.99.100/get");
        this.post = new PostMethod("http://192.168.99.100/post");
    }

    public int performGetRequest(String textMessage){
        System.out.println("Send request");
        int status = 0;
        try {
            client = new HttpClient();
            client.executeMethod(get);
            status = get.getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(textMessage);
        return status;
    }

    public void performPostRequest(String textMessage, String paramName, String paramValue){
        post.addParameter(paramName, paramValue);
        System.out.println(textMessage);
        try {
            client = new HttpClient();
            client.executeMethod(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
