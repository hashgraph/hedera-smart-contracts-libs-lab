package com.hedera.examples;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.ContractLogInfo;
import com.hedera.hashgraph.sdk.TransactionRecord;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.abi.EventValues;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

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
}
