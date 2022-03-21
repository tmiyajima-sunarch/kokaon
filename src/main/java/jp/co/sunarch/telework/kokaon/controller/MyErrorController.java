package jp.co.sunarch.telework.kokaon.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * @author takeshi
 */
@Controller
public class MyErrorController implements ErrorController {
  @RequestMapping(path = "/error", produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView handleError(HttpServletRequest req, ModelAndView mav) {
    var statusCode = this.determineStatusCode(req);
    mav.setStatus(statusCode);
    mav.setViewName("error");
    mav.addObject("message",
        statusCode == HttpStatus.NOT_FOUND ? "ページが見つかりません" : "システムエラーが発生しました");
    return mav;
  }

  private HttpStatus determineStatusCode(HttpServletRequest req) {
    var statusCode = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (statusCode != null & statusCode.toString().equals("404")) {
      return HttpStatus.NOT_FOUND;
    } else {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
  }
}
