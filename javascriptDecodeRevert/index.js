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
    AccountId, ContractId, TransactionId
} from "@hashgraph/sdk";

import * as abiDecoder from "abi-decoder";
import axios from "axios";

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

async function processByTransactionId(transactionId) {
    console.log(`querying for transaction id ${transactionId}`);

    let nonce = 0;
    while (nonce >= 0) {
        const transactionResult = await getContractResultsByTransactionId(transactionId, nonce);
        if (transactionResult.status === "no data found") {
            // no data found
            nonce = -1;
        } else {
            if (nonce === 0) {
                // first transaction
                console.log(`Client api call result`);
            } else {
                console.log(`\nNested contract call`);
            }
            if (transactionResult.result === 'CONTRACT_REVERT_EXECUTED') {
                // attempt to decode the error
                const error = await getErrorFromMirror(transactionResult.contractId, transactionResult.blockHash, false);
                if (error.data != "0x") {
                    await processError(error, false, 0);
                } else {
                    console.dir(transactionResult);
                }
            } else {
                console.dir(transactionResult);
            }
            nonce += 1;
        }
    }
}

async function getContractResultsByTransactionId(transactionId, nonce) {
    const result = {
        callResult : "",
        error_message: "",
        from: "",
        to: "",
        blockHash: "",
        result: "",
        status: "",
        contractId: ""
    }
    const url = `https://${mirrorUrl}.mirrornode.hedera.com/api/v1/contracts/results/${transactionId}?nonce=${nonce}`;

    const response = await axios(url, { validateStatus: false });
    const jsonResponse = response.data;

    if (response.status === 404) {
        result.status = "no data found";
    } else {
        result.callResult = jsonResponse.call_result;
        result.error_message = jsonResponse.error_message;
        result.from = `${AccountId.fromSolidityAddress(jsonResponse.from)} (${jsonResponse.from})`;
        result.to = `${AccountId.fromSolidityAddress(jsonResponse.to)} (${jsonResponse.to})`;
        result.blockHash = jsonResponse.block_hash;
        result.result = jsonResponse.result;
        result.contractId = jsonResponse.contract_id;
    }
    return result;
}

async function getErrorFromMirror(contractId, blockHash, silent) {
    const error = {
        data: "",
        signature: ""
    };

    // get the results from mirror
    let url = `https://${mirrorUrl}.mirrornode.hedera.com/api/v1/contracts/${contractId}/results`;

    if (blockHash) {
        url = url.concat(`?order=asc&block.hash=${blockHash}&internal=true`);
    } else {
        url = url.concat("?order=desc");
    }

    const response = await axios(url);
    const jsonResponse = response.data;

    if (jsonResponse.results[0].error_message) {
        let error_message = jsonResponse.results[0].error_message;
        if (error_message) {
            return errorSignature(error_message);
        } else {
            if (!silent) {
                console.error("no error message found");
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
        console.log(`no error signature found for ${error.data}`);
    }
}

async function main() {

    console.log("");
    // get the command line parameters
    const args = process.argv.slice(2);
    if (args.length == 2) {
        mirrorUrl = args[0];
        const userInput = args[1];
        let contractId;
        let transactionId;

        try {
            transactionId = TransactionId.fromString(userInput).toString();
        } catch (e) {
            // input was not a transaction, continue
        }

        try {
            const testContractId = ContractId.fromString(userInput).toString();
            if (testContractId.length === userInput.length) {
                // a transactionId will parse to a contractId !
                contractId = testContractId;
            }
        } catch (e) {
            // input was not a contract, continue
        }

        if (transactionId) {
            // convert transaction id format to one understood by the API
            transactionId = transactionId.replaceAll(".","-");
            transactionId = transactionId.replace("0-0-", "0.0.");
            transactionId = transactionId.replace("@","-");

            await processByTransactionId(transactionId);

        } else if (contractId) {
            // get the signature and data for the error
            const error = await getErrorFromMirror(contractId.toString(),"", false);
            await processError(error, false, 0);
        } else {
            console.error(`${userInput} didn't resolve to a valid contractId (0.0.xxxxx) or transactionId (0.0.xxxx@yyyyy.zzzzz)`);
        }
    } else {
        console.error("invalid command line arguments supplied, please consult README.md");
    }
}

void main();
