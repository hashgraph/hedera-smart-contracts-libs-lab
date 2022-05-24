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
    AccountId
} from "@hashgraph/sdk";

import * as abiDecoder from "abi-decoder";
import axios from "axios";

let contractId = "";
let mirrorUrl = "";

function errorSignature(error_message) {
    const error = {
        data: "",
        signature: ""
    };

    const signature = error_message.substr(0, 8).replace('0x', '');
    error.signature = signature;
    error.data = error_message;
    return error;
}

async function getErrorFromMirror(silent) {
    const error = {
        data: "",
        signature: ""
    };

    // get the results from mirror
    const url = `https://${mirrorUrl}.mirrornode.hedera.com/api/v1/contracts/${contractId}/results?order=desc&limit=1`;

    const response = await axios(url);
    const jsonResponse = response.data;

    if (jsonResponse.results[0].error_message) {
        let error_message = jsonResponse.results[0].error_message;
        if (error_message) {
            return errorSignature(error_message);
        } else {
            if (!silent) {
                console.error("xno error message found");
            }
            return error;
        }
    } else {
        if (!silent) {
            console.error("no error message found");
        }
        return error;
    }
}

async function getAbi(signature, silent) {
    const url = `https://www.4byte.directory/api/v1/signatures/?hex_signature=${signature}`;

    const response = await axios(url);
    const jsonResponse = response.data;

    if (jsonResponse.count == 1) {
        return jsonResponse.results[0].text_signature;
    } else if (jsonResponse.count == 0) {
        if (!silent) {
            console.error("response from www.4byte.directory contained no data");
        }
    } else {
        if (!silent) {
            console.error("response from www.4byte.directory resulted in too many results");
        }
    }
    return "";
}

async function processError(error, silent, indent) {
    if (error.signature) {
        // get the abi for the signature
        const errorFunction = await getAbi(error.signature);
        if (errorFunction != "") {
            const abi = []
            const abiFragment = {
                outputs: [],
                name: "",
                inputs: [],
                stateMutability: "view",
                type: "function"
            };
            abiFragment.name = "";

            // name and parameters are plain text such as BootstrapCallFailedError(address,bytes)
            // need to convert to an actual ABI
            const nameAndParameters = errorFunction.split("(");
            abiFragment.name = nameAndParameters[0]; // the function's name
            const parameterList = nameAndParameters[1].replace(')','');
            // now split the parameters into an array
            const parameters = parameterList.split(',');
            parameters.forEach(parameter => {
                const input = {
                    name: "",
                    internalType: "",
                    type: ""
                };
                input.internalType = parameter;
                input.type = parameter;
                abiFragment.inputs.push(input);
            });

            abi.push(abiFragment);
            // Setup a abiDecoder with the ABI for the error
            abiDecoder.addABI(abi);
            const decodedError = abiDecoder.decodeMethod(error.data);

            if (decodedError) {
                console.log(`${".".repeat(indent)}Error is ${decodedError.name}`);
                decodedError.params.forEach(parameter => {
                    console.log(`${".".repeat(indent)}Parameter (${parameter.type}) = ${parameter.value}`);

                    if (parameter.type == 'address') {
                        console.log(`${".".repeat(indent)}=> Hedera address ${AccountId.fromSolidityAddress(parameter.value)}`);
                    }
                    console.log("");

                    if ((parameter.type == 'bytes') && (parameter.value != null)) {
                        const innerError = errorSignature(parameter.value, true);
                        processError(innerError, true, indent + 2)
                    }
                });
            }
        }
    } else if (!silent) {
        console.error("no error signature found");
    }
}

async function main() {

    console.log("");
    // get the command line parameters
    const args = process.argv.slice(2);
    if (args.length == 2) {
        mirrorUrl = args[0];
        contractId = args[1];

        // get the signature and data for the error
        const error = await getErrorFromMirror(false);
        await processError(error, false, 0);
    } else {
        console.error("invalid command line arguments supplied, please consult README.md");
    }
}

void main();
