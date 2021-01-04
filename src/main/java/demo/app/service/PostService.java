package demo.app.service;

import java.io.InputStream;
import java.util.List;

import demo.app.model.PostModel;

public interface PostService {
	List<PostModel> listAllPosts();
	List<PostModel> batchUpload(String contentType, InputStream inputStream);
}
