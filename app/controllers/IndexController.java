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
import play.libs.ws.WSResponse;

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

	private final List<String> allowedHeaders;
	private final List<String> excludedHeaders;

	@Inject
	public IndexController
	(
		ApiClient apiClient,

		LogRepository logRepository
	)
	{
		this.apiClient = apiClient;

		this.logRepository = logRepository;

		allowedHeaders = new ArrayList<String>(){
			{
				add("authorization");
				add("x-us-access-token");
				add("x-us-client-id");
				add("x-client-id");
				add("x-iam-client-id");
				add("x-iam-access-token");
				add("ins-client-id");
				add("x-diagnostics-client-id");
				add("x-diagnostics-access-token");
				add("X-ds-client-id");
				add("X-ds-access-token");
			}
		};

		excludedHeaders = Arrays.asList(
			"x-forwarded-proto",
			"x-forwarded-port",
			"Host",
			"x-forwarded-for",
			"request-id",
			"Tls-Session-Info",
			"Remote-Address",
			"x-ms-request-root-id",
			"Content-Length",
			"Timeout-Access",
			"x-ms-request-id",
			"Raw-Request-URI",
			"Content-Type"
		);
	}

	private String[] formatHeadersForResponse(Map<String, List<String>> headers)
	{
		String[] formattedHeaders = new String[headers.size() * 2];

		int index = 0;
		for (Map.Entry<String, List<String>> entry: headers.entrySet()) {
			formattedHeaders[index++] = entry.getKey();
			formattedHeaders[index++] = entry.getValue().get(0);
		}

		return formattedHeaders;
	}

	private Map<String, List<String>> formatHeadersForRequest(Map<String, List<String>> requestHeaders) {
		Map<String, List<String>> headers = new HashMap<>();
		Set<String> keys = requestHeaders.keySet();
		for(String key: keys)
		{
			List<String> values = requestHeaders.get(key);

			if(!excludedHeaders.contains(key))
			{
				headers.put(key, values);
			}
		}
		return headers;
	}

	private Result handleResponse(WSResponse response) {

		if(response.getContentType().contains("application/json"))
		{
			return status(response.getStatus(), response.getBody()).as(response.getHeaderValues(CONTENT_TYPE).get(0)).withHeaders(formatHeadersForResponse(response.getHeaders()));
		}
		else if(response.getContentType().contains("text/html"))
		{
			return status(response.getStatus(), response.getBody()).as(response.getHeaderValues(CONTENT_TYPE).get(0)).withHeaders(formatHeadersForResponse(response.getHeaders()));
		}
		else
		{
			return status(response.getStatus(), response.getBody());
		}

	}

	public String getHost(Http.Request request) {
		return ((request.header("x-forwarded-myhost").isPresent()) ? request.header("x-forwarded-myhost").get() : "localhost");
	}

	public Result index()
	{
		return ok("INDEX");
	}

	public CompletionStage<Result> root(Http.Request request) throws UnsupportedEncodingException {
		return get("/", request);
	}

	public CompletionStage<Result> get(String path, Http.Request request) throws UnsupportedEncodingException {
		if(request.header("x-forwarded-port").isPresent())
		{
			Date startTime = Utils.getCurrentDate();
			String port = request.header("x-forwarded-port").get();
			Map<String, List<String>> headers = formatHeadersForRequest(request.getHeaders().asMap());

			String host = getHost(request);
			String decodedURI = URLDecoder.decode(request.uri(), "UTF-8");
			final String url = "http://" + host + ":" + port + decodedURI;

			return apiClient.get(url, headers).thenApplyAsync(response -> {
				Date endTime = Utils.getCurrentDate();

				Response apiResponse = new Response();
				apiResponse.setBody(response.getBody());
				apiResponse.setStatus(response.getStatus());
				apiResponse.setHeaders(Json.toJson(response.getHeaders()).toString());
				apiResponse.setDuration(endTime.getTime() - startTime.getTime());

				CompletableFuture.supplyAsync(new Supplier<CompletionStage<Request>>() {
					@Override
					public CompletionStage<Request> get() {
						return IndexController.this.logRepository.saveRequest(host, port, decodedURI, "GET", request, headers, apiResponse);
					}
				});
				return handleResponse(response);
			}).exceptionally(exception -> {
				Response apiResponse = new Response();
				apiResponse.setBody(exception.getMessage());
				apiResponse.setStatus(0);
				apiResponse.setHeaders(Json.toJson(new HashMap<>()).toString());
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

			Map<String, List<String>> headers = formatHeadersForRequest(request.getHeaders().asMap());

			String host = getHost(request);
			String decodedURI = URLDecoder.decode(request.uri(), "UTF-8");
			final String url = "http://" + host + ":" + port + decodedURI;

			return apiClient.post(url, request.body().asJson(), headers).thenApplyAsync(response -> {
				Date endTime = Utils.getCurrentDate();

				Response apiResponse = new Response();
				apiResponse.setBody(response.getBody());
				apiResponse.setStatus(response.getStatus());
				apiResponse.setHeaders(Json.toJson(response.getHeaders()).toString());
				apiResponse.setDuration(endTime.getTime() - startTime.getTime());

				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, decodedURI, "POST", request, headers, apiResponse));

				return handleResponse(response);
			}).exceptionally(exception -> {
				Response apiResponse = new Response();
				apiResponse.setBody(exception.getMessage());
				apiResponse.setStatus(0);
				apiResponse.setHeaders(Json.toJson(new HashMap<>()).toString());
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

			Map<String, List<String>> headers = formatHeadersForRequest(request.getHeaders().asMap());

			Map payload = Utils.convertObject(request.body().asJson(), HashMap.class);

			String host = getHost(request);
			String decodedURI = URLDecoder.decode(request.uri(), "UTF-8");
			final String url = "http://" + host + ":" + port + decodedURI;

			return apiClient.put(url, payload, headers).thenApplyAsync(response -> {
				Date endTime = Utils.getCurrentDate();

				Response apiResponse = new Response();
				apiResponse.setBody(response.getBody());
				apiResponse.setStatus(response.getStatus());
				apiResponse.setHeaders(Json.toJson(response.getHeaders()).toString());
				apiResponse.setDuration(endTime.getTime() - startTime.getTime());

				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, decodedURI, "PUT", request, headers, apiResponse));

				return handleResponse(response);
			}).exceptionally(exception -> {
				Response apiResponse = new Response();
				apiResponse.setBody(exception.getMessage());
				apiResponse.setStatus(0);
				apiResponse.setHeaders(Json.toJson(new HashMap<>()).toString());
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

			Map<String, List<String>> headers = formatHeadersForRequest(request.getHeaders().asMap());

			Map payload = Utils.convertObject(request.body().asJson(), HashMap.class);

			String host = getHost(request);
			String decodedURI = URLDecoder.decode(request.uri(), "UTF-8");
			final String url = "http://" + host + ":" + port + decodedURI;

			return apiClient.delete(url, payload, headers).thenApplyAsync(response -> {
				Date endTime = Utils.getCurrentDate();

				Response apiResponse = new Response();
				apiResponse.setBody(response.getBody());
				apiResponse.setStatus(response.getStatus());
				apiResponse.setHeaders(Json.toJson(response.getHeaders()).toString());
				apiResponse.setDuration(endTime.getTime() - startTime.getTime());

				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, decodedURI, "DELETE", request, headers, apiResponse));

				return handleResponse(response);
			}).exceptionally(exception -> {
				Response apiResponse = new Response();
				apiResponse.setBody(exception.getMessage());
				apiResponse.setStatus(0);
				apiResponse.setHeaders(Json.toJson(new HashMap<>()).toString());
				apiResponse.setDuration(0);
				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, decodedURI, "DELETE", request, headers, apiResponse));

				return ok("Error");
			});
		}

		return CompletableFuture.supplyAsync(() -> {
			return ok("Error");
		});
	}

	public CompletionStage<Result> options(String path, Http.Request request) throws UnsupportedEncodingException {
		if(request.header("x-forwarded-port").isPresent())
		{
			Date startTime = Utils.getCurrentDate();
			String port = request.header("x-forwarded-port").get();

			Map<String, List<String>> headers = formatHeadersForRequest(request.getHeaders().asMap());

			String host = getHost(request);
			String decodedURI = URLDecoder.decode(request.uri(), "UTF-8");
			final String url = "http://" + host + ":" + port + decodedURI;

			return apiClient.options(url, request.body().asJson(), headers).thenApplyAsync(response -> {
				Date endTime = Utils.getCurrentDate();

				Response apiResponse = new Response();
				apiResponse.setBody(response.getBody());
				apiResponse.setStatus(response.getStatus());
				apiResponse.setHeaders(Json.toJson(response.getHeaders()).toString());
				apiResponse.setDuration(endTime.getTime() - startTime.getTime());

				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, decodedURI, "OPTIONS", request, headers, apiResponse));

				return handleResponse(response);
			}).exceptionally(exception -> {
				Response apiResponse = new Response();
				apiResponse.setBody(exception.getMessage());
				apiResponse.setStatus(0);
				apiResponse.setHeaders(Json.toJson(new HashMap<>()).toString());
				apiResponse.setDuration(0);
				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, decodedURI, "POST", request, headers, apiResponse));

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