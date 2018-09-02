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

import com.utils.JsonUtility;
import com.utils.RequestResponseUtility;

/**
 * Servlet implementation class UserAPI
 */
@WebServlet("/user")
public class UserAPI extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static JsonUtility jsonUtility = new JsonUtility();

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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			Map<String, String> requestBody = RequestResponseUtility.getRequestBody(request, jsonUtility);
			String[] optionalParams = { "firstname", "lastname", "password" };
			List<String> errors = RequestResponseUtility.validateOptionalBodyParams(requestBody,
					Arrays.asList(optionalParams));
			if (!errors.isEmpty()) {
				String errorJson = jsonUtility.convertToJson("errors", errors);
				RequestResponseUtility.buildErrorResponse(request, response, errorJson);
				return;
			}
		} catch (Exception e) {

		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
