package com.spoloborota.calculator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;
import org.testcontainers.shaded.org.apache.http.HttpResponse;
import org.testcontainers.shaded.org.apache.http.HttpStatus;
import org.testcontainers.shaded.org.apache.http.client.ClientProtocolException;
import org.testcontainers.shaded.org.apache.http.client.ResponseHandler;
import org.testcontainers.shaded.org.apache.http.client.methods.HttpGet;
import org.testcontainers.shaded.org.apache.http.client.methods.HttpPost;
import org.testcontainers.shaded.org.apache.http.client.methods.HttpUriRequest;
import org.testcontainers.shaded.org.apache.http.entity.ContentType;
import org.testcontainers.shaded.org.apache.http.entity.StringEntity;
import org.testcontainers.shaded.org.apache.http.impl.client.BasicResponseHandler;
import org.testcontainers.shaded.org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class CalculationIT {


    @Test
    public void test() {
        System.out.println("This is an integration test.==============");
    }

    @Test
    public void givenUserDoesNotExists_whenUserInfoIsRetrieved_then404IsReceived()
            throws ClientProtocolException, IOException {

        // Given
        String name = RandomStringUtils.randomAlphabetic( 8 );
        HttpUriRequest request = new HttpGet( "https://api.github.com/users/" + name );

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    public void sync() throws ClientProtocolException, IOException {
        // Given
        final String jsonMimeType = "application/json";
        final HttpPost request = new HttpPost("http://localhost:8080/calculate");
        StringEntity stringEntity = new StringEntity("2+2*2", Charset.forName("utf-8"));
        stringEntity.setContentType("application/json");
        request.setEntity(stringEntity);

        // When
        final HttpResponse response = HttpClientBuilder.create().build().execute(request);
        BasicResponseHandler handler = new BasicResponseHandler();
//        try {
//            Thread.sleep(5*60*1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // Then
        final String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        String result = handler.handleEntity(response.getEntity());
        System.out.println("RESULT SYNC IS " + result);
        assertEquals(jsonMimeType, mimeType);
    }

    @Test
    public void async() throws ClientProtocolException, IOException {// Given
        final String jsonMimeType = "application/json";
        final HttpPost request = new HttpPost("http://localhost:8080/async/calculate");
        StringEntity stringEntity = new StringEntity("2+2*2", Charset.forName("utf-8"));
        stringEntity.setContentType("application/json");
        request.setEntity(stringEntity);

        // When
        final HttpResponse response = HttpClientBuilder.create().build().execute(request);
        BasicResponseHandler handler = new BasicResponseHandler();
//        try {
//            Thread.sleep(5*60*1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // Then
        final String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        String result = handler.handleEntity(response.getEntity());
        System.out.println("RESULT ASYNC IS " + result);
        assertEquals(jsonMimeType, mimeType);
    }

//    @Test
//    public void calc_test() throws Exception {
//        MvcResult result = mockMvc.perform(get("/async/calculate").contentType(MediaType.APPLICATION_JSON).content("2+2*2"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andReturn();
//        System.out.println("Result: " + result.getResponse().getContentAsString());
//        // @formatter:on
//    }
}
