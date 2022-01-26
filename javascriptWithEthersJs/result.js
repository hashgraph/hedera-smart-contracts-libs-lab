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
        // calling "get_message" which takes no parameters
        let functionCallAsHexString = abiInterface.encodeFunctionData("get_message", []);
        // remove the leading "0x"
        functionCallAsHexString = functionCallAsHexString.replace("0x","");
        // convert to a Uint8Array
        const functionCallAsUint8Array = Buffer.from(functionCallAsHexString, "hex");

        // using a query first (node responds immediately, no consensus latency)
        const contractCall = await new ContractCallQuery()
            .setContractId(contractId)
            .setFunctionParameters(functionCallAsUint8Array)
            .setQueryPayment(new Hbar(2))
            .setGas(100000)
            .execute(client);

        let results = abiInterface.decodeFunctionResult("get_message", contractCall.bytes);
        console.log("Result from query")
        console.log(results);

        // doing the same with a transaction and a record
        const transaction = await new ContractExecuteTransaction()
            .setContractId(contractId)
            .setFunctionParameters(functionCallAsUint8Array)
            .setGas(100000)
            .execute(client);

        // a record contains the output of the function
        const record = await transaction.getRecord(client);
        // the result of the function call is in record.contractFunctionResult.bytes
        // let's parse it using ethers.js
        results = abiInterface.decodeFunctionResult("get_message", record.contractFunctionResult.bytes);
        console.log("Result from transaction + record")
        console.log(results);
    }
}

void main();
