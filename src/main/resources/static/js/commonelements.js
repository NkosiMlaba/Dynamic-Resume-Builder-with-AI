
var desktopNavSection = document.getElementById('desktop-nav');
desktopNavSection.innerHTML = `<div class="logo"><a href="home.html" onclick="toggleMenu()">Nkosikhona Mlaba</a></div>
<div>
    <ul class="nav-links">
        <li><a href="home.html">Home</a></li>
        <li><a href="createuser.html">Sign Up</a></li>
        <li><a href="login.html">Login</a></li>
        <li><a href="#contact">Contact</a></li>
    </ul>
</div>`

var contactSection = document.getElementById('contact');
contactSection.innerHTML = `<p class="section__text__p1">Get in Touch</p>
<h1 class="title">Contact Me</h1>
<div class="contact-info-upper-container">
    <div class="contact-info-container">
        <img src="/assets/email_image.png" alt="Email icon" class="icon contact-icon email-icon" />
        <p><a href="mailto:nkosimlaba397@gmail.com">nkosimlaba397@gmail.com</a></p>
    </div>
    <div class="contact-info-container">
        <img src="/assets/linkedin_image.png" alt="LinkedIn icon" id="linkedin-icon-contact"
            class="icon contact-icon" />
        <p><a href="https://www.linkedin.com/in/nkosikhona-mlaba-1545a7273">LinkedIn</a></p>
    </div>
</div>`


var footerElement = document.querySelector('footer');
footerElement.innerHTML = `
<p style="margin-bottom: 50px">Nkosikhona Mlaba. All rights reserved</p>`