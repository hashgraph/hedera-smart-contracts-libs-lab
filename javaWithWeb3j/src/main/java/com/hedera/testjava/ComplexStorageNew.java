package com.hedera.testjava;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
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
import org.web3j.tuples.generated.Tuple2;
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
public class ComplexStorageNew extends Contract {
    public static final String BINARY = "60806040523480156200001157600080fd5b50604051608080620012508339810180604052620000339190810190620002d0565b8151805183916000916200004f918391602090910190620000e2565b5060208281015180516200006a9260018501920190620000e2565b5050815180518392506002916200008791839160200190620000e2565b506020820151816001015590505033600160a060020a03167fed780f1dfaf928f77dd066a615b98641a01d34bf8b139c6f87ae25365fdc9a1f8383604051620000d292919062000402565b60405180910390a25050620004c3565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200012557805160ff191683800117855562000155565b8280016001018555821562000155579182015b828111156200015557825182559160200191906001019062000138565b506200016392915062000167565b5090565b6200018491905b808211156200016357600081556001016200016e565b90565b6000601f820183136200019957600080fd5b8151620001b0620001aa826200045a565b62000433565b91508082526020830160208301858383011115620001cd57600080fd5b620001da83828462000486565b50505092915050565b600060408284031215620001f657600080fd5b62000202604062000433565b82519091506001604060020a038111156200021c57600080fd5b6200022a8482850162000187565b82525060206200023d84848301620002bb565b60208301525092915050565b6000604082840312156200025c57600080fd5b62000268604062000433565b82519091506001604060020a038111156200028257600080fd5b620002908482850162000187565b82525060208201516001604060020a03811115620002ad57600080fd5b6200023d8482850162000187565b6000620002c9825162000184565b9392505050565b60008060408385031215620002e457600080fd5b82516001604060020a03811115620002fb57600080fd5b620003098582860162000249565b92505060208301516001604060020a038111156200032657600080fd5b6200033485828601620001e3565b9150509250929050565b60006200034b8262000482565b8084526200036181602086016020860162000486565b6200036c81620004b9565b9093016020019392505050565b80516040808452600091908401906200039382826200033e565b9150506020830151620003aa6020860182620003f1565b509392505050565b8051604080845260009190840190620003cc82826200033e565b91505060208301518482036020860152620003e882826200033e565b95945050505050565b620003fc8162000184565b82525050565b60408082528101620004158185620003b2565b905081810360208301526200042b818462000379565b949350505050565b6040518181016001604060020a03811182821017156200045257600080fd5b604052919050565b60006001604060020a038211156200047157600080fd5b506020601f91909101601f19160190565b5190565b60005b83811015620004a357818101518382015260200162000489565b83811115620004b3576000848401525b50505050565b601f01601f191690565b610d7d80620004d36000396000f3006080604052600436106100985763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663243dc8da811461009d5780632cf07395146100c85780636bb632a9146100ea5780638c9fb6f91461010a5780639096c2131461012a578063be9c5e341461014a578063bea80d0c1461016a578063c22565131461018d578063cfd91f2b146101ad575b600080fd5b3480156100a957600080fd5b506100b26101d0565b6040516100bf9190610c58565b60405180910390f35b3480156100d457600080fd5b506100e86100e3366004610b05565b61031b565b005b3480156100f657600080fd5b506100e8610105366004610b3a565b610354565b34801561011657600080fd5b506100e8610125366004610b6f565b6103ae565b34801561013657600080fd5b506100e8610145366004610a93565b6103ed565b34801561015657600080fd5b506100e8610165366004610ad0565b610418565b34801561017657600080fd5b5061017f610439565b6040516100bf929190610c8e565b34801561019957600080fd5b506100e86101a8366004610a93565b61058d565b3480156101b957600080fd5b506101c26105a7565b6040516100bf929190610c69565b6101d86107a7565b604080516000805460606020601f60026000196101006001871615020190941693909304928301819004028401810185529383018181529293919284929091849184018282801561026a5780601f1061023f5761010080835404028352916020019161026a565b820191906000526020600020905b81548152906001019060200180831161024d57829003601f168201915b50505050508152602001600182018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561030c5780601f106102e15761010080835404028352916020019161030c565b820191906000526020600020905b8154815290600101906020018083116102ef57829003601f168201915b50505050508152505090505b90565b8051805182916000916103359183916020909101906107be565b50602082810151805161034e92600185019201906107be565b50505050565b805180518051805180518594600c949093859391928492839161037e9183916020909101906107be565b50602082810151805161039792600185019201906107be565b505050505050506020820151816002015590505050565b8051805180518392600892909183916103cc918391602001906107be565b5060208281015180516103e592600185019201906107be565b505050505050565b8051805182916004916104079183916020909101906107be565b506020820151816001015590505050565b80516006908155602080830151805184939261034e926007929101906107be565b6104416107a7565b604080516000805460206002600180841615610100026000190190931604601f8101829004909102840160609081018652948401818152929485949293859284928491908401828280156104d65780601f106104ab576101008083540402835291602001916104d6565b820191906000526020600020905b8154815290600101906020018083116104b957829003601f168201915b50505050508152602001600182018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105785780601f1061054d57610100808354040283529160200191610578565b820191906000526020600020905b81548152906001019060200180831161055b57829003601f168201915b50505091909252509195509193505050509091565b8051805182916002916104079183916020909101906107be565b6105af6107a7565b6105b761083c565b60408051600080546020600260018316156101000260001901909216829004601f8101829004909102840160609081018652948401818152929491939285928492849184018282801561064b5780601f106106205761010080835404028352916020019161064b565b820191906000526020600020905b81548152906001019060200180831161062e57829003601f168201915b50505050508152602001600182018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106ed5780601f106106c2576101008083540402835291602001916106ed565b820191906000526020600020905b8154815290600101906020018083116106d057829003601f168201915b50505091909252505060408051845460606020601f6002600019610100600187161502019094169390930492830181900402830181018452928201818152949650909385935084929091849184018282801561078a5780601f1061075f5761010080835404028352916020019161078a565b820191906000526020600020905b81548152906001019060200180831161076d57829003601f168201915b505050505081526020016001820154815250509050915091509091565b604080518082019091526060808252602082015290565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106107ff57805160ff191683800117855561082c565b8280016001018555821561082c579182015b8281111561082c578251825591602001919060010190610811565b50610838929150610854565b5090565b60408051808201909152606081526000602082015290565b61031891905b80821115610838576000815560010161085a565b6000601f8201831361087f57600080fd5b813561089261088d82610cd5565b610cae565b915080825260208301602083018583830111156108ae57600080fd5b6108b9838284610d01565b50505092915050565b6000604082840312156108d457600080fd5b6108de6040610cae565b9050813567ffffffffffffffff8111156108f757600080fd5b6109038482850161086e565b825250602061091484848301610a80565b60208301525092915050565b60006040828403121561093257600080fd5b61093c6040610cae565b9050600061094a8484610a80565b825250602082013567ffffffffffffffff81111561096757600080fd5b6109148482850161086e565b60006040828403121561098557600080fd5b61098f6040610cae565b9050813567ffffffffffffffff8111156109a857600080fd5b61094a8482850161086e565b6000602082840312156109c657600080fd5b6109d06020610cae565b9050813567ffffffffffffffff8111156109e957600080fd5b6109f584828501610a3f565b82525092915050565b600060408284031215610a1057600080fd5b610a1a6040610cae565b9050813567ffffffffffffffff811115610a3357600080fd5b610903848285016109b4565b600060208284031215610a5157600080fd5b610a5b6020610cae565b9050813567ffffffffffffffff811115610a7457600080fd5b6109f584828501610973565b6000610a8c8235610318565b9392505050565b600060208284031215610aa557600080fd5b813567ffffffffffffffff811115610abc57600080fd5b610ac8848285016108c2565b949350505050565b600060208284031215610ae257600080fd5b813567ffffffffffffffff811115610af957600080fd5b610ac884828501610920565b600060208284031215610b1757600080fd5b813567ffffffffffffffff811115610b2e57600080fd5b610ac884828501610973565b600060208284031215610b4c57600080fd5b813567ffffffffffffffff811115610b6357600080fd5b610ac8848285016109fe565b600060208284031215610b8157600080fd5b813567ffffffffffffffff811115610b9857600080fd5b610ac884828501610a3f565b6000610baf82610cfd565b808452610bc3816020860160208601610d0d565b610bcc81610d39565b9093016020019392505050565b8051604080845260009190840190610bf18282610ba4565b9150506020830151610c066020860182610c49565b509392505050565b8051604080845260009190840190610c268282610ba4565b91505060208301518482036020860152610c408282610ba4565b95945050505050565b610c5281610318565b82525050565b60208082528101610a8c8184610c0e565b60408082528101610c7a8185610c0e565b90508181036020830152610ac88184610bd9565b60408082528101610c9f8185610c0e565b9050610a8c6020830184610c49565b60405181810167ffffffffffffffff81118282101715610ccd57600080fd5b604052919050565b600067ffffffffffffffff821115610cec57600080fd5b506020601f91909101601f19160190565b5190565b82818337506000910152565b60005b83811015610d28578181015183820152602001610d10565b8381111561034e5750506000910152565b601f01601f1916905600a265627a7a72305820434297549adbf21e3eaa0db863188302fa317b6cda2cc42efcd5a28f3ca373fd6c6578706572696d656e74616cf50037\n";

