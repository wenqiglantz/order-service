package com.github.wenqiglantz.service.orderservice.restcontroller;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.messaging.MessagePact;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dapr.client.domain.CloudEvent;
import io.pactfoundation.consumer.dsl.LambdaDsl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "customer-service", port = "9100", providerType = ProviderType.ASYNCH)
@SpringBootTest
public class SubscriberControllerTest {

    @Autowired
    private SubscriberController subscriberController;

    private static final String TOPIC = "order-service";
    private static final String PUBSUBNAME = "customer-order-integration";
    private static final String ROUTE_OPERATIONS = "/customer-operations";
    private static final String JSON = "application/json";
    private static final String CLOUD_EVENT_TYPE_SENT = "com.dapr.event.sent";
    private static final String CLOUD_EVENT_SPEC_VERSION = "1.0";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Pact(consumer = "order-service")
    public MessagePact validCustomerCrudFromProvider(MessagePactBuilder builder) {

        final Map<String, Object> expectedMetadata = new HashMap<>();
        expectedMetadata.put("topic", TOPIC);
        expectedMetadata.put("pubsubName", PUBSUBNAME);
        expectedMetadata.put("route", ROUTE_OPERATIONS);
        expectedMetadata.put("Content-Type", JSON);

        return builder
                .hasPactWith("customer-service")
                .expectsToReceive("valid CustomerWasCreated from provider") //request description for CustomerWasCreated
                .withContent(LambdaDsl.newJsonBody((object) -> {
                    object.uuid("customerId", UUID.fromString("595eed0c-eff5-4278-90ad-b952f18dbee8"));
                    object.stringMatcher("firstName", "^[a-zA-Z0-9]{1,16}$", "test");
                    object.stringMatcher("lastName", "^[a-zA-Z0-9]{1,16}$", "test");
                    object.stringValue("status", "CREATED");
                }).build())
                .expectsToReceive("valid CustomerWasUpdated from provider") //request description for CustomerWasUpdated
                .withContent(LambdaDsl.newJsonBody((object) -> {
                    object.uuid("customerId", UUID.fromString("595eed0c-eff5-4278-90ad-b952f18dbee8"));
                    object.stringMatcher("firstName", "^[a-zA-Z0-9]{1,16}$", "testUpdated");
                    object.stringMatcher("lastName", "^[a-zA-Z0-9]{1,16}$", "test");
                    object.stringValue("status", "UPDATED");
                }).build())
                .expectsToReceive("valid CustomerWasDeleted from provider") //request description for CustomerWasDeleted
                .withContent(LambdaDsl.newJsonBody((object) -> {
                    object.uuid("customerId", UUID.fromString("595eed0c-eff5-4278-90ad-b952f18dbee8"));
                    object.stringValue("status", "DELETED");
                }).build())
                .withMetadata(expectedMetadata)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "validCustomerCrudFromProvider")
    public void testCustomerCreatedEventFromProvider(MessagePact messagePact) throws Exception {
        //get data from messagePact
        String data = messagePact.getMessages().get(0).getContents().valueAsString();

        //convert data json string into LinkedHashMap as Dapr CloudEvent stores data in LinkedHashMap format
        LinkedHashMap<String, String> map = OBJECT_MAPPER.readValue(data, LinkedHashMap.class);

        //construct a Dapr CloudEvent based on messagePact, to be passed to SubscriberController to be consumed
        CloudEvent cloudEvent = new CloudEvent();
        cloudEvent.setId(UUID.randomUUID().toString());
        cloudEvent.setType(CLOUD_EVENT_TYPE_SENT);
        cloudEvent.setSpecversion(CLOUD_EVENT_SPEC_VERSION);
        cloudEvent.setDatacontenttype(JSON);
        cloudEvent.setData(map);

        subscriberController.consumeCustomerCrudEvent(cloudEvent);
    }


    @Test
    @PactTestFor(pactMethod = "validCustomerCrudFromProvider")
    public void testCustomerUpdatedEventFromProvider(MessagePact messagePact) throws Exception {

        //prep the data first before updating it
        testCustomerCreatedEventFromProvider(messagePact);

        //get data from messagePact
        String data = messagePact.getMessages().get(1).getContents().valueAsString();

        //convert data json string into LinkedHashMap as Dapr CloudEvent stores data in LinkedHashMap format
        LinkedHashMap<String, String> map = OBJECT_MAPPER.readValue(data, LinkedHashMap.class);

        //construct a Dapr CloudEvent based on messagePact, to be passed to SubscriberController to be consumed
        CloudEvent cloudEvent = new CloudEvent();
        cloudEvent.setId(UUID.randomUUID().toString());
        cloudEvent.setType(CLOUD_EVENT_TYPE_SENT);
        cloudEvent.setSpecversion(CLOUD_EVENT_SPEC_VERSION);
        cloudEvent.setDatacontenttype(JSON);
        cloudEvent.setData(map);

        subscriberController.consumeCustomerCrudEvent(cloudEvent);
    }

    @Test
    @PactTestFor(pactMethod = "validCustomerCrudFromProvider")
    public void testCustomerDeletedEventFromProvider(MessagePact messagePact) throws Exception {

        //prep the data first before updating it
        testCustomerCreatedEventFromProvider(messagePact);

        //get data from messagePact
        String data = messagePact.getMessages().get(2).getContents().valueAsString();

        //convert data json string into LinkedHashMap as Dapr CloudEvent stores data in LinkedHashMap format
        LinkedHashMap<String, String> map = OBJECT_MAPPER.readValue(data, LinkedHashMap.class);

        //construct a Dapr CloudEvent based on messagePact, to be passed to SubscriberController to be consumed
        CloudEvent cloudEvent = new CloudEvent();
        cloudEvent.setId(UUID.randomUUID().toString());
        cloudEvent.setType(CLOUD_EVENT_TYPE_SENT);
        cloudEvent.setSpecversion(CLOUD_EVENT_SPEC_VERSION);
        cloudEvent.setDatacontenttype(JSON);
        cloudEvent.setData(map);

        subscriberController.consumeCustomerCrudEvent(cloudEvent);
    }
}
