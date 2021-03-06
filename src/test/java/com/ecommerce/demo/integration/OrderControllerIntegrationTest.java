package com.ecommerce.demo.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecommerce.demo.DemoApplication;
import com.ecommerce.demo.entity.Order;
import com.ecommerce.demo.entity.User;
import com.ecommerce.demo.model.AddItemRequest;
import com.ecommerce.demo.repository.CartRepository;
import com.ecommerce.demo.repository.OrderRepository;
import com.ecommerce.demo.repository.ProductRepository;
import com.ecommerce.demo.repository.UserRepository;
import com.ecommerce.demo.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = DemoApplication.class)
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartService cartService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = userRepository.findByUsername("esoner.sezgin@gmail.com").orElseThrow(AssertionError::new);
    }

    @Test
    public void testPlaceOrderAndGet() throws Exception {
        AddItemRequest addItemRequest = new AddItemRequest();
        addItemRequest.setQuantity(5);
        addItemRequest.setToppingIds(List.of(5L, 6L));
        addItemRequest.setDrinkId(1L);
        cartService.add(user, addItemRequest);

        mvc.perform(post("/api/order")
                .with(user(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult mvcResult = mvc.perform(get("/api/order")
                .with(user(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        @SuppressWarnings("unchecked")
        List<Order> result = (List<Order>) objectMapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
        assertThat(result).hasSize(1);
    }
}
