package hr.mucnjakf.main;

import hr.mucnjakf.utilities.reflection.ReflectionUtilities;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        createDocumentation();

        Parent root = FXMLLoader.load(getClass().getResource("/view/TeamBuilder.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setTitle("Team Builder");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createDocumentation() {
        String classesController    = "src/main/java/hr/mucnjakf/controller";
        String classesDatabase      = "src/main/java/hr/mucnjakf/database";
        String classesFormation     = "src/main/java/hr/mucnjakf/formation";
        String classesJndi          = "src/main/java/hr/mucnjakf/jndi";
        String classesMain          = "src/main/java/hr/mucnjakf/main";
        String classesModel         = "src/main/java/hr/mucnjakf/model";
        String classesNetwork       = "src/main/java/hr/mucnjakf/network";
        String classesRmi           = "src/main/java/hr/mucnjakf/rmi";
        String classesAlert         = "src/main/java/hr/mucnjakf/utilities/alert";
        String classesConvert       = "src/main/java/hr/mucnjakf/utilities/convert";
        String classesReflection    = "src/main/java/hr/mucnjakf/utilities/reflection";
        String classesSerialization = "src/main/java/hr/mucnjakf/utilities/serialization";
        String classesString        = "src/main/java/hr/mucnjakf/utilities/string";

        final String DOCUMENTATION_FILENAME
                = "E:\\dev\\FootballManager\\FootballManagerClient\\documentation\\documentation.html";

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(DOCUMENTATION_FILENAME))) {

            StringBuilder sbController = writeClasses(classesController);
            StringBuilder sbDatabase = writeClasses(classesDatabase);
            StringBuilder sbFormation = writeClasses(classesFormation);
            StringBuilder sbJndi = writeClasses(classesJndi);
            StringBuilder sbMain = writeClasses(classesMain);
            StringBuilder sbModel = writeClasses(classesModel);
            StringBuilder sbNetwork = writeClasses(classesNetwork);
            StringBuilder sbRmi = writeClasses(classesRmi);
            StringBuilder sbAlert = writeClasses(classesAlert);
            StringBuilder sbConvert = writeClasses(classesConvert);
            StringBuilder sbReflection = writeClasses(classesReflection);
            StringBuilder sbSerialization = writeClasses(classesSerialization);
            StringBuilder sbString = writeClasses(classesString);

            writer.write(
                    sbController.toString() +
                            sbDatabase +
                            sbFormation +
                            sbJndi +
                            sbMain +
                            sbModel +
                            sbNetwork +
                            sbRmi +
                            sbAlert +
                            sbConvert +
                            sbReflection +
                            sbSerialization +
                            sbString);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private StringBuilder writeClasses(String classesPath) {
        final String CLASSES_PACKAGE
                = classesPath.substring(classesPath.indexOf("/") + 1).replace("/", ".").concat(".");

        StringBuilder sbClass = new StringBuilder();

        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(classesPath));

            stream.forEach(file -> {
                String filename = file.getFileName().toString();
                String className = filename.substring(0, filename.indexOf("."));

                sbClass
                        .append("<!DOCTYPE html>")
                        .append("<html>")
                        .append("<head>")
                        .append("<title>Documentation</title>")
                        .append("</head>")
                        .append("<body>");

                sbClass
                        .append("<h2>Class: ")
                        .append(className)
                        .append("</h2>");

                try {
                    Class<?> inputClass = Class.forName(CLASSES_PACKAGE.concat(className));
                    ReflectionUtilities.readClassAndMembersInfo(inputClass, sbClass);
                } catch (ClassNotFoundException ignored) {
                }
            });

        }catch (IOException e) {
            e.printStackTrace();
        }

        return sbClass;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
