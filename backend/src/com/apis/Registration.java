package com.apis;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.User;
import com.utils.JsonUtility;
import com.utils.RequestResponseUtility;
import com.utils.UserUtilities;

/**
 * Servlet implementation class Registration
 */
@WebServlet("/registration")
public class Registration extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final JsonUtility jsonUtil = new JsonUtility();
	private UserUtilities userUtil = new UserUtilities();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Converting JSON present in the request body to map
			Map<String, String> responseBody = RequestResponseUtility.getRequestBody(request, jsonUtil);

			// Begin: Validating required parameters in map
			String[] requiredParams = { "firstname", "lastname", "email", "password" };
			List<String> userExceptions = RequestResponseUtility.validateRequiredBodyParams(responseBody,
					(List<String>) Arrays.asList(requiredParams));
			if (!userExceptions.isEmpty()) {
				String errorJson = RequestResponseUtility.logAndBuildJsonOfUserException(this.getClass().getName(),
						"doPost", userExceptions);
				RequestResponseUtility.buildErrorResponse(request, response, errorJson);
				return;
			}
			// End Validation

			String firstname = responseBody.get("firstname");
			String lastname = responseBody.get("lastname");
			String email = responseBody.get("email");
			String password = responseBody.get("password");

			// Begin Validating whether this user with email already exists or not
			User user = userUtil.getUserByEmail(email);
			if (user != null) {
				userExceptions.add("User already exist with email " + email);
				String errorJson = RequestResponseUtility.logAndBuildJsonOfUserException(this.getClass().getName(),
						"doPost", userExceptions);
				RequestResponseUtility.buildErrorResponse(request, response, errorJson);
				return;
			}
			// End Validation

			// Creating user if all validations were passed
			user = userUtil.addUser(firstname, lastname, email, password);
			String responseJson = jsonUtil.convertToJson("user", user);
			if (user != null) {
				RequestResponseUtility.buildSuccessfulResponse(request, response, responseJson);
			} else {
				userExceptions.add("User was not created due to some issue. Please contact website");
				Map<String, String> normalProperties = new HashMap<String, String>();
				normalProperties.put("errors", jsonUtil.convertStringsTOJsonArray(userExceptions));
				Map<String, Object> objects = new HashMap<>();
				objects.put("user", user);
				String errorJson = jsonUtil.convertToJson(normalProperties, objects);
				RequestResponseUtility.buildErrorResponse(request, response, errorJson);
			}
		} catch (Exception e) {
			String errorJson = "{\"error\": \"Exception occurred in post method of registration API and erroe was " + e
					+ "\"}";
			RequestResponseUtility.buildErrorResponse(request, response, errorJson);
		}
	}

}
