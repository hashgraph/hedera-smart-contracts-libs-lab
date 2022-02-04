package com.hedera.test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.8.9-SNAPSHOT.
 */
@SuppressWarnings("rawtypes")
public class ShipIt extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50610262806100206000396000f3006080604052600436106100405763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663d51cd4ac8114610045575b600080fd5b34801561005157600080fd5b5061007373ffffffffffffffffffffffffffffffffffffffff6004351661014c565b6040805173ffffffffffffffffffffffffffffffffffffffff808b1682528916602082015290810187905260608101869052608081018560008111156100b557fe5b60ff168152602001848152602001806020018360001916600019168152602001828103825284818151815260200191508051906020019080838360005b8381101561010a5781810151838201526020016100f2565b50505050905090810190601f1680156101375780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390f35b6000602081815291815260409081902080546001808301546002808501546003860154600487015460058801546006890180548b516101009982161599909902600019011695909504601f81018c90048c0288018c01909a5289875273ffffffffffffffffffffffffffffffffffffffff9788169a97909516989297919660ff9091169591939192908301828280156102265780601f106101fb57610100808354040283529160200191610226565b820191906000526020600020905b81548152906001019060200180831161020957829003601f168201915b50505050509080600701549050885600a165627a7a723058207623bd815501fd75633dfcf1bbcf2f0c1d7d060d5e13c438f1f3fc79d294d5d80029\n";

    public static final String FUNC_SHIPMENTS = "shipments";

    protected ShipIt() {
        super(BINARY);
    }

    @Deprecated
    protected ShipIt(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ShipIt(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ShipIt(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ShipIt(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Tuple8<Address, Address, Uint256, Uint256, Uint8, Uint256, Utf8String, Bytes32>> shipments(Address param0) {
        final Function function = new Function(FUNC_SHIPMENTS, 
                Arrays.<Type>asList(param0), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bytes32>() {}));
        return new RemoteFunctionCall<Tuple8<Address, Address, Uint256, Uint256, Uint8, Uint256, Utf8String, Bytes32>>(function,
                new Callable<Tuple8<Address, Address, Uint256, Uint256, Uint8, Uint256, Utf8String, Bytes32>>() {
                    @Override
                    public Tuple8<Address, Address, Uint256, Uint256, Uint8, Uint256, Utf8String, Bytes32> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple8<Address, Address, Uint256, Uint256, Uint8, Uint256, Utf8String, Bytes32>(
                                (Address) results.get(0), 
                                (Address) results.get(1), 
                                (Uint256) results.get(2), 
                                (Uint256) results.get(3), 
                                (Uint8) results.get(4), 
                                (Uint256) results.get(5), 
                                (Utf8String) results.get(6), 
                                (Bytes32) results.get(7));
                    }
                });
    }

    public String getABI_shipments(Address param0) {
        final Function function = new Function(
                FUNC_SHIPMENTS, 
                Arrays.<Type>asList(param0), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_shipments decodeABI_shipments(String abiToDecode) {
        final Function function = new Function(FUNC_SHIPMENTS, 
                Arrays.asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference <org.web3j.abi.datatypes.Address>() {}, new TypeReference <org.web3j.abi.datatypes.Address>() {}, new TypeReference <org.web3j.abi.datatypes.generated.Uint256>() {}, new TypeReference <org.web3j.abi.datatypes.generated.Uint256>() {}, new TypeReference <org.web3j.abi.datatypes.generated.Uint8>() {}, new TypeReference <org.web3j.abi.datatypes.generated.Uint256>() {}, new TypeReference <org.web3j.abi.datatypes.Utf8String>() {}, new TypeReference <org.web3j.abi.datatypes.generated.Bytes32>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_shipments functionResponse_shipments = new FunctionResponse_shipments();
        functionResponse_shipments.sender = (Address) response.get(0);
        functionResponse_shipments.receiver = (Address) response.get(1);
        functionResponse_shipments.creationDate = (Uint256) response.get(2);
        functionResponse_shipments.departureDate = (Uint256) response.get(3);
        functionResponse_shipments.status = (Uint8) response.get(4);
        functionResponse_shipments.packageWeight = (Uint256) response.get(5);
        functionResponse_shipments.depot = (Utf8String) response.get(6);
        functionResponse_shipments.receiverHash = (Bytes32) response.get(7);
        return functionResponse_shipments;
    }

    @Deprecated
    public static ShipIt load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ShipIt(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ShipIt load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ShipIt(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ShipIt load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ShipIt(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ShipIt load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ShipIt(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ShipIt> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ShipIt.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ShipIt> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ShipIt.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<ShipIt> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ShipIt.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ShipIt> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ShipIt.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class FunctionResponse_shipments {
        public Address sender;

        public Address receiver;

        public Uint256 creationDate;

        public Uint256 departureDate;

        public Uint8 status;

        public Uint256 packageWeight;

        public Utf8String depot;

        public Bytes32 receiverHash;
    }
}
