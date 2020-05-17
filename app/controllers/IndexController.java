package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.Response;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Result;
import play.libs.Json;
import play.mvc.Http;
import repositories.LogRepository;
import utils.ApiClient;
import utils.Utils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

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

	public CompletionStage<Result> get(String path, Http.Request request)
	{
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
			String url = "http://" + host + ":" + port + "/" + path;

			Map<String, String[]> queryStringParams = request.queryString();
			if(!queryStringParams.keySet().isEmpty())
			{
				String[] params = new String[queryStringParams.keySet().size()];
				int index = 0;
				for(String key: queryStringParams.keySet())
				{
					String[] values = queryStringParams.get(key);
					for(String value: values)
					{
						params[index++] = key + "=" + value;
					}
				}

				url += "?" + String.join("&", params);
			}

			return apiClient.get(url, headers).thenApplyAsync(response -> {
				Date endTime = Utils.getCurrentDate();

				Response apiResponse = new Response();
				apiResponse.setBody(response.getBody());
				apiResponse.setStatus(response.getStatus());
				apiResponse.setHeaders(Json.toJson(response.getHeaders()));
				apiResponse.setDuration(endTime.getTime() - startTime.getTime());

				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, path, "GET", request, headers, apiResponse));

				return status(response.getStatus(), response.asJson());
			}).exceptionally(exception -> {
				Response apiResponse = new Response();
				apiResponse.setBody(exception.getMessage());
				apiResponse.setStatus(0);
				apiResponse.setHeaders(Json.toJson(new HashMap<>()));
				apiResponse.setDuration(0);
				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, path, "GET", request, headers, apiResponse));

				return ok("Error");
			});
		}

		return CompletableFuture.supplyAsync(() -> {
			return ok("Error");
		});
	}

	public CompletionStage<Result> post(String path, Http.Request request)
	{
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
			String url = "http://" + host + ":" + port + "/" + path;

			Map<String, String[]> queryStringParams = request.queryString();
			if(!queryStringParams.keySet().isEmpty())
			{
				String[] params = new String[queryStringParams.keySet().size()];
				int index = 0;
				for(String key: queryStringParams.keySet())
				{
					String[] values = queryStringParams.get(key);
					if(values.length == 1)
					{
						params[index++] = key + "=" + values[0];
					}
				}

				url += "?" + String.join("&", params);
			}

			return apiClient.post(url, request.body().asJson(), headers).thenApplyAsync(response -> {
				Date endTime = Utils.getCurrentDate();

				Response apiResponse = new Response();
				apiResponse.setBody(response.getBody());
				apiResponse.setStatus(response.getStatus());
				apiResponse.setHeaders(Json.toJson(response.getHeaders()));
				apiResponse.setDuration(endTime.getTime() - startTime.getTime());

				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, path, "POST", request, headers, apiResponse));

				return status(response.getStatus(), response.asJson());
			}).exceptionally(exception -> {
				Response apiResponse = new Response();
				apiResponse.setBody(exception.getMessage());
				apiResponse.setStatus(0);
				apiResponse.setHeaders(Json.toJson(new HashMap<>()));
				apiResponse.setDuration(0);
				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, path, "POST", request, headers, apiResponse));

				return ok("Error");
			});
		}

		return CompletableFuture.supplyAsync(() -> {
			return ok("Error");
		});
	}

	public CompletionStage<Result> put(String path, Http.Request request)
	{
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
			String url = "http://" + host + ":" + port + "/" + path;

			Map<String, String[]> queryStringParams = request.queryString();
			if(!queryStringParams.keySet().isEmpty())
			{
				String[] params = new String[queryStringParams.keySet().size()];
				int index = 0;
				for(String key: queryStringParams.keySet())
				{
					String[] values = queryStringParams.get(key);
					if(values.length == 1)
					{
						params[index++] = key + "=" + values[0];
					}
				}

				url += "?" + String.join("&", params);
			}

			return apiClient.put(url, payload, headers).thenApplyAsync(response -> {
				Date endTime = Utils.getCurrentDate();

				Response apiResponse = new Response();
				apiResponse.setBody(response.getBody());
				apiResponse.setStatus(response.getStatus());
				apiResponse.setHeaders(Json.toJson(response.getHeaders()));
				apiResponse.setDuration(endTime.getTime() - startTime.getTime());

				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, path, "PUT", request, headers, apiResponse));

				return status(response.getStatus(), response.asJson());
			}).exceptionally(exception -> {
				Response apiResponse = new Response();
				apiResponse.setBody(exception.getMessage());
				apiResponse.setStatus(0);
				apiResponse.setHeaders(Json.toJson(new HashMap<>()));
				apiResponse.setDuration(0);
				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, path, "PUT", request, headers, apiResponse));

				return ok("Error");
			});
		}

		return CompletableFuture.supplyAsync(() -> {
			return ok("Error");
		});
	}

	public CompletionStage<Result> delete(String path, Http.Request request)
	{
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
			String url = "http://" + host + ":" + port + "/" + path;

			Map<String, String[]> queryStringParams = request.queryString();
			if(!queryStringParams.keySet().isEmpty())
			{
				String[] params = new String[queryStringParams.keySet().size()];
				int index = 0;
				for(String key: queryStringParams.keySet())
				{
					String[] values = queryStringParams.get(key);
					if(values.length == 1)
					{
						params[index++] = key + "=" + values[0];
					}
				}

				url += "?" + String.join("&", params);
			}

			return apiClient.delete(url, headers).thenApplyAsync(response -> {
				Date endTime = Utils.getCurrentDate();

				Response apiResponse = new Response();
				apiResponse.setBody(response.getBody());
				apiResponse.setStatus(response.getStatus());
				apiResponse.setHeaders(Json.toJson(response.getHeaders()));
				apiResponse.setDuration(endTime.getTime() - startTime.getTime());

				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, path, "DELETE", request, headers, apiResponse));

				return status(response.getStatus(), response.asJson());
			}).exceptionally(exception -> {
				Response apiResponse = new Response();
				apiResponse.setBody(exception.getMessage());
				apiResponse.setStatus(0);
				apiResponse.setHeaders(Json.toJson(new HashMap<>()));
				apiResponse.setDuration(0);
				CompletableFuture.supplyAsync(() -> this.logRepository.saveRequest(host, port, path, "DELETE", request, headers, apiResponse));

				return ok("Error");
			});
		}

		return CompletableFuture.supplyAsync(() -> {
			return ok("Error");
		});
	}

    public CompletionStage<Result> test(Http.Request request)
    {
        Map<String, Object> payload = new HashMap<>();
        payload.put("source", "Desktop");
        payload.put("caller", "Browse");

        String url = "http://localhost:9032/search-ads";
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("X-Quikr-Client", Collections.singletonList("jobs"));

        return apiClient.post(url, Json.toJson(payload), headers).thenApplyAsync(response -> {
            return status(response.getStatus(), response.asJson());
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