package ch.hsr.apparch.recipe.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;

@Log4j2
@Tag("API")
@Profile("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class PurchaseListApiTest {


    private static final String LOCATION_HEADER = "Location";
    private static final String HOST_NAME = "localhost";
    private static final String REST_PATH = "purchaseLists";
    private static final String BASE_PATH = '/' + REST_PATH;
    
    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private RequestSpecification spec;
    private RequestSpecification specWithoutBaseURI;

    @BeforeEach
    void setUp() {
        this.spec = new RequestSpecBuilder()
                .setBaseUri("http://" + HOST_NAME + ':' + port + "/api")
                .setAccept(ContentType.JSON)
                .build();

        this.specWithoutBaseURI = new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .build();

        RestAssured.config().logConfig(new LogConfig()
                .defaultStream(LoggingOutputStream.createLoggingPrintStream(LOGGER))
                .and().enablePrettyPrinting(true)
                .and().enableLoggingOfRequestAndResponseIfValidationFails()
        );
    }

    @Test
    void getRoot() {
        LOGGER.traceEntry("getRoot");

        LOGGER.info("GET / with intention to get resource links");
        RestAssured
                .given(spec)
                .when().get("/")
                .then()
                .assertThat().contentType(ContentType.JSON)
                .assertThat().body("_links.purchaseLists.href", containsString("/purchaseLists{?page,size,sort}"));
    }

    @Test
    void ensureLoadedSampleData() {
        LOGGER.traceEntry("emptyPurchaseLists");

        LOGGER.info("GET {} with intention to get empty result", BASE_PATH);
        RestAssured
                .given(spec)
                .when().get(BASE_PATH)
                .then()
                .assertThat().contentType(ContentType.JSON)
                .assertThat().body("_embedded.purchaseLists", not(empty()));

        LOGGER.traceExit();
    }

    @Test
    void savePurchaseList() {
        LOGGER.traceEntry("savePurchaseList");

        final int record_id = 1;
        final String record_name = "Test";
        final LocalDate record_date = LocalDate.of(2100, 12, 11);
        final String record_date_string = record_date.format(DateTimeFormatter.ISO_DATE);
        final String record = "{\"name\": \"" + record_name + "\", \"date\": \"" +
                record_date.format(DateTimeFormatter.ISO_DATE) + "\"}";

        LOGGER.info("ADD {} to id {} to test adding records", record, record_id);
        RestAssured
                .given(spec).contentType(ContentType.JSON).and().body(record)
                .when().post(BASE_PATH)
                .then()
                .assertThat().contentType(ContentType.JSON)
                .assertThat().body("name", is(record_name))
                .assertThat().body("date", is(record_date_string));

        LOGGER.info("DEL {} with id {} to clear server after test", BASE_PATH, record_id);
        RestAssured
                .given(spec)
                .when().delete(BASE_PATH + "/{record_id}", record_id);

        LOGGER.traceExit();
    }

    @Test
    void saveOldPurchaseList() {
        LOGGER.traceEntry("saveOldPurchaseList");

        final int record_id = 1;
        final String record_name = "Test";
        final LocalDate record_date = LocalDate.of(2000, 12, 11);
        final String record_date_string = record_date.format(DateTimeFormatter.ISO_DATE);
        final String record = "{\"name\": \"" + record_name + "\", \"date\": \"" +
                record_date.format(DateTimeFormatter.ISO_DATE) + "\"}";

        LOGGER.info("PUT {} with old date, which will result in fail (500)", record);
        RestAssured
                .given(spec).contentType(ContentType.JSON).and().body(record)
                .when().put(BASE_PATH + "/{record_id}", record_id)
                .then().assertThat().statusCode(is(500));

        LOGGER.traceExit();
    }

    @Test
    void updatePurchaseList() {
        LOGGER.traceEntry("updatePurchaseList");

        final int record_id = 1;
        String record_name = "Test";
        final LocalDate record_date = LocalDate.of(2100, 12, 11);
        final String record_date_string = record_date.format(DateTimeFormatter.ISO_DATE);
        String record = "{\"name\": \"" + record_name + "\", \"date\": \"" +
                record_date.format(DateTimeFormatter.ISO_DATE) + "\"}";

        LOGGER.info("PUT {} std record");
        RestAssured
                .given(spec).contentType(ContentType.JSON).body(record)
                .when().put(BASE_PATH + "/{record_id}", record_id)
                .then()
                .assertThat().contentType(ContentType.JSON)
                .assertThat().body("name", is(record_name))
                .assertThat().body("date", is(record_date_string));

        LOGGER.info("PATCH record to update data");

        record_name = "AnotherTest";
        record = "{\"name\": \"" + record_name + "\", \"date\": \"" +
                record_date.format(DateTimeFormatter.ISO_DATE) + "\"}";

        ResponseOptions<?> result = RestAssured
                .given(spec).contentType(ContentType.JSON).and().body(record)
                .when().put(BASE_PATH + "/{record_id}", record_id);

        String location = result.getHeader(LOCATION_HEADER);

        LOGGER.info("GET updated data and compare");
        RestAssured
                .given(specWithoutBaseURI)
                .when().get(location)
                .then()
                .assertThat().body("name", is(record_name))
                .assertThat().body("date", is(record_date_string));
    }
}
