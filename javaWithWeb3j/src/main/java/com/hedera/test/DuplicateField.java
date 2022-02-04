package com.hedera.test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
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
public class DuplicateField extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50610272806100206000396000f30060806040526004361061006c5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde038114610071578063313ce567146100fb5780638052474d14610126578063a3f4df7e1461013b578063f76f8d7814610150575b600080fd5b34801561007d57600080fd5b50610086610165565b6040805160208082528351818301528351919283929083019185019080838360005b838110156100c05781810151838201526020016100a8565b50505050905090810190601f1680156100ed5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561010757600080fd5b5061011061019c565b6040805160ff9092168252519081900360200190f35b34801561013257600080fd5b506100866101a1565b34801561014757600080fd5b506100866101d8565b34801561015c57600080fd5b5061008661020f565b60408051808201909152600181527f4100000000000000000000000000000000000000000000000000000000000000602082015290565b600090565b60408051808201909152600181527f4200000000000000000000000000000000000000000000000000000000000000602082015281565b60408051808201909152600181527f4100000000000000000000000000000000000000000000000000000000000000602082015281565b60408051808201909152600481527f534d424c000000000000000000000000000000000000000000000000000000006020820152815600a165627a7a723058203282bedce61f44c2fb398d85e3179106cd6f9ae6aeceb89d1b894a33a93a61610029\n";

    public static final String FUNC_name = "name";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_Name = "Name";

    public static final String FUNC_NAME = "NAME";

    public static final String FUNC_SYMBOL = "SYMBOL";

    protected DuplicateField() {
        super(BINARY);
    }

    @Deprecated
    protected DuplicateField(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DuplicateField(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected DuplicateField(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected DuplicateField(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Utf8String> name() {
        final Function function = new Function(FUNC_name, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public String getABI_name() {
        final Function function = new Function(
                FUNC_name, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_name decodeABI_name(String abiToDecode) {
        final Function function = new Function(FUNC_name, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_name functionResponse_name = new FunctionResponse_name();
        functionResponse_name.return1 = (Utf8String) response.get(0);
        return functionResponse_name;
    }

    public RemoteFunctionCall<Uint8> decimals() {
        final Function function = new Function(FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public String getABI_decimals() {
        final Function function = new Function(
                FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_decimals decodeABI_decimals(String abiToDecode) {
        final Function function = new Function(FUNC_DECIMALS, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_decimals functionResponse_decimals = new FunctionResponse_decimals();
        functionResponse_decimals.return1 = (Uint8) response.get(0);
        return functionResponse_decimals;
    }

    public RemoteFunctionCall<Utf8String> Name() {
        final Function function = new Function(FUNC_Name, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public String getABI_Name() {
        final Function function = new Function(
                FUNC_Name, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_Name decodeABI_Name(String abiToDecode) {
        final Function function = new Function(FUNC_Name, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_Name functionResponse_Name = new FunctionResponse_Name();
        functionResponse_Name.return1 = (Utf8String) response.get(0);
        return functionResponse_Name;
    }

    public RemoteFunctionCall<Utf8String> NAME() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public String getABI_NAME() {
        final Function function = new Function(
                FUNC_NAME, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_NAME decodeABI_NAME(String abiToDecode) {
        final Function function = new Function(FUNC_NAME, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_NAME functionResponse_NAME = new FunctionResponse_NAME();
        functionResponse_NAME.return1 = (Utf8String) response.get(0);
        return functionResponse_NAME;
    }

    public RemoteFunctionCall<Utf8String> SYMBOL() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public String getABI_SYMBOL() {
        final Function function = new Function(
                FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_SYMBOL decodeABI_SYMBOL(String abiToDecode) {
        final Function function = new Function(FUNC_SYMBOL, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_SYMBOL functionResponse_SYMBOL = new FunctionResponse_SYMBOL();
        functionResponse_SYMBOL.return1 = (Utf8String) response.get(0);
        return functionResponse_SYMBOL;
    }

    @Deprecated
    public static DuplicateField load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DuplicateField(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static DuplicateField load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DuplicateField(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static DuplicateField load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DuplicateField(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static DuplicateField load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DuplicateField(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<DuplicateField> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DuplicateField.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<DuplicateField> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DuplicateField.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<DuplicateField> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(DuplicateField.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<DuplicateField> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(DuplicateField.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class FunctionResponse_name {
        public Utf8String return1;
    }

    public static class FunctionResponse_decimals {
        public Uint8 return1;
    }

    public static class FunctionResponse_Name {
        public Utf8String return1;
    }

    public static class FunctionResponse_NAME {
        public Utf8String return1;
    }

    public static class FunctionResponse_SYMBOL {
        public Utf8String return1;
    }
}
