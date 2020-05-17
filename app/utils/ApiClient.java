package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;

import java.util.List;
import java.util.concurrent.CompletionStage;
import play.libs.ws.*;
import java.util.Map;
import java.util.Set;
import play.libs.Json;

public class ApiClient implements WSBodyReadables, WSBodyWritables
{
	private final WSClient client;

	@Inject
	public ApiClient(WSClient client)
	{
		this.client = client;
	}

	public CompletionStage<WSResponse> get(String url, Map<String, List<String>> additionalHeaders)
	{
		WSRequest request = this.client.url(url);

		for(String key: additionalHeaders.keySet())
		{
			if(null != additionalHeaders.get(key))
			{
				List<String> values = additionalHeaders.get(key);
				request.addHeader(key, String.join(";", values));
			}
		}

		return request.get();
	}

	public CompletionStage<WSResponse> post(String url, JsonNode payload, Map<String, List<String>> additionalHeaders)
	{
		WSRequest request = this.client.url(url);

		for(String key: additionalHeaders.keySet())
		{
			if(null != additionalHeaders.get(key))
			{
				List<String> values = additionalHeaders.get(key);
				request.addHeader(key, String.join(";", values));
			}
		}

		return request.post(payload);
	}

	public CompletionStage<WSResponse> put(String url, Map payload, Map<String, List<String>> additionalHeaders)
	{
		WSRequest request = this.client.url(url);

		for(String key: additionalHeaders.keySet())
		{
			if(null != additionalHeaders.get(key))
			{
				List<String> values = additionalHeaders.get(key);
				request.addHeader(key, String.join(";", values));
			}
		}

		return request.put(Json.toJson(payload));
	}

	public CompletionStage<WSResponse> delete(String url, Map<String, List<String>> additionalHeaders)
	{
		WSRequest request = this.client.url(url);

		for(String key: additionalHeaders.keySet())
		{
			if(null != additionalHeaders.get(key))
			{
				List<String> values = additionalHeaders.get(key);
				request.addHeader(key, String.join(";", values));
			}
		}

		return request.delete();
	}
}