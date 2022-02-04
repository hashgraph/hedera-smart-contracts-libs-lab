package com.hedera.test;

import io.reactivex.Flowable;
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
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
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
public class HumanStandardToken extends Contract {
    public static final String BINARY = "60c0604052600460808190527f48302e310000000000000000000000000000000000000000000000000000000060a090815261003e91600691906100d0565b5034801561004b57600080fd5b506040516109ab3803806109ab8339810160409081528151602080840151838501516060860151336000908152600185529586208590559484905590850180519395909491939101916100a3916003918601906100d0565b506004805460ff191660ff841617905580516100c69060059060208401906100d0565b505050505061016b565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061011157805160ff191683800117855561013e565b8280016001018555821561013e579182015b8281111561013e578251825591602001919060010190610123565b5061014a92915061014e565b5090565b61016891905b8082111561014a5760008155600101610154565b90565b6108318061017a6000396000f3006080604052600436106100955763ffffffff60e060020a60003504166306fdde0381146100a7578063095ea7b31461013157806318160ddd1461016957806323b872dd14610190578063313ce567146101ba57806354fd4d50146101e557806370a08231146101fa57806395d89b411461021b578063a9059cbb14610230578063cae9ca5114610254578063dd62ed3e146102bd575b3480156100a157600080fd5b50600080fd5b3480156100b357600080fd5b506100bc6102e4565b6040805160208082528351818301528351919283929083019185019080838360005b838110156100f65781810151838201526020016100de565b50505050905090810190601f1680156101235780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561013d57600080fd5b50610155600160a060020a0360043516602435610372565b604080519115158252519081900360200190f35b34801561017557600080fd5b5061017e6103d9565b60408051918252519081900360200190f35b34801561019c57600080fd5b50610155600160a060020a03600435811690602435166044356103df565b3480156101c657600080fd5b506101cf6104cc565b6040805160ff9092168252519081900360200190f35b3480156101f157600080fd5b506100bc6104d5565b34801561020657600080fd5b5061017e600160a060020a0360043516610530565b34801561022757600080fd5b506100bc61054b565b34801561023c57600080fd5b50610155600160a060020a03600435166024356105a6565b34801561026057600080fd5b50604080516020600460443581810135601f8101849004840285018401909552848452610155948235600160a060020a031694602480359536959460649492019190819084018382808284375094975061063f9650505050505050565b3480156102c957600080fd5b5061017e600160a060020a03600435811690602435166107da565b6003805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561036a5780601f1061033f5761010080835404028352916020019161036a565b820191906000526020600020905b81548152906001019060200180831161034d57829003601f168201915b505050505081565b336000818152600260209081526040808320600160a060020a038716808552908352818420869055815186815291519394909390927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925928290030190a35060015b92915050565b60005481565b600160a060020a038316600090815260016020526040812054821180159061042a5750600160a060020a03841660009081526002602090815260408083203384529091529020548211155b80156104365750600082115b156104c157600160a060020a03808416600081815260016020908152604080832080548801905593881680835284832080548890039055600282528483203384528252918490208054879003905583518681529351929391927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9281900390910190a35060016104c5565b5060005b9392505050565b60045460ff1681565b6006805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561036a5780601f1061033f5761010080835404028352916020019161036a565b600160a060020a031660009081526001602052604090205490565b6005805460408051602060026001851615610100026000190190941693909304601f8101849004840282018401909252818152929183018282801561036a5780601f1061033f5761010080835404028352916020019161036a565b3360009081526001602052604081205482118015906105c55750600082115b156106375733600081815260016020908152604080832080548790039055600160a060020a03871680845292819020805487019055805186815290519293927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef929181900390910190a35060016103d3565b5060006103d3565b336000818152600260209081526040808320600160a060020a038816808552908352818420879055815187815291519394909390927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925928290030190a383600160a060020a031660405180807f72656365697665417070726f76616c28616464726573732c75696e743235362c81526020017f616464726573732c627974657329000000000000000000000000000000000000815250602e019050604051809103902060e060020a9004338530866040518563ffffffff1660e060020a0281526004018085600160a060020a0316600160a060020a0316815260200184815260200183600160a060020a0316600160a060020a03168152602001828051906020019080838360005b8381101561077f578181015183820152602001610767565b50505050905090810190601f1680156107ac5780820380516001836020036101000a031916815260200191505b509450505050506000604051808303816000875af19250505015156107d057600080fd5b5060019392505050565b600160a060020a039182166000908152600260209081526040808320939094168252919091522054905600a165627a7a723058203f2de808df5359509254dc2a0d616b226de2b64f0bf28bae7323aeba4487199b0029\n";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_VERSION = "version";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_APPROVEANDCALL = "approveAndCall";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    protected HumanStandardToken() {
        super(BINARY);
    }

