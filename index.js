 $("#login-button").click(function(event){
		 event.preventDefault();

	 $('form').fadeOut(500);
	 $('.wrapper').addClass('form-success');
});

$("#signup-button").click(function(event){
    event.preventDefault();

  $('form').fadeOut(500);
  $('.wrapper').addClass('form-success');
});
