package demo.app.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import demo.app.client.PostClient;
import demo.app.model.PostModel;
import demo.app.service.AsynchronousService;
import demo.app.service.InputContentType;
import demo.app.service.InputParserFactory;
import demo.app.service.impl.PostAsynchronousService;
import feign.FeignException;

@SpringBootTest
public class MockTestPostAsynchronousService {
	
	@Value("classpath:post_array.json")
	private Resource postJsonArray;

	@Mock
	private PostClient postClient;
	
	@InjectMocks
	private AsynchronousService<PostModel, PostModel> asynchronousService = new PostAsynchronousService(postClient);
	
	@Autowired
	private InputParserFactory parserFactory;
	
	@Test
	public void testAsynchronousService_FallBack() throws InterruptedException, ExecutionException, IOException {
		Mockito.when(postClient.createPost(Mockito.any(PostModel.class))).thenThrow(FeignException.class);
		final String errorMessage = "ERROR";
		List<PostModel> postRequestList = parsePostJson(postJsonArray.getInputStream());
		List<PostModel> postResultList = asynchronousService.asynchronousCreatePost(postRequestList, (request) -> {
			request.setError(errorMessage);
			return request;
		});
		
		assertNotNull(postResultList);
		assertEquals(2, postResultList.size());
		assertTrue(postResultList.stream().allMatch(createResult -> errorMessage.equals(createResult.getError())));
	}
	
	private List<PostModel> parsePostJson(InputStream is) {
		return parserFactory.getParser(InputContentType.JSON).parse(new InputStreamReader(is));
	}
}
