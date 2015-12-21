package fr.umlv.javanotebook.exercice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

// TODO Change name
public class Exercices {

	//	private final int id;
	//	private String respons;

	// TODO Changer Integer en Exerice plus tard eventuellement
	private final HashMap<Integer, String> exercices = new HashMap<>();

	public Exercices(){

	}

	/**
	 * @return returns the number of exercises in the exercice folder
	 */
	public static String countFiles() {
		return "" + (new File("./exercice").listFiles().length - 1);
	}

	public static void main(String[] args) {
		Exercices ex = new Exercices() ;
		ex.getAllExercicesAndAnswers();
		System.out.println(ex.exercices);

		System.out.println(countFiles());
	}

	/**
	 * Fetches all the exercise
	 * Associating the id of the exercise with the awaited respons
	 */
	public void getAllExercicesAndAnswers() {
		Path path = Paths.get("./exercice/answers.rep");
		try {
			Files.lines(path).forEach(l ->
					exercices.put(Integer.parseInt(l.split(" : ")[0]), l.split(" : ")[1])
			);
		} catch (IOException e) {
			// TODO Someting with exeception
			System.err.println(e);
		}
	}

}