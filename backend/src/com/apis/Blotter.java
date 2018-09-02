package com.apis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.Order;
import com.models.OrderStatus;
import com.models.OrderType;
import com.models.User;
import com.utils.JsonUtility;
import com.utils.OrdersUtility;
import com.utils.RequestResponseUtility;

/**
 * Servlet implementation class Blotter
 */
@WebServlet("/blotter")
public class Blotter extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final JsonUtility jsonUtility = new JsonUtility();
	private static final OrdersUtility ordersUtility = new OrdersUtility();

	/**
	 * Get all orders of a user based on filters which were sent in parameters
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
			String[] requiredParams = { "blotter_type" };
			List<String> errors = RequestResponseUtility.validateRequiredQureuParams(request,
					Arrays.asList(requiredParams));
			if (!errors.isEmpty()) {
				String errorJson = jsonUtility.convertToJson("errors", errors);
				RequestResponseUtility.buildErrorResponse(request, response, errorJson);
				return;
			} else {
				String blotterType = request.getParameter("blotter_type");
				String dateInMilliSecFilter = request.getParameter("date");
				String orderTypeFilter = request.getParameter("order_type");
				List<OrderStatus> orderStatuses = new ArrayList<>();
				if (blotterType.toLowerCase().equals("order")) {
					orderStatuses.add(OrderStatus.CANCELED);
					orderStatuses.add(OrderStatus.OPENED);
					orderStatuses.add(OrderStatus.EXECUTED);
				} else if (blotterType.toLowerCase().equals("trade")) {
					orderStatuses.add(OrderStatus.SETTLED);
				} else {
					String errorJson = jsonUtility.convertToJson("errors",
							errors.add("Please provide valid blotter type. It's either order or trade"));
					RequestResponseUtility.buildErrorResponse(request, response, errorJson);
					return;
				}
				OrderType orderType = null;
				if (orderTypeFilter != null && !orderTypeFilter.equals("")) {
					if (orderTypeFilter.toLowerCase().equals(OrderType.BUY.toString().toLowerCase())) {
						orderType = OrderType.BUY;
					} else if (orderTypeFilter.toLowerCase().equals(OrderType.SELL.toString().toLowerCase())) {
						orderType = OrderType.SELL;
					} else {
						String errorJson = jsonUtility.convertToJson("errors",
								errors.add("Please provide valid order type. It's either buy or sell"));
						RequestResponseUtility.buildErrorResponse(request, response, errorJson);
						return;
					}
				}
				List<Order> orders = ordersUtility.getOrders(user, orderStatuses, dateInMilliSecFilter, orderType);
				String responseJson = jsonUtility.convertToJson("orders", orders);
				RequestResponseUtility.buildSuccessfulResponse(request, response, responseJson);
			}
		} catch (Exception e) {
			String errorJson = RequestResponseUtility.logAndBuildJsonOfSystemException(this.getClass().getName(),
					"doPost", e);
			RequestResponseUtility.buildErrorResponse(request, response, errorJson);
		}
	}

}
