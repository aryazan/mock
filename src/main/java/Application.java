import org.apache.http.HttpStatus;

public class Application {
    private void sendGetApiRequestViaConnector() {
        ApiConnector.getInstance().performGetRequest("perform GET request");
    }

    private void sendPostRequestViaConnector() {
        ApiConnector.getInstance().performPostRequest("perform POST request", "paramName", "paramValue");
    }

    private void runGetRequestsInAnotherThread() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                ApiConnector.getInstance().performGetRequest("perform GET request in thread" + Thread.currentThread().getId());
            }
        });
        t.start();
    }

    private void runPostIfGet200() throws Exception {
        int status = ApiConnector.getInstance().performGetRequest("text");
        if (status == HttpStatus.SC_OK) {
            ApiConnector.getInstance().performPostRequest("text", "param", "value");
        } else {
            throw new Exception("GET response is not 200");
        }
    }

    public void runMethod(int method) throws Exception {
        switch (method) {
            case 1:
                sendGetApiRequestViaConnector();
                break;
            case 2:
                sendPostRequestViaConnector();
                break;
            case 3:
                runGetRequestsInAnotherThread();
                break;
            case 4:
                runPostIfGet200();
                break;
            default:
                break;
        }
    }

}
