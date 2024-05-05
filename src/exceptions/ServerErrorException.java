package exceptions;

public class ServerErrorException extends RuntimeException {

	public ServerErrorException(final String message) {
		super(message);
	}

}

