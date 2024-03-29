const { app, BrowserWindow } = require("electron"); 

const PAGE = {
    HOME: 'src/home/home.html', 
}

require('electron-reload')(__dirname);


function createWindow() {
    let window = new BrowserWindow({
        width: 800,
        height: 600,
        webPreferences: {
            nodeIntegration: true
        }
    });
    window.setFullScreenable(false);
    window.setResizable(false);
    window.removeMenu();
    window.setMenuBarVisibility = false;
    window.loadFile(PAGE.HOME);
}

app.whenReady().then(createWindow);