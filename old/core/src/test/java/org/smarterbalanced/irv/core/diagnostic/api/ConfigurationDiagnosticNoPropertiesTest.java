package org.smarterbalanced.irv.core.diagnostic.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConfigurationDiagnosticNoPropertiesTest {

  @Test
  public void noConfig() {
    ConfigurationDiagnostic configurationDiagnostic = new ConfigurationDiagnostic();
    configurationDiagnostic.setContentPath("");
    configurationDiagnostic.runDiagnostics();
    assertTrue(configurationDiagnostic.getErrors().size() > 0);
    assertEquals((Integer)0, configurationDiagnostic.getStatusRating());
    assertEquals("failed", configurationDiagnostic.getStatusText());
  }

}
