package com.hedera.examples;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class GenerateTestWrappers {
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    File testFolder = new File("./Solidity/tests");
    FilenameFilter extensionFilter = new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".abi");
      }
    };

//    for (final File fileEntry : testFolder.listFiles(extensionFilter)) {
//      String abiFileLocation = fileEntry.getPath();
//      String byteCodeFileLocation = abiFileLocation.replace("abi", "bin");
//
//      String destinationDir = "./src/main/java";
//      String contractName = fileEntry.getName().replace(".abi", "");
//      String basePackageName = "com.hedera.test";
//
//        System.out.println("Processing " + contractName);
//        Utils.wrap(abiFileLocation, byteCodeFileLocation, destinationDir, contractName, basePackageName, false);
//    }

    for (final File fileEntry : testFolder.listFiles(extensionFilter)) {
      String abiFileLocation = fileEntry.getPath();
      String byteCodeFileLocation = abiFileLocation.replace("abi", "bin");

      String destinationDir = "./src/main/java";
      String contractName = fileEntry.getName().replace(".abi", "");
      String basePackageName = "com.hedera.testjava";

      if ("ComplexStorage".contains(contractName)) {
        System.out.println("Skipping " + contractName);
      } else {
        System.out.println("Processing " + contractName);
        Utils.wrap(abiFileLocation, byteCodeFileLocation, destinationDir, contractName, basePackageName, true);
      }
    }
  }
}
