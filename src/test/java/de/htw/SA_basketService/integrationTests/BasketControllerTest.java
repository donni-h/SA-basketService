package de.htw.SA_basketService.integrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htw.SA_basketService.core.domain.model.Basket;
import de.htw.SA_basketService.core.domain.model.Item;
import de.htw.SA_basketService.port.producer.BasketProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.UUID;

import static junit.framework.TestCase.assertFalse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@TestPropertySource(properties = {
        "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://example.com/oauth2/default/v1/keys",
        "KEYCLOAK_CERTS=your-test-keycloak-certs-value"
})
public class BasketControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @MockBean
    private BasketProducer basketProducer;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TEST_USERNAME = "testuser";

    @BeforeEach
    void setUp() {
        // Mock the basketProducer method to do nothing
        doNothing().when(basketProducer).changeAmountOfPlant(any(UUID.class), any(Integer.class));
    }

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void createBasket() throws Exception {
        mockMvc.perform(post("/basket")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(TEST_USERNAME));
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void addItemToBasket() throws Exception {
        // First create a basket
        mockMvc.perform(post("/basket"));

        // Create an item to add
        UUID plantId = UUID.fromString("c9786544-c29d-4989-bd05-47564fb84a9c");
        Item item = new Item(plantId, "PRODUKT 9000", BigDecimal.valueOf(200.00), "http://example.com/image9000.jpg");

        // Add the item to the basket
        mockMvc.perform(put("/basket/additem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].plantId").value(plantId.toString()));

        verify(basketProducer).changeAmountOfPlant(eq(plantId), eq(-1));
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void getBasketByUsername() throws Exception {
        // First create a basket
        mockMvc.perform(post("/basket"));

        // Then get the basket
        mockMvc.perform(get("/basket"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(TEST_USERNAME));
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void getAllBaskets() throws Exception {
        // Create a basket for the test user
        mockMvc.perform(post("/basket"));

        // Get all baskets
        mockMvc.perform(get("/baskets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].username").value(TEST_USERNAME));
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void removeAllItemsFromBasket() throws Exception {
        // First create a basket and add some items
        mockMvc.perform(post("/basket"));
        Item item1 = new Item(UUID.randomUUID(), "Item 1", BigDecimal.valueOf(10.00), "http://example.com/image1.jpg");
        Item item2 = new Item(UUID.randomUUID(), "Item 2", BigDecimal.valueOf(20.00), "http://example.com/image2.jpg");
        mockMvc.perform(put("/basket/additem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item1)));
        mockMvc.perform(put("/basket/additem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(item2)));

        // Remove all items
        mockMvc.perform(put("/basket/removeallitems"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void deleteBasket() throws Exception {
        // First create a basket
        mockMvc.perform(post("/basket"));

        // Then delete the basket
        mockMvc.perform(delete("/basket"))
                .andExpect(status().isOk());

        // Verify that the basket is deleted by trying to get it
        mockMvc.perform(get("/basket"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void deleteAllBaskets() throws Exception {
        // First create a basket
        mockMvc.perform(post("/basket"));

        // Delete all baskets
        mockMvc.perform(delete("/baskets"))
                .andExpect(status().isOk());

        // Verify that all baskets are deleted
        mockMvc.perform(get("/baskets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}