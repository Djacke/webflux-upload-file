package cn.ops;

import cn.ops.model.Person;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.codec.Charsets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;

/**
 * Created by chenjq on 03/05/2018.
 */


@RunWith(SpringRunner.class)
@ActiveProfiles("integTest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FileUploadControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @Test
  public void testUpload() throws IOException, URISyntaxException {

    webTestClient.post().uri("/api/upload")
        .syncBody(generateBody2())
        .exchange().expectStatus().isOk()
        .expectBody().consumeWith(result -> {
      System.out.println(new String(result.getResponseBody(), Charsets.UTF_8));
    });
  }

  @Test
  public void requestPart() {
    webTestClient
        .post()
        .uri("/api/requestPart")
        .syncBody(generateBody2())
        .exchange().expectStatus().isOk();
  }

  private MultiValueMap<String, HttpEntity<?>> generateBody2() {
    MultipartBodyBuilder builder = new MultipartBodyBuilder();
    builder.part("fieldPart", "fieldValue");
    builder.part("fileParts", new ClassPathResource("/foo.txt", MultipartHttpMessageReader.class));
    builder.part("fileParts", new ClassPathResource("/tongji.png", getClass()));
    builder.part("jsonPart", new Person("Jason"));
    builder.part("file", new ClassPathResource("/testfile.xlsx", MultipartHttpMessageReader.class));
    return builder.build();
  }

}
