package demo.app.service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import rx.Observable;

public interface AsynchronousService<T, U> {

	public List<U> asynchronousCreatePost(List<T> postRequest, Function<T, U> fallBackMethod) throws InterruptedException, ExecutionException;
	
	public List<Observable<U>> reactiveCreatePost(List<T> postRequest, Function<T, U> fallBackMethod) throws InterruptedException, ExecutionException;
}
