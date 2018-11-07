package ch.hsr.apparch.purchaselist.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@Tag("API")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class PurchaseListApiTest {


    private static final String LOCATION_HEADER = "Location";
    private static final String HOST_NAME = "localhost";
    private static final String REST_PATH = "purchaseLists";
    private static final String BASE_PATH = '/' + REST_PATH;
    private final Logger logger = LogManager.getLogger("Testing");
    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;


    private RequestSpecification spec;
    private RequestSpecification specWithoutBaseURI;


    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.spec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .setBaseUri("http://" + HOST_NAME + ':' + port)
                .setAccept(ContentType.JSON)
                .build();

        this.specWithoutBaseURI = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .setAccept(ContentType.JSON)
                .build();

        RestAssured.config().logConfig(new LogConfig()
                .defaultStream(LoggingOutputStream.createLoggingPrintStream(logger))
                .and().enablePrettyPrinting(true)
                .and().enableLoggingOfRequestAndResponseIfValidationFails()
        );
    }

    @Test
    void getRoot() {
        logger.traceEntry("getRoot");

        logger.info("GET / with intention to get resource links");
        RestAssured
                .given(spec).and().filter(document("BasePath"))
                .when().get("/")
                .then()
                .assertThat().contentType(ContentType.JSON)
                .assertThat().body("_links.purchaseLists.href", containsString("/purchaseLists{?page,size,sort}"));
    }

    @Test
    void emptyPurchaseLists() {
        logger.traceEntry("emptyPurchaseLists");

        logger.info("GET {} with intention to get empty result", BASE_PATH);
        RestAssured
                .given(spec).and().filter(document("BasePath" + REST_PATH + "GET"))
                .when().get(BASE_PATH)
                .then()
                .assertThat().contentType(ContentType.JSON)
                .assertThat().body("_embedded.purchaseLists", empty());

        logger.traceExit();
    }

    @Test
    void savePurchaseList() {
        logger.traceEntry("savePurchaseList");

        final int record_id = 1;
        final String record_name = "Test";
        final LocalDate record_date = LocalDate.of(2100, 12, 11);
        final String record_date_string = record_date.format(DateTimeFormatter.ISO_DATE);
        final String record = "{\"name\": \"" + record_name + "\", \"date\": \"" +
                record_date.format(DateTimeFormatter.ISO_DATE) + "\"}";

        logger.info("ADD {} to id {} to test adding records", record, record_id);
        RestAssured
                .given(spec).contentType(ContentType.JSON).and().body(record).and().filter(document(REST_PATH + "PUT"))
                .when().put(BASE_PATH + "/{record_id}", record_id)
                .then()
                .assertThat().contentType(ContentType.JSON)
                .assertThat().body("name", is(record_name))
                .assertThat().body("date", is(record_date_string));

        // Check existance of new element
        logger.info("GET {} to get saved record and assert data", BASE_PATH);
        RestAssured
                .given(spec)
                .when().get(BASE_PATH)
                .then()
                .assertThat().contentType(ContentType.JSON)
                .assertThat().body("_embedded.purchaseLists", not(empty()))
                .assertThat().body("_embedded.purchaseLists[0].name", is(record_name))
                .assertThat().body("_embedded.purchaseLists[0].date", is(record_date_string));

        logger.info("DEL {} with id {} to clear server after test", BASE_PATH, record_id);
        RestAssured
                .given(spec).and().filter(document("BasePath" + REST_PATH + "DEL"))
                .when().delete(BASE_PATH + "/{record_id}", record_id);

        logger.traceExit();
    }

    @Test
    void saveOldPurchaseList() {
        logger.traceEntry("saveOldPurchaseList");

        final int record_id = 1;
        final String record_name = "Test";
        final LocalDate record_date = LocalDate.of(2000, 12, 11);
        final String record_date_string = record_date.format(DateTimeFormatter.ISO_DATE);
        final String record = "{\"name\": \"" + record_name + "\", \"date\": \"" +
                record_date.format(DateTimeFormatter.ISO_DATE) + "\"}";

        logger.info("PUT {} with old date, which will result in fail (500)", record);
        RestAssured
                .given(spec).contentType(ContentType.JSON).and().body(record)
                .when().put(BASE_PATH + "/{record_id}", record_id)
                .then().assertThat().statusCode(is(500));

        logger.traceExit();
    }

    @Test
    void updatePurchaseList() {
        logger.traceEntry("updatePurchaseList");

        final int record_id = 1;
        String record_name = "Test";
        final LocalDate record_date = LocalDate.of(2100, 12, 11);
        final String record_date_string = record_date.format(DateTimeFormatter.ISO_DATE);
        String record = "{\"name\": \"" + record_name + "\", \"date\": \"" +
                record_date.format(DateTimeFormatter.ISO_DATE) + "\"}";

        logger.info("PUT {} std record");
        RestAssured
                .given(spec).contentType(ContentType.JSON).and().body(record).and().filter(document(REST_PATH + "PUT"))
                .when().put(BASE_PATH + "/{record_id}", record_id)
                .then()
                .assertThat().contentType(ContentType.JSON)
                .assertThat().body("name", is(record_name))
                .assertThat().body("date", is(record_date_string));

        logger.info("PATCH record to update data");

        record_name = "AnotherTest";
        record = "{\"name\": \"" + record_name + "\", \"date\": \"" +
                record_date.format(DateTimeFormatter.ISO_DATE) + "\"}";

        ResponseOptions<?> result = RestAssured
                .given(spec).contentType(ContentType.JSON).and().body(record)
                .when().put(BASE_PATH + "/{record_id}", record_id);

        String location = result.getHeader(LOCATION_HEADER);

        logger.info("GET updated data and compare");
        RestAssured
                .given(specWithoutBaseURI)
                .when().get(location)
                .then()
                .assertThat().body("name", is(record_name))
                .assertThat().body("date", is(record_date_string));
    }
}