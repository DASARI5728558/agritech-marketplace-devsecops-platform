package com.example.agrimarket.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProduceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createFarmerProduceOrder_endToEnd() throws Exception {
        String farmerPayload = """
                {"name":"Asha Devi","email":"asha@example.com","phone":"9876543210",
                 "farmName":"Green Valley Farm","location":"Nashik"}
                """;

        String farmerResponse = mockMvc.perform(post("/api/v1/farmers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(farmerPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Asha Devi")))
                .andReturn().getResponse().getContentAsString();

        Long farmerId = objectMapper.readTree(farmerResponse).get("id").asLong();

        String producePayload = """
                {"name":"Vermicompost","category":"potato","unit":"10 kg bag",
                 "pricePerUnit":179.00,"quantityAvailable":50,"organic":true,
                 "farmerId":%d}
                """.formatted(farmerId);

        String produceResponse = mockMvc.perform(post("/api/v1/produce")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(producePayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Vermicompost")))
                .andExpect(jsonPath("$.farmerId", is(farmerId.intValue())))
                .andReturn().getResponse().getContentAsString();

        Long produceId = objectMapper.readTree(produceResponse).get("id").asLong();

        mockMvc.perform(get("/api/v1/produce/" + produceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityAvailable", is(50)));

        String buyerPayload = """
                {"name":"Ravi Kumar","email":"ravi@example.com","deliveryAddress":"12 MG Road"}
                """;

        String buyerResponse = mockMvc.perform(post("/api/v1/buyers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buyerPayload))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long buyerId = objectMapper.readTree(buyerResponse).get("id").asLong();

        String orderPayload = """
                {"buyerId":%d,"produceId":%d,"quantity":5}
                """.formatted(buyerId, produceId);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(jsonPath("$.totalPrice", is(895.0)));

        mockMvc.perform(get("/api/v1/produce/" + produceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityAvailable", is(45)));
    }

    @Test
    void findById_returns404WhenMissing() throws Exception {
        mockMvc.perform(get("/api/v1/produce/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    void createProduce_validationFailsWithoutRequiredFields() throws Exception {
        mockMvc.perform(post("/api/v1/produce")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
