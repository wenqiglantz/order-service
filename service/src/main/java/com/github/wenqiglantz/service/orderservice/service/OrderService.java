package com.github.wenqiglantz.service.orderservice.service;

import com.github.wenqiglantz.service.orderservice.data.event.CustomerWasCreated;
import com.github.wenqiglantz.service.orderservice.data.event.CustomerWasDeleted;
import com.github.wenqiglantz.service.orderservice.data.event.CustomerWasUpdated;

public interface OrderService {

    void consumeCustomerWasCreated(CustomerWasCreated customerWasCreated);

    void consumeCustomerWasUpdated(CustomerWasUpdated customerWasUpdated);

    void consumeCustomerWasDeleted(CustomerWasDeleted customerWasDeleted);
}