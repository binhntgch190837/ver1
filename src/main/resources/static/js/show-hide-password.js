var input = $("#show_hide_password input");
var icon = $("#show_hide_password i");

icon.on("click", function (event) {
event.preventDefault();

if (input.attr("type") === "text") {
  input.attr("type", "password");
  icon.addClass("fa-eye-slash");
  icon.removeClass("fa-eye");
} else if (input.attr("type") === "password") {
  input.attr("type", "text");
  icon.removeClass("fa-eye-slash");
  icon.addClass("fa-eye");
}
});