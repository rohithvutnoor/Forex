package com.apis;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.CurrencyPairPrice;
import com.models.User;
import com.utils.CurrencyPairPriceUtility;
import com.utils.JsonUtility;
import com.utils.RequestResponseUtility;

/**
 * Servlet implementation class CurrencyPairPriceAPI
 */
@WebServlet("/currency-pair-price")
public class CurrencyPairPriceAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static JsonUtility jsonUtility = new JsonUtility();
	private static CurrencyPairPriceUtility currencyPairPriceUtility = new CurrencyPairPriceUtility();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CurrencyPairPriceAPI() {
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
			String[] requiredParams = { "date" };
			List<String> userExceptions = RequestResponseUtility.validateRequiredQureuParams(request,
					(List<String>) Arrays.asList(requiredParams));
			if (!userExceptions.isEmpty()) {
				String errorJson = RequestResponseUtility.logAndBuildJsonOfUserException(this.getClass().getName(),
						"doPost", userExceptions);
				RequestResponseUtility.buildErrorResponse(request, response, errorJson);
				return;
			}
			Long dateInMilliSeconds = Long.parseLong(request.getParameter("date"));
			String id = request.getParameter("id");
			Date date = new Date(dateInMilliSeconds);
			if (id == null) {
				List<CurrencyPairPrice> currencyPairPrices = currencyPairPriceUtility.getList(date);
				String responseJson = jsonUtility.convertToJson("currency_pair_prices", currencyPairPrices);
				RequestResponseUtility.buildSuccessfulResponse(request, response, responseJson);
			} else {
				CurrencyPairPrice currencyPairPrice = currencyPairPriceUtility
						.getCurrencyPairPrice(Integer.parseInt(id));
				String responseJson = jsonUtility.convertToJson("currency_pair_price", currencyPairPrice);
				RequestResponseUtility.buildSuccessfulResponse(request, response, responseJson);
			}
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
			String[] requiredParams = { "currency_pair_id", "on_date", "selling_price", "buying_price" };
			List<String> errors = RequestResponseUtility.validateRequiredBodyParams(requestBody,
					Arrays.asList(requiredParams));
			if (!errors.isEmpty()) {
				String errorJson = jsonUtility.convertToJson("errors", errors);
				RequestResponseUtility.buildErrorResponse(request, response, errorJson);
				return;
			} else {
				Integer currencyPairId = Integer.parseInt(requestBody.get("currency_pair_id"));
				Date onDate = new Date(Long.parseLong(requestBody.get("on_date")));
				Double sellingPrice = Double.parseDouble(requestBody.get("selling_price"));
				Double buyingPrice = Double.parseDouble(requestBody.get("buying_price"));
				CurrencyPairPrice currencyPairPrice = currencyPairPriceUtility.addCurrencyPairPrice(currencyPairId,
						onDate, sellingPrice, buyingPrice);
				String responseJson = jsonUtility.convertToJson("currency_pair", currencyPairPrice);
				RequestResponseUtility.buildSuccessfulResponse(request, response, responseJson);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String errorJson = RequestResponseUtility.logAndBuildJsonOfSystemException(this.getClass().getName(),
					"doPost", e);
			RequestResponseUtility.buildErrorResponse(request, response, errorJson);
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}
}
