pragma solidity ^0.6.12;

contract StatefulContract {
    // sample event
    event SetMessage(address indexed from, string message);

    // the message we're storing
    string message;

    constructor(string memory message_) public {
        emit SetMessage(msg.sender, message_);
        message = message_;
    }

    function set_message(string memory message_) public {
        emit SetMessage(msg.sender, message_);
        message = message_;
    }

    // return a string
    function get_message() public view returns (string memory messageOut) {
        messageOut = message;
    }

    function VerifyMessage(bytes32 _hashedMessage, uint8 _v, bytes32 _r, bytes32 _s) public pure returns (address) {

        bytes memory prefix = "\x19Ethereum Signed Message:\n32";
        bytes32 prefixedHashMessage = keccak256(
            abi.encodePacked(prefix, _hashedMessage)
        );
        address signer = ecrecover(prefixedHashMessage, _v, _r, _s);
        return signer;
    }
}
