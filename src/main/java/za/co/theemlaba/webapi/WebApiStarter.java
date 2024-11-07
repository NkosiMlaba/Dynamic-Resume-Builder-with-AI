package za.co.theemlaba.webapi;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;
import io.javalin.http.Context;
import org.apache.log4j.chainsaw.Main;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class WebApiStarter {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final UserController controller = new UserController();

    public static void main(String[] args) {
        Javalin app = startServer(args);

        createStopListenerThread(app);
        createShutdownHook(app);

        app.before(WebApiStarter::logRequest);
        app.after(WebApiStarter::logResponse);

        app.get("/", WebApiStarter::showHomePage);
        
        app.get("/register", WebApiStarter::showRegistrationPage);
        app.post("/register", WebApiStarter::handleRegistration);
        
        app.get("/login", WebApiStarter::showLoginPage);
        app.post("/login", WebApiStarter::handleLogin);

        app.get("/dashboard", WebApiStarter::showDashboard);

        app.get("/capture-resume", WebApiStarter::showResumeCapturePage);
        app.post("/capture-resume", WebApiStarter::handleResumeUpdate);

        app.get("/capture-job-description", WebApiStarter::showCaptureJobDescriptionPage);
        app.post("/capture-job-description", WebApiStarter::captureJobDescription);
        app.post("/generate-from-last-job", WebApiStarter::generateFromLastJob);

        app.get("/download-page", WebApiStarter::showDownloadPage);
        app.get("/regenerate-and-download", WebApiStarter::regenerateResumeAndDownload);
        app.get("/download-cv", WebApiStarter::downloadResume);

        app.get("/capture-cover-letter-description", WebApiStarter::showCaptureCoverLetterPage);
        app.post("/capture-cover-letter-description", WebApiStarter::captureCoverLetterDescription);
        app.get("/generate-cover-letter", WebApiStarter::generateCoverLetter);
        app.post("/generate-cover-letter-from-last", WebApiStarter::generateCoverLetterFromLastJob);
        
        app.get("/download-page-cover-letter", WebApiStarter::showDownloadCoverLetter);
        app.get("/regenerate-cover-letter", WebApiStarter::regenerateCoverLetterAndDownload);
        app.get("/regenerate-cover-letter-from-last", WebApiStarter::regenerateCoverLetterFromDescriptionAndDownload);
        app.get("/download-cover-letter", WebApiStarter::downloadCoverLetter);

        app.get("/settings", WebApiStarter::showSettingsPage);
        app.post("/settings", WebApiStarter::handleSettingsUpdate);
    }

    /**
     * Renders the home page.
     *
     * @param ctx The Javalin context object.
     */
    public static void showHomePage(Context ctx) {
        ctx.render("home.html");
    }
    
    /**
     * Renders the registration page.
     *
     * @param ctx The Javalin context object.
     */
    public static void showRegistrationPage(Context ctx) {
        ctx.render("createuser.html");
    }
    
    /**
     * Handles the registration process.
     *
     * @param ctx The Javalin context object.
     */
    public static void handleRegistration(Context ctx) {
        Map<String, String> receivedData = extractRegistrationInformation(ctx);
        String email = controller.registerUser(receivedData);
    
        if (email != null) {
            ctx.sessionAttribute("email", email);
            ctx.sessionAttribute("sessionId", ctx.req().getSession().getId());
            ctx.result("Login successful");
            ctx.redirect("/capture-resume");
        } else {
            ctx.redirect("/register");
        }
    }

    public static void showLoginPage(Context ctx) {
        ctx.render("login.html");
    }

    public static void handleLogin(Context ctx) {
        Map<String, String> receivedData = extractLoginInformation(ctx);
        String email = controller.authenticateUser(receivedData);

        if (email != null) {
            ctx.sessionAttribute("email", email);
            ctx.sessionAttribute("sessionId", ctx.req().getSession().getId());
            ctx.result("Login successful");
            ctx.redirect("/dashboard");
        } else {
            ctx.status(401).result("Invalid credentials");
        }
    }

    public static void logRequest(Context ctx) {
        logger.info("Received {} request to {}", ctx.method(), ctx.url().toString());
    }

    public static void logResponse(Context ctx) {
        logger.info("Responded with status {}", ctx.status());
    }

    public static void showDashboard(Context ctx) {
        String email = returnEmailIfValidSession(ctx);
        if (email != null) {
            ctx.render("dashboard.html");
        } else {
            ctx.redirect("/login");
        }
    }

    public static void showResumeCapturePage (Context ctx) {
        String email = returnEmailIfValidSession(ctx);
        if (email != null) {
            Map<String, Object> model = controller.hasLastResume(email);
            ctx.render("captureresume.html", model);
        } else {
            ctx.redirect("/login");
        }
    }

    public static void handleResumeUpdate (Context ctx) {
        String email = returnEmailIfValidSession(ctx);
        
        if (email != null) {
            Map<String, String> receivedData = extractResumeInformation(ctx);
            receivedData.put("email", email);
            email = controller.handleStoreResumeData(receivedData);
            if (email != null) {
                ctx.redirect("/dashboard");
            } else {
                ctx.redirect("/capture-resume");
            }
        } else {
            ctx.redirect("/login");
        }
    }

    public static void showCaptureCoverLetterPage(Context ctx){
        String email = returnEmailIfValidSession(ctx);
        if (email!= null) {
            Map<String, Object> model = controller.hasLastJobDescription(email);
            ctx.render("capturecoverletter.html", model);
        } else {
            ctx.redirect("/login");
        }
        
    }

    public static void showCaptureJobDescriptionPage(Context ctx) {
        String email = returnEmailIfValidSession(ctx);
        if (email != null) {
            Map<String, Object> model = controller.hasLastJobDescription(email);
            ctx.render("capturejobdescription.html", model);
        } else {
            ctx.redirect("/login");
        }
        
    }

    public static void captureJobDescription(Context ctx) {
        Map<String, String> receivedData = extractJobDescriptionInformation(ctx);
        String email = controller.handleJobDescription(receivedData);
        if (email != null) {
            ctx.redirect("/download-page");
        } else {
            ctx.redirect("/dashboard");
        }
    }

    public static void regenerateResumeAndDownload(Context ctx) {
        String email = returnEmailIfValidSession(ctx);
        if (email != null) {
            email = controller.regenerateResume(email);
            downloadResume(ctx);
        } else {
            ctx.redirect("/login");
        }
    }

    public static void generateFromLastJob(Context ctx) {
        String email = returnEmailIfValidSession(ctx);
        
        if (email != null) {
            
            email = controller.regenerateResume(email);
            ctx.render("download.html");
            
        } else {
            ctx.redirect("/login");
        }
    }

    public static void captureCoverLetterDescription(Context ctx) {
        Map<String, String> receivedData = extractCoverLetterDescriptionInformation(ctx);
        String email = controller.handleCoverLetterDescription(receivedData);
        if (email != null) {
            
            ctx.redirect("/download-page-cover-letter");
        } else {
            ctx.redirect("/dashboard");
        }
    }

    public static void generateCoverLetterFromLastJob(Context ctx) {
        String email = returnEmailIfValidSession(ctx);
        
        if (email != null) {
            
            email = controller.generateCoverLetterFromDescription(email);
            ctx.render("downloadcoverletter.html");
            
        } else {
            ctx.redirect("/login");
        }
    }

    public static void regenerateCoverLetterAndDownload(Context ctx) {
        String email = returnEmailIfValidSession(ctx);

        if (email != null) {
            email = controller.generateCoverLetter(email);
            downloadCoverLetter(ctx);
        } else {
            ctx.redirect("/login");
        }
    }

    public static void regenerateCoverLetterFromDescriptionAndDownload(Context ctx) {
        String email = returnEmailIfValidSession(ctx);

        if (email != null) {
            email = controller.generateCoverLetterFromDescription(email);
            downloadCoverLetter(ctx);
        } else {
            ctx.redirect("/login");
        }
    }

    public static void generateCoverLetter (Context ctx) {
        String email = returnEmailIfValidSession(ctx);

        if (email != null) {
            email = controller.generateCoverLetter(email);
            ctx.render("downloadcoverletter.html");
        } else {
            ctx.redirect("/login");
        }
    }



    public static void showSettingsPage(Context ctx) {
        String email = returnEmailIfValidSession(ctx);
        if (email == null) {
            ctx.redirect("/login");
            return;
        }
        
        ctx.render("settings.html");
    }

    public static void handleSettingsUpdate(Context ctx) {
        String email = returnEmailIfValidSession(ctx);
        if (email == null) {
            ctx.redirect("/login");
            return;
        }

        String selectedFormat = ctx.formParam("format");
        if (email != null && selectedFormat != null) {
            controller.updatePreferredFormat(email, selectedFormat);
            ctx.redirect("/dashboard");
        } else {
            ctx.redirect("/settings");
        }
    }

    public static void showDownloadPage(Context ctx) {
        String email = returnEmailIfValidSession(ctx);
        if (email == null) {
            ctx.redirect("/login");
            return;
        }

        ctx.render("download.html");
    }

    public static void showDownloadCoverLetter(Context ctx) {
        String email = returnEmailIfValidSession(ctx);
        if (email == null) {
            ctx.redirect("/login");
            return;
        }

        Map<String, Object> model = controller.hasLastJobDescription(email);
        ctx.render("downloadcoverletter.html", model);
    }

    public static void downloadResume(Context ctx) {
        String email = returnEmailIfValidSession(ctx);
        if (email != null) {
            String resumeFilePath = controller.getResumeFilePath(email);
            sendFile(ctx, resumeFilePath);
        } else {
            ctx.redirect("/login");
        }
    }

    public static void downloadCoverLetter(Context ctx) {
        String email = returnEmailIfValidSession(ctx);
        if (email != null) {
            String resumeFilePath = controller.getCoverLetterFilePath(email);
            sendFile(ctx, resumeFilePath);
        } else {
            ctx.redirect("/login");
        }
    }

    private static void sendFile(Context ctx, String filePath) {
        Path path = Paths.get(filePath);
        try {
            if (Files.exists(path)) {
                ctx.header("Content-Disposition", "attachment; filename=" + path.getFileName().toString());
                ctx.result(Files.newInputStream(path));
            } else {
                ctx.status(404).result("File not found");
            }
        } catch (Exception e) {
            ctx.status(404).result("File not found error code " + e.getMessage());
        }
    }

    public static Javalin startServer(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> cors.addRule(it -> it.anyHost()));

            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.directory = "src/main/resources/static";
                staticFileConfig.location = Location.EXTERNAL;
            });

            config.fileRenderer(new JavalinThymeleaf(buildTemplateEngine()));
        });

        return app.start(7000);
    }

    public static TemplateEngine buildTemplateEngine() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    public static String returnEmailIfValidSession (Context ctx) {
        String email = ctx.sessionAttribute("email");
        String sessionId = ctx.req().getSession().getId();
        if (email != null && sessionId.equals(ctx.sessionAttribute("sessionId"))) {
            return email;
        } else {
            return null;
        }
    }

    public static Map<String, String> extractLoginInformation(Context ctx) {
        Map<String, String> loginInformation = new HashMap<>();
        loginInformation.put("email", ctx.formParam("email"));
        loginInformation.put("password", ctx.formParam("password"));
        return loginInformation;
    }

    public static Map<String, String> extractJobDescriptionInformation(Context ctx) {
        Map<String, String> jobInformation = new HashMap<>();
        jobInformation.put("jobdescription", ctx.formParam("jobdescription"));
        jobInformation.put("email", ctx.sessionAttribute("email"));
        return jobInformation;
    }

    public static Map<String, String> extractCoverLetterDescriptionInformation(Context ctx) {
        Map<String, String> jobInformation = new HashMap<>();
        jobInformation.put("coveletterdescription", ctx.formParam("coveletterdescription"));
        jobInformation.put("email", ctx.sessionAttribute("email"));
        return jobInformation;
    }

    public static Map<String, String> extractResumeInformation(Context ctx) {
        Map<String, String> resumeInformation = new HashMap<>();
        resumeInformation.put("resume", ctx.formParam("resume"));
        return resumeInformation;
    }

    public static Map<String, String> extractRegistrationInformation(Context ctx) {
        Map<String, String> registrationInformation = new HashMap<>();
        registrationInformation.put("email", ctx.formParam("email"));
        registrationInformation.put("password", ctx.formParam("password"));
        registrationInformation.put("firstname", ctx.formParam("firstname"));
        registrationInformation.put("lastname", ctx.formParam("lastname"));
        registrationInformation.put("confirmpassword", ctx.formParam("confirmpassword"));
        return registrationInformation;
    }

    public static void createStopListenerThread (Javalin app) {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("stop")) {
                    System.out.println("Shutting down server...");
                    app.stop();
                    break;
                }
            }
            scanner.close();
        }).start();
    }

    public static void createShutdownHook(Javalin app) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (app != null) {
                System.out.println("Shutting down server...");
                app.stop();
            }
        }));
    }
}
