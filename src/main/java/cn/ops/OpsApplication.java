package cn.ops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * Created by chenjq on 02/05/2018.
 */

@SpringBootApplication
@EnableWebFlux
@EnableAutoConfiguration
public class OpsApplication {
  public static void main(String[] args) {
    SpringApplication.run(OpsApplication.class, args);
  }
}
