# Hedera Javascript SDK with Ethers.js

Example use of the Hedera JavaScript SDK along with some components of the Ethers.js library.

## Partial use of Ethers.js

While importing the complete `ethers.js` library into the project would work, the `ethers.js` library is built from modular components meaning we only need to import a single component from the library to enable us to benefit from ABI parsing.

The project therefore imports `"@ethersproject/abi": "^5.5.0"` and uses the `Interface` class as follows

```javascript
const abiInterface = new Interface(abi);
```

where `abi` is the json representation of a contract's abi.

consequently

```javascript
abiInterface.encodeFunctionData(functionName, parameterArray)
```

will create the necessary solidity call parameter from a given function name and array of parameters

and

```javascript
abiInterface.decodeFunctionResult(functionName, result);
```

will decode the result of a contract call for a given function name

## Run the example

The example runs through the following steps
* Deploying the contract to Hedera with a `Hello Hedera` constructor parameter value (this emits a `SetMessage` event)
* Queries the contract for the current message value
* Sets the contract message to `Hello again` (this emits a `SetMessage` event)
* Uses a query to fetch the contract's current message value (`get_message`)
* Uses a transaction to fetch the contract's current message value (`get_message`)
* Calls `set_message` with the current date time as a parameter (this emits a `SetMessage` event)
* Fetches the emitted event using a record
* Pauses 10s
* Fetches all the events for the contract using a mirror node query

```shell
yarn install

node index.js
```

will output

```shell
Deploying the contract
new contract ID: 0.0.29560194

get_message Query
[ 'Hello Hedera' ]

Calling set_message with 'Hello again' parameter value

get_message Query
[ 'Hello again' ]

get_message transaction
[ 'Hello again' ]

Getting event(s) from record
Calling set_message to trigger new event

get_message Query
[ '1/27/2022, 1:05:01 PM' ]
Record event: from '0.0.11093' update to '1/27/2022, 1:05:01 PM'

Getting event(s) from mirror
Waiting 10s to allow transaction propagation to mirror
Mirror event(s): from '0.0.11093' update to 'Hello Hedera'
Mirror event(s): from '0.0.11093' update to 'Hello again'
Mirror event(s): from '0.0.11093' update to '1/27/2022, 1:05:01 PM'
```
