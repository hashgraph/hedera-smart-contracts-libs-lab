import {
    Client,
    PrivateKey,
    ContractCreateTransaction,
    FileCreateTransaction,
    AccountId, Hbar, ContractExecuteTransaction, ContractCallQuery,
} from "@hashgraph/sdk";

import { Interface } from "@ethersproject/abi";
import * as dotenv from "dotenv";
import * as fs from "fs";
import axios from "axios";

dotenv.config({path : '../.env'});

let bytecode;
let abi;
let client = Client.forTestnet();
const constructMessage = 'Hello Hedera';
let abiInterface;

const delay = ms => new Promise(res => setTimeout(res, ms));

/**
 * Runs each step of the example one after the other
 */
async function main() {
    // Import the compiled contract
    bytecode = JSON.parse(fs.readFileSync('./bytecode.json', 'utf8'));

    // Import the ABI
    abi = JSON.parse(fs.readFileSync('./abi.json', 'utf8'));

    // Setup an ethers.js interface using the abi
    abiInterface = new Interface(abi);

    client.setOperator(
        AccountId.fromString(process.env.OPERATOR_ID),
        PrivateKey.fromString(process.env.OPERATOR_KEY)
    );

    client.setMaxTransactionFee(new Hbar(5));
    client.setMaxQueryPayment(new Hbar(5))

    // deploy the contract to Hedera from bytecode
    const contractId = await deployContract();
    // query the contract's get_message function
    await queryGetMessage(contractId);
    // call the contract's set_message function
    await callSetMessage(contractId, 'Hello again');
    // query the contract's get_message function
    await queryGetMessage(contractId);
    // call the contract's get_message function
    await callGetMessage(contractId);
    // get call events from a transaction record
    await getEventsFromRecord(contractId);
    // get contract events from a mirror node
    await getEventsFromMirror(contractId);
}

/**
 * Deploys the contract to Hedera by first creating a file containing the bytecode, then creating a contract from the resulting
 * FileId, specifying a parameter value for the constructor and returning the resulting ContractId
 */
async function deployContract() {
    console.log(`\nDeploying the contract`);

    // The contract bytecode is located on the `object` field
    const contractByteCode = /** @type {string} */ (bytecode.object);

    // Create a file on Hedera which contains the contact bytecode.
    // Note: The contract bytecode **must** be hex encoded, it should not
    // be the actual data the hex represents
    const fileTransactionResponse = await new FileCreateTransaction()
        .setKeys([client.operatorPublicKey])
        .setContents(contractByteCode)
        .execute(client);

    // Fetch the receipt for transaction that created the file
    const fileReceipt = await fileTransactionResponse.getReceipt(client);

    // The file ID is located on the transaction receipt
    const fileId = fileReceipt.fileId;

    // get the constructor parameter
    // .slice(2) to remove leading '0x'
    const constructParameterAsHexString = abiInterface.encodeDeploy([constructMessage]).slice(2);
    // convert to a Uint8Array
    const constructorParametersAsUint8Array = Buffer.from(constructParameterAsHexString, 'hex');
    // Create the contract
    const contractTransactionResponse = await new ContractCreateTransaction()
        // Set the parameters that should be passed to the contract constructor
        // using the output from the ethers.js library
        .setConstructorParameters(constructorParametersAsUint8Array)
        // Set gas to create the contract
        .setGas(100_000)
        // The contract bytecode must be set to the file ID containing the contract bytecode
        .setBytecodeFileId(fileId)
        .execute(client);

    // Fetch the receipt for the transaction that created the contract
    const contractReceipt = await contractTransactionResponse.getReceipt(client);

    // The contract ID is located on the transaction receipt
    const contractId = contractReceipt.contractId;

    console.log(`new contract ID: ${contractId.toString()}`);
    return contractId;
}

/**
 * Invokes the set_message function of the contract
 * @param contractId
 * @param newMessage
 * @returns {Promise<void>}
 */
async function callSetMessage(contractId, newMessage) {
    console.log(`\nCalling set_message with '${newMessage}' parameter value`);

    // generate function call with function name and parameters
    const functionCallAsUint8Array = encodeFunctionParameters('set_message',  [newMessage]);

    // execute the transaction calling the set_message contract function
    const transaction = await new ContractExecuteTransaction()
        .setContractId(contractId)
        .setFunctionParameters(functionCallAsUint8Array)
        .setGas(100000)
        .execute(client);

    // get the receipt for the transaction
    await transaction.getReceipt(client);
}

/**
 * Invokes the get_message function of the contract using a query
 * The get_message function doesn't mutate the contract's state, therefore a query can be used
 * @param contractId
 * @returns {Promise<void>}
 */
