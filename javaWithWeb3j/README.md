# Hedera Java SDK with Web3.j

com.hedera.examples.Example use of the Hedera Java SDK along with the Web3.j library.

## Running the examples

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
./gradlew build
./gradlew runExample
```

will output

```shell
Deploying contract
contract bytecode file: 0.0.29570774
new contract ID: 0.0.29570775

get_message Query
Hello Hedera

Calling set_message with 'Hello again parameter value

get_message Query
Hello again

get_message transaction
Hello again

Getting event(s) from record
Calling set_message to trigger new event

get_message Query
Fri Jan 28 17:21:53 CET 2022

Events' data
0x0000000000000000000000000000000000002b55
Fri Jan 28 17:21:53 CET 2022


Getting event(s) from mirror
Waiting 10s to allow transaction propagation to mirror
0x0000000000000000000000000000000000002b55
Hello Hedera

0x0000000000000000000000000000000000002b55
Hello again

0x0000000000000000000000000000000000002b55
Fri Jan 28 17:21:53 CET 2022
```
