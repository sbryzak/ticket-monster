
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

Date.prototype.toCalendarDate = function() {
    return { 'day': this.getDay(), 'month': this.getMonth(), 'year': this.getFullYear()};
}

Date.prototype.toTimeOfDay = function() {
    return { 'hours': this.getHours(), 'minutes': this.getMinutes(),
        'seconds':this.getSeconds(), 'milliseconds': this.getMilliseconds()};
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
    return target.empty().append(renderTemplate(template, data))
}





