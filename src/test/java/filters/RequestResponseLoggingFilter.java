package filters;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import utils.LogHelper;

public class RequestResponseLoggingFilter implements Filter {

	
	@Override
	public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
			FilterContext context) {

		Response response = context.next(requestSpec, responseSpec);
		
		LogHelper.info("Request URL: " + requestSpec.getBaseUri() + requestSpec.getUserDefinedPath());
		LogHelper.info("Request Body: " + requestSpec.getBody());
		
		// Skip logging response for "Get All Users" request - since the response is big
        if (requestSpec.getMethod().equalsIgnoreCase("GET") && requestSpec.getUserDefinedPath().contains("uap/users")) {
            return response;
        }
		
		LogHelper.info("Response: " + response.getBody().asPrettyString());
		return response;
	}

}
