package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

@WebServlet(urlPatterns = {"/pot", "/pot/*"})
public class SampleServlet extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private SampleService sampleService;

    @Override
    public void init() throws ServletException {
        sampleService = new SampleService(resolveDataFilePath());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepareJsonResponse(response);

        Optional<Integer> id = readId(request);
        if (id.isPresent()) {
            Optional<Sample> sample = sampleService.getById(id.get());
            if (sample.isEmpty()) {
                writeError(response, HttpServletResponse.SC_NOT_FOUND, "Sample was not found.");
                return;
            }
            writeJson(response, HttpServletResponse.SC_OK, sample.get());
            return;
        }

        writeJson(response, HttpServletResponse.SC_OK, sampleService.getAll());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepareJsonResponse(response);

        Optional<Sample> sample = readSample(request, response);
        if (sample.isEmpty()) {
            return;
        }

        Sample createdSample = sampleService.add(sample.get());
        writeJson(response, HttpServletResponse.SC_CREATED, createdSample);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepareJsonResponse(response);

        Optional<Sample> sample = readSample(request, response);
        if (sample.isEmpty()) {
            return;
        }

        Optional<Integer> id = readId(request);
        if (id.isEmpty() && sample.get().getId() > 0) {
            id = Optional.of(sample.get().getId());
        }

        if (id.isEmpty()) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Sample id is required.");
            return;
        }

        Optional<Sample> updatedSample = sampleService.update(id.get(), sample.get());
        if (updatedSample.isEmpty()) {
            writeError(response, HttpServletResponse.SC_NOT_FOUND, "Sample was not found.");
            return;
        }

        writeJson(response, HttpServletResponse.SC_OK, updatedSample.get());
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        prepareJsonResponse(response);

        Optional<Integer> id = readId(request);
        if (id.isEmpty()) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Sample id is required.");
            return;
        }

        boolean removed = sampleService.delete(id.get());
        if (!removed) {
            writeError(response, HttpServletResponse.SC_NOT_FOUND, "Sample was not found.");
            return;
        }

        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private Path resolveDataFilePath() {
        String realPath = getServletContext().getRealPath("/WEB-INF/sample_data.json");
        if (realPath != null) {
            return Paths.get(realPath);
        }

        String basePath = System.getProperty("catalina.base", System.getProperty("user.dir"));
        return Paths.get(basePath, "sample_data.json");
    }

    private Optional<Sample> readSample(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Sample sample = GSON.fromJson(request.getReader(), Sample.class);
            if (sample == null) {
                writeError(response, HttpServletResponse.SC_BAD_REQUEST, "JSON body is required.");
                return Optional.empty();
            }
            return Optional.of(sample);
        } catch (JsonSyntaxException exception) {
            writeError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON body.");
            return Optional.empty();
        }
    }

    private Optional<Integer> readId(HttpServletRequest request) {
        String rawId = request.getParameter("id");

        if ((rawId == null || rawId.isBlank()) && request.getPathInfo() != null) {
            rawId = request.getPathInfo().replaceFirst("^/", "");
        }

        if (rawId == null || rawId.isBlank()) {
            return Optional.empty();
        }

        try {
            return Optional.of(Integer.parseInt(rawId));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    private void prepareJsonResponse(HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        setCorsHeaders(response);
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "3600");
    }

    private void writeJson(HttpServletResponse response, int status, Object body) throws IOException {
        response.setStatus(status);
        response.getWriter().write(GSON.toJson(body));
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        writeJson(response, status, Map.of("error", message));
    }
}
