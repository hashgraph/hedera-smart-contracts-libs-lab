package com.hedera.examples;

import java.io.IOException;

public class GenerateWrappers {
  public static void main(String[] args) throws IOException, ClassNotFoundException {

    String abiFileLocation = "./Solidity/abi.json";
    String byteCodeFileLocation = "./Solidity/bytecode.json";
    String destinationDir = "./src/main/java";
    String contractName = "Stateful";
    String basePackageName = "com.hedera.examples";

    Utils.wrap(abiFileLocation, byteCodeFileLocation, destinationDir, contractName, basePackageName, true);
  }
}
