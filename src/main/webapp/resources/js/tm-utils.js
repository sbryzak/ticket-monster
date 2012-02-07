
var dayNames = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
var monthNames = ["January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"];

Date.prototype.toPrettyString = function () {
    return dayNames[this.getDay()] + " " +
            this.getDate() + " " +
            monthNames[this.getMonth()] + " " +
            this.getFullYear() + " @ " +
            this.getHours().toZeroPaddedString(2) + ":" +
            this.getMinutes().toZeroPaddedString(2);
}

Number.prototype.toZeroPaddedString = function(digits) {
    val = this + "";
    while (val.length < digits) val = "0" + val;
    return val;
}

function renderTemplate(template, data) {
    return _.template(template.html(), (data == undefined) ? {} : data);
}
function applyTemplate(target, template, data) {
    return target.html(renderTemplate(template, data))
}



