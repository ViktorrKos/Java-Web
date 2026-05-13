package com.weblab;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

// Крок 6: Змінили URL-шлях на /group
@WebServlet("/group")
public class MyServlet extends HttpServlet {

    // Крок 5: Формуємо відповідь з вашим текстом
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Варіант 3</title></head>");
            out.println("<body>");

            // Ваш індивідуальний текст
            out.println("<h2>Java Web: базова структура застосунку.</h2>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}
