import {
    Client,
    ContractCreateTransaction,
    ContractExecuteTransaction,
    FileCreateTransaction,
    AccountId,
    Hbar,
    PrivateKey, ContractCallQuery,
} from"@hashgraph/sdk";

import Web3 from "web3";

import * as dotenv from "dotenv";
dotenv.config({path : '../.env'});

const web3 = new Web3;
let abi;
let client;
let contractId;

let maxTransactionFee = new Hbar(5);
let maxQueryPayment = new Hbar(5);
let defaultGas = 100_000;

function getClient() {
    return client;
}
function getOperatorId() {
    return process.env.OPERATOR_ID;
}
function getOperatorKey() {
    return process.env.OPERATOR_KEY;
}

function setClient(newClient) {
    client = newClient;
}
function setAbi(newAbi) {
    abi = newAbi;
}
function setContractId(newContractId) {
    contractId = newContractId;
}
function setMaxTransactionFee(newMaxfee) {
    maxTransactionFee = new Hbar(newMaxfee);
}

function setMaxQueryPayment(newMaxPaymentHbar) {
    maxQueryPayment = new Hbar(newMaxPaymentHbar);
}

function setDefaultGas(newDefaultGas) {
    defaultGas = newDefaultGas;
}

async function deploy (tokenJson, parameters, gas) {

    client = Client.forNetwork(process.env.NETWORK);
    client.setOperator(
        AccountId.fromString(process.env.OPERATOR_ID),
        PrivateKey.fromString(process.env.OPERATOR_KEY)
    );

    client.setMaxTransactionFee(maxTransactionFee);
    client.setMaxQueryPayment(maxQueryPayment)

    abi = tokenJson.abi;

    if (typeof(parameters) === 'undefined') {
        parameters = [];
    }

    console.log(`\nDeploying the contract`);

    const fileTransactionResponse = await new FileCreateTransaction()
        .setKeys([client.operatorPublicKey])
        .setContents(tokenJson.bytecode)
        .execute(client);

    // TODO: optionally append here

    // Fetch the receipt for transaction that created the file
    const fileReceipt = await fileTransactionResponse.getReceipt(client);

    // The file ID is located on the transaction receipt
    const fileId = fileReceipt.fileId;

    const maxGas = gas ? undefined : defaultGas;

    const contractTransaction = await new ContractCreateTransaction()
        // Set gas to create the contract
        .setGas(maxGas)
        // The contract bytecode must be set to the file ID containing the contract bytecode
        .setBytecodeFileId(fileId);

    // add constructor parameters if necessary
    if (parameters.length > 0) {
        const functionAbi = abi.find(func => (func.name === functionName && func.type === "constructor"));
        const encodedParametersHex = web3.eth.abi.encodeFunctionCall(functionAbi, parameters).slice(2);
        const constructorParametersAsUint8Array = Buffer.from(encodedParametersHex, 'hex');

        contractTransaction.setConstructorParameters(constructorParametersAsUint8Array)
    }

    // Create the contract
    const contractTransactionResponse = await contractTransaction.execute(client);

    // Fetch the receipt for the transaction that created the contract
    const contractReceipt = await contractTransactionResponse.getReceipt(client);

    // The contract ID is located on the transaction receipt
    contractId = contractReceipt.contractId;

    console.log(`new contract ID: ${contractId.toString()}`);

    return contractId;
}

async function call(functionName, parameters, gas) {
    console.log(`calling ${functionName} with [${parameters}]`);

    // generate function call with function name and parameters
    const functionCallAsUint8Array = encodeFunctionCall(functionName, parameters);

    const maxGas = gas ? undefined : defaultGas;

    // execute the transaction
    const transaction = await new ContractExecuteTransaction()
        .setContractId(contractId)
        .setFunctionParameters(functionCallAsUint8Array)
        .setGas(maxGas)
        .execute(client);

    // a record contains the output of the function
    const record = await transaction.getRecord(client);
    // the result of the function call is in record.contractFunctionResult.bytes
    // let`s parse it using ethers.js
    const results = decodeFunctionResult(functionName, record.contractFunctionResult.bytes);
    console.log(results);
    return results;
}

async function query(functionName, parameters, gas) {
    console.log(`querying ${functionName} with [${parameters}]`);

    // generate function call with function name and parameters
    const functionCallAsUint8Array = encodeFunctionCall(functionName, parameters);

    const maxGas = gas ? undefined : defaultGas;

    // execute the query
    const contractFunctionResult = await new ContractCallQuery()
        .setContractId(contractId)
        .setFunctionParameters(functionCallAsUint8Array)
        .setGas(maxGas)
        .execute(client);

    // the result of the function call is in the query
    // let`s parse it using ethers.js
    const results = decodeFunctionResult(functionName, contractFunctionResult.bytes);
    console.log(results);
    return results;

}

/**
 * Encodes a function call so that the contract's function can be executed or called
 * @param functionName the name of the function to call
 * @param parameters the array of parameters to pass to the function
 */
function encodeFunctionCall(functionName, parameters) {
    const functionAbi = abi.find(func => (func.name === functionName && func.type === "function"));
    const encodedParametersHex = web3.eth.abi.encodeFunctionCall(functionAbi, parameters).slice(2);
    return Buffer.from(encodedParametersHex, 'hex');
}

/**
 * Decodes the result of a contract's function execution
 * @param functionName the name of the function within the ABI
 * @param resultAsBytes a byte array containing the execution result
 */
function decodeFunctionResult(functionName, resultAsBytes) {
    const functionAbi = abi.find(func => func.name === functionName);
    const functionParameters = functionAbi.outputs;
    const resultHex = '0x'.concat(Buffer.from(resultAsBytes).toString('hex'));
    const result = web3.eth.abi.decodeParameters(functionParameters, resultHex);
    return result;
}

export {
    deploy,
    call,
    query,
    setMaxTransactionFee,
    setMaxQueryPayment,
    setDefaultGas,
    setAbi,
    setContractId,
    setClient,
    getClient,
    getOperatorId,
    getOperatorKey
}

