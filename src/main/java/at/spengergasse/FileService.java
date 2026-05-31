package at.spengergasse;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private boolean[][] result;
    private GraphAnalyzer analyzer;

    public boolean[][] processCsv(InputStream input) {

        List<boolean[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;

            while ((line = reader.readLine()) != null) {

                if (!line.matches("^(?:[01](?:;[01])*)$")) {
                    throw new FormatException("Ungültiges Format: " + line);
                }

                String[] parts = line.split(";");
                boolean[] row = new boolean[parts.length];

                for (int i = 0; i < parts.length; i++) {
                    row[i] = "1".equals(parts[i]);
                }

                rows.add(row);
            }

        } catch (IOException | FormatException e) {
            throw new RuntimeException("Fehler beim Lesen der Datei", e);
        }

        result = rows.toArray(new boolean[0][]);
        analyzer = new GraphAnalyzer(result);

        return result;
    }

    public int[][] getDistance() {
        return analyzer.computeDistanceMatrix();
    }

    public boolean[][] getResult() {
        return result;
    }

    public static class FormatException extends Exception {
        public FormatException(String msg) {
            super(msg);
        }
    }
}