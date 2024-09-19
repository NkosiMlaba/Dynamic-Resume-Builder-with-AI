package za.co.theemlaba.webapi;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;
import io.javalin.http.Context;
import org.apache.log4j.chainsaw.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import za.co.theemlaba.domain.UserController;
import za.co.theemlaba.domain.user.UserManager;

public class WebApiStarter {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Javalin app = startServer(args);
        UserController controller = new UserController();
        UserManager userManager = new UserManager();

        app.get("/", ctx -> {
            ctx.render("home.html");
        });

        app.get("/register", ctx -> {
            ctx.render("createuser.html");
        });

        app.post("/register", ctx -> {
            String receivedData = ctx.body();
            ctx.json(userManager.handleRegister(receivedData));
        });

        app.get("/login", ctx -> {
            ctx.render("login.html");
        });

        app.post("/login", ctx -> {
            String receivedData = ctx.body();
            ctx.json(userManager.handleLogin(receivedData));

            String email = "thembani@gmail.com";
            if (email != null) {
                ctx.sessionAttribute("email", email);
                ctx.sessionAttribute("sessionId", ctx.req().getSession().getId());
                
                ctx.result("Login successful");
                ctx.redirect("/dashboard");

            } else {
                ctx.status(401).result("Invalid credentials");
            }
        });

        app.before(ctx -> {
            logger.info("Received {} request to {}", ctx.method(), ctx.url().toString());
        });

        app.after(ctx -> {
            logger.info("Responded with status {}", ctx.status());
        });

        app.get("/api/greeting", ctx -> {
            ctx.json("Hello from Javalin Server with CORS!");
        });

        
        app.post("/api/echo/{data}", ctx -> {
            String receivedData = ctx.pathParam("data");
            ctx.result("Echo: " + receivedData);
        });

        app.get("/user/{name}", ctx -> {
            String name = ctx.pathParam("name");
            Map<String, Object> model = new HashMap<>();
            model.put("name", name);
            ctx.render("user.html", model);
        });

        

        app.get("/dashboard", ctx -> {
            String email = ctx.sessionAttribute("email");
            String sessionId = ctx.req().getSession().getId();
            if (email != null && sessionId.equals(ctx.sessionAttribute("sessionId"))) {
                ctx.render("dashboard.html");
            } else {
                ctx.redirect("/login");
            }
        });

        app.post("/generate-cv", ctx -> {
            String email = ctx.sessionAttribute("email");
            String sessionId = ctx.req().getSession().getId();
            if (email != null && sessionId.equals(ctx.sessionAttribute("sessionId"))) {
                ctx.redirect("/download-cv");
            } else {
                ctx.redirect("/login");
            }
        });

        app.get("/download-page", ctx -> {
            ctx.render("download.html");
        });

        app.get("/download-cv", ctx -> {
            String email = ctx.sessionAttribute("email");
            String sessionId = ctx.req().getSession().getId();

            if (email != null && sessionId.equals(ctx.sessionAttribute("sessionId"))) {
                String cvFilePath = generateCvForUser(email);

                sendFile(ctx, cvFilePath);
            } else {
                ctx.redirect("/login");
            }
        });
    }

    private static String generateCvForUser(String email) {
        return "src/main/resources/resumes/" + email +"/GeneratedCV.docx";
    }

    private static void sendFile(Context ctx, String filePath) throws Exception {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            ctx.header("Content-Disposition", "attachment; filename=" + path.getFileName().toString());
            ctx.result(Files.newInputStream(path));
        } else {
            ctx.status(404).result("File not found");
        }
    }

    public static Javalin startServer(String[] args) {
        Javalin app = Javalin.create(config -> {

            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });

            config.staticFiles.add(staticFileConfig -> {
                staticFileConfig.directory = "src/main/resources/static";
                staticFileConfig.location = Location.EXTERNAL;
            });

            config.fileRenderer(new JavalinThymeleaf(buildTemplateEngine()));
            });

        return app.start(7000);
    }

    public static TemplateEngine buildTemplateEngine () {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
}
