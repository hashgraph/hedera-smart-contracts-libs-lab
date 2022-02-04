package com.hedera.testjava;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes2;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.StaticArray5;
import org.web3j.abi.datatypes.generated.Uint256;
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
public class ArraysInStruct extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b506113d2806100206000396000f3fe608060405234801561001057600080fd5b506004361061002b5760003560e01c8063428aa32114610030575b600080fd5b61004a60048036038101906100459190611324565b61004c565b005b5050565b6000604051905090565b600080fd5b600080fd5b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b6100b282610069565b810181811067ffffffffffffffff821117156100d1576100d061007a565b5b80604052505050565b60006100e4610050565b90506100f082826100a9565b919050565b600067ffffffffffffffff8211156101105761010f61007a565b5b602082029050602081019050919050565b600080fd5b600080fd5b600067ffffffffffffffff8211156101465761014561007a565b5b61014f82610069565b9050602081019050919050565b82818337600083830152505050565b600061017e6101798461012b565b6100da565b90508281526020810184848401111561019a57610199610126565b5b6101a584828561015c565b509392505050565b600082601f8301126101c2576101c1610064565b5b81356101d284826020860161016b565b91505092915050565b60006101ee6101e9846100f5565b6100da565b9050808382526020820190506020840283018581111561021157610210610121565b5b835b8181101561025857803567ffffffffffffffff81111561023657610235610064565b5b80860161024389826101ad565b85526020850194505050602081019050610213565b5050509392505050565b600082601f83011261027757610276610064565b5b81356102878482602086016101db565b91505092915050565b600080fd5b600080fd5b6000819050919050565b6102ad8161029a565b81146102b857600080fd5b50565b6000813590506102ca816102a4565b92915050565b600067ffffffffffffffff8211156102eb576102ea61007a565b5b602082029050602081019050919050565b600067ffffffffffffffff8211156103175761031661007a565b5b61032082610069565b9050602081019050919050565b600061034061033b846102fc565b6100da565b90508281526020810184848401111561035c5761035b610126565b5b61036784828561015c565b509392505050565b600082601f83011261038457610383610064565b5b813561039484826020860161032d565b91505092915050565b60006103b06103ab846102d0565b6100da565b905080838252602082019050602084028301858111156103d3576103d2610121565b5b835b8181101561041a57803567ffffffffffffffff8111156103f8576103f7610064565b5b808601610405898261036f565b855260208501945050506020810190506103d5565b5050509392505050565b600082601f83011261043957610438610064565b5b813561044984826020860161039d565b91505092915050565b600067ffffffffffffffff82111561046d5761046c61007a565b5b602082029050602081019050919050565b60007fffff00000000000000000000000000000000000000000000000000000000000082169050919050565b6104b38161047e565b81146104be57600080fd5b50565b6000813590506104d0816104aa565b92915050565b60006104e96104e484610452565b6100da565b9050808382526020820190506020840283018581111561050c5761050b610121565b5b835b81811015610535578061052188826104c1565b84526020840193505060208101905061050e565b5050509392505050565b600082601f83011261055457610553610064565b5b81356105648482602086016104d6565b91505092915050565b600067ffffffffffffffff8211156105885761058761007a565b5b602082029050602081019050919050565b60006105ac6105a78461056d565b6100da565b905080838252602082019050602084028301858111156105cf576105ce610121565b5b835b818110156105f857806105e488826102bb565b8452602084019350506020810190506105d1565b5050509392505050565b600082601f83011261061757610616610064565b5b8135610627848260208601610599565b91505092915050565b600067ffffffffffffffff82111561064b5761064a61007a565b5b602082029050602081019050919050565b6000819050919050565b61066f8161065c565b811461067a57600080fd5b50565b60008135905061068c81610666565b92915050565b60006106a56106a084610630565b6100da565b905080838252602082019050602084028301858111156106c8576106c7610121565b5b835b818110156106f157806106dd888261067d565b8452602084019350506020810190506106ca565b5050509392505050565b600082601f8301126107105761070f610064565b5b8135610720848260208601610692565b91505092915050565b600067ffffffffffffffff8211156107445761074361007a565b5b602082029050602081019050919050565b60008115159050919050565b61076a81610755565b811461077557600080fd5b50565b60008135905061078781610761565b92915050565b60006107a061079b84610729565b6100da565b905080838252602082019050602084028301858111156107c3576107c2610121565b5b835b818110156107ec57806107d88882610778565b8452602084019350506020810190506107c5565b5050509392505050565b600082601f83011261080b5761080a610064565b5b813561081b84826020860161078d565b91505092915050565b600067ffffffffffffffff82111561083f5761083e61007a565b5b602082029050602081019050919050565b6000819050919050565b61086381610850565b811461086e57600080fd5b50565b6000813590506108808161085a565b92915050565b600061089961089484610824565b6100da565b905080838252602082019050602084028301858111156108bc576108bb610121565b5b835b818110156108e557806108d18882610871565b8452602084019350506020810190506108be565b5050509392505050565b600082601f83011261090457610903610064565b5b8135610914848260208601610886565b91505092915050565b600067ffffffffffffffff8211156109385761093761007a565b5b602082029050602081019050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600061097482610949565b9050919050565b61098481610969565b811461098f57600080fd5b50565b6000813590506109a18161097b565b92915050565b60006109ba6109b58461091d565b6100da565b905080838252602082019050602084028301858111156109dd576109dc610121565b5b835b81811015610a0657806109f28882610992565b8452602084019350506020810190506109df565b5050509392505050565b600082601f830112610a2557610a24610064565b5b8135610a358482602086016109a7565b91505092915050565b600067ffffffffffffffff821115610a5957610a5861007a565b5b602082029050919050565b6000610a77610a7284610a3e565b6100da565b90508060208402830185811115610a9157610a90610121565b5b835b81811015610ad857803567ffffffffffffffff811115610ab657610ab5610064565b5b808601610ac3898261036f565b85526020850194505050602081019050610a93565b5050509392505050565b600082601f830112610af757610af6610064565b5b6005610b04848285610a64565b91505092915050565b600067ffffffffffffffff821115610b2857610b2761007a565b5b602082029050919050565b6000610b46610b4184610b0d565b6100da565b90508060208402830185811115610b6057610b5f610121565b5b835b81811015610ba757803567ffffffffffffffff811115610b8557610b84610064565b5b808601610b9289826101ad565b85526020850194505050602081019050610b62565b5050509392505050565b600082601f830112610bc657610bc5610064565b5b6005610bd3848285610b33565b91505092915050565b600067ffffffffffffffff821115610bf757610bf661007a565b5b602082029050919050565b6000610c15610c1084610bdc565b6100da565b90508060208402830185811115610c2f57610c2e610121565b5b835b81811015610c585780610c4488826104c1565b845260208401935050602081019050610c31565b5050509392505050565b600082601f830112610c7757610c76610064565b5b6005610c84848285610c02565b91505092915050565b600067ffffffffffffffff821115610ca857610ca761007a565b5b602082029050919050565b6000610cc6610cc184610c8d565b6100da565b90508060208402830185811115610ce057610cdf610121565b5b835b81811015610d095780610cf588826102bb565b845260208401935050602081019050610ce2565b5050509392505050565b600082601f830112610d2857610d27610064565b5b6005610d35848285610cb3565b91505092915050565b600067ffffffffffffffff821115610d5957610d5861007a565b5b602082029050919050565b6000610d77610d7284610d3e565b6100da565b90508060208402830185811115610d9157610d90610121565b5b835b81811015610dba5780610da6888261067d565b845260208401935050602081019050610d93565b5050509392505050565b600082601f830112610dd957610dd8610064565b5b6005610de6848285610d64565b91505092915050565b600067ffffffffffffffff821115610e0a57610e0961007a565b5b602082029050919050565b6000610e28610e2384610def565b6100da565b90508060208402830185811115610e4257610e41610121565b5b835b81811015610e6b5780610e578882610778565b845260208401935050602081019050610e44565b5050509392505050565b600082601f830112610e8a57610e89610064565b5b6005610e97848285610e15565b91505092915050565b600067ffffffffffffffff821115610ebb57610eba61007a565b5b602082029050919050565b6000610ed9610ed484610ea0565b6100da565b90508060208402830185811115610ef357610ef2610121565b5b835b81811015610f1c5780610f088882610871565b845260208401935050602081019050610ef5565b5050509392505050565b600082601f830112610f3b57610f3a610064565b5b6005610f48848285610ec6565b91505092915050565b600067ffffffffffffffff821115610f6c57610f6b61007a565b5b602082029050919050565b6000610f8a610f8584610f51565b6100da565b90508060208402830185811115610fa457610fa3610121565b5b835b81811015610fcd5780610fb98882610992565b845260208401935050602081019050610fa6565b5050509392505050565b600082601f830112610fec57610feb610064565b5b6005610ff9848285610f77565b91505092915050565b6000610600828403121561101957611018610290565b5b6110246102806100da565b9050600082013567ffffffffffffffff81111561104457611043610295565b5b611050848285016101ad565b6000830152506020611064848285016102bb565b602083015250604082013567ffffffffffffffff81111561108857611087610295565b5b61109484828501610424565b604083015250606082013567ffffffffffffffff8111156110b8576110b7610295565b5b6110c484828501610262565b606083015250608082013567ffffffffffffffff8111156110e8576110e7610295565b5b6110f48482850161053f565b60808301525060a082013567ffffffffffffffff81111561111857611117610295565b5b61112484828501610602565b60a08301525060c082013567ffffffffffffffff81111561114857611147610295565b5b611154848285016106fb565b60c08301525060e082013567ffffffffffffffff81111561117857611177610295565b5b611184848285016106fb565b60e08301525061010082013567ffffffffffffffff8111156111a9576111a8610295565b5b6111b5848285016107f6565b6101008301525061012082013567ffffffffffffffff8111156111db576111da610295565b5b6111e7848285016108ef565b6101208301525061014082013567ffffffffffffffff81111561120d5761120c610295565b5b61121984828501610a10565b6101408301525061016082013567ffffffffffffffff81111561123f5761123e610295565b5b61124b84828501610ae2565b6101608301525061018082013567ffffffffffffffff81111561127157611270610295565b5b61127d84828501610bb1565b610180830152506101a061129384828501610c62565b6101a0830152506102406112a984828501610d13565b6101c0830152506102e06112bf84828501610dc4565b6101e0830152506103806112d584828501610dc4565b610200830152506104206112eb84828501610e75565b610220830152506104c061130184828501610f26565b6102408301525061056061131784828501610fd7565b6102608301525092915050565b6000806040838503121561133b5761133a61005a565b5b600083013567ffffffffffffffff8111156113595761135861005f565b5b61136585828601610262565b925050602083013567ffffffffffffffff8111156113865761138561005f565b5b61139285828601611002565b915050925092905056fea2646970667358221220782cfbe1724e4358ec0870a148d75ac585ed81dbfdecf411e7e53b28ce84cacf64736f6c634300080b0033";

    public static final String FUNC_CALLFUNCTION = "callFunction";

    protected ArraysInStruct() {
        super(BINARY);
    }

    @Deprecated
    protected ArraysInStruct(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ArraysInStruct(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ArraysInStruct(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ArraysInStruct(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> callFunction(List<byte[]> bytesArrayField, Entity newEntity) {
        final Function function = new Function(
                FUNC_CALLFUNCTION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.DynamicBytes>(
                        org.web3j.abi.datatypes.DynamicBytes.class,
                        org.web3j.abi.Utils.typeMap(bytesArrayField, org.web3j.abi.datatypes.DynamicBytes.class)), 
                newEntity), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ArraysInStruct load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ArraysInStruct(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ArraysInStruct load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ArraysInStruct(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ArraysInStruct load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ArraysInStruct(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ArraysInStruct load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ArraysInStruct(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ArraysInStruct> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ArraysInStruct.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ArraysInStruct> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ArraysInStruct.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<ArraysInStruct> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ArraysInStruct.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ArraysInStruct> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ArraysInStruct.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class Entity extends DynamicStruct {
        public byte[] bytesField;

        public byte[] extraData;

        public List<String> stringArrayField;

        public List<byte[]> bytesArrayField;

        public List<byte[]> bytes2ArrayField;

        public List<byte[]> bytes32ArrayField;

        public List<BigInteger> unitArrayField;

        public List<BigInteger> unit256ArrayField;

        public List<Boolean> boolField;

        public List<BigInteger> intArrayField;

        public List<String> addressArrayField;

        public List<String> stringArrayFieldStatic;

        public List<byte[]> bytesArrayFieldStatic;

        public List<byte[]> bytes2ArrayFieldStatic;

        public List<byte[]> bytes32ArrayFieldStatic;

        public List<BigInteger> unitArrayFieldStatic;

        public List<BigInteger> unit256ArrayFieldStatic;

        public List<Boolean> boolFieldStatic;

        public List<BigInteger> intArrayFieldStatic;

        public List<String> addressArrayFieldStatic;

        public Entity(byte[] bytesField, byte[] extraData, List<String> stringArrayField, List<byte[]> bytesArrayField, List<byte[]> bytes2ArrayField, List<byte[]> bytes32ArrayField, List<BigInteger> unitArrayField, List<BigInteger> unit256ArrayField, List<Boolean> boolField, List<BigInteger> intArrayField, List<String> addressArrayField, List<String> stringArrayFieldStatic, List<byte[]> bytesArrayFieldStatic, List<byte[]> bytes2ArrayFieldStatic, List<byte[]> bytes32ArrayFieldStatic, List<BigInteger> unitArrayFieldStatic, List<BigInteger> unit256ArrayFieldStatic, List<Boolean> boolFieldStatic, List<BigInteger> intArrayFieldStatic, List<String> addressArrayFieldStatic) {
            super(new org.web3j.abi.datatypes.DynamicBytes(bytesField), 
                    new org.web3j.abi.datatypes.generated.Bytes32(extraData), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                            org.web3j.abi.datatypes.Utf8String.class,
                            org.web3j.abi.Utils.typeMap(stringArrayField, org.web3j.abi.datatypes.Utf8String.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.DynamicBytes>(
                            org.web3j.abi.datatypes.DynamicBytes.class,
                            org.web3j.abi.Utils.typeMap(bytesArrayField, org.web3j.abi.datatypes.DynamicBytes.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes2>(
                            org.web3j.abi.datatypes.generated.Bytes2.class,
                            org.web3j.abi.Utils.typeMap(bytes2ArrayField, org.web3j.abi.datatypes.generated.Bytes2.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                            org.web3j.abi.datatypes.generated.Bytes32.class,
                            org.web3j.abi.Utils.typeMap(bytes32ArrayField, org.web3j.abi.datatypes.generated.Bytes32.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                            org.web3j.abi.datatypes.generated.Uint256.class,
                            org.web3j.abi.Utils.typeMap(unitArrayField, org.web3j.abi.datatypes.generated.Uint256.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                            org.web3j.abi.datatypes.generated.Uint256.class,
                            org.web3j.abi.Utils.typeMap(unit256ArrayField, org.web3j.abi.datatypes.generated.Uint256.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Bool>(
                            org.web3j.abi.datatypes.Bool.class,
                            org.web3j.abi.Utils.typeMap(boolField, org.web3j.abi.datatypes.Bool.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Int256>(
                            org.web3j.abi.datatypes.generated.Int256.class,
                            org.web3j.abi.Utils.typeMap(intArrayField, org.web3j.abi.datatypes.generated.Int256.class)), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                            org.web3j.abi.datatypes.Address.class,
                            org.web3j.abi.Utils.typeMap(addressArrayField, org.web3j.abi.datatypes.Address.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.Utf8String>(
                            org.web3j.abi.datatypes.Utf8String.class,
                            org.web3j.abi.Utils.typeMap(stringArrayFieldStatic, org.web3j.abi.datatypes.Utf8String.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.DynamicBytes>(
                            org.web3j.abi.datatypes.DynamicBytes.class,
                            org.web3j.abi.Utils.typeMap(bytesArrayFieldStatic, org.web3j.abi.datatypes.DynamicBytes.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.generated.Bytes2>(
                            org.web3j.abi.datatypes.generated.Bytes2.class,
                            org.web3j.abi.Utils.typeMap(bytes2ArrayFieldStatic, org.web3j.abi.datatypes.generated.Bytes2.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.generated.Bytes32>(
                            org.web3j.abi.datatypes.generated.Bytes32.class,
                            org.web3j.abi.Utils.typeMap(bytes32ArrayFieldStatic, org.web3j.abi.datatypes.generated.Bytes32.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.generated.Uint256>(
                            org.web3j.abi.datatypes.generated.Uint256.class,
                            org.web3j.abi.Utils.typeMap(unitArrayFieldStatic, org.web3j.abi.datatypes.generated.Uint256.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.generated.Uint256>(
                            org.web3j.abi.datatypes.generated.Uint256.class,
                            org.web3j.abi.Utils.typeMap(unit256ArrayFieldStatic, org.web3j.abi.datatypes.generated.Uint256.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.Bool>(
                            org.web3j.abi.datatypes.Bool.class,
                            org.web3j.abi.Utils.typeMap(boolFieldStatic, org.web3j.abi.datatypes.Bool.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.generated.Int256>(
                            org.web3j.abi.datatypes.generated.Int256.class,
                            org.web3j.abi.Utils.typeMap(intArrayFieldStatic, org.web3j.abi.datatypes.generated.Int256.class)), 
                    new org.web3j.abi.datatypes.generated.StaticArray5<org.web3j.abi.datatypes.Address>(
                            org.web3j.abi.datatypes.Address.class,
                            org.web3j.abi.Utils.typeMap(addressArrayFieldStatic, org.web3j.abi.datatypes.Address.class)));
            this.bytesField = bytesField;
            this.extraData = extraData;
            this.stringArrayField = stringArrayField;
            this.bytesArrayField = bytesArrayField;
            this.bytes2ArrayField = bytes2ArrayField;
            this.bytes32ArrayField = bytes32ArrayField;
            this.unitArrayField = unitArrayField;
            this.unit256ArrayField = unit256ArrayField;
            this.boolField = boolField;
            this.intArrayField = intArrayField;
            this.addressArrayField = addressArrayField;
            this.stringArrayFieldStatic = stringArrayFieldStatic;
            this.bytesArrayFieldStatic = bytesArrayFieldStatic;
            this.bytes2ArrayFieldStatic = bytes2ArrayFieldStatic;
            this.bytes32ArrayFieldStatic = bytes32ArrayFieldStatic;
            this.unitArrayFieldStatic = unitArrayFieldStatic;
            this.unit256ArrayFieldStatic = unit256ArrayFieldStatic;
            this.boolFieldStatic = boolFieldStatic;
            this.intArrayFieldStatic = intArrayFieldStatic;
            this.addressArrayFieldStatic = addressArrayFieldStatic;
        }

        public Entity(DynamicBytes bytesField, Bytes32 extraData, DynamicArray<Utf8String> stringArrayField, DynamicArray<DynamicBytes> bytesArrayField, DynamicArray<Bytes2> bytes2ArrayField, DynamicArray<Bytes32> bytes32ArrayField, DynamicArray<Uint256> unitArrayField, DynamicArray<Uint256> unit256ArrayField, DynamicArray<Bool> boolField, DynamicArray<Int256> intArrayField, DynamicArray<Address> addressArrayField, StaticArray5<Utf8String> stringArrayFieldStatic, StaticArray5<DynamicBytes> bytesArrayFieldStatic, StaticArray5<Bytes2> bytes2ArrayFieldStatic, StaticArray5<Bytes32> bytes32ArrayFieldStatic, StaticArray5<Uint256> unitArrayFieldStatic, StaticArray5<Uint256> unit256ArrayFieldStatic, StaticArray5<Bool> boolFieldStatic, StaticArray5<Int256> intArrayFieldStatic, StaticArray5<Address> addressArrayFieldStatic) {
            super(bytesField, extraData, stringArrayField, bytesArrayField, bytes2ArrayField, bytes32ArrayField, unitArrayField, unit256ArrayField, boolField, intArrayField, addressArrayField, stringArrayFieldStatic, bytesArrayFieldStatic, bytes2ArrayFieldStatic, bytes32ArrayFieldStatic, unitArrayFieldStatic, unit256ArrayFieldStatic, boolFieldStatic, intArrayFieldStatic, addressArrayFieldStatic);
            this.bytesField = bytesField.getValue();
            this.extraData = extraData.getValue();
            this.stringArrayField = stringArrayField.getValue();
            this.bytesArrayField = bytesArrayField.getValue();
            this.bytes2ArrayField = bytes2ArrayField.getValue();
            this.bytes32ArrayField = bytes32ArrayField.getValue();
            this.unitArrayField = unitArrayField.getValue();
            this.unit256ArrayField = unit256ArrayField.getValue();
            this.boolField = boolField.getValue();
            this.intArrayField = intArrayField.getValue();
            this.addressArrayField = addressArrayField.getValue();
            this.stringArrayFieldStatic = stringArrayFieldStatic.getValue();
            this.bytesArrayFieldStatic = bytesArrayFieldStatic.getValue();
            this.bytes2ArrayFieldStatic = bytes2ArrayFieldStatic.getValue();
            this.bytes32ArrayFieldStatic = bytes32ArrayFieldStatic.getValue();
            this.unitArrayFieldStatic = unitArrayFieldStatic.getValue();
            this.unit256ArrayFieldStatic = unit256ArrayFieldStatic.getValue();
            this.boolFieldStatic = boolFieldStatic.getValue();
            this.intArrayFieldStatic = intArrayFieldStatic.getValue();
            this.addressArrayFieldStatic = addressArrayFieldStatic.getValue();
        }
    }
}
