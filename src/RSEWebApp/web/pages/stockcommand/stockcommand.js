function resetForm() {
    $("#symbol").val('');
    $("#type-dropdown").val('LMT');
    $("#number").val('');
    $("#amount").val('');
    $("#direction-dropdown").val('Buying');
}

$(function() { // onload...do
    //add a function to the submit event
    $("#command-form").submit(function() {
        $.ajax({
            type: 'GET',
            dataType: "json",
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            error: function(errorObject) {
                var errorJSON = errorObject.responseJSON;

                alert("Error status " + errorJSON.status + ": " + errorJSON.message);
            },
            success: function(successMessage) {
                alert(successMessage);
                resetForm();
            }
        });
        return false;
    });
});