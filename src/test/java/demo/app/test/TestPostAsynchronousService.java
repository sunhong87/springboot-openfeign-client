package demo.app.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import demo.app.model.PostModel;
import demo.app.service.AsynchronousService;
import demo.app.service.InputContentType;
import demo.app.service.InputParserFactory;
import rx.Observable;

@SpringBootTest
public class TestPostAsynchronousService {
	
	@Value("classpath:post_array.json")
	private Resource postJsonArray;
	
	@Autowired
	private AsynchronousService<PostModel, PostModel> asynchronousService;
	
	@Autowired
	private InputParserFactory parserFactory;
	
	@Test
	public void test_AsynchronousCreatePost() throws InterruptedException, ExecutionException, IOException {
		final String errorMessage = "ERROR";
		List<PostModel> postRequestList = parsePostJson(postJsonArray.getInputStream());
		List<PostModel> postResultList = asynchronousService.asynchronousCreatePost(postRequestList, (request) -> {
			request.setError(errorMessage);
			return request;
		});
		
		assertNotNull(postResultList);
		assertEquals(2, postResultList.size());
		assertTrue(postResultList.stream().allMatch(createResult -> createResult.getId() != null));
	}
	
	@Test
	public void test_ReactiveCreatePost() throws InterruptedException, ExecutionException, IOException {
		final String errorMessage = "ERROR";
		List<PostModel> postRequestList = parsePostJson(postJsonArray.getInputStream());
		List<Observable<PostModel>> observaleResultList = asynchronousService.reactiveCreatePost(postRequestList, (request) -> {
			request.setError(errorMessage);
			return request;
		});
		
		List<PostModel> resultList = observaleResultList.parallelStream()
			.map(observable -> observable.toBlocking().single())
			.collect(Collectors.toList());
		
		assertFalse(resultList.isEmpty());
	}
	
	private List<PostModel> parsePostJson(InputStream is) {
		return parserFactory.getParser(InputContentType.JSON).parse(new InputStreamReader(is));
	}
}
