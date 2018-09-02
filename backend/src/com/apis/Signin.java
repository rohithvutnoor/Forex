package com.apis;

import java.io.IOException;
import java.util.Arrays;
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
 * Servlet implementation class Signin
 */
@WebServlet("/signin")
public class Signin extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private UserUtilities userUtil = new UserUtilities();
	private static final JsonUtility jsonUtil = new JsonUtility();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Signin() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Map<String, String> responseBody = RequestResponseUtility.getRequestBody(request, jsonUtil);
			// Begin: Validating required parameters in map
			String[] requiredParams = { "email", "password" };
			List<String> userExceptions = RequestResponseUtility.validateRequiredBodyParams(responseBody,
					(List<String>) Arrays.asList(requiredParams));
			if (!userExceptions.isEmpty()) {
				String errorJson = RequestResponseUtility.logAndBuildJsonOfUserException(this.getClass().getName(),
						"doPost", userExceptions);
				RequestResponseUtility.buildErrorResponse(request, response, errorJson);
				return;
			}
			// End Validation
			String email = responseBody.get("email");
			String password = responseBody.get("password");
			// Validating user with email and password
			if (userUtil.validateUser(email, password)) {
				// If user validated successfully user will be sent with the token as response
				User user = userUtil.getUserByEmail(email);
				userUtil.updateToken(user);
				String responseJson = jsonUtil.convertToJson("user", user);
				RequestResponseUtility.buildSuccessfulResponse(request, response, responseJson);
			} else {
				// else error response will be sent which will state user didn't validated with
				// given credentials
				userExceptions.add("User doesn't exist with given credentials. Please give valid credentials.");
				String errorJson = jsonUtil.convertToJson("errors", userExceptions);
				RequestResponseUtility.buildErrorResponse(request, response, errorJson);
			}
		} catch (Exception e) {
			// If any run time exception occurs error response will be sent
			String errorJson = RequestResponseUtility.logAndBuildJsonOfSystemException(this.getClass().getName(),
					"doPost", e);
			RequestResponseUtility.buildErrorResponse(request, response, errorJson);
		}
	}

}
