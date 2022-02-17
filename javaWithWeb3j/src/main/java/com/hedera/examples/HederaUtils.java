package com.hedera.examples;
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

public class HederaUtils {

    public static void wrap(String abiFileLocation, String byteCodeFileLocation, String destination, String contractName, String basePackageName, boolean javaNative) throws IOException, ClassNotFoundException {

      File abiFile = new File(abiFileLocation);
      File destinationDir = new File(destination);
      boolean useJavaNativeTypes = javaNative;
      boolean useJavaPrimitiveTypes = javaNative;
      int addressLength = 20;
      boolean abiFuncs = true;

      InputStream inputstream = new FileInputStream(byteCodeFileLocation);
      String byteCodeHex = HederaUtils.readFromInputStream(inputstream);
      inputstream.close();

      // try to get from Json if it is indeed Json
      try {
        Gson gson = new Gson();
        JsonObject jsonBytecode = gson.fromJson(byteCodeHex, JsonObject.class);

        byteCodeHex = jsonBytecode.getAsJsonPrimitive("object")
                .getAsString();
      } catch (Exception e) {
        // do nothing
      }

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

  public static String readFromInputStream(InputStream inputStream)
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
