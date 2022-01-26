import {
    Client,
    PrivateKey,
    ContractCreateTransaction,
    FileCreateTransaction,
    AccountId,
} from "@hashgraph/sdk";

import { ethers } from "ethers";
import * as dotenv from "dotenv";
import * as fs from "fs";

dotenv.config({path : '../.env'});

async function main() {
    // Import the compiled contract
    const bytecode = JSON.parse(fs.readFileSync('./bytecode.json', 'utf8'));

    // Import the ABI
    const abi = JSON.parse(fs.readFileSync('./abi.json', 'utf8'));

    let client = Client.forTestnet();

    client.setOperator(
        AccountId.fromString(process.env.OPERATOR_ID),
        PrivateKey.fromString(process.env.OPERATOR_KEY)
    );

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

    // get command line parameters from node
    const myArgs = process.argv.slice(2);
    let constructMessage = "Hello Hedera";
    if (myArgs.length !== 0) {
        constructMessage = myArgs[0];
    }
    console.log('constructor value: ', constructMessage);

    // get the constructor parameter
    let abiInterface = new ethers.utils.Interface(abi);
    let constructParameterAsHexString = abiInterface.encodeDeploy([constructMessage]);
    // remove the leading "0x"
    constructParameterAsHexString = constructParameterAsHexString.replace("0x","");
    // convert to a Uint8Array
    const constructorParametersAsUint8Array = Buffer.from(constructParameterAsHexString, "hex");
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
    const contractReceipt = await contractTransactionResponse.getReceipt(
        client
    );

    // The contract ID is located on the transaction receipt
    const contractId = contractReceipt.contractId;

    console.log(`new contract ID: ${contractId.toString()}`);
}

void main();
