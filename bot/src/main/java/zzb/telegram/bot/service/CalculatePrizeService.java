package zzb.telegram.bot.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import zzb.telegram.bot.models.CalculateRequest;
import zzb.telegram.bot.models.CalculateResponse;
import zzb.telegram.bot.models.Data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@Service
public class CalculatePrizeService {

    @Value("${calc.url}")
    private String calcUrl;
    private static final Logger logger = LoggerFactory.getLogger(CalculatePrizeService.class);

    public CalculateResponse getCalculateResults(CalculateRequest calculateRequest) {

        RestTemplate restTemplate = new RestTemplate();

        RequestEntity<String> requestEntity = RequestEntity
                .post(calcUrl)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(calculateRequest.toString());

        ResponseEntity<String> exchange = restTemplate.exchange(requestEntity, String.class);

        String s = exchange.getBody();
        if (s != null) {
            InputStream is = new ByteArrayInputStream(s.getBytes());
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(is);
                JsonNode d = jsonNode.get("d");

                Gson gson = new Gson();
                Data data = gson.fromJson(d.asText(), Data.class);

                return new CalculateResponse(data);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage(), e);
                return null;

            }
        }
        return null;
    }
}
