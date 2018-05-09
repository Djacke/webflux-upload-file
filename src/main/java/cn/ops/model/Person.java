package cn.ops.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by chenjq on 05/05/2018.
 */
public class Person {
  private String name;

  @JsonCreator
  public Person(@JsonProperty("name") String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
