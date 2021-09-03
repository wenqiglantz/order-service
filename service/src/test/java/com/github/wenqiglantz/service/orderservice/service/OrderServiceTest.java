package com.github.wenqiglantz.service.orderservice.service;

import com.github.wenqiglantz.service.orderservice.data.event.CustomerWasCreated;
import com.github.wenqiglantz.service.orderservice.persistence.entity.Customer;
import com.github.wenqiglantz.service.orderservice.persistence.repository.CustomerRepository;
import com.github.wenqiglantz.service.orderservice.service.impl.OrderServiceImpl;
import com.github.wenqiglantz.service.orderservice.data.event.CustomerWasDeleted;
import com.github.wenqiglantz.service.orderservice.data.event.CustomerWasUpdated;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Optional;

public class OrderServiceTest {
    private static final String CUSTOMER_ID = "ABCDEFG12345678910HIJKLMNOP12345";
    private static final String FIRST_NAME = "FIRST_NAME";
    private static final String LAST_NAME = "LAST_NAME";
    private static final String LAST_NAME_NEW = "NEW";

    private static final Customer CUSTOMER = Customer.builder()
            .customerId(CUSTOMER_ID)
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .build();

    private static final CustomerWasCreated CUSTOMER_WAS_CREATED = CustomerWasCreated.builder()
            .customerId(CUSTOMER_ID)
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .build();

    private static final CustomerWasUpdated CUSTOMER_WAS_UPDATED = CustomerWasUpdated.builder()
            .customerId(CUSTOMER_ID)
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME_NEW)
            .build();

    private static final CustomerWasDeleted CUSTOMER_WAS_DELETED = CustomerWasDeleted.builder()
            .customerId(CUSTOMER_ID)
            .build();

    private CustomerRepository customerRepository;

    @RegisterExtension
    Mockery context = new JUnit5Mockery();

    @BeforeEach
    public void setUp() {
        customerRepository = context.mock(CustomerRepository.class);
    }

    @Test
    public void consumeCustomerWasCreated() {
        context.checking(new Expectations() {
            {
                oneOf(customerRepository).findByCustomerId(CUSTOMER_ID);
                will(returnValue(Optional.empty()));
                oneOf(customerRepository).save(with(any(Customer.class)));
            }
        });

        OrderService orderService = new OrderServiceImpl(customerRepository);
        orderService.consumeCustomerWasCreated(CUSTOMER_WAS_CREATED);
    }

    @Test
    public void consumeCustomerWasCreatedAlreadyExists() {
        context.checking(new Expectations() {
            {
                oneOf(customerRepository).findByCustomerId(CUSTOMER_ID);
                will(returnValue(Optional.of(CUSTOMER)));
            }
        });

        OrderService orderService = new OrderServiceImpl(customerRepository);
        orderService.consumeCustomerWasCreated(CUSTOMER_WAS_CREATED);
    }

    @Test
    public void consumeCustomerWasUpdated() {
        context.checking(new Expectations() {
            {
                oneOf(customerRepository).findByCustomerId(CUSTOMER_ID);
                will(returnValue(Optional.of(CUSTOMER)));
                oneOf(customerRepository).save(with(any(Customer.class)));
            }
        });

        OrderService orderService = new OrderServiceImpl(customerRepository);
        orderService.consumeCustomerWasUpdated(CUSTOMER_WAS_UPDATED);
    }

    @Test
    public void consumeCustomerWasUpdatedReturnsEmptyResults() {
        context.checking(new Expectations() {
            {
                oneOf(customerRepository).findByCustomerId("test");
                will(returnValue(Optional.empty()));
            }
        });

        OrderService orderService = new OrderServiceImpl(customerRepository);
        CustomerWasUpdated customerWasUpdated = CustomerWasUpdated.builder()
                .customerId("test")
                .firstName("updated").build();
        orderService.consumeCustomerWasUpdated(customerWasUpdated);
    }

    @Test
    public void consumeCustomerWasDeleted() {
        context.checking(new Expectations() {
            {
                oneOf(customerRepository).findByCustomerId(CUSTOMER_ID);
                will(returnValue(Optional.of(CUSTOMER)));
                oneOf(customerRepository).delete(with(any(Customer.class)));
            }
        });

        OrderService orderService = new OrderServiceImpl(customerRepository);
        orderService.consumeCustomerWasDeleted(CUSTOMER_WAS_DELETED);
    }

    @Test
    public void consumeCustomerWasDeletedReturnsEmptyResults() {
        context.checking(new Expectations() {
            {
                oneOf(customerRepository).findByCustomerId("test");
                will(returnValue(Optional.empty()));
            }
        });

        OrderService orderService = new OrderServiceImpl(customerRepository);
        CustomerWasDeleted customerWasDeleted = CustomerWasDeleted.builder().customerId("test").build();
        orderService.consumeCustomerWasDeleted(customerWasDeleted);
    }
}
