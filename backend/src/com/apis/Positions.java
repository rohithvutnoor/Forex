package com.apis;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.Order;
import com.models.User;
import com.utils.JsonUtility;
import com.utils.OrdersUtility;
import com.utils.RequestResponseUtility;

/**
 * Servlet implementation class Postions
 */
@WebServlet("/positions")
public class Positions extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static JsonUtility jsonUtility = new JsonUtility();
	private static OrdersUtility orderUtility = new OrdersUtility();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Positions() {
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
			String[] requiredParams = { "currency_pair_id", "ordered_date" };
			List<String> userExceptions = RequestResponseUtility.validateRequiredQureuParams(request,
					(List<String>) Arrays.asList(requiredParams));
			if (!userExceptions.isEmpty()) {
				String errorJson = RequestResponseUtility.logAndBuildJsonOfUserException(this.getClass().getName(),
						"doPost", userExceptions);
				RequestResponseUtility.buildErrorResponse(request, response, errorJson);
				return;
			}
			Long dateInMilliSeconds = Long.parseLong(request.getParameter("ordered_date"));
			Date orderDate = new Date(dateInMilliSeconds);
			Integer currencyPairId = Integer.parseInt(request.getParameter("currency_pair_id"));
			List<Order> orders = orderUtility.getAllOrders(user, currencyPairId, orderDate);
			Map<String, String> positions = orderUtility.positions(user, currencyPairId, orderDate);
			Map<String, Object> objects = new HashMap<>();
			objects.put("orders", orders);
			String responseJson = jsonUtility.convertToJson(positions, objects);
			RequestResponseUtility.buildSuccessfulResponse(request, response, responseJson);
		} catch (Exception e) {
			String errorJson = RequestResponseUtility.logAndBuildJsonOfSystemException(this.getClass().getName(),
					"doPost", e);
			RequestResponseUtility.buildErrorResponse(request, response, errorJson);
		}
	}

}
