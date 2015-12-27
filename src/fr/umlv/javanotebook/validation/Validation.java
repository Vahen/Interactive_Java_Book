package fr.umlv.javanotebook.validation;

import jdk.jshell.JShell;
import jdk.jshell.Snippet.Status;
import jdk.jshell.SnippetEvent;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
/**
 * this class is used for validate a String for Java Language.
 * example:
 * Validation valid = new Validation()
 * val.addInQueue("public void test(){}");
 * if (!valid.accept()) {
 * 	routingContext.response().end(val.status());
 * }
 * else {
 * 	System.out.println(val.validate());
 * }
 * val.reset();
 */
public class Validation {

    private final ReentrantLock rlock = new ReentrantLock();
    private final Condition condition = rlock.newCondition();
    private String input = null;
    private int nbWait = 0;
    private final JShell js;

    public Validation() {
        js = JShell.create();
    }

    /**
     * add the answer of user in input (thread-safe)
     *
     * @param input is the answer of user.
     */
    public void addInQueue(String input) {
        Objects.requireNonNull(input);
        rlock.lock();
        try {
            while (this.input != null) {
                try {
                    nbWait++;
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.input = input;
        } finally {
            rlock.unlock();
        }
    }

    /**
     * free the code for continue to validate another input.
     */
    public void reset() {
        input = null;
        if (nbWait > 0) {
            condition.signal();
            nbWait--;
        }
    }

    /**
     * the fonction tests if code is valid or not.
     *
     * @return return true if code is valid, false else.
     */
    public boolean accept() {
        for (SnippetEvent e : js.eval(input)) {
            if (e.causeSnippet() == null) {
                switch (e.status()) {
                    case VALID:
                        break;
                    case RECOVERABLE_DEFINED:
                        return false;
                    case RECOVERABLE_NOT_DEFINED:
                        return false;
                    case REJECTED:
                        return false;
                    default:
                        throw new IllegalStateException();
                }
            }
        }
        return true;
    }

    /**
     * the fonction return some trick for correcting the code
     *
     * @return the specified problem on the code.
     * @throws IllegalStateException if error on program eval, or if
     */
    public String status() {
        for (SnippetEvent e : js.eval(input)) {
            if (e.causeSnippet() == null) {
                switch (e.status()) {
                    case VALID:
                        break;
                    case RECOVERABLE_DEFINED:
                        return "Code with unresolved references";
                    case RECOVERABLE_NOT_DEFINED:
                        return "Code possibly reparable, but failed";
                    case REJECTED:
                        return "Code failed";
                    default:
                        throw new IllegalStateException("internal error");
                }
            }
        }
        throw new IllegalStateException("valid when accept return false (invalid code)");
    }

    /**
     * This function return the answer of user with the good format for testing
     *
     * @return value of code for answer question
     */
    public String validate() {
        StringBuilder sbrow = new StringBuilder();
        List<SnippetEvent> events = js.eval(input);
        for (SnippetEvent e : events) {
            StringBuilder sb = new StringBuilder();
            if (e.previousStatus() == Status.NONEXISTENT) {
                sb.append("addition");
            } else {
                sb.append("modification");
            }
            sb.append(" of ");
            sb.append(e.snippet().source());
            sbrow.append(sb.toString());
            if (e.value() != null) {
                sbrow.append(e.value());
            }
            System.out.flush();
        }
        System.out.println("In function validate " + sbrow);
        return sbrow.toString();

    }

	/*private String takeFromQueue() {
        String tmp=null;
		try {
			tmp=queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Objects.requireNonNull(tmp);
	}*/


}
