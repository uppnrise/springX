package dev.bnacar.springx.testing.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bnacar.springx.testing.annotation.JsonTest;
import dev.bnacar.springx.testing.assertion.RestAssertions;
import dev.bnacar.springx.testing.mock.MockResponses;
import dev.bnacar.springx.testing.runner.JsonTestExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TestingIntegrationTest.TestConfig.class})
@AutoConfigureMockMvc
public class TestingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRestAssertions() throws Exception {
        // Perform request
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/test")
                        .contentType(MediaType.APPLICATION_JSON));

        // Use RestAssertions to verify response
        RestAssertions.assertAll(resultActions,
                RestAssertions.isOk(),
                RestAssertions.hasJsonField("$.success", true),
                RestAssertions.hasJsonField("$.data"),
                RestAssertions.hasJsonArrayOfSize("$.items", 3)
        );
    }

    @Test
    public void testMockResponses() {
        // Test OK response
        ResponseEntity<String> okResponse = MockResponses.mockOkResponse("Test data");
        assertEquals(HttpStatus.OK, okResponse.getStatusCode());
        assertEquals("Test data", okResponse.getBody());

        // Test Created response
        ResponseEntity<TestDto> createdResponse = MockResponses.mockCreatedResponse(new TestDto(1L, "Test"));
        assertEquals(HttpStatus.CREATED, createdResponse.getStatusCode());
        assertEquals(1L, createdResponse.getBody().getId());
        assertEquals("Test", createdResponse.getBody().getName());

        // Test No Content response
        ResponseEntity<Void> noContentResponse = MockResponses.mockNoContentResponse();
        assertEquals(HttpStatus.NO_CONTENT, noContentResponse.getStatusCode());
        assertNull(noContentResponse.getBody());

        // Test Optional
        Optional<String> emptyOptional = MockResponses.mockEmptyOptional();
        assertTrue(emptyOptional.isEmpty());

        Optional<String> presentOptional = MockResponses.mockOptional("Test");
        assertTrue(presentOptional.isPresent());
        assertEquals("Test", presentOptional.get());

        // Test List
        List<String> emptyList = MockResponses.mockEmptyList();
        assertTrue(emptyList.isEmpty());

        List<String> list = MockResponses.mockList("Item1", "Item2", "Item3");
        assertEquals(3, list.size());
        assertEquals("Item1", list.get(0));
        assertEquals("Item2", list.get(1));
        assertEquals("Item3", list.get(2));

        // Test Exception
        RuntimeException exception = MockResponses.mockException(RuntimeException.class, "Test exception");
        assertEquals("Test exception", exception.getMessage());
    }

    @ExtendWith(JsonTestExtension.class)
    @TestTemplate
    @JsonTest(TestDto.class)
    public void testJsonTest(TestDto testDto, ObjectMapper objectMapper) throws Exception {
        // Verify test object was created
        assertNotNull(testDto);
        assertNotNull(testDto.getId());
        assertNotNull(testDto.getName());

        // Test serialization/deserialization
        String json = objectMapper.writeValueAsString(testDto);
        TestDto deserializedDto = objectMapper.readValue(json, TestDto.class);

        assertEquals(testDto.getId(), deserializedDto.getId());
        assertEquals(testDto.getName(), deserializedDto.getName());
    }

    @Configuration
    @EnableAutoConfiguration
    static class TestConfig {

        @Bean
        public TestController testController() {
            return new TestController();
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @RestController
    static class TestController {

        @GetMapping("/api/test")
        public TestResponse test() {
            return new TestResponse(true, "Test data", Arrays.asList("Item1", "Item2", "Item3"));
        }
    }

    static class TestResponse {
        private boolean success;
        private String data;
        private List<String> items;

        public TestResponse(boolean success, String data, List<String> items) {
            this.success = success;
            this.data = data;
            this.items = items;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getData() {
            return data;
        }

        public List<String> getItems() {
            return items;
        }
    }

    static class TestDto {
        private Long id;
        private String name;

        public TestDto() {
        }

        public TestDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
