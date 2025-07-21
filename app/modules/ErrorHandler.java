package modules;

import exceptions.MyException;
import play.http.HttpErrorHandler;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Singleton;

@Singleton
public class ErrorHandler implements HttpErrorHandler {
    public CompletionStage<Result> onClientError(RequestHeader request, int statusCode, String message) {
        System.out.println("Client error");
        System.out.println(message);
        System.out.println(statusCode);
        System.out.println(request.method());
        System.out.println(request.host());
        System.out.println(request.path());
        System.out.println(request.queryString());
        return CompletableFuture.completedFuture(Results.status(statusCode, "A client error occurred: " + message));
    }

    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        Integer httpsStatusCode = 500;
        String content = exception.getMessage();
        Integer errorCode = 5000;

        if((exception instanceof MyException) || (exception.getCause() instanceof MyException))
        {
            MyException myException;
            if(exception instanceof MyException)
            {
                myException = (MyException) exception;
            }
            else
            {
                myException = (MyException) exception.getCause();
            }

            httpsStatusCode = myException.getHttpStatusCode();
            content = myException.getDescription();
            errorCode = myException.getCode();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("code", errorCode);
        response.put("description", content);

        System.out.println("Client error");
        System.out.println(exception.getMessage());
        System.out.println(errorCode);
        System.out.println(content);
        System.out.println(request.method());
        System.out.println(request.host());
        System.out.println(request.path());
        System.out.println(request.queryString());

        return CompletableFuture.completedFuture(Results.status(httpsStatusCode, Json.toJson(response)));
    }
}
