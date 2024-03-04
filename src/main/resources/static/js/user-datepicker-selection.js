$(document).ready(function(){
    // Initialize the datepicker
    $('.input-daterange').datepicker({
        format: 'dd-mm-yyyy',
        autoclose: true,
        calendarWeeks : true,
        clearBtn: true,
        disableTouchKeyboard: true
    });
});

$('#datepicker-submit').click(function(event) {
    event.preventDefault();

    // Get the selected start and end dates
    var startDate = $('#start').val();
    var endDate = $('#end').val();

    // Check if start and end dates are empty
    if (startDate === '' || endDate === '') {
        startDate = null;
        endDate = null;
    } else {
        // Convert the dates to the format expected by the server
        startDate = moment(startDate, 'DD-MM-YYYY').format('YYYY-MM-DD');
        endDate = moment(endDate, 'DD-MM-YYYY').format('YYYY-MM-DD');
    }

    // Construct the URL with the formatted dates as query parameters
    var url = '/user/book-index?';

    // Add start date parameter if not null
    if (startDate !== null) {
        url += 'startDate=' + startDate + '&';
    }

    // Add end date parameter if not null
    if (endDate !== null) {
        url += 'endDate=' + endDate;
    }

    // Redirect to the URL
    window.location.href = url;
});