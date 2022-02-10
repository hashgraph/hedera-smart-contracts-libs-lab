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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.AccountId;
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
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.ReceiptStatusException;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionRecord;
import com.hedera.hashgraph.sdk.TransactionResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public final class Example {

    private static Dotenv dotenv = Dotenv.configure().directory("../").load();
    private static final AccountId OPERATOR_ID = AccountId.fromString(Objects.requireNonNull(dotenv.get("OPERATOR_ID")));
    private static final PrivateKey OPERATOR_KEY = PrivateKey.fromString(Objects.requireNonNull(dotenv.get("OPERATOR_KEY")));
    private static JsonArray abi;
    private static Client client = Client.forTestnet();
    private static Gson gson = new Gson();
    private Example() {
    }

    public static void main(String[] args) throws PrecheckStatusException, TimeoutException, IOException, ReceiptStatusException, InterruptedException {

        InputStream inputstream = new FileInputStream("../contracts/abi.json");
        String abiInFile = readFromInputStream(inputstream);
        abi = gson.fromJson(abiInFile, JsonArray.class);

        client.setOperator(OPERATOR_ID, OPERATOR_KEY);

        client.setDefaultMaxTransactionFee(new Hbar(5));
        client.setDefaultMaxQueryPayment(new Hbar(5));

        // deploy the contract to Hedera from bytecode
        ContractId contractId = deployContract();
        // query the contract's get_message function
        queryGetMessage(contractId);
        // call the contract's set_message function
        callSetMessage(contractId, "Hello again");
        // query the contract's get_message function
        queryGetMessage(contractId);
        // call the contract's get_message function
        callGetMessage(contractId);
        // get call events from a transaction record
        getEventsFromRecord(contractId);
        // get contract events from a mirror node
        getEventsFromMirror(contractId);
    }

    private static ContractId deployContract() throws IOException, PrecheckStatusException, TimeoutException, ReceiptStatusException {
        System.out.println("Deploying contract");
        // Import the compiled contract
        InputStream inputstream = new FileInputStream("../contracts/bytecode.json");
        String bytecodeInFile = readFromInputStream(inputstream);

        JsonObject jsonBytecode = gson.fromJson(bytecodeInFile, JsonObject.class);

        String byteCodeHex = jsonBytecode.getAsJsonPrimitive("object")
                .getAsString();
        byte[] byteCode = byteCodeHex.getBytes(StandardCharsets.UTF_8);

        // create the contract's bytecode file
        TransactionResponse fileTransactionResponse = new FileCreateTransaction()
            // Use the same key as the operator to "own" this file
            .setKeys(OPERATOR_KEY)
            .setContents(byteCode)
            .execute(client);

        TransactionReceipt fileReceipt = fileTransactionResponse.getReceipt(client);
        FileId newFileId = Objects.requireNonNull(fileReceipt.fileId);

        System.out.println("contract bytecode file: " + newFileId);

        // set the input parameter
        Utf8String constructMessage = new Utf8String("Hello Hedera");
        // generate a constructor input
        String encodedConstructorString = FunctionEncoder.encodeConstructor(Arrays.asList(constructMessage));
        // convert hex to byte array
        byte[] encodedConstructorBytes = Hex.decode(encodedConstructorString);

        TransactionResponse contractTransactionResponse = new ContractCreateTransaction()
            .setBytecodeFileId(newFileId)
            .setGas(100_000)
            .setConstructorParameters(encodedConstructorBytes)
            .execute(client);

        TransactionReceipt contractReceipt = contractTransactionResponse.getReceipt(client);
        ContractId newContractId = Objects.requireNonNull(contractReceipt.contractId);

        System.out.println("new contract ID: " + newContractId);

        return newContractId;
    }

    /**
     * Invokes the get_message function of the contract using a query
     * The get_message function doesn't mutate the contract's state, therefore a query can be used
     * @param contractId
     */
    private static void queryGetMessage(ContractId contractId) throws PrecheckStatusException, TimeoutException {
        System.out.println("\nget_message Query");

        // generate function call with function name and parameters
        Function function = new Function("get_message", Arrays.asList(), Arrays.asList(new TypeReference<Utf8String>() {}));
        String encodedFunctionString = FunctionEncoder.encode(function);
        // convert hex to byte array
        byte[] encodedFunctionBytes = Hex.decode(encodedFunctionString.replace("0x", ""));

        // query the contract
        ContractFunctionResult response = new ContractCallQuery()
            .setContractId(contractId)
            .setFunctionParameters(encodedFunctionBytes)
            .setQueryPayment(new Hbar(2))
            .setGas(100000)
            .execute(client);

        String responseHex = Hex.toHexString(response.asBytes());
        List<Type> functionReturnDecoder = FunctionReturnDecoder.decode(responseHex, function.getOutputParameters());
        if (!functionReturnDecoder.isEmpty()) {
            System.out.println((Utf8String) functionReturnDecoder.get(0));
        }
    }

    /**
     * Invokes the get_message function of the contract using a transaction and uses the resulting record to determine
     * the returned value from the function
     * Note: The get_message function doesn't mutate the contract's state, therefore a query could be used, but this shows how to
     * process return values from a contract function that does mutate contract state using a TransactionRecord
     * @param contractId
     */
    private static void callGetMessage(ContractId contractId) throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
        System.out.println("\nget_message transaction");

        // generate function call with function name and parameters
        Function function = new Function("get_message", Arrays.asList(), Arrays.asList(new TypeReference<Utf8String>() {}));
        String encodedFunctionString = FunctionEncoder.encode(function);
        // convert hex to byte array
        byte[] encodedFunctionBytes = Hex.decode(encodedFunctionString.replace("0x", ""));

        TransactionResponse response = new ContractExecuteTransaction()
                .setContractId(contractId)
                .setFunctionParameters(ByteString.copyFrom(encodedFunctionBytes))
                .setGas(100000)
                .execute(client);

        // a record contains the output of the function
        TransactionRecord record = response.getRecord(client);
        // the result of the function call is in record.contractFunctionResult.bytes
        String responseHex = Hex.toHexString(record.contractFunctionResult.asBytes());
        List<Type> functionReturnDecoder = FunctionReturnDecoder.decode(responseHex, function.getOutputParameters());
        if (!functionReturnDecoder.isEmpty()) {
            System.out.println((Utf8String) functionReturnDecoder.get(0));
        }
    }

    /**
     * Invokes the set_message function of the contract
     * @param contractId
     * @param newMessage
     * @returns {Promise<void>}
     */
    private static void callSetMessage(ContractId contractId, String newMessage) throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
        System.out.println("\nCalling set_message with '" + newMessage + " parameter value");

        // generate function call with function name and parameters
        Function function = new Function("set_message", Arrays.asList(new Utf8String(newMessage)), Arrays.asList());
        String encodedFunctionString = FunctionEncoder.encode(function);
        // convert hex to byte array
        byte[] encodedFunctionBytes = Hex.decode(encodedFunctionString.replace("0x", ""));

        TransactionResponse response = new ContractExecuteTransaction()
                .setContractId(contractId)
                .setFunctionParameters(ByteString.copyFrom(encodedFunctionBytes))
                .setGas(100000)
                .execute(client);

        // get the receipt for the transaction
        response.getReceipt(client);
    }

    /**
     * Gets events from a contract function invocation using a TransactionRecord
     * Note: This function calls the contract's set_message function in order to generate a new event
     * @param contractId
     */
    private static void getEventsFromRecord(ContractId contractId) throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
        System.out.println("\nGetting event(s) from record");

        // calling "set_message" with the current date/time to generate a new event
        String newMessage = new Date().toString();
        // generate function call with function name and parameters
        // generate function call with function name and parameters
        Function function = new Function("set_message", Arrays.asList(new Utf8String(newMessage)), Arrays.asList());
        String encodedFunctionString = FunctionEncoder.encode(function);
        // convert hex to byte array
        byte[] encodedFunctionBytes = Hex.decode(encodedFunctionString.replace("0x", ""));

        System.out.println("Calling set_message to trigger new event");
        // execute the transaction calling the set_message contract function
        TransactionResponse response = new ContractExecuteTransaction()
                .setContractId(contractId)
                .setFunctionParameters(ByteString.copyFrom(encodedFunctionBytes))
                .setGas(100000)
                .execute(client);

        // a record contains the output of the function
        // as well as events, let's get events for this transaction
        TransactionRecord record = response.getRecord(client);

        // query the contract's get_message function to witness update
        queryGetMessage(contractId);

        System.out.println("\nEvents' data");

        // the events from the function call are in record.contractFunctionResult.logs.data
        // let's parse the logs using web3.j
        // there may be several log entries
        for (ContractLogInfo contractLogInfo : record.contractFunctionResult.logs) {
            EventValues eventValues = extractEventFromRecord("SetMessage"
                    , Arrays.asList(
                        new TypeReference<Address>(true) {} // the event has an address
                        , new TypeReference<Utf8String>() {} // and a string holding the message
                    )
                    , contractLogInfo);
            outputEventToConsole(eventValues);
        }
    }

    /**
     * Gets all the events for a given ContractId from a mirror node
     * Note: To particular filtering is implemented here, in practice you'd only want to query for events
     * in a time range or from a given timestamp for example
     * @param contractId
     */

    private static void getEventsFromMirror(ContractId contractId) throws InterruptedException, IOException {
        System.out.println("\nGetting event(s) from mirror");
        System.out.println("Waiting 10s to allow transaction propagation to mirror");
        Thread.sleep(10000);

        URL url = new URL("https://testnet.mirrornode.hedera.com/api/v1/contracts/".concat(contractId.toString()).concat("/results/logs?order=asc"));

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();

        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {

            String data = "";
            Scanner scanner = new Scanner(url.openStream());

            //Write all the JSON data into a string using a scanner
            while (scanner.hasNext()) {
                data += scanner.nextLine();
            }

            //Close the scanner
            scanner.close();

            JsonObject response = gson.fromJson(data, JsonObject.class);

            Event event = new Event("SetMessage"
                    , Arrays.asList(
                        new TypeReference<Address>(true) {} // the event has an address
                        , new TypeReference<Utf8String>() {} // and a string holding the message
                    )
            );
            for (JsonElement jsonElement : response.getAsJsonArray("logs")) {
                JsonObject log = jsonElement.getAsJsonObject();
                // decode the event data
                List<String> topics = new ArrayList<>();
                for (JsonElement topicElement : log.getAsJsonArray("topics")) {
                    topics.add(topicElement.getAsString());
                }

                EventValues eventValues = extractEventData(event, topics, log.get("data").getAsString());
                outputEventToConsole(eventValues);
            }
        }
    }

    private static void outputEventToConsole(EventValues eventValues) {
        for (Type eventValue : eventValues.getIndexedValues()) {
            System.out.println(eventValue.toString());
        }
        for (Type eventValue : eventValues.getNonIndexedValues()) {
            System.out.println(eventValue.toString());
        }
        System.out.println("");
    }

    private static EventValues extractEventFromRecord(String eventName, List<TypeReference<?>> parameters, ContractLogInfo contractLogInfo) {
        final List<String> topics = new ArrayList<>();

        Event event = new Event(eventName, parameters);

        // get topics from log
        for (ByteString topic : contractLogInfo.topics) {
            topics.add("0x".concat(Hex.toHexString(topic.toByteArray())));
        }
        String logData = Hex.toHexString(contractLogInfo.data.toByteArray());
        return extractEventData(event, topics, logData);
    }

    private static EventValues extractEventData(Event event, List<String> topics, String logData) {
        // generate an event signature using web3.j
        String encodedEventSignature = EventEncoder.encode(event);
        if (topics == null || topics.size() == 0 || !topics.get(0).equals(encodedEventSignature)) {
            return null;
        }

        List<Type> indexedValues = new ArrayList<>();
        List<Type> nonIndexedValues =
                FunctionReturnDecoder.decode(logData, event.getNonIndexedParameters());

        List<TypeReference<Type>> indexedParameters = event.getIndexedParameters();
        for (int i = 0; i < indexedParameters.size(); i++) {
            Type value =
                    FunctionReturnDecoder.decodeIndexedValue(
                            topics.get(i + 1), indexedParameters.get(i));
            indexedValues.add(value);
        }
        return new EventValues(indexedValues, nonIndexedValues);
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
