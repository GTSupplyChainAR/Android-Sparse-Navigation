package com.thad.sparsenavigation.Scripts;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thad.sparsenavigation.R;
import com.thad.sparsenavigation.Scripts.model.Experiment;
import com.thad.sparsenavigation.Scripts.model.Book;
import com.thad.sparsenavigation.Scripts.model.PickPath;

import java.util.*;


public class Converter {
    private Scanner scanner;
    private File file;
    private String pickPathsFileName;
    private String experimentsFileName;
    private String packageStructureFromRoot;
    private Context context;
    private InputStream inputStream;

    // constants used in experiments
    private final static int PARTICIPANT_ID_POSITION_IN_TOKEN = 0;
    private final static int NUMBER_OF_POSITIONS_ON_SCREEN = 4;
    private final static int POSITION_NAME_POSITION_IN_TOKEN = 2;
    private final static int PATH_ID_BEGIN_POSITION_IN_TOKEN = 3;
    private final static int PATH_ID_END_POSITION_IN_TOKEN = 13;

    // constants used in pick-paths
    private final static int NUMBER_OF_BOOKS_PER_PATH_ID = 10;
    private final static int BOOK_NAME_POSITION_IN_TOKEN = 13;
    private final static int BOOK_AUTHOR_POSITION_IN_TOKEN = 14;
    private final static int BOOK_TAG_POSITION_IN_TOKEN = 15;
    private final static int PATH_ID_POSITION_IN_TOKEN = 0;
    private final static int TYPE_POSITION_IN_TOKEN = 1;
    private final static int ORDER_OR_UNORDERED_POSITION_IN_TOKEN = 2;



    public Converter(Context context) {
        this.context = context;
        pickPathsFileName = "pickpaths.csv";
        experimentsFileName = "experiment.csv";
        packageStructureFromRoot = "glass/src/main/java/com/thad/sparsenavigation/Scripts/";
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public String getPickPathsFileName() {
        return pickPathsFileName;
    }

    public void setPickPathsFileName(String pickPathsFileName) {
        this.pickPathsFileName = pickPathsFileName;
    }

    public String getExperimentsFileName() {
        return experimentsFileName;
    }

    public void setExperimentsFileName(String experimentsFileName) {
        this.experimentsFileName = experimentsFileName;
    }

    private void openFile(String fileName) {
        try {
            Log.d("TAG", fileName);
            if (fileName.compareTo("pickpaths.csv") == 0){
                inputStream = this.context.getResources().openRawResource(R.raw.pickpaths);
            } else if (fileName.compareTo("experiment.csv") == 0) {
                inputStream = this.context.getResources().openRawResource(R.raw.experiment);

            }
            file = File.createTempFile("pre", "suf");
            copyFile(inputStream, new FileOutputStream(file));
            scanner = new Scanner(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Experiment> parseExperiments() {
        openFile(experimentsFileName);

        List<Experiment> list = new ArrayList<>();
        scanner.nextLine();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            line = line.trim();
            String[] tokens = line.split(",");

            Experiment experiment = new Experiment();
            if (!tokens[PARTICIPANT_ID_POSITION_IN_TOKEN].equals("")) {
                experiment.setParticipantId(tokens[PARTICIPANT_ID_POSITION_IN_TOKEN]);
                Map<String, List<Integer>> trainingPathOrder = new HashMap<>();
                for (int i = 0; i < NUMBER_OF_POSITIONS_ON_SCREEN; i++) {
                    List<Integer> pathIds = new ArrayList<>();
                    for (int tokenPosition = PATH_ID_BEGIN_POSITION_IN_TOKEN; tokenPosition < PATH_ID_END_POSITION_IN_TOKEN; tokenPosition++) {
                        pathIds.add(Integer.parseInt(tokens[tokenPosition]));
                    }
                    trainingPathOrder.put(tokens[POSITION_NAME_POSITION_IN_TOKEN], pathIds);
                    line = scanner.nextLine().trim();
                    tokens = line.split(",");
                }
                experiment.setTrainingPathOrder(trainingPathOrder);

                Map<String, List<Integer>> testingPathOrder = new HashMap<>();
                for (int i = 0; i < NUMBER_OF_POSITIONS_ON_SCREEN; i++) {
                    List<Integer> pathIds = new ArrayList<>();
                    for (int tokenPosition = PATH_ID_BEGIN_POSITION_IN_TOKEN; tokenPosition < PATH_ID_END_POSITION_IN_TOKEN; tokenPosition++) {
                        pathIds.add(Integer.parseInt(tokens[tokenPosition]));
                    }
                    testingPathOrder.put(tokens[POSITION_NAME_POSITION_IN_TOKEN], pathIds);
                    if (i != 3) {
                        tokens = scanner.nextLine().trim().split(",");
                    }
                }
                experiment.setTestingPathOrder(testingPathOrder);

            }
            list.add(experiment);
        }
        System.out.println("Generated List");
        System.out.println(list);
        return list;
    }

    public List<PickPath> parsePickPaths() {
        openFile(pickPathsFileName);
        scanner.nextLine();

        List<PickPath> list = new ArrayList<>();

        while (scanner.hasNext()) {
//            System.out.println("list so far");
//            System.out.println(list);
            String line = scanner.nextLine();
            line = line.trim();
            String[] tokens = line.split(",");
            PickPath pickPath = new PickPath();

            if (!tokens[PATH_ID_POSITION_IN_TOKEN].equals("")) {
                String pathId = tokens[PATH_ID_POSITION_IN_TOKEN];
                pickPath.setPathId(pathId);

                String type = tokens[TYPE_POSITION_IN_TOKEN];
                pickPath.setType(type);

//                System.out.println("PathId, Type: " + pathId + ", " + type);
//                Token[13, 14, 15] -> book details
                List<Book> booksInPath = new ArrayList<>();
                while (!tokens[ORDER_OR_UNORDERED_POSITION_IN_TOKEN].equals("orderedBooksAndLocations")) {
                    line = scanner.nextLine();
                    tokens = line.split(",");
                }
                tokens = line.split(",");

                for (int i = 0; i < NUMBER_OF_BOOKS_PER_PATH_ID; i++) {
                    Book book = new Book();
                    book.setName(tokens[BOOK_NAME_POSITION_IN_TOKEN]);
                    book.setAuthor(tokens[BOOK_AUTHOR_POSITION_IN_TOKEN]);
                    book.setLocationTag(tokens[BOOK_TAG_POSITION_IN_TOKEN]);
                    booksInPath.add(book);

                    line = scanner.nextLine();
                    tokens = line.split(",");
                }
//                System.out.println(booksInPath);
                pickPath.setBooksInPath(booksInPath);
                list.add(pickPath);
            }
        }

        System.out.println("Generated List");
        System.out.println(list);

        return list;
    }


    // For debugging/testing
//    public static void main(String[] args) {
//        Converter converter = new Converter();
//        List<Experiment> experiments = converter.parseExperiments();
//        List<PickPath> pickpaths = converter.parsePickPaths();
//        for (Experiment e: experiments) {
//            System.out.println(e.getParticipantId());
//            if
//        }
//    }
}