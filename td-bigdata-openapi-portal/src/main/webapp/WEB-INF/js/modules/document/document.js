jQuery(document).ready(function() {
	$('#init_tab').css('background-color', '#3598DC');
});
function docBtnClick(self) {
	$(self).parent().children().css('background-color', '#3fc9d5');
	$(self).css('background-color', '#3598DC');
}