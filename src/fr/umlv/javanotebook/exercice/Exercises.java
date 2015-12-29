package fr.umlv.javanotebook.exercice;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import fr.umlv.javanotebook.test.Test_1;

/**
 * this class implement a list of Exersise.
 */
public class Exercises {

    private final List<Exercise> exercices = new ArrayList<>();

    /**
     * Fetches all the exercise Associating the id of the exercise with the
     * awaited respons
     */
    public Exercises() {
        getAllExercicesAndAnswers();
    }

    /**
     * @return returns the number of exercises in the exercice folder
     */
    public String countFiles() {
        return Integer.toString(exercices.size());
    }

    private void getAllExercicesAndAnswers() {
        Path path = Paths.get("./exercice/answers.rep");
        try {
            Files.lines(path).forEach(
                    l -> exercices.add(new Exercise(l.split(" : ")[0], Test_1.class)));
        } catch (IOException e) {
            throw new IllegalArgumentException("The file " + path + " doesn't exist");
        }
    }

    /**
     * get the answer for the exercice with number key.
     *
     * @param key is the number of the exercice
     * @return the good answer for the exercice
     */
    public Method[] getAnswerFromKey(String key) {
        for (Exercise ex : exercices) {
            if (ex.getNumero().compareTo(key) == 0) {
                return ex.getRespons();
            }
        }
        throw new IllegalArgumentException("Answer to exercice " + key + " doesn't exist");
    }

    /**
     * get the web format for the exercice with the number key
     *
     * @param key is the number of the exercice
     * @return the web format for the exercice
     */
    public String getToWebFromKey(String key) {
        for (Exercise ex : exercices) {
            if (ex.getNumero().compareTo(key) == 0) {
                return ex.toWeb();
            }
        }
        throw new IllegalStateException("Exercice doesnt exist");
    }

}
