import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        List<String> listFolder = new ArrayList<>(List.of(new String[]{"src/main", "src/main", "src/test", "res/drawables", "res/vectors", "res/icons", "savegames"}));
        List<String> listFile = new ArrayList<>(List.of(new String[]{"temp.txt", "src/main/Main.java", "src/main/Utils.java"}));
        StringBuilder temp = new StringBuilder();

        createFolder(listFolder, temp);
        createFiles(listFile, temp);

        GameProgress save1 = new GameProgress(50, 50, 50, 30.5);
        GameProgress save2 = new GameProgress(40, 30, 60, 20.5);
        GameProgress save3 = new GameProgress(30, 40, 30, 10.5);

        List<GameProgress> saveFile = new ArrayList<>(List.of(new GameProgress[]{save1, save2, save3}));
        List<String> saveFilePath = new ArrayList<>();

        createSavegame(saveFile, saveFilePath, temp);

        zipFiles("savegames/zip.zip", saveFilePath);

        removeSavegames(saveFilePath);

        logTemp(temp);
    }

    private static void removeSavegames(List<String> saveFilePath) {
        for (String path : saveFilePath) {
            try {
                Files.delete(Paths.get(path));
            } catch (IOException x) {
                System.err.println(x);
            }
        }
    }

    private static void createSavegame(List<GameProgress> saveFile, List<String> saveFilePath, StringBuilder temp) {
        for (int i = 0; i < saveFile.size(); i++) {
            try (FileOutputStream fos = new FileOutputStream("savegames/save" + (i == 0 ? "" : i) + ".dat"); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(saveFile.get(i));
                saveFilePath.add("savegames/save" + (i == 0 ? "" : i) + ".dat");
                temp.append("файла " + "save" + (i == 0 ? "" : i) + ".dat" + "успешно записан\n");
            } catch (IOException ex) {
                temp.append("при записи файла " + "save" + (i == 0 ? "" : i) + ".dat" + "произошла ошибка\n");
            }
        }
    }

    private static void zipFiles(String path, List<String> saveFilePath) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path));) {
            for (String filePath : saveFilePath) {
                FileInputStream fis = new FileInputStream(filePath);
                ZipEntry entry = new ZipEntry(filePath.split("/")[1]);
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
                fis.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void logTemp(StringBuilder temp) {
        try (FileWriter writer = new FileWriter("temp.txt", false)) {
            writer.write(String.valueOf(temp));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createFiles(List<String> listFile, StringBuilder temp) {
        for (String fileName : listFile) {
            try {
                File file = new File(fileName);
                file.createNewFile();
                temp.append("фаил " + file.getName() + " успешно создан \n");
            } catch (IOException e) {
                temp.append("произошла ошибка при создание файла " + fileName + "\n");
                throw new RuntimeException(e);
            }
        }
    }

    private static void createFolder(List<String> listFolder, StringBuilder temp) {
        for (String folderPath : listFolder) {
            File folder = new File(folderPath);
            folder.mkdirs();
            temp.append("дериктория " + folder.getName() + " успешно создана \n");
        }
    }
}
