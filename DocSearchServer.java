import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class FileHelpers {
    static List<File> getFiles(Path start) throws IOException {
        File f = start.toFile();
        List<File> result = new ArrayList<>();
        if (f.isDirectory()) {
            System.out.println("It's a folder");
            File[] paths = f.listFiles();
            for (File subFile : paths) {
                result.addAll(getFiles(subFile.toPath()));
            }
        } else {
            result.add(start.toFile());
        }
        return result;
    }

    static String readFile(File f) throws IOException {
        System.out.println(f.toString());
        return new String(Files.readAllBytes(f.toPath()));
    }
}

class Handler implements URLHandler {
    List<File> files;

    Handler(String directory) throws IOException {
        this.files = FileHelpers.getFiles(Paths.get(directory));
    }

    public String handleRequest(URI url) throws IOException {
        if (url.getPath().equals("/")) {
            return "There are " + files.size() + " files to search";
        } else if (url.getPath().equals("/search")) {
            if (url.getQuery().isEmpty()) {
                return String.format("Input a valid query!");
            }
            
            String[] parameters = url.getQuery().split("=");
            boolean found = false;
            String foundItems = "";
            int numFound = 0;

            for (int i = 0; i < files.size(); i++) {
                if(files.get(i).toString().contains(parameters[1])) {
                    foundItems += "\"" + files.get(i).toString() + "\" contains \"" + parameters[1] + "\"\n";
                    found = true;
                    numFound++;
                }
            }
            if (!found) {
                return String.format("\"%s\" not found D:", parameters[1]);
            }

            return String.format("There are %d files found\n%s", numFound, foundItems);
        }
        return "Don't know how to handle that path!";
    }

    class DocSearchServer {
        public static void main(String[] args) throws IOException {
            if (args.length == 0) {
                System.out.println("Missing port number! Try any number between 1024 to 49151");
                return;
            }

            int port = Integer.parseInt(args[0]);

            Server.start(port, new Handler("./technical/"));
        }
    }
}