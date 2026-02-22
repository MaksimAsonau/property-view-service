package by.asonau.propertyviewservice.controller;

import by.asonau.propertyviewservice.testutil.TestHotelFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PropertyViewApiIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void postHotels_ShouldReturn201_AndBodyContainsId() throws Exception {
        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestHotelFactory.validHotelJson("Hotel A", "Hilton", "Minsk", "Belarus")))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Hotel A"))
                .andExpect(jsonPath("$.phone").value("+111"))
                .andExpect(jsonPath("$.address").value("1 Main Street, Minsk, 220000, Belarus"));
    }

    @Test
    void getHotels_ShouldReturn200_AndReturnList() throws Exception {
        createHotel("Hotel A", "Hilton", "Minsk", "Belarus");

        mockMvc.perform(get("/hotels"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Hotel A"));
    }

    @Test
    void endToEnd_Create_AddAmenities_GetDetails_ShouldWork() throws Exception {
        long id = createHotel("Hotel A", "Hilton", "Minsk", "Belarus");

        mockMvc.perform(post("/hotels/{id}/amenities", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                [" Free   WiFi ", "Spa", " "]
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.amenities", hasSize(2)));

        mockMvc.perform(get("/hotels/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Hotel A"))
                .andExpect(jsonPath("$.address.city").value("Minsk"))
                .andExpect(jsonPath("$.amenities", hasSize(2)));
    }

    @Test
    void getSearch_ShouldReturn200_AndFilterByCityCaseInsensitive() throws Exception {
        createHotel("Hotel Minsk", "Brand", "Minsk", "Belarus");
        createHotel("Hotel Moscow", "Brand", "Moscow", "Russia");

        mockMvc.perform(get("/search")
                        .param("city", "minsk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Hotel Minsk"));
    }

    @Test
    void getHistogramCity_ShouldReturn200_AndCountsCorrect() throws Exception {
        createHotel("Hotel A", "Hilton", "Minsk", "Belarus");
        createHotel("Hotel B", "Hilton", "Minsk", "Belarus");
        createHotel("Hotel C", "Hilton", "Moscow", "Russia");

        mockMvc.perform(get("/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Minsk").value(2))
                .andExpect(jsonPath("$.Moscow").value(1));
    }

    @Test
    void getHotelById_ShouldReturn404_WhenNotFound() throws Exception {
        mockMvc.perform(get("/hotels/12345"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message", containsString("Hotel with id=")));
    }

    @Test
    void getHistogram_ShouldReturn400_WhenParamUnknown() throws Exception {
        mockMvc.perform(get("/histogram/unknown"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(
                        "Invalid histogram param. Allowed: brand, city, country, amenities"
                ))
                .andExpect(jsonPath("$.errors", hasSize(0)));
    }

    @Test
    void postHotels_ShouldReturn400_WhenValidationFails() throws Exception {
        mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestHotelFactory.invalidHotelJson_NameBlank()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0]", is("name: Name is required")));
    }

    private long createHotel(String name, String brand, String city, String country) throws Exception {
        String body = mockMvc.perform(post("/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestHotelFactory.validHotelJson(name, brand, city, country)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(body);
        return json.get("id").asLong();
    }
}