
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
    return { 'day': this.getDate(), 'month': this.getMonth(), 'year': this.getFullYear(),
        asString:function(){ return this.year + '-' + (this.month+1).toZeroPaddedString(2) + '-' + this.day.toZeroPaddedString(2)},
        asDate:function(){return new Date(this.year, this.month, this.day,0,0,0,0)}};
}

Date.prototype.asArray = function() {
    return [this.getFullYear(), this.getMonth(), this.getDate()]
}


Date.prototype.toTimeOfDay = function() {
    return { 'hours': this.getHours(), 'minutes': this.getMinutes(),
        'seconds':this.getSeconds(), 'milliseconds': this.getMilliseconds()};
}

Date.prototype.diff = function(other) {
    return parseInt((this.toCalendarDate().asDate().getTime() - other.toCalendarDate().asDate().getTime())/(1000.0 * 60 * 60 * 24))
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





