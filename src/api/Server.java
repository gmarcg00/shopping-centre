package api;

import edu.salle.url.api.ApiHelper;
import edu.salle.url.api.exception.ApiException;

public class Server {
    private ApiHelper apiHelper;

    public Server() {
        try {
            this.apiHelper = new ApiHelper();
        }catch (ApiException e) {
            System.out.println("Failed to create ApiHelper: " + e.getMessage());
        }
    }

    public ApiHelper getApiHelper() {
        return apiHelper;
    }
}
