package za.co.theemlaba.webapi;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.FileRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;

import org.apache.log4j.chainsaw.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.get;
import io.javalin.http.Handler;

// import io.javalin.plugin.rendering.JavalinRenderer;
// import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import java.util.HashMap;
import java.util.Map;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

//templating.engine.ThymeleafTemplateEngine;



import za.co.theemlaba.domain.UserController;
import za.co.theemlaba.domain.user.UserManager;


public class WebApiStarter {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    
    public static void main(String[] args) {
        Javalin app = startServer(args);
        UserController controller = new UserController();
        UserManager userManager = new UserManager();

        app.post("/register", ctx -> {
            String receivedData = ctx.body();
            ctx.json(userManager.handleRegister(receivedData));
        });

        app.post("/login", ctx -> {
            String receivedData = ctx.body();
            ctx.json(userManager.handleLogin(receivedData));
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

        app.get("/user", ctx -> {
            // String name = ctx.pathParam("name");
            Map<String, Object> model = new HashMap<>();
            model.put("name", "Javalin BOB");
            ctx.render("user.html", model);
        });
    }

    public static Javalin startServer(String[] args) {
                
        Javalin app = Javalin.create(config -> {

            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();  // Allows CORS requests from any host
                });
            });

            // config.staticFiles.add(staticFileConfig -> {
            //     staticFileConfig.directory = "/public";
            //     staticFileConfig.location = Location.CLASSPATH;
            // });

            config.fileRenderer(new JavalinThymeleaf());
        });

        return app.start(7000);
    }

    // Configure Thymeleaf template engine
    private static FileRenderer configureThymeleaf() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/public");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false); // Disable cache during development

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return new JavalinThymeleaf(templateEngine);
    }}
