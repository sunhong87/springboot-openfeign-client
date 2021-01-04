package demo.app.service;

import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

public enum InputContentType {
	JSON(Constant.JSON_TEXT), CSV(Constant.CSV_TEXT);

	private String typeName;

	private InputContentType(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String toString() {
		return this.typeName;
	}
	
	public static InputContentType fromContentType(final String contentType) {
		return Stream.of(InputContentType.values()).filter(type -> type.toString().equalsIgnoreCase(contentType))
			.findFirst()
			.orElseThrow(() -> new UnsupportedMediaTypeStatusException("Unsupport conten type " + contentType));
	}

	public interface Constant {
		String JSON_TEXT = MediaType.APPLICATION_JSON_VALUE;
		String CSV_TEXT = "text/csv";
	}
}
