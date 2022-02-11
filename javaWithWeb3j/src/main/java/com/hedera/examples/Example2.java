package com.hedera.examples;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.ContractCallQuery;
import com.hedera.hashgraph.sdk.ContractCreateTransaction;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileCreateTransaction;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.ReceiptStatusException;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.web3j.protocol.hedera.utils.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public final class Example2 {

    private static final Dotenv dotenv = Dotenv.configure().directory("../").load();
    private static final AccountId OPERATOR_ID = AccountId.fromString(Objects.requireNonNull(dotenv.get("OPERATOR_ID")));
    private static final PrivateKey OPERATOR_KEY = PrivateKey.fromString(Objects.requireNonNull(dotenv.get("OPERATOR_KEY")));
    private static final Client client = Client.forTestnet();
    // Instantiate an object using web3j generated code
    private static Stateful statefulContract;

    private Example2() {
    }

    public static void main(String[] args) throws PrecheckStatusException, TimeoutException, ReceiptStatusException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        statefulContract = (Stateful) Utils.contract(Stateful.class);

        client.setOperator(OPERATOR_ID, OPERATOR_KEY);

        client.setDefaultMaxTransactionFee(new Hbar(5));
        client.setDefaultMaxQueryPayment(new Hbar(5));

        // deploy the contract to Hedera from bytecode
        ContractId contractId = deployContract();
        queryGetMessage(contractId);
        querySetMessage(contractId, "hi there");
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
        byte[] constructor = Utils.functionBytes(statefulContract.getABI_Constructor("Hello Hedera"));

        TransactionResponse contractTransactionResponse = new ContractCreateTransaction()
            .setBytecodeFileId(newFileId)
            .setGas(100_000)
            .setConstructorParameters(constructor)
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
        byte[] functionBytes = Utils.functionBytes(statefulContract.getABI_get_message());

        // query the contract
        Hbar response = new ContractCallQuery()
            .setContractId(contractId)
            .setFunctionParameters(functionBytes)
            .setQueryPayment(new Hbar(2))
            .setGas(100000)
            .getCost(client);

        // get the response from the query
        System.out.println(response.toString());
    }

    private static void querySetMessage(ContractId contractId, String newMessage) throws PrecheckStatusException, TimeoutException, ReceiptStatusException {

        // generate function call with function name and parameters
        byte[] functionBytes = Utils.functionBytes(statefulContract.getABI_set_message(newMessage));

        Hbar response = new ContractCallQuery()
                .setContractId(contractId)
                .setFunctionParameters(functionBytes)
                .setGas(100000)
                .getCost(client);

        // get the receipt for the transaction
        System.out.println(response.toString());
    }
}
