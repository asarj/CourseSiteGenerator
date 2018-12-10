function buildSections() {
    var jsonFile = "./js/SectionsData.json";
    $.getJSON(jsonFile, function (json) {
        addLectures(json);
        addLabs(json);
        addRecitations(json);
    });
}
function addLectures(data) {
    var lecturesRow = $("#lectures_row");
    for (var i = 0; i < data.lectures.length; i++) {
        var lec = data.lectures[i];
        var text = "<td class='rec_" + (i%2) + " rec_cell'>"
                 + "<strong>" + lec.section + "</strong><br />"
                 + lec.days + "<br />"
                 + lec.time + "<br />"
                 + lec.room + "<br />"
                 + "<br /></td>";
        lecturesRow.append(text);
    }
}
function getAMorPM(testTime) {
    if (testTime >= 12)
        return "pm";
    else
        return "am";
}
function addLabs(data) {
    if (data.labs.length > 0) {
        $("#labs_heading").append("LABS");
        addLabsOrRecs(data.labs, "#labs_table", "lab");
    }
}
function addRecitations(data) {
    if (data.recitations.length > 0) {
        $("#recitations_heading").append("RECITATIONS");
        addLabsOrRecs(data.recitations, "#recitations_table", "rec");
    }
}
function addLabsOrRecs(tableData, tableId, cellClass) {
    var table = $(tableId);
    var rowParity = 0;
    for (var i = 0; i < tableData.length; i+=2) {
        var text = "<tr>";
        var section = tableData[i];
        var cellParity = rowParity;
        text += buildLabOrRecCell(cellParity, section, cellClass);
        cellParity++;
        cellParity %= 2;
        if ((i+1) < tableData.length) {
            section = tableData[i+1];
            text += buildLabOrRecCell(cellParity, section, cellClass);
        }
        else
            text += "<td></td>";
        text += "</tr>";
        table.append(text);
        rowParity++;
        rowParity %= 2;
    }
}
function buildLabOrRecCell(classNum, cellData, cellClass) {
    var text = "<td class='" + cellClass + "_" + classNum + "'>"
                + "<table><tr><td valign='top' class='" + cellClass + "_cell'>" 
                + cellData.section + "<br />"
                + cellData.day_time + "<br />"
                + cellData.location + "<br /></td></tr>"
                + "<tr>";
    
    // LAB/REC TA #1
    text += "<td class='ta_cell'><strong>Supervising TA</strong><br />";
    if (cellData.ta_1 != "none")
        text += "<img src='./images/tas/" 
            + cellData.ta_1.replace(/\s/g, '')
            + ".jpg' width='100' height='100' />"
            + "<br clear='both' />"
            + "(" + cellData.ta_1 + ")<br />";
    else
        text += "TBA";
    text += "</td>";
    
    // LAB/REC TA #2
    text += "<td class='ta_cell'><strong>Supervising TA</strong><br />";
    if (cellData.ta_2 != "none")
        text += "<img src='./images/tas/" 
            + cellData.ta_2.replace(/\s/g, '')
            + ".jpg' width='100' height='100' />"
            + "<br clear='both' />"
            + "(" + cellData.ta_2 + ")<br />";            
    else
        text += "TBA";
    text += "</table></td>";
    return text;
}