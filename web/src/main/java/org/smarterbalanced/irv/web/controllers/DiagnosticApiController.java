package org.smarterbalanced.irv.web.controllers;

import static org.smarterbalanced.irv.core.diagnostic.api.DiagnosticManager.diagnosticStatuses;
import static org.smarterbalanced.irv.core.diagnostic.api.DiagnosticManager.localDiagnosticStatus;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The type Diagnostic api controller.
 */
@Controller
public class DiagnosticApiController {

  /**
   * Diagnostics response entity.
   *
   * @param level   Diagnostic level
   * @param request http request
   * @return Diagnostic results XML
   */
  @ResponseBody
  @RequestMapping(value = "/statusLocal",
          method = RequestMethod.GET,
          produces = "text/xml; charset=utf-8")
  public ResponseEntity diagnostics(
          @RequestParam(value = "level", required = false, defaultValue = "0") Integer level,
          HttpServletRequest request) {
    String response;
    try {
      StringBuffer url = request.getRequestURL();
      String uri = request.getRequestURI();
      String contextPath = request.getContextPath();
      String baseUrl = url.substring(0, url.length() - uri.length() + contextPath.length());
      response = localDiagnosticStatus(level, baseUrl);
    } catch (Exception e) {
      return new ResponseEntity<>("500 internal Server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  /**
   * Diagnostic statuses for all instances of the Item Viewer Service in an AWS ECS cluster.
   *
   * @param level   Diagnostic level
   * @param request http request
   * @return Diagnostic results XML
   */
  @ResponseBody
  @RequestMapping(value = "/status",
          method = RequestMethod.GET,
          produces = "text/xml; charset=utf-8")
  public ResponseEntity globalDiagnostics(
          @RequestParam(value = "level", required = false, defaultValue = "0") Integer level,
          HttpServletRequest request) {
    String response = null;
    if (level == null) {
      level = 0;
    }
    try {
      response = diagnosticStatuses(level);
    } catch (Exception e) {
      return new ResponseEntity<>("500 internal Server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}
