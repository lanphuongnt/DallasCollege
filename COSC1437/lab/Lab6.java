package Lab6;

public class Lab6 {
	public static void main(String arg[]) {


		String test1 = "bell"; 		//true
		String test2 = "eel"; 		//true
		String test3 = "llama"; 		//true
		String test4 = "best"; 		//false
		String test5 = "rightat"; 		//false
		String test6 = "elephant"; 	//false
		String test7 = "honorificabilitudinitatibus"; 	//false

		System.out.println(doubleChecker(test1));
		System.out.println(doubleChecker(test2));
		System.out.println(doubleChecker(test3));
		System.out.println(doubleChecker(test4));
		System.out.println(doubleChecker(test5));
		System.out.println(doubleChecker(test6));
		System.out.println(doubleChecker(test7));
	}

	public static boolean MethodName(String s) {
		return s.matches("Your regex");
	}

	/**
	 * Part 1) Write a method called affirmative which takes a string and returns
	 * true if the string contains: “True”, “true”, “Yes”, “yes”, “Y”, “y”;
	 */
	public static boolean affirmative(String s) {
		return s.matches("[tT]rue|[yY](es)?");
	}

	/**
	 * Part 2) Write a method conVolPattern which will check if that string input as
	 * a parameter has a consonant followed by a vowel followed by a consonant
	 * pattern. This method should continue to check the pattern for the entire
	 * string.
	 */

	public static boolean conVolPattern(String s) {
		return s.matches("([a-zA-Z&&[^aeuioAEUIO]][aeuioAEUIO])+[a-zA-Z&&[^aeuioAEUIO]]?");
	}

	/**
	 * Part 3) Write a method called checkPhoneFormat which takes a string as a
	 * parameter and uses a regular expression to check whether the string matches
	 * the following phone format. Hyphens may be added but All other formats should
	 * return false.
	 */
	public static boolean checkPhoneFormat(String s) {
		return s.matches("\\(\\d{3}\\)[ -]\\d{3}[ -]\\d{4}");
	}
	
	/**
	 * Part 4) 
	 * Write a method called twoNum that uses a regular expression 
	 * that checks if a string contains exactly 2 numbers
	 */
	
	public static boolean twoNum(String s) {
		return s.matches("(\\D*\\d\\D*){2}");
	}
	
	/**
	 * Part 5) 
	 * Write a method called PasswordValidate, 
	 * which will check a string to confirm that it contains 
	 * at least 1 number, 
	 * at least 1 uppercase letter, 
	 * at least 1 special symbol (!@#$%^&*), 
	 * and is at least 4 characters long (should be done with regex not with String.length)
	 */
	
	public static boolean passwordValidate(String s) {
		return s.matches("^(?=.*\\d)(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.{4,}).*$");
	}
	
	public static boolean doubleChecker(String s) {
		return s.matches("^.*(.)\\1.*$");
	}
}
