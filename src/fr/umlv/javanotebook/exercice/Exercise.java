package fr.umlv.javanotebook.exercice;

import org.parboiled.common.FileUtils;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

/**
 * this class implement an Exercise with his number and his respons.
 */
class Exercise {

    private final String number;
    private final String folder;
    private String testExercise;

    private Exercise(String number, String folder) {
        this.number = Objects.requireNonNull(number);
        this.folder = Objects.requireNonNull(folder);
    }

    /**
     * Create an instance of Exercise
     * @param number number of the Exercise
     * @return new Exercise use private constructor for build Exercise.
     */
    public static Exercise create_Exercise(String number, String folder) {
        return new Exercise(number, folder);
    }

    private String generateHtml(char[] markdown) {
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
        String markdown = "<p>There is no Exercise " + number + "</p>";
        try {
            markdown = getInput(file);
        } catch (FileNotFoundException e) {
            return markdown;
        }
        return generateHtml(markdown.toCharArray());
    }

    private String getInput(String file) throws FileNotFoundException {
        InputStream input;
        input = new FileInputStream(file);
        String s = FileUtils.readAllText(input);
        String[] tokens = s.split("\\$");
        testExercise = Objects.requireNonNull(tokens[1], "There is no testCode for current exercise ( " + number + ") !!!");

        return tokens[0];
    }

    String getNumero() {
        return number;
    }

    String getTestExercise() {
        return testExercise;
    }
}
