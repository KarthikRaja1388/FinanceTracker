// JavaScript Document
$(function(){
	$('.navToggle').on('click',function(){
		$('#navContainer').toggleClass('slideMenu');
	});	
});


function validate(){
	var pwd = $('#password').val();
	var cpwd = $('#cpassword').val();
	
	if(cpwd != pwd){
		$('#cpasswordError').text("* password doesn't match");
	}
}


