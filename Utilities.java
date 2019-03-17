public class Utilities {

	public static void logger(String msg) {

		String callerClassName = new Exception().getStackTrace()[1].getClassName();

		System.out.println("[" + callerClassName + "] " + msg);

	}

}