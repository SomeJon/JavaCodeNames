package codenames;

import codenames.constant.attribute.AttributeNames;
import data.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {

    public static User getUser (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(AttributeNames.USER) : null;
        return sessionAttribute != null ? (User)sessionAttribute : null;
    }
}
