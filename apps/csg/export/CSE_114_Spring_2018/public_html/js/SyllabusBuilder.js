function buildSyllabus() {
    var jsonFile = "./js/SyllabusData.json";
    $.getJSON(jsonFile, function (json) {
        addDescription(json);
        addTopics(json);
        addPrerequisites(json);
        addOutcomes(json);
        addTextbooks(json);
        addGradedComponents(json);
        addGradingNote(json);
        addAcademicDishonesty(json);
        addSpecialAssistance(json);
    });
}
function addDescription(data) {
    var description = $("#syllabus_course_description");
    description.html(data.description);
}
function addTopics(data) {    
    var topicsList = $("#syllabus_course_topics");
    for (var i = 0; i < data.topics.length; i++) {
        var text = "<li>";
        text += data.topics[i];
        text += "</li>";
        topicsList.append(text);
    }
}
function addPrerequisites(data) {
    var prerequisites = $("#syllabus_prerequisites");
    prerequisites.html(data.prerequisites);
}
function addOutcomes(data) {    
    var outcomesList = $("#syllabus_course_outcomes");
    for (var i = 0; i < data.outcomes.length; i++) {
        var text = "<li>";
        text += data.outcomes[i];
        text += "</li>";
        outcomesList.append(text);
    }
}
function addTextbooks(data) {
    var textbookData = $("#textbook_data");
    for (var i = 0; i < data.textbooks.length; i++) {
        var textbook = data.textbooks[i];
        var text = "<a href='" + textbook.link + "'><img class='textbook_image' width='100' height='125' src='" 
                + textbook.photo + "' /></a><a href='" + textbook.link + "'><em>" + textbook.title
                + "</em></a><br />"
                + "by ";
        for (var j = 0; j < textbook.authors.length; j++) {
            var author = textbook.authors[j];
            if (j > 0)
                text += ", ";
            text += "<strong>" + author + "</strong>";
        }
        text += "<br />";
        text += "Published by " + textbook.publisher + ", " + textbook.year;
        text += "<br clear='both' /><br /><br />";
        textbookData.append(text);
    }
}
function addGradedComponents(data) {
    var gradedComponents = $("#graded_components");
    for (var i = 0; i < data.gradedComponents.length; i++) {
        var component = data.gradedComponents[i];
        var text = "<li><strong>" + component.name + "</strong> - " + component.description + "<br /><br /></li>";
        gradedComponents.append(text);
    }

    var gradingBreakdown = $("#grading_breakdown");
    for (var i = 0; i < data.gradedComponents.length; i++) {
        var component = data.gradedComponents[i];
        var text = "<tr><td>" + component.name + "</td><td align='right'>" + component.weight + "%</td><td></td></tr>";
        gradingBreakdown.append(text);
    }    
    text = "<tr class='grading_total'><td></td><td align='right'><strong>100 %</strong></td></tr>";
    gradingBreakdown.append(text);
}
function addGradingNote(data) {
    var gradingNote = $("#grading_note");
    gradingNote.append(data.gradingNote);
}
function addAcademicDishonesty(data) {
    var academicDishonesty = $("#academic_dishonesty");
    academicDishonesty.append(data.academicDishonesty);
}
function addSpecialAssistance(data) {
    var specialAssistance = $("#special_assistance");
    specialAssistance.append(data.specialAssistance);
}