package de.fhg.ids.app.ldcontainer;

import com.jayway.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.StartsWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;

import static com.jayway.restassured.RestAssured.*;
import static org.apache.commons.lang3.StringUtils.containsOnly;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Matchers.contains;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ControllerTest {

    @Value("${local.server.port}")
    int port;

    @BeforeClass
    public static void prepareDatasets() throws IOException {
        new DatasetInstaller().installTestResources();
    }

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void listDatasetDescriptions() {
        when().
                get("/").
        then().
                statusCode(200).
                body("id.size()", equalTo(1));
    }

    @Test
    public void getWholeDatasetContent() {
        Assert.assertEquals(6306600, when().get("/dataset/{id}", 0).asByteArray().length);
    }

    @Test
    public void getWholeDatasetMetadata() {
        Assert.assertEquals(HttpStatus.SC_OK, when().get("/dataset/{id}/meta", 0).statusCode());
        Assert.assertFalse(when().get("/dataset/{id}/meta", 0).body().asString().isEmpty());
    }

}
