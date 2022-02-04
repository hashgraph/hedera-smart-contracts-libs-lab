package com.hedera.examples;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.ContractLogInfo;
import com.hedera.hashgraph.sdk.TransactionRecord;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.codegen.SolidityFunctionWrapperGenerator;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Utils {
  public static TransactionReceipt web3jTransactionReceiptFromRecord(TransactionRecord transactionRecord) {
    TransactionReceipt transactionReceipt = new TransactionReceipt();
    List<Log> logs = new ArrayList<>();

    for (ContractLogInfo contractLogInfo : transactionRecord.contractFunctionResult.logs) {
      Log log = new Log();
      log.setData(Hex.toHexString(contractLogInfo.data.toByteArray()));
      List<String> topics = new ArrayList<>();
      for (ByteString topic : contractLogInfo.topics) {
        topics.add("0x".concat(Hex.toHexString(topic.toByteArray())));
      }
      log.setTopics(topics);
      logs.add(log);
    }
    transactionReceipt.setLogs(logs);
    return transactionReceipt;
  }

  public static TransactionReceipt web3TransactionReceiptFromMirrorLogs(JsonObject mirrorResponse) {
    TransactionReceipt transactionReceipt = new TransactionReceipt();
    List<Log> logs = new ArrayList<>();

    for (JsonElement jsonElement : mirrorResponse.getAsJsonArray("logs")) {
      JsonObject mirrorLog = jsonElement.getAsJsonObject();

      Log log = new Log();
      log.setData(mirrorLog.get("data").getAsString());

      // decode the event data
      List<String> topics = new ArrayList<>();
      for (JsonElement topic : mirrorLog.getAsJsonArray("topics")) {
        topics.add(topic.getAsString());
      }
      log.setTopics(topics);

      logs.add(log);
    }
    transactionReceipt.setLogs(logs);
    return transactionReceipt;
  }

    public static void wrap(String abiFileLocation, String byteCodeFileLocation, String destination, String contractName, String basePackageName, boolean javaNative) throws IOException, ClassNotFoundException {

      File abiFile = new File(abiFileLocation);
      File destinationDir = new File(destination);
      boolean useJavaNativeTypes = javaNative;
      boolean useJavaPrimitiveTypes = javaNative;
      int addressLength = 20;
      boolean abiFuncs = false;

      InputStream inputstream = new FileInputStream(byteCodeFileLocation);
      String byteCodeHex = Utils.readFromInputStream(inputstream);
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
