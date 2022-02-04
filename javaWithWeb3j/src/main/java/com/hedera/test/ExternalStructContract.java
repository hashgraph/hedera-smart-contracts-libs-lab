package com.hedera.test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
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
public class ExternalStructContract extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b506040518060400160405280600181526020016040518060400160405280600181526020017f31000000000000000000000000000000000000000000000000000000000000008152508152506000808201518160000155602082015181600101908051906020019061008392919061008c565b50905050610190565b8280546100989061012f565b90600052602060002090601f0160209004810192826100ba5760008555610101565b82601f106100d357805160ff1916838001178555610101565b82800160010185558215610101579182015b828111156101005782518255916020019190600101906100e5565b5b50905061010e9190610112565b5090565b5b8082111561012b576000816000905550600101610113565b5090565b6000600282049050600182168061014757607f821691505b6020821081141561015b5761015a610161565b5b50919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b6104d68061019f6000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80636d4ce63c1461003b5780638c2c06e714610059575b600080fd5b610043610075565b60405161005091906102d7565b60405180910390f35b610073600480360381019061006e91906101fe565b610080565b005b61007d6100c4565b90565b604051806040016040528083815260200182815250600080820151816000015560208201518160010190805190602001906100bc9291906100de565b509050505050565b604051806040016040528060008152602001606081525090565b8280546100ea906103b7565b90600052602060002090601f01602090048101928261010c5760008555610153565b82601f1061012557805160ff1916838001178555610153565b82800160010185558215610153579182015b82811115610152578251825591602001919060010190610137565b5b5090506101609190610164565b5090565b5b8082111561017d576000816000905550600101610165565b5090565b600061019461018f8461031e565b6102f9565b9050828152602081018484840111156101ac57600080fd5b6101b7848285610375565b509392505050565b6000813590506101ce81610489565b92915050565b600082601f8301126101e557600080fd5b81356101f5848260208601610181565b91505092915050565b6000806040838503121561021157600080fd5b600061021f858286016101bf565b925050602083013567ffffffffffffffff81111561023c57600080fd5b610248858286016101d4565b9150509250929050565b61025b8161036b565b82525050565b600061026c8261034f565b610276818561035a565b9350610286818560208601610384565b61028f81610478565b840191505092915050565b60006040830160008301516102b26000860182610252565b50602083015184820360208601526102ca8282610261565b9150508091505092915050565b600060208201905081810360008301526102f1818461029a565b905092915050565b6000610303610314565b905061030f82826103e9565b919050565b6000604051905090565b600067ffffffffffffffff82111561033957610338610449565b5b61034282610478565b9050602081019050919050565b600081519050919050565b600082825260208201905092915050565b6000819050919050565b82818337600083830152505050565b60005b838110156103a2578082015181840152602081019050610387565b838111156103b1576000848401525b50505050565b600060028204905060018216806103cf57607f821691505b602082108114156103e3576103e261041a565b5b50919050565b6103f282610478565b810181811067ffffffffffffffff8211171561041157610410610449565b5b80604052505050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602260045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b6000601f19601f8301169050919050565b6104928161036b565b811461049d57600080fd5b5056fea26469706673582212206e7531c2deb4322587da51cf4d6505980ca1361e45d59b97e986227c9f3eb93164736f6c63430008040033\n";

    public static final String FUNC_GET = "get";

    public static final String FUNC_SET = "set";

    protected ExternalStructContract() {
        super(BINARY);
    }

    @Deprecated
    protected ExternalStructContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ExternalStructContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ExternalStructContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ExternalStructContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<ExternalStruct> get() {
        final Function function = new Function(FUNC_GET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<ExternalStruct>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public String getABI_get() {
        final Function function = new Function(
                FUNC_GET, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_get decodeABI_get(String abiToDecode) {
        final Function function = new Function(FUNC_GET, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<ExternalStruct>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_get functionResponse_get = new FunctionResponse_get();
        functionResponse_get.ext = (ExternalStruct) response.get(0);
        return functionResponse_get;
    }

    public RemoteFunctionCall<TransactionReceipt> set(Int256 id_int, Utf8String id_ext) {
        final Function function = new Function(
                FUNC_SET, 
                Arrays.<Type>asList(id_int, id_ext), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public String getABI_set(Int256 id_int, Utf8String id_ext) {
        final Function function = new Function(
                FUNC_SET, 
                Arrays.<Type>asList(id_int, id_ext), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    @Deprecated
    public static ExternalStructContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ExternalStructContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ExternalStructContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ExternalStructContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ExternalStructContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ExternalStructContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ExternalStructContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ExternalStructContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ExternalStructContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ExternalStructContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<ExternalStructContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ExternalStructContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ExternalStructContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ExternalStructContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ExternalStructContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ExternalStructContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class ExternalStruct extends DynamicStruct {
        public Int256 id_int;

        public Utf8String id_ext;

        public ExternalStruct(Int256 id_int, Utf8String id_ext) {
            super(id_int, id_ext);
            this.id_int = id_int;
            this.id_ext = id_ext;
        }
    }

    public static class FunctionResponse_get {
        public ExternalStruct ext;
    }
}