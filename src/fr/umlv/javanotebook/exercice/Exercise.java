package fr.umlv.javanotebook.exercice;

import org.parboiled.common.FileUtils;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * this class implement an Exercise with his number and his respons.
 */
class Exercise {

    private final List<Method> respons;
    private final String number;
    private final String folder;

    private Exercise(String number, List<Method> respons, String folder) {
        this.number = Objects.requireNonNull(number);
        this.respons = Objects.requireNonNull(respons);
        this.folder = Objects.requireNonNull(folder);
    }

    /**
     * Create an instance of Exercise
     * @param number number of the Exercise
     * @param class_test_respons class of the respons test
     * @return new Exercise use private constructor for build Exercise.
     */
    public static Exercise create_Exercise(String number, Class<?> class_test_respons, String folder) {
        return new Exercise(number, filter(number, class_test_respons), folder);
    }

    private static List<Method> filter(String number, Class<?> class_test_respons) {
        List<Method> tempo = new ArrayList<>();
        for (Method m : class_test_respons.getDeclaredMethods()) {
            if (m.getName().startsWith("test_" + number)) {
                m.setAccessible(true);
                tempo.add(m);
            }
        }
        return tempo;
    }

    private static String generateHtml(char[] markdown) {
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL);
        return processor.markdownToHtml(markdown);
    }

    /**
     * Parses the markdown file and returns the html code generated by the
     * PegDown processor
     *
     * @return returns the html code generated by the PegDown processor
     */
    String toWeb() {
        String file = folder + number + ".md";
        InputStream input;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return "<p>There is no Exercise " + number + "</p>";
        }
        return generateHtml(FileUtils.readAllChars(input));
    }

    List<Method> getRespons() {
        return respons;
    }

    String getNumero() {
        return number;
    }
}
