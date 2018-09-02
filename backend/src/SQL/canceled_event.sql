CREATE EVENT change_status_from_opened_canceled 
  ON SCHEDULE EVERY 1 DAY STARTS '2018-08-30 16:00:00' DO 
   UPDATE forex.orders SET status = "CANCELED" WHERE
	status = "OPENED";