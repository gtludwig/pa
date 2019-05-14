package ie.gtludwig.pa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Principal principal) {

        if (logger.isDebugEnabled()) {
            logger.debug("getWelcome is executed");

        }
        return principal == null ? "homeNotSignedIn" : "welcome";
    }
}