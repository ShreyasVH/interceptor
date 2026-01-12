package repositories;

import exceptions.DBInteractionException;
import models.Request;
import models.Response;

import com.google.inject.Inject;
import modules.DatabaseExecutionContext;

import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

import play.db.jpa.JPAApi;
import play.libs.Json;
import play.mvc.Http;

public class LogRepository
{
    private final JPAApi jpaApi;
    private final DatabaseExecutionContext databaseExecutionContext;

    @Inject
    public LogRepository(JPAApi jpaApi, DatabaseExecutionContext databaseExecutionContext) {
        this.jpaApi = jpaApi;
        this.databaseExecutionContext = databaseExecutionContext;
    }

    public CompletionStage<Request> saveRequest(String host, String port, String path, String method, Http.Request request, Map requestHeaders, Response response)
    {
        return CompletableFuture.supplyAsync(() -> {
            Request apiRequest = new Request();
            apiRequest.setHost(host);
            apiRequest.setPort(port);
            apiRequest.setPath(path);
            apiRequest.setMethod(method);
            apiRequest.setPayload(request.hasBody() ? request.body().asJson().toString(): "");
            apiRequest.setHeaders(Json.toJson(requestHeaders).toString());

            try
            {
                apiRequest.setResponse(response);

                return jpaApi.withTransaction(em -> {
                    em.persist(apiRequest);
                    return apiRequest;
                });
            }
            catch(Exception ex)
            {
                String message = "DB Interaction Failed. Exception: " + ex;
                throw new DBInteractionException(400, message);
            }
        }, this.databaseExecutionContext);
    }

    public void clear()
    {
        try
        {
            jpaApi.withTransaction(em -> {
                em.createQuery("DELETE FROM Request").executeUpdate();
                em.createQuery("DELETE FROM Response").executeUpdate();
            });
        }
        catch(Exception ex)
        {
            String message = "DB Interaction Failed. Exception: " + ex;
            throw new DBInteractionException(400, message);
        }
    }
}
