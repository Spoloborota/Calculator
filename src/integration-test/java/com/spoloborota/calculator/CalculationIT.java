package com.spoloborota.calculator;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spoloborota.calculator.entity.Calculation;
import org.junit.Test;
import org.testcontainers.shaded.org.apache.http.HttpResponse;
import org.testcontainers.shaded.org.apache.http.client.methods.HttpGet;
import org.testcontainers.shaded.org.apache.http.client.methods.HttpPost;
import org.testcontainers.shaded.org.apache.http.client.utils.URIBuilder;
import org.testcontainers.shaded.org.apache.http.entity.StringEntity;
import org.testcontainers.shaded.org.apache.http.impl.client.BasicResponseHandler;
import org.testcontainers.shaded.org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

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

    private static ObjectMapper getObjectMapper() {
        ObjectMapper jacksonMapper = new ObjectMapper();
        jacksonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jacksonMapper.registerModule(new JavaTimeModule());
        jacksonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return jacksonMapper;
    }
    private static Object extract(final byte[] json, final Class<?> aClass) throws IOException {
        if (json == null) {
            return null;
        }
        return getObjectMapper().readValue(json, aClass);
    }
    private static Object extract(final String json, final Class<?> aClass) throws IOException {
        if (json == null) {
            return null;
        }
        return getObjectMapper().readValue(json, aClass);
    }

    @Test
    public void all() throws IOException, URISyntaxException {

        //calculate
        assertEquals("2401.0", calculate("(-7*8+9-(9/4.5))^2"));
        assertEquals("6.0", calculate("2+2*2"));
        assertEquals("13.5", calculate("9*1+4.5"));

        //count
        assertEquals("3", count());

        //onDate
        String onDateJsonResult = onDate();
        JsonContents onDateJson = (JsonContents)extract(onDateJsonResult, JsonContents.class);
        Map<Long,Calculation> onDateMap = onDateJson.getContent().stream()
                .collect(Collectors.toMap(Calculation::getId, c->c));
        byte[] jsonOnDateBytesExpected = Files.readAllBytes(
                Paths.get(getClass().getClassLoader().getResource("onDate.json").toURI()));
        JsonContents jsonOnDateExpected = (JsonContents)extract(jsonOnDateBytesExpected, JsonContents.class);
        jsonOnDateExpected.getContent().forEach(c -> assertEquals(onDateMap.get(c.getId()),c));

        //onOperation
        String onOperationJsonResult = onOperation();
        JsonContents onOperationJson = (JsonContents)extract(onOperationJsonResult, JsonContents.class);
        Map<Long,Calculation> onOperationMap = onOperationJson.getContent().stream()
                .collect(Collectors.toMap(Calculation::getId, c->c));
        byte[] jsonOnOperationBytesExpected = Files.readAllBytes(
                Paths.get(getClass().getClassLoader().getResource("onOperation.json").toURI()));
        JsonContents jsonOnOperationExpected = (JsonContents)extract(jsonOnOperationBytesExpected, JsonContents.class);
        jsonOnOperationExpected.getContent().forEach(c -> {assertEquals(onOperationMap.get(c.getId()),c); });

        //operation
        assertEquals("1", operation());

        //popular
        assertEquals("2.0", popular());
    }

    private String popular() throws IOException {
        HttpGet request = new HttpGet(uriPopular);
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        return handler.handleEntity(response.getEntity());
    }

    private String operation() throws IOException, URISyntaxException {
        HttpGet request = new HttpGet(new URIBuilder(uriOperation)
                .addParameter("operation", "-")
                .build());
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        return handler.handleEntity(response.getEntity());
    }

    private String onOperation() throws IOException, URISyntaxException {
        HttpGet request = new HttpGet(new URIBuilder(uriOnOperation)
                .addParameter("page","0")
                .addParameter("size","5")
                .addParameter("operation", "-")
                .build());
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        return handler.handleEntity(response.getEntity());
    }

    private String onDate() throws IOException, URISyntaxException {
        HttpGet request = new HttpGet(new URIBuilder(uriOnDate)
                .addParameter("page","0")
                .addParameter("size","5")
                .addParameter("date", LocalDate.now().toString())
                .build());
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        return handler.handleEntity(response.getEntity());
    }

    private String count() throws IOException, URISyntaxException {
        HttpGet request = new HttpGet(new URIBuilder(uriCount)
                .addParameter("date", LocalDate.now().toString())
                .build());
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
