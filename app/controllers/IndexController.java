package controllers;

import com.google.inject.Inject;
import models.Request;
import models.Response;
import play.mvc.Result;
import play.libs.Json;
import play.mvc.Http;
import repositories.LogRepository;
import utils.ApiClient;
import utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

public class IndexController extends BaseController
{
	private final ApiClient apiClient;

	private final LogRepository logRepository;

	@Inject
	public IndexController
	(
		ApiClient apiClient,

		LogRepository logRepository
	)
	{
		this.apiClient = apiClient;

		this.logRepository = logRepository;
	}

	public Result index()
	{
		return ok("INDEX");
	}

	public CompletionStage<Result> get(String path, Http.Request request) throws UnsupportedEncodingException {
		if(request.header("x-forwarded-port").isPresent())
		{
			Date startTime = Utils.getCurrentDate();
			String port = request.header("x-forwarded-port").get();
			Map<String, List<String>> requestHeaders = request.getHeaders().toMap();
			Map<String, List<String>> headers = new HashMap<>();
			Set<String> keys = requestHeaders.keySet();
			for(String key: keys)
			{
				List<String> values = requestHeaders.get(key);
				List<String> allowedHeaders = new ArrayList<String>(){
					{
						add("Authorization");
					}
				};

				if((key.toLowerCase().contains("quikr")) || allowedHeaders.contains(key))
				{
					headers.put(key, values);
				}
			}

			String host = "localhost";
			String decodedURI = URLDecoder.decode(request.uri(), "UTF-8");
			final String url = "http://" + host + ":" + port + decodedURI;

			return apiClient.get(url, headers).thenApplyAsync(response -> {
				Date endTime = Utils.getCurrentDate();

				Response apiResponse = new Response();
				apiResponse.setBody(response.getBody());
				apiResponse.setStatus(response.getStatus());
				apiResponse.setHeaders(Json.toJson(response.getHeaders()));
				apiResponse.setDuration(endTime.getTime() - startTime.getTime());

				CompletableFuture.supplyAsync(new Supplier<CompletionStage<Request>>() {
					@Override
					public CompletionStage<Request> get() {
						return IndexController.this.logRepository.saveRequest(host, port, decodedURI, "GET", request, headers, apiResponse);
					}
				});
				return status(response.getStatus(), response.getBody());
			}).exceptionally(exception -> {
				Response apiResponse = new Response();
				apiResponse.setBody(exception.getMessage());
				apiResponse.setStatus(0);
				apiResponse.setHeaders(Json.toJson(new HashMap<>()));
				apiResponse.setDuration(0);
				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, decodedURI, "GET", request, headers, apiResponse));

				return ok("Error");
			});
		}

		return CompletableFuture.supplyAsync(() -> {
			return ok("Error");
		});
	}

	public CompletionStage<Result> post(String path, Http.Request request) throws UnsupportedEncodingException {
		if(request.header("x-forwarded-port").isPresent())
		{
			Date startTime = Utils.getCurrentDate();
			String port = request.header("x-forwarded-port").get();
			Map<String, List<String>> requestHeaders = request.getHeaders().toMap();
            Map<String, List<String>> headers = new HashMap<>();
            Set<String> keys = requestHeaders.keySet();
            for(String key: keys)
            {
                List<String> values = requestHeaders.get(key);
                List<String> allowedHeaders = new ArrayList<String>(){
                    {
                        add("Authorization");
                    }
                };

                if((key.toLowerCase().contains("quikr")) || allowedHeaders.contains(key))
                {
                    headers.put(key, values);
                }
            }

			String host = "localhost";
			String decodedURI = URLDecoder.decode(request.uri(), "UTF-8");
			final String url = "http://" + host + ":" + port + decodedURI;

			return apiClient.post(url, request.body().asJson(), headers).thenApplyAsync(response -> {
				Date endTime = Utils.getCurrentDate();

				Response apiResponse = new Response();
				apiResponse.setBody(response.getBody());
				apiResponse.setStatus(response.getStatus());
				apiResponse.setHeaders(Json.toJson(response.getHeaders()));
				apiResponse.setDuration(endTime.getTime() - startTime.getTime());

				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, decodedURI, "POST", request, headers, apiResponse));

				return status(response.getStatus(), response.getBody());
			}).exceptionally(exception -> {
				Response apiResponse = new Response();
				apiResponse.setBody(exception.getMessage());
				apiResponse.setStatus(0);
				apiResponse.setHeaders(Json.toJson(new HashMap<>()));
				apiResponse.setDuration(0);
				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, decodedURI, "POST", request, headers, apiResponse));

				return ok("Error");
			});
		}

		return CompletableFuture.supplyAsync(() -> {
			return ok("Error");
		});
	}

	public CompletionStage<Result> put(String path, Http.Request request) throws UnsupportedEncodingException {
		if(request.header("x-forwarded-port").isPresent())
		{
			Date startTime = Utils.getCurrentDate();
			String port = request.header("x-forwarded-port").get();
			Map<String, List<String>> requestHeaders = request.getHeaders().toMap();
			Map<String, List<String>> headers = new HashMap<>();
			Set<String> keys = requestHeaders.keySet();
			for(String key: keys)
			{
				List<String> values = requestHeaders.get(key);
				List<String> allowedHeaders = new ArrayList<String>(){
					{
						add("Authorization");
					}
				};

				if((key.toLowerCase().contains("quikr")) || allowedHeaders.contains(key))
				{
					headers.put(key, values);
				}
			}

			Map payload = Utils.convertObject(request.body().asJson(), HashMap.class);

			String host = "localhost";
			String decodedURI = URLDecoder.decode(request.uri(), "UTF-8");
			final String url = "http://" + host + ":" + port + decodedURI;

			return apiClient.put(url, payload, headers).thenApplyAsync(response -> {
				Date endTime = Utils.getCurrentDate();

				Response apiResponse = new Response();
				apiResponse.setBody(response.getBody());
				apiResponse.setStatus(response.getStatus());
				apiResponse.setHeaders(Json.toJson(response.getHeaders()));
				apiResponse.setDuration(endTime.getTime() - startTime.getTime());

				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, decodedURI, "PUT", request, headers, apiResponse));

				return status(response.getStatus(), response.getBody());
			}).exceptionally(exception -> {
				Response apiResponse = new Response();
				apiResponse.setBody(exception.getMessage());
				apiResponse.setStatus(0);
				apiResponse.setHeaders(Json.toJson(new HashMap<>()));
				apiResponse.setDuration(0);
				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, decodedURI, "PUT", request, headers, apiResponse));

				return ok("Error");
			});
		}

		return CompletableFuture.supplyAsync(() -> {
			return ok("Error");
		});
	}

	public CompletionStage<Result> delete(String path, Http.Request request) throws UnsupportedEncodingException {
		if(request.header("x-forwarded-port").isPresent())
		{
			Date startTime = Utils.getCurrentDate();
			String port = request.header("x-forwarded-port").get();
			Map<String, List<String>> requestHeaders = request.getHeaders().toMap();
			Map<String, List<String>> headers = new HashMap<>();
			Set<String> keys = requestHeaders.keySet();
			for(String key: keys)
			{
				List<String> values = requestHeaders.get(key);
				List<String> allowedHeaders = new ArrayList<String>(){
					{
						add("Authorization");
					}
				};

				if((key.toLowerCase().contains("quikr")) || allowedHeaders.contains(key))
				{
					headers.put(key, values);
				}
			}

			String host = "localhost";
			String decodedURI = URLDecoder.decode(request.uri(), "UTF-8");
			final String url = "http://" + host + ":" + port + decodedURI;

			return apiClient.delete(url, headers).thenApplyAsync(response -> {
				Date endTime = Utils.getCurrentDate();

				Response apiResponse = new Response();
				apiResponse.setBody(response.getBody());
				apiResponse.setStatus(response.getStatus());
				apiResponse.setHeaders(Json.toJson(response.getHeaders()));
				apiResponse.setDuration(endTime.getTime() - startTime.getTime());

				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, decodedURI, "DELETE", request, headers, apiResponse));

				return status(response.getStatus(), response.getBody());
			}).exceptionally(exception -> {
				Response apiResponse = new Response();
				apiResponse.setBody(exception.getMessage());
				apiResponse.setStatus(0);
				apiResponse.setHeaders(Json.toJson(new HashMap<>()));
				apiResponse.setDuration(0);
				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, decodedURI, "DELETE", request, headers, apiResponse));

				return ok("Error");
			});
		}

		return CompletableFuture.supplyAsync(() -> {
			return ok("Error");
		});
	}

    public CompletionStage<Result> clear()
	{
		return CompletableFuture.supplyAsync(() -> {
			this.logRepository.clear();

			return ok("success");
		});
	}
}