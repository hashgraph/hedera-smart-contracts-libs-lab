/*-
 *
 * Smart Contracts Libs Labs
 *
 * Copyright (C) 2019 - 2021 Hedera Hashgraph, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

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
//        com.hedera.web3j.protocol.Utils.wrap(abiFileLocation, byteCodeFileLocation, destinationDir, contractName, basePackageName, false);
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
        HederaUtils.wrap(abiFileLocation, byteCodeFileLocation, destinationDir, contractName, basePackageName, true);
      }
    }
  }
}
