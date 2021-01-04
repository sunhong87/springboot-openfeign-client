package demo.app.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import demo.app.model.PostModel;
import demo.app.service.InputContentType;
import demo.app.service.PostService;

@SpringBootTest
public class TestPostService {

	@Autowired
	private PostService postService;
	
	@Value("classpath:post_array.json")
	private Resource postJsonArray;
	
	@Value("classpath:post.json")
	private Resource postJsonObject;
	
	@Value("classpath:post.csv")
	private Resource postCsv;
	
	@Test
	public void testFetchAllPopst() {
		List<PostModel> postList = postService.listAllPosts();
		assertNotNull(postList);
		assertFalse(postList.isEmpty());
	}
	
	@Test
	public void testBatchUpload_With_JsonArray() throws IOException {
		List<PostModel> postList = postService.batchUpload(MediaType.APPLICATION_JSON_VALUE, postJsonArray.getInputStream());
		assertNotNull(postList);
		assertFalse(postList.isEmpty());
		assertEquals(2, postList.size());
	}
	
	@Test
	public void testBatchUpload_With_JsonObject() throws IOException {
		List<PostModel> postList = postService.batchUpload(MediaType.APPLICATION_JSON_VALUE, postJsonObject.getInputStream());
		assertNotNull(postList);
		assertFalse(postList.isEmpty());
		assertEquals(1, postList.size());
	}
	
	@Test
	public void testBatchUpload_With_CSV() throws IOException {
		List<PostModel> postList = postService.batchUpload(InputContentType.Constant.CSV_TEXT, postCsv.getInputStream());
		assertNotNull(postList);
		assertFalse(postList.isEmpty());
		assertEquals(2, postList.size());
	}
}
