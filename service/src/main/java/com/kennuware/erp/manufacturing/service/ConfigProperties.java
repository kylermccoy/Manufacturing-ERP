package com.kennuware.erp.manufacturing.service;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "servers")
public class ConfigProperties {

  @NotBlank
  private String accounting;
  @NotBlank
  private String inventory;
  @NotBlank
  private String sales;

}