    public static final String FUNC_GETFOO = "getFoo";

    public static final String FUNC_SETFOO = "setFoo";

    public static final String FUNC_SETNAZ = "setNaz";

    public static final String FUNC_SETNUU = "setNuu";

    public static final String FUNC_SETBAZ = "setBaz";

    public static final String FUNC_SETBOZ = "setBoz";

    public static final String FUNC_GETFOOUINT = "getFooUint";

    public static final String FUNC_SETBAR = "setBar";

    public static final String FUNC_GETFOOBAR = "getFooBar";

    public static final Event ACCESS_EVENT = new Event("Access", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Struct0>() {}, new TypeReference<Struct1>() {}));
    ;

    protected ComplexStorageNew() {
        super(BINARY);
    }

    @Deprecated
    protected ComplexStorageNew(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ComplexStorageNew(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ComplexStorageNew(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ComplexStorageNew(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Struct0> getFoo() {
        final Function function = new Function(FUNC_GETFOO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Struct0>() {}));
        return executeRemoteCallSingleValueReturn(function, Struct0.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setFoo(Struct0 _toSet) {
        final Function function = new Function(
                FUNC_SETFOO, 
                Arrays.<Type>asList(_toSet), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setNaz(Struct5 _naz) {
        final Function function = new Function(
                FUNC_SETNAZ, 
                Arrays.<Type>asList(_naz), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setNuu(Struct3 _toSet) {
        final Function function = new Function(
                FUNC_SETNUU, 
                Arrays.<Type>asList(_toSet), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setBaz(Struct1 _toSet) {
        final Function function = new Function(
                FUNC_SETBAZ, 
                Arrays.<Type>asList(_toSet), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setBoz(Struct2 _toSet) {
        final Function function = new Function(
                FUNC_SETBOZ, 
                Arrays.<Type>asList(_toSet), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple2<Struct0, BigInteger>> getFooUint() {
        final Function function = new Function(FUNC_GETFOOUINT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Struct0>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<Struct0, BigInteger>>(function,
                new Callable<Tuple2<Struct0, BigInteger>>() {
                    @Override
                    public Tuple2<Struct0, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<Struct0, BigInteger>(
                                (Struct0) results.get(0), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> setBar(Struct1 _toSet) {
        final Function function = new Function(
                FUNC_SETBAR, 
                Arrays.<Type>asList(_toSet), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple2<Struct0, Struct1>> getFooBar() {
        final Function function = new Function(FUNC_GETFOOBAR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Struct0>() {}, new TypeReference<Struct1>() {}));
        return new RemoteFunctionCall<Tuple2<Struct0, Struct1>>(function,
                new Callable<Tuple2<Struct0, Struct1>>() {
                    @Override
                    public Tuple2<Struct0, Struct1> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<Struct0, Struct1>(
                                (Struct0) results.get(0), 
                                (Struct1) results.get(1));
                    }
                });
    }

    public List<AccessEventResponse> getAccessEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ACCESS_EVENT, transactionReceipt);
        ArrayList<AccessEventResponse> responses = new ArrayList<AccessEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AccessEventResponse typedResponse = new AccessEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._address = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._foo = (Struct0) eventValues.getNonIndexedValues().get(0);
            typedResponse._bar = (Struct1) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AccessEventResponse> accessEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, AccessEventResponse>() {
            @Override
            public AccessEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ACCESS_EVENT, log);
                AccessEventResponse typedResponse = new AccessEventResponse();
                typedResponse.log = log;
                typedResponse._address = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._foo = (Struct0) eventValues.getNonIndexedValues().get(0);
                typedResponse._bar = (Struct1) eventValues.getNonIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Flowable<AccessEventResponse> accessEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ACCESS_EVENT));
        return accessEventFlowable(filter);
    }

    @Deprecated
    public static ComplexStorageNew load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ComplexStorageNew(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ComplexStorageNew load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ComplexStorageNew(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ComplexStorageNew load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ComplexStorageNew(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ComplexStorageNew load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ComplexStorageNew(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ComplexStorageNew> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, Struct0 _foo, Struct1 _bar) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_foo, 
                _bar));
        return deployRemoteCall(ComplexStorageNew.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<ComplexStorageNew> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, Struct0 _foo, Struct1 _bar) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_foo, 
                _bar));
        return deployRemoteCall(ComplexStorageNew.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<ComplexStorageNew> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, Struct0 _foo, Struct1 _bar) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_foo, 
                _bar));
        return deployRemoteCall(ComplexStorageNew.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<ComplexStorageNew> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, Struct0 _foo, Struct1 _bar) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_foo, 
                _bar));
        return deployRemoteCall(ComplexStorageNew.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class Struct0 extends DynamicStruct {
        public String id;

        public String name;

        public Struct0(String id, String name) {
            super(new org.web3j.abi.datatypes.Utf8String(id), 
                    new org.web3j.abi.datatypes.Utf8String(name));
            this.id = id;
            this.name = name;
        }

        public Struct0(Utf8String id, Utf8String name) {
            super(id, name);
            this.id = id.getValue();
            this.name = name.getValue();
        }
    }

    public static class Struct1 extends DynamicStruct {
        public String id;

        public BigInteger data;

        public Struct1(String id, BigInteger data) {
            super(new org.web3j.abi.datatypes.Utf8String(id), 
                    new org.web3j.abi.datatypes.generated.Uint256(data));
            this.id = id;
            this.data = data;
        }

        public Struct1(Utf8String id, Uint256 data) {
            super(id, data);
            this.id = id.getValue();
            this.data = data.getValue();
        }
    }

    public static class Struct2 extends DynamicStruct {
        public BigInteger data;

        public String id;

        public Struct2(BigInteger data, String id) {
            super(new org.web3j.abi.datatypes.generated.Uint256(data), 
                    new org.web3j.abi.datatypes.Utf8String(id));
            this.data = data;
            this.id = id;
        }

        public Struct2(Uint256 data, Utf8String id) {
            super(data, id);
            this.data = data.getValue();
            this.id = id.getValue();
        }
    }

    public static class Struct3 extends DynamicStruct {
        public Struct0 foo;

        public Struct3(Struct0 foo) {
            super(foo);
            this.foo = foo;
        }
    }

    public static class Struct4 extends DynamicStruct {
        public Struct3 nuu;

        public Struct4(Struct3 nuu) {
            super(nuu);
            this.nuu = nuu;
        }
    }

    public static class Struct5 extends DynamicStruct {
        public Struct4 nar;

        public BigInteger data;

        public Struct5(Struct4 nar, BigInteger data) {
            super(nar, 
                    new org.web3j.abi.datatypes.generated.Uint256(data));
            this.nar = nar;
            this.data = data;
        }

        public Struct5(Struct4 nar, Uint256 data) {
            super(nar, data);
            this.nar = nar;
            this.data = data.getValue();
        }
    }

    public static class AccessEventResponse extends BaseEventResponse {
        public String _address;

        public Struct0 _foo;

        public Struct1 _bar;
    }
}
