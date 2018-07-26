package com.spoloborota.calculator;

import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;


public class CalculationIT {

    private static final String uriCalculate = "http://localhost:8080/async/calculate";
    private static final String uriCount = "http://localhost:8080/async/count";
    private static final String uriOnDate = "http://localhost:8080/async/onDate";
    private static final String uriOnOperation = "http://localhost:8080/async/onOperation";
    private static final String uriOperation = "http://localhost:8080/async/operation";
    private static final String uriPopular = "http://localhost:8080/async/popular";

    private static final String jsonContentType = "application/json";

    private static final BasicResponseHandler handler = new BasicResponseHandler();

    @Test
    public void all() throws IOException, InterruptedException {
        assertEquals("2401.0", calculate("(-7*8+9-(9/4.5))^2"));
        assertEquals("6.0", calculate("2+2*2"));
        assertEquals("13.5", calculate("9*1+4.5"));
        assertEquals("3", count());

    }

    private String onDate(){
        MultipartEntityBuilder builder = MultipartEntityBuilder
        HttpPost request = new HttpPost(uriCount);
        StringEntity stringEntity = new StringEntity("\"" + LocalDate.now().toString() + "\"", Charset.forName("utf-8"));
        stringEntity.setContentType(jsonContentType);
        request.setEntity(stringEntity);
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        return handler.handleEntity(response.getEntity());

        return null;
    }

    private String count() throws IOException {
        HttpPost request = new HttpPost(uriCount);
        StringEntity stringEntity = new StringEntity("\"" + LocalDate.now().toString() + "\"", Charset.forName("utf-8"));
        stringEntity.setContentType(jsonContentType);
        request.setEntity(stringEntity);
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        return handler.handleEntity(response.getEntity());
    }

    private String calculate(String expression) throws IOException {
        HttpPost request = new HttpPost(uriCalculate);
        StringEntity stringEntity = new StringEntity(expression, Charset.forName("utf-8"));
        stringEntity.setContentType(jsonContentType);
        request.setEntity(stringEntity);
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        return handler.handleEntity(response.getEntity());
    }
}
