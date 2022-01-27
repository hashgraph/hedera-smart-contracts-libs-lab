package com.hedera.examples;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.ContractCallQuery;
import com.hedera.hashgraph.sdk.ContractCreateTransaction;
import com.hedera.hashgraph.sdk.ContractExecuteTransaction;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
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
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;

import javax.management.ConstructorParameters;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

    public static void main(String[] args) throws PrecheckStatusException, TimeoutException, IOException, ReceiptStatusException {

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
//        // get call events from a transaction record
//        await getEventsFromRecord(contractId);
//        // get contract events from a mirror node
//        await getEventsFromMirror(contractId);
//
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
