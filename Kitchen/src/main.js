const { app, BrowserWindow } = require("electron"); 

function createWindow() {
    let window = new BrowserWindow({
        width: 800,
        height: 600,
        webPreferences: {
            nodeIntegration: true
        }
    });
    window.loadFile('src/index.html');
}

app.whenReady().then(createWindow);