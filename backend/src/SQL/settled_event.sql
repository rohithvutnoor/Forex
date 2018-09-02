CREATE EVENT change_status_from_executed_settled 
  ON SCHEDULE EVERY 1 DAY STARTS '2018-08-30 16:00:00' DO 
   UPDATE forex.orders SET status = "SETTLED" WHERE
	status = "EXECUTED" AND DATEDIFF(now(),ordered_date)=2;