
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        File main = new File("src/main");
        File utils = new File("src/main");
        File test = new File("src/test");
        File drawables = new File("res/drawables");
        File vectors = new File("res/vectors");
        File icons = new File("res/icons");
        File savegames = new File("savegames");
        File tempTxt = new File("temp.txt");
        File mainJava = new File("src/main/Main.java");
        File utilsJava = new File("src/main/Utils.java");
        List<File> listFolder = new ArrayList<>(List.of(new File[]{main, utils, test, drawables, vectors, icons, savegames}));
        List<File> listFile = new ArrayList<>(List.of(new File[]{mainJava, utilsJava}));
        StringBuilder temp = new StringBuilder();

        try {
            tempTxt.createNewFile();
            temp.append("фаил " + tempTxt.getName() + " успешно создан \n");
        } catch (IOException e) {
            temp.append("произошла ошибка при создание файла " + tempTxt.getName() + "\n");
            throw new RuntimeException(e);
        }


        for (File folder : listFolder) {
            folder.mkdirs();
            temp.append("дериктория " + folder.getName() + " успешно создана \n");
        }

        for (File file : listFile) {
            try {
                file.createNewFile();
                temp.append("фаил " + file.getName() + " успешно создан \n");
            } catch (IOException e) {
                temp.append("произошла ошибка при создание файла " + file.getName() + "\n");
                throw new RuntimeException(e);
            }
        }

        GameProgress save1 = new GameProgress(50, 50, 50, 30.5);
        GameProgress save2 = new GameProgress(40, 30, 60, 20.5);
        GameProgress save3 = new GameProgress(30, 40, 30, 10.5);

        List<GameProgress> saveFile = new ArrayList<>(List.of(new GameProgress[]{save1, save2, save3}));
        List<String> saveFilePath = new ArrayList<>();

        for (int i = 0; i < saveFile.size(); i++) {
            try (FileOutputStream fos = new FileOutputStream("savegames/save" + (i == 0 ? "" : i) + ".dat");
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(saveFile.get(i));
                saveFilePath.add("savegames/save" + (i == 0 ? "" : i) + ".dat");
                temp.append("файла " + "save" + (i == 0 ? "" : i) + ".dat" + "успешно записан\n");
            } catch (IOException ex) {
                temp.append("при записи файла " + "save" + (i == 0 ? "" : i) + ".dat" + "произошла ошибка\n");
            }
        }

        zipFiles("savegames/zip.zip", saveFilePath);

        try (FileWriter writer = new FileWriter("temp.txt", false)) {
            writer.write(String.valueOf(temp));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String path : saveFilePath) {
            try {
                Files.delete(Paths.get(path));
            } catch (IOException x) {
                System.err.println(x);
            }
        }
    }

    private static void zipFiles(String path, List<String> saveFilePath) {
        try (ZipOutputStream zout = new ZipOutputStream(new
                FileOutputStream(path));
        ) {
            for (String filePath :
                    saveFilePath) {
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
}

//    Далее реализуйте метод zipFiles(), принимающий в качестве аргументов String полный путь к файлу архива (например, "/Users/admin/Games/GunRunner/savegames/zip.zip") и
//    список запаковываемых объектов в виде списка строчек String полного пути к файлу (например, "/Users/admin/Games/GunRunner/savegames/save3.dat").
//    В методе Вам потребуется реализовать блок try-catch с объектами ZipOutputStream и FileOutputStream.
//    Внутри него пробегитесь по списку файлов и для каждого организуйте запись в блоке try-catch через FileInputStream.
//    Для этого создайте экземпляр ZipEntry и уведомьте ZipOutputStream о новом элементе архива с помощью метода putNextEntry().
//    Далее необходимо считать упаковываемый файл с помощью метода read() и записать его с помощью метода write().
//    После чего уведомьте ZipOutputStream о записи файла в архив с помощью метода closeEntry().
