package demo.app.service.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import demo.app.client.PostClient;
import demo.app.model.PostModel;
import demo.app.service.AsynchronousService;
import demo.app.service.InputContentType;
import demo.app.service.InputParser;
import demo.app.service.InputParserFactory;
import demo.app.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

	private PostClient postClient;
	
	private InputParserFactory parserFactory;
	
	private AsynchronousService<PostModel, PostModel> asynchronousService;
	
	@Override
	public List<PostModel> listAllPosts() {
		log.debug("List all posts...");
		return postClient.listAllPosts();
	}

	@Override
	public List<PostModel> batchUpload(String contentType, InputStream inputStream) {
		log.debug("Upload posts in batch...");
		try(InputStreamReader isr = new InputStreamReader(inputStream)) {
			List<PostModel> parsedPostModel = getParserByContentType(contentType).parse(isr);
			List<PostModel> uploadResults = asynchronousService.asynchronousCreatePost(parsedPostModel, (request) -> {
				request.setError("Error when creating...");
				return request;
			});
			return uploadResults;
		} catch (Exception e) {
			log.error("Batch upload fail...", e);
		}
		return new ArrayList<>();
	}

	private InputParser getParserByContentType(String contentType) {
		InputContentType inputContentType = InputContentType.fromContentType(contentType);
		return Optional.ofNullable(parserFactory.getParser(inputContentType))
				.orElseThrow(() -> new UnsupportedOperationException("Cannot find any parser to support current conten-type " + contentType));
	}
}
