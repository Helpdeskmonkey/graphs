package at.spengergasse;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.UploadHandler;


import java.util.Arrays;
import java.util.List;

@Route
public class MainView extends VerticalLayout {

    private final FileService fileService;

    public MainView(GreetService service) {

        this.fileService = new FileService();

        TextArea output = new TextArea();
        output.setWidth("600px");
        output.setHeight("350px");

        UploadHandler inMemoryHandler1 = (event) -> {

            fileService.processCsv(event.getInputStream());

            int[][] dist = fileService.getDistance();

            GraphAnalyzer analyzer = new GraphAnalyzer(fileService.getResult());

            int[] ecc = analyzer.computeEccentricities(dist);
            int radius = analyzer.computeRadius(ecc);
            int diameter = analyzer.computeDiameter(ecc);
            List<Integer> center = analyzer.computeCenter(ecc, radius);
            List<List<Integer>> components = analyzer.computeComponents();
            List<String> bridges = analyzer.computeBridges();
            List<Integer> articulations = analyzer.computeArticulations();

            String text =
                    "Exzentrizitäten: " + Arrays.toString(ecc) + "\n" +
                            "Radius: " + radius + "\n" +
                            "Durchmesser: " + diameter + "\n" +
                            "Zentrum: " + center + "\n\n" +
                            "Distanzmatrix:\n" +
                            "Komponenten: " + components + "\n" +
                            "Brücken: " + bridges + "\n" +
                            "Artikulationen: " + articulations + "\n" +
                            Arrays.deepToString(dist);



            getUI().ifPresent(ui ->
                    ui.access(() -> output.setValue(text))
            );;
        };

        Upload upload = new Upload(inMemoryHandler1);
        upload.setDropAllowed(true);

        add(upload);
        add(output);

        addClassName("centered-content");
    }
}