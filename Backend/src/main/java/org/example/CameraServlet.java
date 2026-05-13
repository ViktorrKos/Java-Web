package org.example;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

// Цей шлях буде використовуватися для отримання даних
@WebServlet("/api/cameras")
public class CameraServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Налаштовуємо відповідь, щоб вона повертала JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Вирішення проблеми з CORS для локального тестування (якщо Vue на іншому порту)
        response.setHeader("Access-Control-Allow-Origin", "*");

        // Створюємо список камер
        List<Camera> cameras = new ArrayList<>();
        cameras.add(new Camera(1, "Alpha 7 IV", "Sony", 33, 2499.99));
        cameras.add(new Camera(2, "EOS R5", "Canon", 45, 3899.00));
        cameras.add(new Camera(3, "Z9", "Nikon", 45, 5496.95));

        // Перетворюємо список у JSON за допомогою Gson
        Gson gson = new Gson();
        String json = gson.toJson(cameras);

        // Відправляємо JSON клієнту
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}
