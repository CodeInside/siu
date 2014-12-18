package A;

import java.io.File;
import java.io.FileReader;

import au.com.bytecode.opencsv.CSVReader;

public class VerifyCSVFiles {
  public static void main(String[] args) throws Exception {
    String path = "d:/work/siu/_test_documents/dicts";

    File dir = new File(path);
    for (File file : dir.listFiles()) {

      String name = file.getName();
      if (name.endsWith("csv")) {

        CSVReader csvReader = new CSVReader(new FileReader(file));
        int line = 0;
        String[] record;
        while ((record = csvReader.readNext()) != null) {
          line++;
          if (record.length != 3) {
            throw new Exception(String.format("check file '%s' line '%s'", name, line));
          }
        }

        System.out.println(name + " is OK");
      }

    }

  }
}
