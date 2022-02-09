const {
    Client,
    ContractCreateTransaction,
    ContractExecuteTransaction,
    FileCreateTransaction,
    AccountId,
    Hbar,
    PrivateKey, ContractCallQuery,
} = require("@hashgraph/sdk");

const { Interface } = require("@ethersproject/abi");
require("dotenv").config({path : '../.env'});

let abi;
let client;
let abiInterface;
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

    abiInterface = new Interface(abi);

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
        // Set the parameters that should be passed to the contract constructor
        // using the output from the ethers.js library
        // get the constructor parameter
        // .slice(2) to remove leading '0x'
        const constructParameterAsHexString = abiInterface.encodeDeploy(parameters).slice(2);
        // convert to a Uint8Array
        const constructorParametersAsUint8Array = Buffer.from(constructParameterAsHexString, 'hex');

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
    const functionCallAsUint8Array = encodeFunctionParameters(functionName, parameters);

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
    const results = abiInterface.decodeFunctionResult(functionName, record.contractFunctionResult.bytes);
    console.log(results);
    return results;
}

async function query(functionName, parameters, gas) {
    console.log(`querying ${functionName} with [${parameters}]`);

    // generate function call with function name and parameters
    const functionCallAsUint8Array = encodeFunctionParameters(functionName, parameters);

    const maxGas = gas ? undefined : defaultGas;

    // execute the query
    const contractFunctionResult = await new ContractCallQuery()
        .setContractId(contractId)
        .setFunctionParameters(functionCallAsUint8Array)
        .setGas(maxGas)
        .execute(client);

    // the result of the function call is in the query
    // let`s parse it using ethers.js
    const results = abiInterface.decodeFunctionResult(functionName, contractFunctionResult.bytes);
    console.log(results);
    return results;

}

/**
 * Helper function to encode function name and parameters that can be used to invoke a contract's function
 * @param functionName the name of the function to invoke
 * @param parameterArray an array of parameters to pass to the function
 */
function encodeFunctionParameters(functionName, parameterArray) {
    // build the call parameters using ethers.js
    // .slice(2) to remove leading '0x'
    const functionCallAsHexString = abiInterface.encodeFunctionData(functionName, parameterArray).slice(2);
    // convert to a Uint8Array
    return Buffer.from(functionCallAsHexString, `hex`);
}

module.exports = {
    deploy: deploy,
    call: call,
    query: query,
    setMaxTransactionFee: setMaxTransactionFee,
    setMaxQueryPayment: setMaxQueryPayment,
    setDefaultGas: setDefaultGas,
    setAbi: setAbi,
    setContractId: setContractId,
    setClient: setClient,
    getClient: getClient,
    getOperatorId: getOperatorId,
    getOperatorKey: getOperatorKey
}

