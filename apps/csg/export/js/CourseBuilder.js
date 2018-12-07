function buildCourse(page, pathHome) {
    var dataFile = pathHome + "/js/CourseData.json";
    loadCourseData(dataFile, page, pathHome);
}

function loadCourseData(jsonFile, page, pathHome) {
    $.getJSON(jsonFile, function (json) {
        json.page = page;
        addCourseBanner(json);
        addCourseLogos(json, pathHome);
        addCourseAuthor(json);
        addCoursePages(json, pathHome);
    });
}

function addCourseBanner(data) {
    // SET THE PAGE TITLE
    document.title = data.code + " - " + data.page;

    var inlinedCourseName = $("#inlined_course");
    if (inlinedCourseName) {
        inlinedCourseName.append(data.code);
    }

    var courseBanner = $("#banner");
    if (courseBanner) {
        var text = data.code + " - " + data.semester + "<br />" + data.title;
        courseBanner.append(text);
    }
}

function addCourseLogos(data, pathHome) {
    // ADD THE COURSE FAVICON
    var head = $("head");
    var faviconImgText = "<link rel='shortcut icon' href='" + pathHome + "/" + data.logos.favicon.href + "' />";
    head.append(faviconImgText);
    
    // LOGO IN THE TOP RIGHT OF THE NAVBAR
    var navbarImageLink = $("#navbar_image_link");
    navbarImageLink.attr("href", data.logos.navbar.href);
    var navbarImgText = "<img class='sbu_navbar' alt='" + data.logos.navbar.alt + "' src='" + pathHome + "/" + data.logos.navbar.src + "' />";
    navbarImageLink.append(navbarImgText);
    
    // LOGO IN THE BOTTOM LEFT OF THE FOOTER
    var leftFooterImageLink = $("#left_footer_image_link");
    leftFooterImageLink.attr("href", data.logos.bottom_left.href);
    var leftImgText = "<img alt='" + data.logos.bottom_left.alt + "' src='" + pathHome + "/" + data.logos.bottom_left.src + "' style='float:left' />";
    leftFooterImageLink.append(leftImgText);

    // LOGO IN THE BOTTOM RIGHT OF THE FOOTER
    var rightFooterImageLink = $("#right_footer_image_link");
    rightFooterImageLink.attr("href", data.logos.bottom_right.href);
    var rightImgText = "<img alt='" + data.logos.bottom_right.alt + "' src='" + pathHome + "/" + data.logos.bottom_right.src + "' style='float:right' />";
    rightFooterImageLink.append(rightImgText);
}

function addCourseAuthor(data) {
    var authorSpan = $("#instructor_link");
    var text = "<a href='" + data.author.link + "'>" + data.author.name + "</a>";
    authorSpan.append(text);
}

function addCoursePages(data, pathHome) {
    var navbar = $("#navbar");
    var navbarLinks = {
        "Home": "index.html",
        "Syllabus": "syllabus.html",
        "Schedule": "schedule.html",
        "HWs": "hws.html",
        "Projects": "projects.html"
    };
    for (var i = 0; i < data.pages.length; i++) {
        var page = data.pages[i];
        var name = page.name;
        var link = page.link;
        var text = "<a class=\'";
        if (name === data.page) {
            text += "open_";
        }
        text += "nav' href='" + pathHome + "/" + link + "' id='" + name.toLowerCase() + "_link'>" + name + "</a>";
        navbar.append(text);
    }
}