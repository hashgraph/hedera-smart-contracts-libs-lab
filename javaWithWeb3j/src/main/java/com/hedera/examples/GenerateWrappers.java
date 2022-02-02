package com.hedera.examples;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.web3j.codegen.SolidityFunctionWrapperGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class GenerateWrappers {
  public static void main(String[] args) throws IOException, ClassNotFoundException {

    File abiFile = new File("./Solidity/abi.json");
    File destinationDir = new File("./src/main/java");
    String contractName = "Stateful";
    String basePackageName = "com.hedera.examples";
    boolean useJavaNativeTypes = true;
    boolean useJavaPrimitiveTypes = true;
    int addressLength = 20;
    boolean abiFuncs = true;
    Gson gson = new Gson();

    InputStream inputstream = new FileInputStream("./Solidity/bytecode.json");
    String bytecodeInFile = readFromInputStream(inputstream);

    JsonObject jsonBytecode = gson.fromJson(bytecodeInFile, JsonObject.class);

    String byteCodeHex = jsonBytecode.getAsJsonPrimitive("object")
            .getAsString();

    File binFile = File.createTempFile("bytecode","temp");
    PrintWriter binFileOut = new PrintWriter(binFile);
    binFileOut.print(byteCodeHex);
    binFileOut.close();

    SolidityFunctionWrapperGenerator solidityFunctionWrapperGenerator = new SolidityFunctionWrapperGenerator(
            binFile,
            abiFile,
            destinationDir,
            contractName,
            basePackageName,
            useJavaNativeTypes,
            useJavaPrimitiveTypes,
            addressLength,
            abiFuncs);

    solidityFunctionWrapperGenerator.generate();
  }

  private static String readFromInputStream(InputStream inputStream)
          throws IOException {
    StringBuilder resultStringBuilder = new StringBuilder();
    try (BufferedReader br
                 = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = br.readLine()) != null) {
        resultStringBuilder.append(line).append("\n");
      }
    }
    return resultStringBuilder.toString();
  }

}
