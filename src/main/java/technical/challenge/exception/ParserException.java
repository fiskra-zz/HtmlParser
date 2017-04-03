package technical.challenge.exception;
/**
 * It is special exception model for Html parse
 */

public class ParserException extends Exception {

	
	private static final long serialVersionUID = 1L;
	
	private String errorMessage;

	public ParserException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

}
