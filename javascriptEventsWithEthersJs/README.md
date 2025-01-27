# Hedera Contract Events with Ethers.js

Example use of the ethers.js library to subscribe to Hedera smart contract events through hashio.

## Run the example

The example runs through the following steps
* Deploying the contract to Hedera with a `Hello Hedera` constructor parameter value (this emits a `SetMessage` event)
* Subscribes to the `SetMessage` event for the contract we just created
* Sets the contract message to `Hello 1`, then `Hello 2` and finally `Hello 3` (this emits `SetMessage` events)
* The subscription outputs the events

```shell
yarn install

node index.js
```

will output

```shell
Deploying the contract
new contract ID: 0.0.4061144

Calling set_message with 'Hello 1' parameter value

Calling set_message with 'Hello 2' parameter value

Calling set_message with 'Hello 3' parameter value
Got event -> 0x00000000000000000000000000000000000004a5, Hello Hedera
Got event -> 0x00000000000000000000000000000000000004a5, Hello 1
Got event -> 0x00000000000000000000000000000000000004a5, Hello 2
```
