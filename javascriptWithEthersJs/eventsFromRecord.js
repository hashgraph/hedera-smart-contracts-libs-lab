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

    if (myArgs.length === 0) {
        console.error("No contract Id specified on command line. Exiting.")
    } else {
        const contractId = ContractId.fromString(myArgs[0]);
        console.log('Using contract Id: ', contractId.toString());

        // build the call parameters using ethers.js
        // calling "set_message" with the current date/time
        const newMessage = Date.now().toLocaleString();
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

        // a record contains the output of the function
        // as well as events, let's get events for this transaction
        const record = await transaction.getRecord(client);
        // the events from the function call are in record.contractFunctionResult.logs.data
        // let's parse the logs using ethers.js
        // there may be several log entries
        console.log("events")
        record.contractFunctionResult.logs.forEach(log => {
            // convert the log.data (uint8Array) to a string
            let logStringHex = "0x".concat(Buffer.from(log.data).toString('hex'));

            // get topics from log
            let logTopics = [];
            log.topics.forEach(topic => {
                logTopics.push("0x".concat(Buffer.from(topic).toString("hex")));
            });

            // decode the event data
            let logRequest = {};
            logRequest.data = logStringHex;
            logRequest.topics = logTopics;
            let event = abiInterface.parseLog(logRequest);
            // output the from address stored in the event
            console.log("from " + AccountId.fromSolidityAddress(event.args.from).toString());
            // output the number of message updates
            console.log("updated " + event.args.messageUpdates + " times");
        });
    }
}

void main();
