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
    AccountId, Hbar, ContractExecuteTransaction, ContractCallQuery, ContractFunctionParameters,
} from "@hashgraph/sdk";
import {ethers, Contract} from 'ethers';

import * as dotenv from "dotenv";
import * as fs from "fs";

dotenv.config({path : '../.env'});

let client = Client.forTestnet();
const constructMessage = 'Hello Hedera';

/**
 * Runs each step of the example one after the other
 */
async function main() {

    const provider = new ethers.providers.JsonRpcProvider("https://testnet.hashio.io/api");

    // Import the ABI
    const abi = JSON.parse(fs.readFileSync('../contracts/abi.json', 'utf8'));

    client.setOperator(
        AccountId.fromString(process.env.OPERATOR_ID),
        PrivateKey.fromString(process.env.OPERATOR_KEY)
    );

    client.setMaxTransactionFee(new Hbar(5));
    client.setMaxQueryPayment(new Hbar(5))

    // deploy the contract to Hedera from bytecode
    const contractId = await deployContract();

    const contract = new Contract(contractId.toSolidityAddress(), abi, provider);

    // // subscribe to events for this contract using the "on" listener
    // // event SetMessage(address indexed from, string message);
    // contract.on("SetMessage", (address, message) => {
    //     console.log(`Got Event -> SetMessage ${address}, ${message}`);
    // });

    // subscribe to events using a filter
    const iface = new ethers.utils.Interface(abi)
    provider.getBlockNumber().then(function(x) {
        contract.queryFilter([contract.filters.SetMessage()], x-10, x+20).then(function(eventLogs) {


            eventLogs.forEach((eventLog) => {
                const event = iface.parseLog(eventLog);
                console.log(`Got event -> ${event.args.from}, ${event.args.message}`);
            });

        })
    });

    // call the contract to create events
    await callSetMessage(contractId, 'Hello 1');
    await callSetMessage(contractId, 'Hello 2');
    await callSetMessage(contractId, 'Hello 3');

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

    const constructorParameters = new ContractFunctionParameters()
        .addString(constructMessage);
    // Create the contract
    const contractTransactionResponse = await new ContractCreateTransaction()
        // Set the parameters that should be passed to the contract constructor
        // using the output from the ethers.js library
        .setConstructorParameters(constructorParameters)
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

    const functionParameters = new ContractFunctionParameters()
        .addString(newMessage);

    // execute the transaction calling the set_message contract function
    const transaction = await new ContractExecuteTransaction()
        .setContractId(contractId)
        .setFunction("set_message", new ContractFunctionParameters().addString(
            newMessage
        ))
        .setGas(100000)
        .execute(client);

    // get the receipt for the transaction
    await transaction.getReceipt(client);
}

void main();
