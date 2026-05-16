package org.example;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/cameras")
public class CameraServlet extends HttpServlet {
    private static final Gson GSON = new Gson();

    private final List<Camera> cameras = List.of(
            new Camera(
                    1,
                    "Sony Alpha 7 IV",
                    "/images/cameras/sony-alpha-7-iv.jpg",
                    "Full-frame mirrorless camera for photo and video shooting.",
                    "Sony",
                    "Alpha 7 IV",
                    33,
                    "Full-frame CMOS",
                    true,
                    2499.99
            ),
            new Camera(
                    2,
                    "Canon EOS R5",
                    "/images/cameras/canon-eos-r5.jpg",
                    "Professional mirrorless camera with high-resolution photo mode.",
                    "Canon",
                    "EOS R5",
                    45,
                    "Full-frame CMOS",
                    true,
                    3899.00
            ),
            new Camera(
                    3,
                    "Nikon Z9",
                    "/images/cameras/nikon-z9.jpg",
                    "Flagship mirrorless camera for sports and studio work.",
                    "Nikon",
                    "Z9",
                    45,
                    "Full-frame stacked CMOS",
                    true,
                    5496.95
            )
    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");

        response.getWriter().write(GSON.toJson(cameras));
    }
}
