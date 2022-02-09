/*-
 *
 * Smart Contracts Libs Labs
 *
 * Copyright (C) 2019 - 2021 Hedera Hashgraph, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import {
    Client,
    PrivateKey,
    ContractCreateTransaction,
    FileCreateTransaction,
    AccountId, Hbar, ContractExecuteTransaction, ContractFunctionParameters,
} from "@hashgraph/sdk";

import * as abiDecoder from "abi-decoder";
import * as dotenv from "dotenv";
import * as fs from "fs";
import axios from "axios";

dotenv.config({path : '../.env'});

let abi;
let client = Client.forTestnet();
const constructMessage = 'Hello Hedera';

const delay = ms => new Promise(res => setTimeout(res, ms));

/**
 * Runs each step of the example one after the other
 */
async function main() {

    // Import the ABI
    abi = JSON.parse(fs.readFileSync('../contracts/abi.json', 'utf8'));

    // Setup an ethers.js interface using the abi
    abiDecoder.addABI(abi);

    client.setOperator(
        AccountId.fromString(process.env.OPERATOR_ID),
        PrivateKey.fromString(process.env.OPERATOR_KEY)
    );

    client.setMaxTransactionFee(new Hbar(5));
    client.setMaxQueryPayment(new Hbar(5))

    // deploy the contract to Hedera from bytecode
    const contractId = await deployContract();
    // call the contract's set_message function
    await callSetMessage(contractId, 'Hello again');
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

    // Import the compiled contract
    const bytecode = JSON.parse(fs.readFileSync('../contracts/bytecode.json', 'utf8'));
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

    // create the constructor parameters
    const contractFunctionParameters = new ContractFunctionParameters()
        .addString(constructMessage);
    // Create the contract
    const contractTransactionResponse = await new ContractCreateTransaction()
        // Set the parameters that should be passed to the contract constructor
        .setConstructorParameters(contractFunctionParameters)
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

    const contractFunctionParameters = new ContractFunctionParameters()
        .addString(newMessage);

    // execute the transaction calling the set_message contract function
    const transaction = await new ContractExecuteTransaction()
        .setContractId(contractId)
        .setFunction("set_message", contractFunctionParameters)
        .setGas(100000)
        .execute(client);

    // get the receipt for the transaction
    await transaction.getReceipt(client);
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

    console.log(`Calling set_message to trigger new event`);
    // execute the transaction calling the set_message contract function
    const transaction = await new ContractExecuteTransaction()
        .setContractId(contractId)
        .setFunction("set_message", new ContractFunctionParameters().addString(newMessage))
        .setGas(100000)
        .execute(client);

    // a record contains the output of the function
    // as well as events, let's get events for this transaction
    const record = await transaction.getRecord(client);

    // the events from the function call are in record.contractFunctionResult.logs.data
    // let's parse the logs using abi-decoder
    // there may be several log entries

    const logs = []

    record.contractFunctionResult.logs.forEach(log => {
        const logJson = {
            data: "",
            topics: []
        }

        // convert the log.data (uint8Array) to a string
        logJson.data = '0x'.concat(Buffer.from(log.data).toString('hex'));

        // get topics from log
        log.topics.forEach(topic => {
            logJson.topics.push('0x'.concat(Buffer.from(topic).toString('hex')));
        });

        logs.push(logJson);
    });

    const events = abiDecoder.decodeLogs(logs);

    console.log(`\nRecord events`);
    for (let eventIndex=0; eventIndex < events.length; eventIndex++) {
        const event = events[eventIndex];
        console.log(`event ${event.name}`);
        for (let eventDataIndex=0; eventDataIndex < event.events.length; eventDataIndex++) {
            const eventData = event.events[eventDataIndex];
            console.log(`  ${eventData.name} : ${eventData.value}`);
        }
    };
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

            // put all the logs into a single array containing log data and topics
            const logs = []

            jsonResponse.logs.forEach(log => {
                // create an object to specify log parsing requirements
                const logJson = {
                    data: log.data,
                    topics: log.topics
                }

                logs.push(logJson);
            });

            const events = abiDecoder.decodeLogs(logs);

            // output the from address and message stored in the event
            console.log(`\nMirror events`);
            for (let eventIndex=0; eventIndex < events.length; eventIndex++) {
                const event = events[eventIndex];
                console.log(`event ${event.name}`);
                for (let eventDataIndex=0; eventDataIndex < event.events.length; eventDataIndex++) {
                    const eventData = event.events[eventDataIndex];
                    console.log(`  ${eventData.name} : ${eventData.value}`);
                }
            };

        })
        .catch(function (err) {
            console.error(err);
        });
}

void main();
