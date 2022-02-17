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

package com.hedera.web3j.protocol;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.ContractCallQuery;
import com.hedera.hashgraph.sdk.ContractCreateTransaction;
import com.hedera.hashgraph.sdk.ContractExecuteTransaction;
import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.ContractLogInfo;
import com.hedera.hashgraph.sdk.FileCreateTransaction;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.ReceiptStatusException;
import com.hedera.hashgraph.sdk.TransactionRecord;
import com.hedera.hashgraph.sdk.TransactionResponse;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.hedera.HederaRPC;
import org.web3j.protocol.hedera.gas.HederaGasProvider;
import org.web3j.protocol.hedera.tx.HederaTransactionManager;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public class Web3jUtils {

  /**
    Class to encapsulate a response from a contract call which includes
    the record for the transaction as well as the function result
   */
  public static class CallResponse {
    TransactionRecord record;
    String functionResult;

    public CallResponse(TransactionRecord transactionRecord) {
      this.functionResult = Hex.toHexString(transactionRecord.contractFunctionResult.asBytes());
      this.record = transactionRecord;
    }

    public TransactionRecord getRecord() {
      return this.record;
    }
    public String getFunctionResult() {
      return this.functionResult;
    }
  }

  /**
   * Deploys a contract given the parameters below
   * @param client the client to use to connect to a Hedera node
   * @param contractObject the instance of the smart contract java object
   * @param constructorParameters the parameters for the constructor call
   * @param deployGas the gas to use for deployment
   * @return ContractId, the id of the created contract
   * @throws PrecheckStatusException in the event of a precheck error
   * @throws TimeoutException in the event of a timeout error
   * @throws ReceiptStatusException in the event of a transaction execution error
   */
  public static ContractId deploy(Client client, Contract contractObject, String constructorParameters, long deployGas) throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
    System.out.println("Deploying contract");
    // Import the compiled contract
    byte[] byteCode = contractObject.getContractBinary().getBytes(StandardCharsets.UTF_8);

    // create the contract's bytecode file
    TransactionResponse fileTransactionResponse = new FileCreateTransaction()
            // Use the same key as the operator to "own" this file
            .setKeys(client.getOperatorPublicKey())
            .setContents(byteCode)
            .execute(client);

    com.hedera.hashgraph.sdk.TransactionReceipt fileReceipt = fileTransactionResponse.getReceipt(client);
    FileId newFileId = Objects.requireNonNull(fileReceipt.fileId);

    System.out.println("contract bytecode file: " + newFileId);

    // generate a constructor input using web3j generated class
    byte[] encodedConstructorBytes = functionBytes(constructorParameters);

    TransactionResponse contractTransactionResponse = new ContractCreateTransaction()
            .setBytecodeFileId(newFileId)
            .setGas(deployGas)
            .setConstructorParameters(encodedConstructorBytes)
            .execute(client);

    com.hedera.hashgraph.sdk.TransactionReceipt contractReceipt = contractTransactionResponse.getReceipt(client);
    ContractId newContractId = Objects.requireNonNull(contractReceipt.contractId);

    return newContractId;
  }

  /**
   * Queries a smart contract given the parameters below
   * @param client the client to use to connect to a Hedera node
   * @param contractId the id of the contract to call
   * @param functionParameters the parameters for the function call
   * @param gas the gas to use for the call
   * @param queryPayment the query payment in Hbar
   * @return the result of the function call
   * @throws PrecheckStatusException in the event of a precheck error
   * @throws TimeoutException in the event of a timeout error
   */
  public static String queryContract(Client client, ContractId contractId, String functionParameters, long gas, Hbar queryPayment) throws PrecheckStatusException, TimeoutException {
      byte[] encodedFunctionBytes = Web3jUtils.functionBytes(functionParameters);

      // query the contract
      ContractFunctionResult response = new ContractCallQuery()
              .setContractId(contractId)
              .setFunctionParameters(encodedFunctionBytes)
              .setQueryPayment(queryPayment)
              .setGas(gas)
              .execute(client);

      // get the response from the query
      return Hex.toHexString(response.asBytes());
  }

  /**
   * @param client the client to use to connect to a Hedera node
   * @param contractId the id of the contract to call
   * @param functionParameters the parameters for the function call
   * @param gas the gas to use for the call
   * @return
   * @throws PrecheckStatusException in the event of a precheck error
   * @throws TimeoutException in the event of a timeout error
   * @throws ReceiptStatusException in the event of a transaction execution error
   */
  public static CallResponse callContract(Client client, ContractId contractId, String functionParameters, long gas) throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
    ByteString encodedFunction = ByteString.copyFrom(functionBytes(functionParameters));

    TransactionResponse response = new ContractExecuteTransaction()
            .setContractId(contractId)
            .setFunctionParameters(encodedFunction)
            .setGas(gas)
            .execute(client);

    // a record contains the output of the function
    TransactionRecord record = response.getRecord(client);

    return new CallResponse(record);
  }

  /**
   * converts a string representation of function parameters to a byte array
   * @param function the function parameters
   * @return byte array
   */
  public static byte[] functionBytes(String function) {
    return Hex.decode(function.replace("0x", ""));
  }

  /**
   * Returns a contract object instance, wrapping the details of web3j.
   * @param contractClass the class of the contract instance to obtain
   * @return an instance of contractClass
   * @throws NoSuchMethodException in the event the contract class doesn't have the specified method
   * @throws InvocationTargetException in the event the instantiation fails
   * @throws InstantiationException in the event the instantiation fails
   * @throws IllegalAccessException in the event the instantiation fails
   * @throws ClassNotFoundException in the event the specified contract class doesn't exist
   */
  public static Object contract(Class contractClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    HederaRPC hederaRPC = new HederaRPC();
    String dummyAddress = "0000000000000000000000000000000000000000";
    HederaTransactionManager hederaTransactionManager = new HederaTransactionManager(hederaRPC, dummyAddress);

    Class[] argumentList = new Class[4];
    argumentList[0] = String.class;
    argumentList[1] = Web3j.class;
    argumentList[2] = TransactionManager.class;
    argumentList[3] = ContractGasProvider.class;

    Constructor<?> myConstructor = Class.forName(contractClass.getName()).
            getDeclaredConstructor(argumentList);

    myConstructor.setAccessible(true);

    return myConstructor.newInstance(dummyAddress, hederaRPC, hederaTransactionManager, new HederaGasProvider());
  }

  /**
   * Generates a web3j TransactionReceipt from a HederaTransactionRecord
   * @param transactionRecord a Hedera TransactionRecord object
   * @return a web3j TransactionReceipt
   */
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

  /**
   * Generates a web3j TransactionReceipt from a Hedera mirror Json response
   * @param mirrorResponse JsonObject from a mirror node
   * @return a web3j TransactionReceipt
   */
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
