package com.hedera.examples;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import com.hedera.examples.Stateful.FunctionResponse_get_message;
import com.hedera.examples.Stateful.SetMessageEventResponse;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.ContractCallQuery;
import com.hedera.hashgraph.sdk.ContractCreateTransaction;
import com.hedera.hashgraph.sdk.ContractExecuteTransaction;
import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.ContractId;
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
import org.web3j.protocol.hedera.utils.Utils;

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
        statefulContract = (Stateful) Utils.contract(Stateful.class);

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
        // Import the compiled contract
        byte[] byteCode = Utils.contractByteCode(statefulContract);

        // create the contract's bytecode file
        TransactionResponse fileTransactionResponse = new FileCreateTransaction()
            // Use the same key as the operator to "own" this file
            .setKeys(OPERATOR_KEY)
            .setContents(byteCode)
            .execute(client);

        TransactionReceipt fileReceipt = fileTransactionResponse.getReceipt(client);
        FileId newFileId = Objects.requireNonNull(fileReceipt.fileId);

        System.out.println("contract bytecode file: " + newFileId);

        // generate a constructor input using web3j generated class
        byte[] encodedConstructorBytes = Utils.functionBytes(statefulContract.getABI_Constructor("Hello Hedera"));

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
     * @param contractId the contractId to invoke
     */
    private static void queryGetMessage(ContractId contractId) throws PrecheckStatusException, TimeoutException {
        System.out.println("\nget_message Query");

        // get the function call
        byte[] encodedFunctionBytes = Utils.functionBytes(statefulContract.getABI_get_message());

        // query the contract
        ContractFunctionResult response = new ContractCallQuery()
            .setContractId(contractId)
            .setFunctionParameters(encodedFunctionBytes)
            .setQueryPayment(new Hbar(2))
            .setGas(100000)
            .execute(client);

        // get the response from the query
        String responseHex = Hex.toHexString(response.asBytes());

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
        ByteString encodedFunction = Utils.functionByteString(statefulContract.getABI_get_message());

        TransactionResponse response = new ContractExecuteTransaction()
                .setContractId(contractId)
                .setFunctionParameters(encodedFunction)
                .setGas(100000)
                .execute(client);

        // a record contains the output of the function
        TransactionRecord record = response.getRecord(client);

        // the result of the function call is in record.contractFunctionResult.bytes
        String responseHex = Hex.toHexString(record.contractFunctionResult.asBytes());

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
        ByteString encodedFunction = Utils.functionByteString(statefulContract.getABI_set_message(newMessage));

        TransactionResponse response = new ContractExecuteTransaction()
                .setContractId(contractId)
                .setFunctionParameters(encodedFunction)
                .setGas(100000)
                .execute(client);

        // get the receipt for the transaction
        response.getReceipt(client);
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
        ByteString encodedFunction = Utils.functionByteString(statefulContract.getABI_set_message(newMessage));

        System.out.println("Calling set_message to trigger new event");
        // execute the transaction calling the set_message contract function
        TransactionResponse response = new ContractExecuteTransaction()
                .setContractId(contractId)
                .setFunctionParameters(encodedFunction)
                .setGas(100000)
                .execute(client);

        // a record contains the output of the function
        // as well as events, let's get events for this transaction
        TransactionRecord transactionRecord = response.getRecord(client);

        // query the contract's get_message function to witness update
        queryGetMessage(contractId);

        System.out.println("\nEvents' data");

        // convert the transaction record to a web3jTransactionReceipt
        org.web3j.protocol.core.methods.response.TransactionReceipt web3jTransactionReceipt = HederaUtils.web3jTransactionReceiptFromRecord(transactionRecord);
        List<SetMessageEventResponse> setMessageEventResponses = statefulContract.getSetMessageEvents(web3jTransactionReceipt);
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

            // convert the mirror logs to a web3jTransactionReceipt
            org.web3j.protocol.core.methods.response.TransactionReceipt web3jTransactionReceipt = HederaUtils.web3TransactionReceiptFromMirrorLogs(mirrorResponse);
            List<SetMessageEventResponse> setMessageEventResponses = statefulContract.getSetMessageEvents(web3jTransactionReceipt);
            for (SetMessageEventResponse setMessageEventResponse : setMessageEventResponses) {
                System.out.println("From (AccountId): " + setMessageEventResponse.from + " (" + AccountId.fromSolidityAddress(setMessageEventResponse.from).toString() + ")");
                System.out.println("Message : " + setMessageEventResponse.message);
            }
        }
    }
}
