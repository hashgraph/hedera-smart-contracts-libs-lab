# Hedera Javascript SDK with Abi-decoder

Example use of the Hedera JavaScript SDK along with abi-decoder.

Note, the abi-decoder library only decodes, therefore the SDK's built in methods for constructing function calls will be used here.

Also, the abi-decode can only decode logs/events from contract execution and the function call made to contracts, not results from the execution.

## Run the example

The example runs through the following steps
* Deploying the contract to Hedera with a `Hello Hedera` constructor parameter value (this emits a `SetMessage` event)
* Sets the contract message to `Hello again` (this emits a `SetMessage` event)
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
new contract ID: 0.0.29570849

Calling set_message with 'Hello again' parameter value

Getting event(s) from record
Calling set_message to trigger new event

Record events
event SetMessage
  from : 0x0000000000000000000000000000000000002b55
  message : 1/28/2022, 6:26:50 PM

Getting event(s) from mirror
Waiting 10s to allow transaction propagation to mirror

Mirror events
event SetMessage
  from : 0x0000000000000000000000000000000000002b55
  message : Hello Hedera
event SetMessage
  from : 0x0000000000000000000000000000000000002b55
  message : Hello again
event SetMessage
  from : 0x0000000000000000000000000000000000002b55
  message : 1/28/2022, 6:26:50 PM
```
