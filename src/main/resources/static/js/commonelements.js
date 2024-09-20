
var desktopNavSection = document.getElementById('desktop-nav');
desktopNavSection.innerHTML = `
<div class="logo">
    <a href="/" onclick="toggleMenu()">Nkosikhona Mlaba</a>
</div>
<div>
    <ul class="nav-links">
        <li><a href="/">Home</a></li>
        <li><a href="/settings">Settings</a></li>
        <li><a href="#contact">Contact</a></li>
    </ul>
</div>`


var footerElement = document.querySelector('footer');
footerElement.innerHTML = `
<p style="margin-bottom: 50px">
    Nkosikhona Mlaba. All rights reserved
</p>`