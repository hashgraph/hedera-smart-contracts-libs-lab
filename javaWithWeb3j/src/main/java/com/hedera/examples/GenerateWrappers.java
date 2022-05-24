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

import java.io.IOException;

public class GenerateWrappers {
  public static void main(String[] args) throws IOException, ClassNotFoundException {

    String abiFileLocation = "./Solidity/abi.json";
    String byteCodeFileLocation = "./Solidity/bytecode.json";
    String destinationDir = "./src/main/java";
    String contractName = "Stateful";
    String basePackageName = "com.hedera.examples";

    HederaUtils.wrap(abiFileLocation, byteCodeFileLocation, destinationDir, contractName, basePackageName, true);
  }
}
