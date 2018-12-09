function buildPage(page, pathHome) {
    var jsonFile = pathHome + "/js/PageData.json";
    $.getJSON(jsonFile, function (json) {
        json.page = page;
        addBanner(json);
        addLogos(json, pathHome);
        addAuthor(json);
        addPages(json, pathHome);
    });
}
function addBanner(data) {
    // SET THE PAGE TITLE
    var code = data.subject + " " + data.number;
    var semester = data.semester + " " + data.year;
    document.title = code + " - " + data.page;

    var inlinedCourseName = $("#inlined_course");
    if (inlinedCourseName) {
        inlinedCourseName.append(code);
    }

    var courseBanner = $("#banner");
    if (courseBanner) {
        var text = code + " - " + semester + "<br />" + data.title;
        courseBanner.append(text);
    }
}
function addLogos(data, pathHome) {
    // ADD THE COURSE FAVICON
    var head = $("head");
    var faviconImgText = "<link rel='shortcut icon' href='" + pathHome + "/" + data.logos.favicon.href + "' />";
    head.append(faviconImgText);
    
    // LOGO IN THE TOP RIGHT OF THE NAVBAR
    var navbarImageLink = $("#navbar_image_link");
    navbarImageLink.attr("href", data.logos.navbar.href);
    var navbarImgText = "<img class='sbu_navbar' alt='" + data.logos.navbar.href + "' src='" + pathHome + "/" + data.logos.navbar.src + "' />";
    navbarImageLink.append(navbarImgText);
    
    // LOGO IN THE BOTTOM LEFT OF THE FOOTER
    var leftFooterImageLink = $("#left_footer_image_link");
    leftFooterImageLink.attr("href", data.logos.bottom_left.href);
    var leftImgText = "<img alt='" + data.logos.bottom_left.href + "' src='" + pathHome + "/" + data.logos.bottom_left.src + "' style='float:left' />";
    leftFooterImageLink.append(leftImgText);

    // LOGO IN THE BOTTOM RIGHT OF THE FOOTER
    var rightFooterImageLink = $("#right_footer_image_link");
    rightFooterImageLink.attr("href", data.logos.bottom_right.href);
    var rightImgText = "<img alt='" + data.logos.bottom_right.href + "' src='" + pathHome + "/" + data.logos.bottom_right.src + "' style='float:right' />";
    rightFooterImageLink.append(rightImgText);
}
function addAuthor(data) {
    var authorSpan = $("#author_link");
    var text = "<a href='" + data.instructor.link + "'>" + data.instructor.name + "</a>";
    authorSpan.append(text);
}
function addPages(data, pathHome) {
    var navbar = $("#navbar");
    var navbarLinks = {
        "Home": "index.html",
        "Syllabus": "syllabus.html",
        "Schedule": "schedule.html",
        "HWs": "hws.html"
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