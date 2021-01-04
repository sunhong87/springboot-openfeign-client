package demo.app.service.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import demo.app.client.PostClient;
import demo.app.model.PostModel;
import demo.app.service.AsynchronousService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;

@Slf4j
@AllArgsConstructor
public class PostAsynchronousService implements AsynchronousService<PostModel, PostModel> {
	
	private PostClient postClient;

	@Override
	public List<PostModel> asynchronousCreatePost(List<PostModel> postRequests, Function<PostModel, PostModel> fallBackMethod) throws InterruptedException, ExecutionException {
		log.info("HystrixCommand: calling create post...");
		List<PostModel> results = new CopyOnWriteArrayList<>();
		List<Future<PostModel>> futureCreateResult = postRequests.stream()
				.map(request -> new HystrixAsynchronousCommand(request, fallBackMethod).queue())
				.collect(Collectors.toList());
		futureCreateResult.parallelStream()
			.forEach(futureResult -> {
				try {
					results.add(futureResult.get());
				} catch (Exception e) {
					log.error("Error execute HystrixAsynchronousCommand...", e);
				}
			});
		return results;
	}
	
	@Override
	public List<Observable<PostModel>> reactiveCreatePost(List<PostModel> postRequests, Function<PostModel, PostModel> fallBackMethod) throws InterruptedException, ExecutionException {
	
		return postRequests.stream()
			.map(request -> new HystrixAsynchronousCommand(request, fallBackMethod).toObservable())
			.collect(Collectors.toList());
	}
	

	private class HystrixAsynchronousCommand extends HystrixCommand<PostModel> {

		private PostModel postRequest;
		
		private Function<PostModel, PostModel> fallBackMethod;
		
		public HystrixAsynchronousCommand(PostModel request, Function<PostModel, PostModel> fallBackMethod) {
			super(HystrixCommandGroupKey.Factory.asKey("HystrixAsynchronousService"), 5000);
			this.postRequest = request;
			this.fallBackMethod = fallBackMethod;
		}

		@Override
		protected PostModel run() throws Exception {
			log.info("HystrixAsynchronousCommand: calling create post connector...");
			return postClient.createPost(postRequest);
		}

		@Override
		protected PostModel getFallback() {
			log.info("HystrixAsynchronousCommand: calling create post connector fail, using fallback...");
			return fallBackMethod.apply(postRequest);
		}
	}
}
