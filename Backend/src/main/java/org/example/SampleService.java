package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SampleService {
    private static final Type SAMPLE_LIST_TYPE = new TypeToken<List<Sample>>() {
    }.getType();

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path dataFile;

    public SampleService() {
        this(Paths.get("sample_data.json"));
    }

    public SampleService(Path dataFile) {
        this.dataFile = dataFile;
    }

    public synchronized List<Sample> getAll() throws IOException {
        return readSamples();
    }

    public synchronized Optional<Sample> getById(int id) throws IOException {
        return readSamples().stream()
                .filter(sample -> sample.getId() == id)
                .findFirst();
    }

    public synchronized Sample add(Sample sample) throws IOException {
        List<Sample> samples = readSamples();
        sample.setId(nextId(samples));
        samples.add(sample);
        writeSamples(samples);
        return sample;
    }

    public synchronized Optional<Sample> update(int id, Sample updatedSample) throws IOException {
        List<Sample> samples = readSamples();

        for (int i = 0; i < samples.size(); i++) {
            if (samples.get(i).getId() == id) {
                updatedSample.setId(id);
                samples.set(i, updatedSample);
                writeSamples(samples);
                return Optional.of(updatedSample);
            }
        }

        return Optional.empty();
    }

    public synchronized boolean delete(int id) throws IOException {
        List<Sample> samples = readSamples();
        boolean removed = samples.removeIf(sample -> sample.getId() == id);

        if (removed) {
            writeSamples(samples);
        }

        return removed;
    }

    private List<Sample> readSamples() throws IOException {
        ensureDataFileExists();

        if (Files.size(dataFile) == 0) {
            return new ArrayList<>();
        }

        try (Reader reader = Files.newBufferedReader(dataFile, StandardCharsets.UTF_8)) {
            List<Sample> samples = gson.fromJson(reader, SAMPLE_LIST_TYPE);
            return samples == null ? new ArrayList<>() : new ArrayList<>(samples);
        }
    }

    private void writeSamples(List<Sample> samples) throws IOException {
        Path parent = dataFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (Writer writer = Files.newBufferedWriter(dataFile, StandardCharsets.UTF_8)) {
            gson.toJson(samples, SAMPLE_LIST_TYPE, writer);
        }
    }

    private void ensureDataFileExists() throws IOException {
        if (Files.exists(dataFile)) {
            return;
        }

        writeSamples(defaultSamples());
    }

    private int nextId(List<Sample> samples) {
        return samples.stream()
                .map(Sample::getId)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;
    }

    private List<Sample> defaultSamples() {
        List<Sample> samples = new ArrayList<>();
        samples.add(new Sample(
                1,
                "Sony",
                "Alpha 7 IV",
                33,
                2499.99,
                "Full-frame mirrorless camera for photo and video shooting."
        ));
        samples.add(new Sample(
                2,
                "Canon",
                "EOS R5",
                45,
                3899.00,
                "Professional mirrorless camera with high-resolution photo mode."
        ));
        samples.add(new Sample(
                3,
                "Nikon",
                "Z9",
                45,
                5496.95,
                "Flagship mirrorless camera for sports and studio work."
        ));
        return samples;
    }
}
