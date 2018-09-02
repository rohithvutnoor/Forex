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

import com.models.CurrencyPair;
import com.models.User;
import com.utils.CurrencyPairUtility;
import com.utils.JsonUtility;
import com.utils.RequestResponseUtility;

/**
 * Servlet implementation class CurrencyPairAPI
 */
@WebServlet("/currency-pair")
public class CurrencyPairAPI extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static JsonUtility jsonUtility = new JsonUtility();
	private static CurrencyPairUtility currencyPairUtility = new CurrencyPairUtility();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CurrencyPairAPI() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			User user = RequestResponseUtility.authorizeUser(request);
			if (user == null) {
				String errorJson = jsonUtility.convertToJson("errors", "Please give valid credentials");
				RequestResponseUtility.buildErrorResponse(request, response, errorJson);
				return;
			}
			List<CurrencyPair> currencyPairs = currencyPairUtility.getListOfCurrencyPair();
			String responseJson = jsonUtility.convertToJson("currency_pairs", currencyPairs);
			RequestResponseUtility.buildSuccessfulResponse(request, response, responseJson);

		} catch (Exception e) {
			String errorJson = RequestResponseUtility.logAndBuildJsonOfSystemException(this.getClass().getName(),
					"doPost", e);
			RequestResponseUtility.buildErrorResponse(request, response, errorJson);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			User user = RequestResponseUtility.authorizeUser(request);
			if (user == null) {
				String errorJson = jsonUtility.convertToJson("errors", "Please give valid credentials");
				RequestResponseUtility.buildErrorResponse(request, response, errorJson);
				return;
			}
			Map<String, String> requestBody = RequestResponseUtility.getRequestBody(request, jsonUtility);
			String[] requiredParams = { "name" };
			List<String> errors = RequestResponseUtility.validateOptionalBodyParams(requestBody,
					Arrays.asList(requiredParams));
			if (!errors.isEmpty()) {
				String errorJson = jsonUtility.convertToJson("errors", errors);
				RequestResponseUtility.buildErrorResponse(request, response, errorJson);
				return;
			} else {
				String currencyName = requestBody.get("name");
				CurrencyPair currencyPair = currencyPairUtility.addCurrencyPair(currencyName);
				String responseJson = jsonUtility.convertToJson("currency_pair", currencyPair);
				RequestResponseUtility.buildSuccessfulResponse(request, response, responseJson);
			}
		} catch (Exception e) {
			String errorJson = RequestResponseUtility.logAndBuildJsonOfSystemException(this.getClass().getName(),
					"doPost", e);
			RequestResponseUtility.buildErrorResponse(request, response, errorJson);
		}
	}

}
