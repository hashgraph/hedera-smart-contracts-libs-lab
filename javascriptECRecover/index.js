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

import * as dotenv from "dotenv";
import * as fs from "fs";
import web3 from "web3";
import Eth from "web3-eth";
import {Interface} from "ethers/lib/utils.js";
import {ethers} from "ethers";

dotenv.config({path : '../.env'});

let abi;
let abiInterface;
let client = Client.forTestnet();
const constructMessage = 'Hello Hedera';

const delay = ms => new Promise(res => setTimeout(res, ms));

/**
 * Runs each step of the example one after the other
 */
async function main() {

    // Import the ABI
    abi = JSON.parse(fs.readFileSync('../contracts/abi.json', 'utf8'));
    abiInterface = new Interface(abi);

    client.setOperator(
        AccountId.fromString(process.env.OPERATOR_ID),
        PrivateKey.fromString(process.env.OPERATOR_KEY)
    );

    client.setMaxTransactionFee(new Hbar(5));
    client.setMaxQueryPayment(new Hbar(5))

    // deploy the contract to Hedera from bytecode
    const contractId = await deployContract();

    const eth = new Eth("https://testnet.hashio.io/api");
    const privateKey = PrivateKey.generateECDSA();

    const hexPrivateKey = "0x"+privateKey.toStringRaw();
    const wallet = new ethers.Wallet(hexPrivateKey);
    console.log(`Wallet address from private key ${wallet.address}`);

    const messageToHash = "testing";
    const hashedMessage = web3.utils.sha3(messageToHash);
    const signature = eth.accounts.sign(hashedMessage, privateKey.toStringRaw());
    const R = signature.r;
    const S = signature.s;
    const V = parseInt(signature.v);

    const functionCallAsUint8Array = encodeFunctionParameters('VerifyMessage',  [hashedMessage, V, R, S]);

    // execute the transaction calling the set_message contract function
    const contractCall = await new ContractExecuteTransaction()
        .setContractId(contractId)
        .setFunctionParameters(functionCallAsUint8Array)
        .setGas(100000)
        .execute(client);

    const record = await contractCall.getRecord(client);

    let results = abiInterface.decodeFunctionResult('VerifyMessage', record.contractFunctionResult.asBytes());
    console.log(`Response from smart contract (execute) = ${results[0]}`);

    // doing the same through a smart contract query
    const contractQuery = await new ContractCallQuery()
        .setContractId(contractId)
        .setFunctionParameters(functionCallAsUint8Array)
        .setQueryPayment(new Hbar(2))
        .setGas(100000)
        .execute(client);

    results = abiInterface.decodeFunctionResult('VerifyMessage', contractQuery.bytes);
    console.log(`Response from smart contract (query) = ${results[0]}`);

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

    // Create the contract
    const contractTransactionResponse = await new ContractCreateTransaction()
        // Set the parameters that should be passed to the contract constructor
        // using the output from the ethers.js library
        .setConstructorParameters(new ContractFunctionParameters().addString(constructMessage))
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

function encodeFunctionParameters(functionName, parameterArray) {
    // build the call parameters using ethers.js
    // .slice(2) to remove leading '0x'
    const functionCallAsHexString = abiInterface.encodeFunctionData(functionName, parameterArray).slice(2);
    // convert to a Uint8Array
    return Buffer.from(functionCallAsHexString, `hex`);
}

void main();
