package project.coffeeshop.news;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import project.coffeeshop.commons.CoffeeShopServlet;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "NewsServlet", value = "/news")
public class NewsServlet extends CoffeeShopServlet {
    private final NewsDao newsDao = new NewsDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<PieceOfNews> news = newsDao.findAll();

        webContext.setVariable("news", news);
        templateEngine.process("news", webContext, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestText = request.getParameter("requestText");
        List<PieceOfNews> news = newsDao.findByTitle(requestText);

        webContext.setVariable("news", news);
        templateEngine.process("news", webContext, response.getWriter());
    }
}
