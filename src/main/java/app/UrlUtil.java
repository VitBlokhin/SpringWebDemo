package app;

import app.controller.ContactsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class UrlUtil {
    private static final Logger logger = LoggerFactory.getLogger(UrlUtil.class);

    public static String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc= WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
            logger.error(uee.getMessage());
        }
        return pathSegment;
    }
} // class UrlUtil
