import {
    Client,
    PrivateKey,
    ContractId,
    ContractCallQuery,
    AccountId,
    ContractExecuteTransaction,
    Hbar
} from "@hashgraph/sdk";

import { ethers } from "ethers";
import * as dotenv from "dotenv";
import * as fs from "fs";

dotenv.config({path : '../.env'});

async function main() {
    // Import the ABI
    const abi = JSON.parse(fs.readFileSync('./abi.json', 'utf8'));
    // create an ethers.js interface from the ABI
    const abiInterface = new ethers.utils.Interface(abi);

    let client = Client.forTestnet();

    client.setOperator(
        AccountId.fromString(process.env.OPERATOR_ID),
        PrivateKey.fromString(process.env.OPERATOR_KEY)
    );

    client.setMaxTransactionFee(new Hbar(5));
    client.setMaxQueryPayment(new Hbar(5))

    // get command line parameters from node
    const myArgs = process.argv.slice(2);

    if (myArgs.length !== 2) {
        console.error("Missing command line parameters. Exiting.")
    } else {
        const contractId = ContractId.fromString(myArgs[0]);
        const newMessage = myArgs[1];
        console.log('Using contract Id: ', contractId.toString());
        console.log('Setting message to: ', newMessage);

        // build the call parameters using ethers.js
        // calling "set_message" with updated message
        let functionCallAsHexString = abiInterface.encodeFunctionData("set_message", [newMessage]);
        // remove the leading "0x"
        functionCallAsHexString = functionCallAsHexString.replace("0x","");
        // convert to a Uint8Array
        let functionCallAsUint8Array = Buffer.from(functionCallAsHexString, "hex");

        // execute the transaction calling the set_message contract function
        const transaction = await new ContractExecuteTransaction()
            .setContractId(contractId)
            .setFunctionParameters(functionCallAsUint8Array)
            .setGas(100000)
            .execute(client);

        // get the receipt for the transaction
        await transaction.getReceipt(client);

        // check the message was set properly in the contract
        functionCallAsHexString = abiInterface.encodeFunctionData("get_message", []);
        // remove the leading "0x"
        functionCallAsHexString = functionCallAsHexString.replace("0x","");
        // convert to a Uint8Array
        functionCallAsUint8Array = Buffer.from(functionCallAsHexString, "hex");

        // (optionally) query the contract to confirm it has updated
        const contractCall = await new ContractCallQuery()
            .setContractId(contractId)
            .setFunctionParameters(functionCallAsUint8Array)
            .setQueryPayment(new Hbar(2))
            .setGas(100000)
            .execute(client);

        let results = abiInterface.decodeFunctionResult("get_message", contractCall.bytes);
        console.log("Result from query")
        console.log(results);
    }
}

void main();
