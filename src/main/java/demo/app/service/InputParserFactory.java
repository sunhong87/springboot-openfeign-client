package demo.app.service;

public interface InputParserFactory {

	public InputParser getParser(InputContentType contentType);
}
