package demo.app.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import demo.app.config.PostClientConfig;
import demo.app.model.PostModel;

@FeignClient(name = "postClient", url = "https://jsonplaceholder.typicode.com", configuration = PostClientConfig.class)
public interface PostClient {
	
	@GetMapping(value = "/posts", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PostModel> listAllPosts();
	
	@PostMapping(value = "posts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PostModel createPost(PostModel postRequest);
}
