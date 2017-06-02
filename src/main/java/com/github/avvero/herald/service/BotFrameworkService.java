package com.github.avvero.herald.service;

import com.github.avvero.herald.config.LoggingClientHttpRequestInterceptor;
import com.github.avvero.herald.dto.bf.ConversationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fxdev-belyaev-ay
 */
@Slf4j
@Service
public class BotFrameworkService {

    @Autowired
    MsOAuthService msOAuthService;

    @Autowired
    private Environment env;


    public void send(ConversationMessage message) {
        send(msOAuthService.token().getAccess_token(), message);
    }

    public void send(String token, ConversationMessage message) {
        String serviceUrl = message.getServiceUrl();
        if (serviceUrl == null || serviceUrl.trim().isEmpty()) {
            serviceUrl = env.getRequiredProperty("botframework.v3.conversations.service.url");
        }
        if (!serviceUrl.endsWith("/")) {
            serviceUrl = serviceUrl + "/";
        }
        String url = String.format(env.getRequiredProperty("botframework.v3.conversations.activities.url"),
                serviceUrl, message.getConversation().getId());

        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new LoggingClientHttpRequestInterceptor());
        restTemplate.setInterceptors(interceptors );

        HttpEntity<ConversationMessage> request = new HttpEntity(message, createHeaders(token));
        restTemplate.postForEntity(url, request, String.class);
    }

    HttpHeaders createHeaders(final String token) {
        return new HttpHeaders() {
            {
                set("Authorization", "Bearer " + token);
                set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            }
        };
    }

}
