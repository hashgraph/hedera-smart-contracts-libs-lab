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
import com.google.gson.JsonObject;
import com.hedera.examples.Stateful.FunctionResponse_get_message;
import com.hedera.examples.Stateful.SetMessageEventResponse;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.ReceiptStatusException;
import com.hedera.hashgraph.sdk.TransactionRecord;
import com.hedera.web3j.protocol.Web3jUtils;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public final class Example {

    private static final Dotenv dotenv = Dotenv.configure().directory("../").load();
    private static final AccountId OPERATOR_ID = AccountId.fromString(Objects.requireNonNull(dotenv.get("OPERATOR_ID")));
    private static final PrivateKey OPERATOR_KEY = PrivateKey.fromString(Objects.requireNonNull(dotenv.get("OPERATOR_KEY")));
    private static final Client client = Client.forTestnet();
    private static final Gson gson = new Gson();
    // Instantiate an object using web3j generated code
    private static Stateful statefulContract;

    private Example() {
    }

    public static void main(String[] args) throws PrecheckStatusException, TimeoutException, ReceiptStatusException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, InterruptedException {
        statefulContract = (Stateful) Web3jUtils.contract(Stateful.class);

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

    private static ContractId deployContract() throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
        System.out.println("Deploying contract");

        // generate a constructor input using web3j generated class
        String constructor = statefulContract.getABI_Constructor("Hello Hedera");
        // deploy the contract
        ContractId newContractId = Web3jUtils.deploy(client, statefulContract, constructor, 100_000);

        return newContractId;
    }

    /**
     * Invokes the get_message function of the contract using a query
     * The get_message function doesn't mutate the contract's state, therefore a query can be used
     * @param contractId the contractId to invoke
     */
    private static void queryGetMessage(ContractId contractId) throws PrecheckStatusException, TimeoutException {
        System.out.println("\nget_message Query");

        // get the function call
        String functionParameters = statefulContract.getABI_get_message();

        String responseHex = Web3jUtils.queryContract(client, contractId, functionParameters, 100_000, new Hbar(2));

        // decode the response to a List of Type
        FunctionResponse_get_message decodedResponse = statefulContract.decodeABI_get_message(responseHex);
        System.out.println(decodedResponse.messageOut);
    }

    /**
     * Invokes the get_message function of the contract using a transaction and uses the resulting record to determine
     * the returned value from the function
     * Note: The get_message function doesn't mutate the contract's state, therefore a query could be used, but this shows how to
     * process return values from a contract function that does mutate contract state using a TransactionRecord
     * @param contractId the contract to invoke
     */
    private static void callGetMessage(ContractId contractId) throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
        System.out.println("\nget_message transaction");

        // get the function call
        String functionParameters = statefulContract.getABI_get_message();

        String responseHex = Web3jUtils.callContract(client, contractId, functionParameters, 100_000).getFunctionResult();

        // decode the response to a List of Type
        FunctionResponse_get_message decodedResponse = statefulContract.decodeABI_get_message(responseHex);
        System.out.println(decodedResponse.messageOut);
    }

    /**
     * Invokes the set_message function of the contract
     * @param contractId the contract to invoke
     * @param newMessage the new message value to set
     */
    private static void callSetMessage(ContractId contractId, String newMessage) throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
        System.out.println("\nCalling set_message with '" + newMessage + " parameter value");

        // generate function call with function name and parameters
        String functionParameters = statefulContract.getABI_set_message(newMessage);

        String responseHex = Web3jUtils.callContract(client, contractId, functionParameters, 100_000).getFunctionResult();
    }

    /**
     * Gets events from a contract function invocation using a TransactionRecord
     * Note: This function calls the contract's set_message function in order to generate a new event
     * @param contractId the contract to invoke
     */
    private static void getEventsFromRecord(ContractId contractId) throws PrecheckStatusException, TimeoutException, ReceiptStatusException {
        System.out.println("\nGetting event(s) from record");

        // calling "set_message" with the current date/time to generate a new event
        String newMessage = new Date().toString();
        // generate function call with function name and parameters
        String functionParameters = statefulContract.getABI_set_message(newMessage);

        System.out.println("Calling set_message to trigger new event");

        TransactionRecord transactionRecord = Web3jUtils.callContract(client, contractId, functionParameters, 100_000).getRecord();

        // query the contract's get_message function to witness update
        queryGetMessage(contractId);

        System.out.println("\nEvents' data");

        List<SetMessageEventResponse> setMessageEventResponses = statefulContract.getSetMessageEvents(Web3jUtils.web3jTransactionReceiptFromRecord(transactionRecord));
        for (SetMessageEventResponse setMessageEventResponse : setMessageEventResponses) {
            System.out.println("From (AccountId): " + setMessageEventResponse.from + " (" + AccountId.fromSolidityAddress(setMessageEventResponse.from).toString() + ")");
            System.out.println("Message : " + setMessageEventResponse.message);
        }
    }

    /**
     * Gets all the events for a given ContractId from a mirror node
     * Note: To particular filtering is implemented here, in practice you'd only want to query for events
     * in a time range or from a given timestamp for example
     * @param contractId the contract to invoke
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

            StringBuilder data = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            //Write all the JSON data into a string using a scanner
            while (scanner.hasNext()) {
                data.append(scanner.nextLine());
            }

            //Close the scanner
            scanner.close();

            JsonObject mirrorResponse = gson.fromJson(data.toString(), JsonObject.class);

            List<SetMessageEventResponse> setMessageEventResponses = statefulContract.getSetMessageEvents(Web3jUtils.web3TransactionReceiptFromMirrorLogs(mirrorResponse));
            for (SetMessageEventResponse setMessageEventResponse : setMessageEventResponses) {
                System.out.println("From (AccountId): " + setMessageEventResponse.from + " (" + AccountId.fromSolidityAddress(setMessageEventResponse.from).toString() + ")");
                System.out.println("Message : " + setMessageEventResponse.message);
            }
        }
    }
}