async function queryGetMessage(contractId) {
    console.log(`\nget_message Query`);
    // generate function call with function name and parameters
    const functionCallAsUint8Array = encodeFunctionParameters('get_message', []);

    // query the contract
    const contractCall = await new ContractCallQuery()
        .setContractId(contractId)
        .setFunctionParameters(functionCallAsUint8Array)
        .setQueryPayment(new Hbar(2))
        .setGas(100000)
        .execute(client);

    let results = abiInterface.decodeFunctionResult('get_message', contractCall.bytes);
    console.log(results);
}

/**
 * Invokes the get_message function of the contract using a transaction and uses the resulting record to determine
 * the returned value from the function
 * Note: The get_message function doesn't mutate the contract's state, therefore a query could be used, but this shows how to
 * process return values from a contract function that does mutate contract state using a TransactionRecord
 * @param contractId
 */
async function callGetMessage(contractId) {
    console.log(`\nget_message transaction`);

    // generate function call with function name and parameters
    const functionCallAsUint8Array = encodeFunctionParameters('get_message', []);

    // doing the same with a transaction and a record
    const transaction = await new ContractExecuteTransaction()
        .setContractId(contractId)
        .setFunctionParameters(functionCallAsUint8Array)
        .setGas(100000)
        .execute(client);

    // a record contains the output of the function
    const record = await transaction.getRecord(client);
    // the result of the function call is in record.contractFunctionResult.bytes
    // let`s parse it using ethers.js
    const results = abiInterface.decodeFunctionResult('get_message', record.contractFunctionResult.bytes);
    console.log(results);
}

/**
 * Gets events from a contract function invocation using a TransactionRecord
 * Note: This function calls the contract's set_message function in order to generate a new event
 * @param contractId
 */
async function getEventsFromRecord(contractId) {
    console.log(`\nGetting event(s) from record`);

    // calling "set_message" with the current date/time to generate a new event
    const newMessage = new Date().toLocaleString();
    const functionCallAsUint8Array = encodeFunctionParameters('set_message',  [newMessage]);

    console.log(`Calling set_message to trigger new event`);
    // execute the transaction calling the set_message contract function
    const transaction = await new ContractExecuteTransaction()
        .setContractId(contractId)
        .setFunctionParameters(functionCallAsUint8Array)
        .setGas(100000)
        .execute(client);

    // a record contains the output of the function
    // as well as events, let's get events for this transaction
    const record = await transaction.getRecord(client);

    // query the contract's get_message function to witness update
    await queryGetMessage(contractId);

    // the events from the function call are in record.contractFunctionResult.logs.data
    // let's parse the logs using ethers.js
    // there may be several log entries

    record.contractFunctionResult.logs.forEach(log => {
        // convert the log.data (uint8Array) to a string
        let logStringHex = '0x'.concat(Buffer.from(log.data).toString('hex'));

        // get topics from log
        let logTopics = [];
        log.topics.forEach(topic => {
            logTopics.push('0x'.concat(Buffer.from(topic).toString('hex')));
        });

        // decode the event data
        let logRequest = {};
        logRequest.data = logStringHex;
        logRequest.topics = logTopics;
        let event = abiInterface.parseLog(logRequest);
        // output the from address stored in the event
        console.log(`Record event: from '${AccountId.fromSolidityAddress(event.args.from).toString()}' update to '${event.args.message}'`);
    });
}

/**
 * Gets all the events for a given ContractId from a mirror node
 * Note: To particular filtering is implemented here, in practice you'd only want to query for events
 * in a time range or from a given timestamp for example
 * @param contractId
 */

async function getEventsFromMirror(contractId) {
    console.log(`\nGetting event(s) from mirror`);
    console.log(`Waiting 10s to allow transaction propagation to mirror`);
    await delay(10000);

    const url = `https://testnet.mirrornode.hedera.com/api/v1/contracts/${contractId.toString()}/results/logs?order=asc`;

    axios.get(url)
        .then(function (response) {
            const jsonResponse = response.data;

            jsonResponse.logs.forEach(log => {
                // create an object to specify log parsing requirements
                let logRequest = {};
                logRequest.data = log.data;
                logRequest.topics = log.topics;
                // parse the logs
                let event = abiInterface.parseLog(logRequest);
                // output the from address and message stored in the event
                console.log(`Mirror event(s): from '${AccountId.fromSolidityAddress(event.args.from).toString()}' update to '${event.args.message}'`);
            });
        })
        .catch(function (err) {
            console.error(err);
        });
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

void main();
