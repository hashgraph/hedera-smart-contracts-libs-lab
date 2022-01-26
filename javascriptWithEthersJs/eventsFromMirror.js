import {
    ContractId,
    AccountId,
} from "@hashgraph/sdk";

import { ethers } from "ethers";
import * as fs from "fs";
import axios from "axios";

async function main() {
    // Import the ABI
    const abi = JSON.parse(fs.readFileSync('./abi.json', 'utf8'));
    // create an ethers.js interface from the ABI
    const abiInterface = new ethers.utils.Interface(abi);

    // get command line parameters from node
    const myArgs = process.argv.slice(2);

    // if (myArgs.length === 0) {
    //     console.error("No contract Id specified on command line. Exiting.")
    // } else {
    //     const contractId = ContractId.fromString(myArgs[0]);
    const contractId = ContractId.fromString("0.0.29549857");
        console.log('Using contract Id: ', contractId.toString());

        const url = 'https://testnet.mirrornode.hedera.com/api/v1/contracts/' + contractId.toString() + '/results/logs?order=asc';

        axios.get(url)
            .then(function (response) {
                const jsonResponse = response.data;
                console.dir(jsonResponse);

                jsonResponse.logs.forEach(log => {
                    // get topics from log

                    // decode the event data
                    let logRequest = {};
                    logRequest.data = log.data;
                    logRequest.topics = log.topics;
                    let event = abiInterface.parseLog(logRequest);
                    // output the from address stored in the event
                    console.log("from " + AccountId.fromSolidityAddress(event.args.from).toString());
                    // output the number of message updates
                    console.log("updated " + event.args.messageUpdates + " times");
                });
            })
            .catch(function (err) {
                console.error(err);
            });
    // }
}

void main();
