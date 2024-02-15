package pl.inpost.pricecalculator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.net.URI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.inpost.pricecalculator.request.DiscountRequest;
import pl.inpost.pricecalculator.request.ProductRequest;
import pl.inpost.pricecalculator.response.DiscountResponse;
import pl.inpost.pricecalculator.response.ProductPriceResponse;
import pl.inpost.pricecalculator.response.ProductResponse;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PriceCalculatorIntegrationTest {

    @ServiceConnection
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    static void setUp() {
        POSTGRE_SQL_CONTAINER.start();
    }

    @Test
    void integrationTest() throws Exception {
        //create product
        ProductRequest productRequest = new ProductRequest(new BigDecimal("100.00"), "test product");
        String responseString = mockMvc.perform(post(URI.create("/product"))
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(productRequest)))
            .andExpect(status().is(HttpStatus.CREATED.value()))
            .andReturn().getResponse().getContentAsString();
        var productResponse = mapper.readValue(responseString, ProductResponse.class);

        //verify product price for count = 4
        responseString = mockMvc.perform(get(URI.create(String.format("/product/%s/price?count=4", productResponse.id()))))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andReturn().getResponse().getContentAsString();
        ProductPriceResponse productPriceResponse = mapper.readValue(responseString, ProductPriceResponse.class);
        assertThat(productPriceResponse.value()).isEqualTo(new BigDecimal("100.00"));

        //add discount with minimalCount = 6 and percentage = 20
        DiscountRequest discountRequest = new DiscountRequest(6, 20);
        responseString = mockMvc.perform(post(URI.create(String.format("/product/%s/discount", productResponse.id())))
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(discountRequest)))
            .andExpect(status().is(HttpStatus.CREATED.value()))
            .andReturn().getResponse().getContentAsString();
        var discountId = mapper.readValue(responseString, DiscountResponse.class).id();

        //verify product price for count = 4
        responseString = mockMvc.perform(get(URI.create(String.format("/product/%s/price?count=4", productResponse.id()))))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andReturn().getResponse().getContentAsString();
        productPriceResponse = mapper.readValue(responseString, ProductPriceResponse.class);
        assertThat(productPriceResponse.value()).isEqualTo(new BigDecimal("100.00"));

        //verify product price for count = 6
        responseString = mockMvc.perform(get(URI.create(String.format("/product/%s/price?count=6", productResponse.id()))))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andReturn().getResponse().getContentAsString();
        productPriceResponse = mapper.readValue(responseString, ProductPriceResponse.class);
        assertThat(productPriceResponse.value()).isEqualTo(new BigDecimal("80.00"));

        //update discount with minimalCount = 10 and percentage = 50
        discountRequest = new DiscountRequest(10, 50);
        mockMvc.perform(put(URI.create(String.format("/product/%s/discount/%s", productResponse.id(), discountId)))
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(discountRequest)))
            .andExpect(status().is(HttpStatus.OK.value()));

        //verify product price for count = 6
        responseString = mockMvc.perform(get(URI.create(String.format("/product/%s/price?count=6", productResponse.id()))))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andReturn().getResponse().getContentAsString();
        productPriceResponse = mapper.readValue(responseString, ProductPriceResponse.class);
        assertThat(productPriceResponse.value()).isEqualTo(new BigDecimal("100.00"));

        //verify product price for count = 10
        responseString = mockMvc.perform(get(URI.create(String.format("/product/%s/price?count=10", productResponse.id()))))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andReturn().getResponse().getContentAsString();
        productPriceResponse = mapper.readValue(responseString, ProductPriceResponse.class);
        assertThat(productPriceResponse.value()).isEqualTo(new BigDecimal("50.00"));

        //delete discount
        mockMvc.perform(delete(URI.create(String.format("/product/%s/discount/%s", productResponse.id(), discountId))))
            .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

        //verify product price for count = 10
        responseString = mockMvc.perform(get(URI.create(String.format("/product/%s/price?count=10", productResponse.id()))))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andReturn().getResponse().getContentAsString();
        productPriceResponse = mapper.readValue(responseString, ProductPriceResponse.class);
        assertThat(productPriceResponse.value()).isEqualTo(new BigDecimal("100.00"));
    }
}
