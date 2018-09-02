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
import com.models.OrderMapping;
import com.models.OrderStatus;
import com.models.OrderType;
import com.models.User;
import com.utils.JsonUtility;
import com.utils.OrdersUtility;
import com.utils.RequestResponseUtility;

@WebServlet("/buy")
public class BuyAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final JsonUtility jsonUtility = new JsonUtility();
	private OrdersUtility orderUtil = new OrdersUtility();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

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
			String[] requiredParams = { "currency_pair_id", "notional_amount" };
			List<String> errors = RequestResponseUtility.validateOptionalBodyParams(requestBody,
					Arrays.asList(requiredParams));
			if (!errors.isEmpty()) {
				String errorJson = jsonUtility.convertToJson("errors", errors);
				RequestResponseUtility.buildErrorResponse(request, response, errorJson);
				return;
			} else {
				Integer currencyPair_id = Integer.parseInt(requestBody.get("currency_pair_id"));
				Double notional_amount = Double.parseDouble(requestBody.get("notional_amount"));

				Order order = orderUtil.addOrder(notional_amount, new Date(System.currentTimeMillis()),
						OrderStatus.OPENED, OrderType.BUY, currencyPair_id, user.getId());
				OrderMapping orderMapping = orderUtil.executeOrCancelBuyOrder(order);
				Map<String, String> normalProperties = new HashMap<>();
				Map<String, Object> objects = new HashMap<>();
				objects.put("order", order);
				objects.put("order_mapping", orderMapping);
				String responseJson = jsonUtility.convertToJson(normalProperties, objects);
				RequestResponseUtility.buildSuccessfulResponse(request, response, responseJson);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String errorJson = RequestResponseUtility.logAndBuildJsonOfSystemException(this.getClass().getName(),
					"doPost", e);
			RequestResponseUtility.buildErrorResponse(request, response, errorJson);
		}

	}
}
