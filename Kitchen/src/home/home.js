const DISPLAY = {
    HIDE: "hide",
    SHOW: "show"
}

let divs = {
    home: document.getElementById("home"),
    newLevel: document.getElementById("newLevel"),
    loadLevel: document.getElementById("loadLevel")
};

let currentPage = divs.home;

function setPage(str) {
    if (currentPage != divs[str]) {
        currentPage.classList = DISPLAY.HIDE;
        currentPage = divs[str];
        currentPage.classList = DISPLAY.SHOW;
    }
}