    @Deprecated
    protected HumanStandardToken(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected HumanStandardToken(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected HumanStandardToken(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected HumanStandardToken(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Utf8String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public String getABI_name() {
        final Function function = new Function(
                FUNC_NAME, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_name decodeABI_name(String abiToDecode) {
        final Function function = new Function(FUNC_NAME, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_name functionResponse_name = new FunctionResponse_name();
        functionResponse_name.return1 = (Utf8String) response.get(0);
        return functionResponse_name;
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

    public RemoteFunctionCall<Utf8String> version() {
        final Function function = new Function(FUNC_VERSION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public String getABI_version() {
        final Function function = new Function(
                FUNC_VERSION, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_version decodeABI_version(String abiToDecode) {
        final Function function = new Function(FUNC_VERSION, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_version functionResponse_version = new FunctionResponse_version();
        functionResponse_version.return1 = (Utf8String) response.get(0);
        return functionResponse_version;
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

    public RemoteFunctionCall<Utf8String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public String getABI_symbol() {
        final Function function = new Function(
                FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_symbol decodeABI_symbol(String abiToDecode) {
        final Function function = new Function(FUNC_SYMBOL, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_symbol functionResponse_symbol = new FunctionResponse_symbol();
        functionResponse_symbol.return1 = (Utf8String) response.get(0);
        return functionResponse_symbol;
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

    public RemoteFunctionCall<TransactionReceipt> approveAndCall(Address _spender, Uint256 _value, DynamicBytes _extraData) {
        final Function function = new Function(
                FUNC_APPROVEANDCALL, 
                Arrays.<Type>asList(_spender, _value, _extraData), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public String getABI_approveAndCall(Address _spender, Uint256 _value, DynamicBytes _extraData) {
        final Function function = new Function(
                FUNC_APPROVEANDCALL, 
                Arrays.<Type>asList(_spender, _value, _extraData), 
                Collections.<TypeReference<?>>emptyList());
        return org.web3j.abi.FunctionEncoder.encode(function);
    }

    public FunctionResponse_approveAndCall decodeABI_approveAndCall(String abiToDecode) {
        final Function function = new Function(FUNC_APPROVEANDCALL, 
                Collections.<Type>emptyList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> response = FunctionReturnDecoder.decode(abiToDecode, function.getOutputParameters());
        FunctionResponse_approveAndCall functionResponse_approveAndCall = new FunctionResponse_approveAndCall();
        functionResponse_approveAndCall.success = (Bool) response.get(0);
        return functionResponse_approveAndCall;
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
    public static HumanStandardToken load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new HumanStandardToken(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static HumanStandardToken load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new HumanStandardToken(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static HumanStandardToken load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new HumanStandardToken(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static HumanStandardToken load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new HumanStandardToken(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<HumanStandardToken> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, Uint256 _initialAmount, Utf8String _tokenName, Uint8 _decimalUnits, Utf8String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_initialAmount, _tokenName, _decimalUnits, _tokenSymbol));
        return deployRemoteCall(HumanStandardToken.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<HumanStandardToken> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, Uint256 _initialAmount, Utf8String _tokenName, Uint8 _decimalUnits, Utf8String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_initialAmount, _tokenName, _decimalUnits, _tokenSymbol));
        return deployRemoteCall(HumanStandardToken.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<HumanStandardToken> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, Uint256 _initialAmount, Utf8String _tokenName, Uint8 _decimalUnits, Utf8String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_initialAmount, _tokenName, _decimalUnits, _tokenSymbol));
        return deployRemoteCall(HumanStandardToken.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<HumanStandardToken> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, Uint256 _initialAmount, Utf8String _tokenName, Uint8 _decimalUnits, Utf8String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_initialAmount, _tokenName, _decimalUnits, _tokenSymbol));
        return deployRemoteCall(HumanStandardToken.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public String getABI_Constructor(Uint256 _initialAmount, Utf8String _tokenName, Uint8 _decimalUnits, Utf8String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_initialAmount, _tokenName, _decimalUnits, _tokenSymbol));
        return encodedConstructor;
    }

    public static class FunctionResponse_name {
        public Utf8String return1;
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

    public static class FunctionResponse_decimals {
        public Uint8 return1;
    }

    public static class FunctionResponse_version {
        public Utf8String return1;
    }

    public static class FunctionResponse_balanceOf {
        public Uint256 balance;
    }

    public static class FunctionResponse_symbol {
        public Utf8String return1;
    }

    public static class FunctionResponse_transfer {
        public Bool success;
    }

    public static class FunctionResponse_approveAndCall {
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
