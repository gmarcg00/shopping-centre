package api;

import api.model.HealthCheckResponse;
import edu.salle.url.api.ApiHelper;
import edu.salle.url.api.exception.ApiException;
import utils.JsonMapper;

public class Server {
    private ApiHelper apiHelper;

    public Server() {
        try {
            apiHelper = new ApiHelper();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean healthCheck(){
        try {
            this.apiHelper = new ApiHelper();
            HealthCheckResponse response = JsonMapper.getMapper().fromJson(
                    apiHelper.getFromUrl("https://balandrau.salle.url.edu/dpoo/healthcheck"),
                    HealthCheckResponse.class
            );

            return response.getMessage().equals("OK");
        }catch (ApiException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public ApiHelper getApiHelper() {
        return apiHelper;
    }
}
