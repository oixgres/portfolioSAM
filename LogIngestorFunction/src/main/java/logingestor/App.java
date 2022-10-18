package logingestor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

// generic utility class
import com.fasterxml.jackson.databind.ObjectMapper;

// AMAZON IMPORTS
/* ***** Lambda ***** */
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent , APIGatewayProxyResponseEvent> {

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Headers", "Content-Type");
        headers.put("Access-Control-Allow-Methods", "OPTIONS,POST");
        headers.put("X-Custom-Header", "application/json");


        try {
            String inputBody = input.getBody();
            ObjectMapper mapper = new ObjectMapper();
            VisitLog log = mapper.readValue(inputBody, VisitLog.class);
            DBManager db = new DBManager();

            db.insertRecord(log);

            String output = String.format("{ \"message\": \"Success\" }");
            return new APIGatewayProxyResponseEvent().withHeaders(headers).withBody(output).withStatusCode(200);
        } catch (IOException e) {
            return new APIGatewayProxyResponseEvent().withHeaders(headers).withBody(e.getMessage()).withStatusCode(400);
        } catch (SQLException e) {
            return new APIGatewayProxyResponseEvent().withHeaders(headers).withBody(e.getMessage()).withStatusCode(400);
        }
    }
}
