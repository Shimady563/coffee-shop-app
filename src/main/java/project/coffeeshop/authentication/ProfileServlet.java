package project.coffeeshop.authentication;

import com.lambdaworks.crypto.SCryptUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.coffeeshop.commons.CoffeeShopServlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static project.coffeeshop.commons.ServletUtil.findCookieByName;
import static project.coffeeshop.commons.ServletUtil.isValidSession;

@WebServlet(name = "ProfileServlet", value = "/profile")
public class ProfileServlet extends CoffeeShopServlet {
    private SessionDao sessionDao;
    private UserDao userDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        sessionDao = new SessionDao();
        userDao = new UserDao();
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> cookieOptional = findCookieByName(cookies, "sessionId");

        if (cookieOptional.isPresent()) {
            UUID sessionId = UUID.fromString(cookieOptional.get().getValue());

            Optional<Session> sessionOptional = sessionDao.findById(sessionId);

            if (sessionOptional.isPresent() && isValidSession(sessionOptional.get(), LocalDateTime.now())) {
                Optional<User> userOptional = userDao.findById(sessionOptional.get().getUserId());
                userOptional.ifPresent(user -> webContext.setVariable("username", user.getUsername()));
            }
        }

        templateEngine.process("profile", webContext, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String newUsername = request.getParameter("newLogin");
        Optional<String> newPassword = Optional.ofNullable(request.getParameter("newPassword"));

        Cookie[] cookies = request.getCookies();
        Optional<Cookie> cookieOptional = findCookieByName(cookies, "sessionId");

        if (cookieOptional.isPresent()) {
            UUID sessionId = UUID.fromString(cookieOptional.get().getValue());

            Optional<Session> sessionOptional = sessionDao.findById(sessionId);

            if (sessionOptional.isPresent() && isValidSession(sessionOptional.get(), LocalDateTime.now())) {
                Optional<User> userOptional = userDao.findById(sessionOptional.get().getUserId());

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    boolean updated = false;

                    if (!user.getUsername().equals(newUsername)) {
                        user.setUsername(newUsername);
                        updated = true;
                    }

                    if (newPassword.isPresent() && !SCryptUtil.check(newPassword.get(), user.getPassword())) {
                        user.setPassword(SCryptUtil.scrypt(newPassword.get(), 16, 16, 16));
                        updated = true;
                    }

                    if (updated) {
                        userDao.update(user);
                        response.sendRedirect(request.getContextPath() + "/profile");
                    }
                }
            }
        }
    }
}