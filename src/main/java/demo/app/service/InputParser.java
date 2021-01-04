package demo.app.service;

import java.io.Reader;
import java.util.List;

import demo.app.model.PostModel;

public interface InputParser {
	List<PostModel> parse(Reader reader);
}
