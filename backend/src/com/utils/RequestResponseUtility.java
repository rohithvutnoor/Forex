package com.utils;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.User;

public class RequestResponseUtility {

	/*
	 * This method will return a map consist of parameters present in body
	 */
	public static Map<String, String> getRequestBody(HttpServletRequest request, JsonUtility jsonUtil) {
		try {
			String jsonBody = "";
			String line = "";
			BufferedReader br = request.getReader();
			while ((line = br.readLine()) != null) {
				jsonBody += line;
			}
			return jsonUtil.getJson(jsonBody);
		} catch (Exception e) {
			System.out.println("Following exception occurred at method getRequestBody in class "
					+ RequestResponseUtility.class.getName() + ": " + e);
		}
		return new HashMap<String, String>();
	}

	/*
	 * This method will build successful response in response servlet
	 */
	public static void buildSuccessfulResponse(HttpServletRequest request, HttpServletResponse response,
			String responseJson) {
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
			out.println(responseJson);
		} catch (Exception e) {
			System.out.println("Following exception occurred at method buildSuccessfulResponse in class "
					+ RequestResponseUtility.class.getName() + ": " + e);
		}
	}

	/*
	 * This method will build error response in response servlet
	 */
	public static void buildErrorResponse(HttpServletRequest request, HttpServletResponse response, String errorJson) {
		try {
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.println(errorJson);
		} catch (Exception e) {
			System.out.println("Following exception occurred at method buildErrorResponse in class "
					+ RequestResponseUtility.class.getName() + ": " + e);
		}
	}

	/*
	 * This method logs the error and creates a JSON string with system exception
	 * send in attributes
	 */
	public static String logAndBuildJsonOfSystemException(String className, String methodName, Exception exception) {
		System.out.println(
				"Following error was occurred at " + methodName + " method in class " + className + " : " + exception);
		String errorJson = "{\"error\": \"Following exception occurred " + exception + ". Please report to website.\"}";
		return errorJson;
	}

	/*
	 * This method logs the error and creates a JSON string with array of user
	 * errors send in attributes
	 */
	public static String logAndBuildJsonOfUserException(String className, String methodName,
			List<String> errorMessages) {
		System.out.println("Following error was occurred at " + methodName + " method in class " + className + " : "
				+ errorMessages);
		String errorJson = "{\"errors\": [";
		for (String errorMessage : errorMessages) {
			errorJson += "\"" + errorMessage + "\",";
		}
		errorJson = errorJson.substring(0, errorJson.length() - 1);
		System.out.println(errorJson);
		errorJson += "]}";
		return errorJson;
	}

	/*
	 * This method takes required parameters list and checks whether all required
	 * parameters were present in query params are not. If they were present it
	 * returns empty list of errors, otherwise sends a list of errors where each and
	 * every error states that particular parameter was required
	 */
	public static List<String> validateRequiredQureuParams(HttpServletRequest request, List<String> requiredParams) {
		List<String> errors = new ArrayList<>();
		for (String requiredParam : requiredParams) {
			String value = request.getParameter(requiredParam);
			if (value == null || value.equals("")) {
				errors.add("Please provide " + requiredParam);
			}
		}
		return errors;
	}

	/*
	 * This method takes required parameters list and checks whether all required
	 * parameters were present in body params are not. If they were present it
	 * returns empty list of errors, otherwise sends a list of errors where each and
	 * every error states that particular parameter was required
	 */
	public static List<String> validateRequiredBodyParams(Map<String, String> bodyParams, List<String> requiredParams) {
		List<String> errors = new ArrayList<>();
		for (String requiredParam : requiredParams) {
			String value = bodyParams.get(requiredParam);
			if (value == null || value.equals("")) {
				errors.add("Please provide " + requiredParam);
			}
		}
		return errors;
	}

	/*
	 * This method takes required parameters list and checks whether all required
	 * parameters were present in body params are not. If they were present it
	 * returns empty list of errors, otherwise sends a list of errors where each and
	 * every error states that particular parameter was required
	 */
	public static List<String> validateOptionalBodyParams(Map<String, String> bodyParams, List<String> optionalParams) {
		List<String> errors = new ArrayList<>();
		if (bodyParams.isEmpty())
			errors.add("Atleast one parmameter should be given.");
		else {
			for (String optionalParam : optionalParams) {
				String value = bodyParams.get(optionalParam);
				if (value != null && value == "") {
					errors.add("Please provide value in " + optionalParam);
				}
			}
		}
		return errors;
	}

	public static User authorizeUser(HttpServletRequest request) {
		UserUtilities userutility = new UserUtilities();
		String[] authorizationToken = request.getHeader("Authorization").split(" ");
		User user = userutility.getUserByEmail(authorizationToken[authorizationToken.length - 1]);
		return user;
	}

}
