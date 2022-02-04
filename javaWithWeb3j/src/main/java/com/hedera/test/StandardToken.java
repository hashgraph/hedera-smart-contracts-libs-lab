package com.hedera.test;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
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
public class StandardToken extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b506103d6806100206000396000f3006080604052600436106100775763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663095ea7b3811461007c57806318160ddd146100b457806323b872dd146100db57806370a0823114610105578063a9059cbb14610126578063dd62ed3e1461014a575b600080fd5b34801561008857600080fd5b506100a0600160a060020a0360043516602435610171565b604080519115158252519081900360200190f35b3480156100c057600080fd5b506100c96101d8565b60408051918252519081900360200190f35b3480156100e757600080fd5b506100a0600160a060020a03600435811690602435166044356101de565b34801561011157600080fd5b506100c9600160a060020a03600435166102cb565b34801561013257600080fd5b506100a0600160a060020a03600435166024356102e6565b34801561015657600080fd5b506100c9600160a060020a036004358116906024351661037f565b336000818152600260209081526040808320600160a060020a038716808552908352818420869055815186815291519394909390927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925928290030190a35060015b92915050565b60005481565b600160a060020a03831660009081526001602052604081205482118015906102295750600160a060020a03841660009081526002602090815260408083203384529091529020548211155b80156102355750600082115b156102c057600160a060020a03808416600081815260016020908152604080832080548801905593881680835284832080548890039055600282528483203384528252918490208054879003905583518681529351929391927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9281900390910190a35060016102c4565b5060005b9392505050565b600160a060020a031660009081526001602052604090205490565b3360009081526001602052604081205482118015906103055750600082115b156103775733600081815260016020908152604080832080548790039055600160a060020a03871680845292819020805487019055805186815290519293927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef929181900390910190a35060016101d2565b5060006101d2565b600160a060020a039182166000908152600260209081526040808320939094168252919091522054905600a165627a7a72305820271c41a8e75b80969a0f27b15a535bc0cea8bc834a3ab7b2a467ed4241073af60029\n";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    protected StandardToken() {
        super(BINARY);
    }

    @Deprecated
    protected StandardToken(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected StandardToken(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected StandardToken(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected StandardToken(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> approve(Address _spender, Uint256 _value) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(_spender, _value), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public String getABI_approve(Address _spender, Uint256 _value) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(_spender, _value), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_approve decodeABI_approve(String abiToDecode) {
        final Function function = new Function(FUNC_APPROVE, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_approve functionResponse_approve = new FunctionResponse_approve();
        functionResponse_approve.success = (Bool) response.get(0);
        return functionResponse_approve;
    }

    public RemoteFunctionCall<Uint256> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public String getABI_totalSupply() {
        final Function function = new Function(
                FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_totalSupply decodeABI_totalSupply(String abiToDecode) {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_totalSupply functionResponse_totalSupply = new FunctionResponse_totalSupply();
        functionResponse_totalSupply.return1 = (Uint256) response.get(0);
        return functionResponse_totalSupply;
    }

    public RemoteFunctionCall<TransactionReceipt> transferFrom(Address _from, Address _to, Uint256 _value) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(_from, _to, _value), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public String getABI_transferFrom(Address _from, Address _to, Uint256 _value) {
        final Function function = new Function(
                FUNC_TRANSFERFROM, 
                Arrays.<Type>asList(_from, _to, _value), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_transferFrom decodeABI_transferFrom(String abiToDecode) {
        final Function function = new Function(FUNC_TRANSFERFROM, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_transferFrom functionResponse_transferFrom = new FunctionResponse_transferFrom();
        functionResponse_transferFrom.success = (Bool) response.get(0);
        return functionResponse_transferFrom;
    }

    public RemoteFunctionCall<Uint256> balanceOf(Address _owner) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(_owner), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public String getABI_balanceOf(Address _owner) {
        final Function function = new Function(
                FUNC_BALANCEOF, 
                Arrays.<Type>asList(_owner), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_balanceOf decodeABI_balanceOf(String abiToDecode) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_balanceOf functionResponse_balanceOf = new FunctionResponse_balanceOf();
        functionResponse_balanceOf.balance = (Uint256) response.get(0);
        return functionResponse_balanceOf;
    }

    public RemoteFunctionCall<TransactionReceipt> transfer(Address _to, Uint256 _value) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(_to, _value), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public String getABI_transfer(Address _to, Uint256 _value) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(_to, _value), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_transfer decodeABI_transfer(String abiToDecode) {
        final Function function = new Function(FUNC_TRANSFER, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_transfer functionResponse_transfer = new FunctionResponse_transfer();
        functionResponse_transfer.success = (Bool) response.get(0);
        return functionResponse_transfer;
    }

    public RemoteFunctionCall<Uint256> allowance(Address _owner, Address _spender) {
        final Function function = new Function(FUNC_ALLOWANCE, 
                Arrays.<Type>asList(_owner, _spender), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public String getABI_allowance(Address _owner, Address _spender) {
        final Function function = new Function(
                FUNC_ALLOWANCE, 
                Arrays.<Type>asList(_owner, _spender), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_allowance decodeABI_allowance(String abiToDecode) {
        final Function function = new Function(FUNC_ALLOWANCE, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_allowance functionResponse_allowance = new FunctionResponse_allowance();
        functionResponse_allowance.remaining = (Uint256) response.get(0);
        return functionResponse_allowance;
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (Address) eventValues.getIndexedValues().get(0);
            typedResponse._to = (Address) eventValues.getIndexedValues().get(1);
            typedResponse._value = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TransferEventResponse> transferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse._from = (Address) eventValues.getIndexedValues().get(0);
                typedResponse._to = (Address) eventValues.getIndexedValues().get(1);
                typedResponse._value = (Uint256) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Flowable<TransferEventResponse> transferEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventFlowable(filter);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (Address) eventValues.getIndexedValues().get(0);
            typedResponse._spender = (Address) eventValues.getIndexedValues().get(1);
            typedResponse._value = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse._owner = (Address) eventValues.getIndexedValues().get(0);
                typedResponse._spender = (Address) eventValues.getIndexedValues().get(1);
                typedResponse._value = (Uint256) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Flowable<ApprovalEventResponse> approvalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventFlowable(filter);
    }

    @Deprecated
    public static StandardToken load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new StandardToken(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static StandardToken load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new StandardToken(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static StandardToken load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new StandardToken(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static StandardToken load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new StandardToken(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<StandardToken> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(StandardToken.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<StandardToken> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(StandardToken.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<StandardToken> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(StandardToken.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<StandardToken> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(StandardToken.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class FunctionResponse_approve {
        public Bool success;
    }

    public static class FunctionResponse_totalSupply {
        public Uint256 return1;
    }

    public static class FunctionResponse_transferFrom {
        public Bool success;
    }

    public static class FunctionResponse_balanceOf {
        public Uint256 balance;
    }

    public static class FunctionResponse_transfer {
        public Bool success;
    }

    public static class FunctionResponse_allowance {
        public Uint256 remaining;
    }

    public static class TransferEventResponse extends BaseEventResponse {
        public Address _from;

        public Address _to;

        public Uint256 _value;
    }

    public static class ApprovalEventResponse extends BaseEventResponse {
        public Address _owner;

        public Address _spender;

        public Uint256 _value;
    }
}
