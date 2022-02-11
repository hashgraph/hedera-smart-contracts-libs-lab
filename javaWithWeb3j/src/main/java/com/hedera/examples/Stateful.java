package com.hedera.examples;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
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
public class Stateful extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b506040516106253803806106258339818101604052602081101561003357600080fd5b810190808051604051939291908464010000000082111561005357600080fd5b8382019150602082018581111561006957600080fd5b825186600182028301116401000000008211171561008657600080fd5b8083526020830192505050908051906020019080838360005b838110156100ba57808201518184015260208101905061009f565b50505050905090810190601f1680156100e75780820380516001836020036101000a031916815260200191505b506040525050503373ffffffffffffffffffffffffffffffffffffffff167f3c55514de50d14506d6b7e1d1e94ee2f674c70c69370e5fe71a0d9de7c883854826040518080602001828103825283818151815260200191508051906020019080838360005b8381101561016757808201518184015260208101905061014c565b50505050905090810190601f1680156101945780820380516001836020036101000a031916815260200191505b509250505060405180910390a280600090805190602001906101b79291906101be565b505061025b565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106101ff57805160ff191683800117855561022d565b8280016001018555821561022d579182015b8281111561022c578251825591602001919060010190610211565b5b50905061023a919061023e565b5090565b5b8082111561025757600081600090555060010161023f565b5090565b6103bb8061026a6000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c80632e9826021461003b57806332af2edb146100f6575b600080fd5b6100f46004803603602081101561005157600080fd5b810190808035906020019064010000000081111561006e57600080fd5b82018360208201111561008057600080fd5b803590602001918460018302840111640100000000831117156100a257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610179565b005b6100fe610246565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561013e578082015181840152602081019050610123565b50505050905090810190601f16801561016b5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3373ffffffffffffffffffffffffffffffffffffffff167f3c55514de50d14506d6b7e1d1e94ee2f674c70c69370e5fe71a0d9de7c883854826040518080602001828103825283818151815260200191508051906020019080838360005b838110156101f25780820151818401526020810190506101d7565b50505050905090810190601f16801561021f5780820380516001836020036101000a031916815260200191505b509250505060405180910390a280600090805190602001906102429291906102e8565b5050565b606060008054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102de5780601f106102b3576101008083540402835291602001916102de565b820191906000526020600020905b8154815290600101906020018083116102c157829003601f168201915b5050505050905090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061032957805160ff1916838001178555610357565b82800160010185558215610357579182015b8281111561035657825182559160200191906001019061033b565b5b5090506103649190610368565b5090565b5b80821115610381576000816000905550600101610369565b509056fea26469706673582212208720a7384c368cb3f32b241cf582a02665fb7d67e933bd6b50b3fbe1b1e81e0864736f6c634300060c0033";

    public static final String FUNC_GET_MESSAGE = "get_message";

    public static final String FUNC_SET_MESSAGE = "set_message";

    public static final Event SETMESSAGE_EVENT = new Event("SetMessage", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected Stateful(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Stateful(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Stateful(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Stateful(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<SetMessageEventResponse> getSetMessageEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SETMESSAGE_EVENT, transactionReceipt);
        ArrayList<SetMessageEventResponse> responses = new ArrayList<SetMessageEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SetMessageEventResponse typedResponse = new SetMessageEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.message = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SetMessageEventResponse> setMessageEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SetMessageEventResponse>() {
            @Override
            public SetMessageEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SETMESSAGE_EVENT, log);
                SetMessageEventResponse typedResponse = new SetMessageEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.message = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<SetMessageEventResponse> setMessageEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SETMESSAGE_EVENT));
        return setMessageEventFlowable(filter);
    }

    public RemoteFunctionCall<String> get_message() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GET_MESSAGE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public String getABI_get_message() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GET_MESSAGE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_get_message decodeABI_get_message(String abiToDecode) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GET_MESSAGE, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_get_message functionResponse_get_message = new FunctionResponse_get_message();
        functionResponse_get_message.messageOut = (String) response.get(0).getValue();
        return functionResponse_get_message;
    }

    public RemoteFunctionCall<TransactionReceipt> set_message(String message_) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SET_MESSAGE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(message_)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public String getABI_set_message(String message_) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SET_MESSAGE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(message_)), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    @Deprecated
    public static Stateful load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Stateful(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Stateful load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Stateful(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Stateful load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Stateful(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Stateful load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Stateful(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Stateful> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String message_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(message_)));
        return deployRemoteCall(Stateful.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Stateful> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String message_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(message_)));
        return deployRemoteCall(Stateful.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Stateful> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String message_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(message_)));
        return deployRemoteCall(Stateful.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Stateful> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String message_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(message_)));
        return deployRemoteCall(Stateful.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public String getABI_Constructor(String message_) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(message_)));
        return encodedConstructor;
    }

    public static class SetMessageEventResponse extends BaseEventResponse {
        public String from;

        public String message;
    }

    public static class FunctionResponse_get_message {
        public String messageOut;
    }
}
