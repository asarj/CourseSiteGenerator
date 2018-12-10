// DATA TO LOAD
var officeHours;
var startHour;
var endHour;
var undergradTAs;

function buildOfficeHours() {
    var dataFile = "./js/OfficeHoursData.json";
    loadOfficeHoursData(dataFile);
}

function loadOfficeHoursData(jsonFile) {
    $.getJSON(jsonFile, function (json) {
	initDays(json);
        addInstructor(json);
        addTAs(json.grad_tas, $("#grad_tas"));
        addTAs(json.undergrad_tas, $("#undergrad_tas"));
        addOfficeHours(json);
    });
}

function initDays(data) {
    // GET THE START AND END HOURS
    startHour = parseInt(data.startHour);
    endHour = parseInt(data.endHour);

    // THEN MAKE THE TIMES
    daysOfWeek = new Array();
    daysOfWeek[0] = "MONDAY";
    daysOfWeek[1] = "TUESDAY";
    daysOfWeek[2] = "WEDNESDAY";
    daysOfWeek[3] = "THURSDAY";
    daysOfWeek[4] = "FRIDAY";    
}
function addInstructor(data) {
    var instructorData = $("#instructor_data");
    var instructor = data.instructor;
    var text = "<a href='" + instructor.link + "'>" + instructor.name + "</a><br />"
                + "<strong>" + instructor.email + "</strong><br />"
                + instructor.room + "<br />"
                + "<strong>Office Hours:</strong><br />"
                + "<table>";
    for (var i = 0; i < instructor.hours.length; i++) {
        var officeHours = instructor.hours[i];
        text += "<tr><td><strong>--" + officeHours.day + "</td><td class='instructor_time'>" + officeHours.time + "</td></tr>";
    }
    text += "</table>";
    instructorData.append(text);
    
    var instructorPhoto = $("#instructor_photo");
    text = "<img src='" + instructor.photo + "' alt='" + instructor.name + "' + class='instructor_photo' />";
    instructorPhoto.append(text);
}
function addTAs(taArray, node) {
    var tasPerRow = 4;
    var numTAs = taArray.length;
    for (var i = 0; i < taArray.length; ) {
        var text = "";
        text = "<tr>";
        for (var j = 0; j < tasPerRow; j++) {
            text += buildTACell(i, numTAs, taArray[i]);
            i++;
        }
        text += "</tr>";
        node.append(text);
    }
}
function buildTACell(counter, numTAs, ta) {
    if (counter >= numTAs)
        return "<td></td>";

    var name = ta.name;
    var abbrName = name.replace(/\s/g, '');
    var email = ta.email;
    var text = "<td class='tas'><img width='100' height='100'"
                + " src='./images/tas/" + abbrName + ".jpg' "
                + " alt='" + name + "' /><br />"
                + "<strong>" + name + "</strong><br />"
                + "<span class='email'>" + email + "</span><br />"
                + "<br /><br /></td>";
    return text;
}
function addOfficeHours(data) {
    for (var i = startHour; i < endHour; i++) {
        // ON THE HOUR
        var textToAppend = "<tr>";
        var amPm = getAMorPM(i);
        var displayNum = i;
        if (i > 12)
            displayNum = displayNum-12;
        textToAppend += "<td>" + displayNum + ":00" + amPm + "</td>"
                    + "<td>" + displayNum + ":30" + amPm + "</td>";
        for (var j = 0; j < 5; j++) {
            textToAppend += "<td id=\"" + daysOfWeek[j]
                                + "_" + displayNum
                                + "_00" + amPm
                                + "\" class=\"open\"></td>";
        }
        textToAppend += "</tr>"; 
        
        // ON THE HALF HOUR
        var altAmPm = amPm;
        if (displayNum === 11)
            altAmPm = "pm";
        var altDisplayNum = displayNum + 1;
        if (altDisplayNum > 12)
            altDisplayNum = 1;
                    
        textToAppend += "<tr>";
        textToAppend += "<td>" + displayNum + ":30" + amPm + "</td>"
                    + "<td>" + altDisplayNum + ":00" + altAmPm + "</td>";
            
        for (var j = 0; j < 5; j++) {
            textToAppend += "<td id=\"" + daysOfWeek[j]
                                + "_" + displayNum
                                + "_30" + amPm
                                + "\" class=\"open\"></td>";
        }
        
        textToAppend += "</tr>";
        var cell = $("#office_hours_table");
        cell.append(textToAppend);
    }
    
    // NOW SET THE OFFICE HOURS
    for (var i = 0; i < data.officeHours.length; i++) {
	var id = data.officeHours[i].day + "_" + data.officeHours[i].time;
	var name = data.officeHours[i].name;
	var cell = $("#" + id);
	if (name === "Lecture") {
	    cell.removeClass("open");
	    cell.addClass("lecture");
	    cell.html("Lecture");
	}
        else if (name === "Recitation") {
            cell.removeClass("open");
            cell.addClass("recitation");
            cell.html("Recitation");
        }
	else {
	    cell.removeClass("open");
	    cell.addClass("time");
	    cell.append(name + "<br />");
	}
    }
}
function getAMorPM(testTime) {
    if (testTime >= 12)
        return "pm";
    else
        return "am";
}