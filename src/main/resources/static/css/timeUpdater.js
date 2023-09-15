function timeDifference(current, previous) {
    const elapsed = current - previous;
    const seconds = Math.floor(elapsed / 1000);

    if (seconds < 60) {
        return seconds === 1 ? "1 second ago" : seconds + " seconds ago";
    } else if (seconds < 3600) {
        const minutes = Math.floor(seconds / 60);
        return minutes === 1 ? "1 minute ago" : minutes + " minutes ago";
    } else if (seconds < 86400) {
        const hours = Math.floor(seconds / 3600);
        return hours === 1 ? "1 hour ago" : hours + " hours ago";
    } else if (seconds < 2592000) { // 30 days
        const days = Math.floor(seconds / 86400);
        return days === 1 ? "1 day ago" : days + " days ago";
    } else if (seconds < 31536000) { // 365 days
        const months = Math.floor(seconds / 2592000);
        return months === 1 ? "1 month ago" : months + " months ago";
    } else {
        const years = Math.floor(seconds / 31536000);
        return years === 1 ? "1 year ago" : years + " years ago";
    }
}

// Add an event listener when the DOM is ready
document.addEventListener("DOMContentLoaded", function () {
    const submissionTimeElements = document.querySelectorAll(".submission-time");

    submissionTimeElements.forEach(function (element) {
        const timestamp = parseInt(element.getAttribute("data-timestamp"));

        // Ensure that the timestamp is a valid number
        if (!isNaN(timestamp)) {
            const timeAgo = timeDifference(new Date(), new Date(timestamp));
            element.textContent = timeAgo;
        } else {
            // Handle the case where the timestamp is not valid
            element.textContent = "Invalid timestamp";
        }
    });
});
