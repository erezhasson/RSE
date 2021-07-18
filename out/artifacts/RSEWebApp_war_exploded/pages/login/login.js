
$(function() {
    $("#loginForm").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            error: function(errorObject) {
                console.log(errorObject);
                var errorContainer = $("#error-message");

                errorContainer.text(errorObject.responseText);
            },
            success: function(nextPageUrl) {
                window.location.replace(nextPageUrl);
            }
        });

        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
});