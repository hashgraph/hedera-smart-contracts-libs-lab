pragma solidity ^0.6.12;

contract StatefulContract {
    // sample event
    event SetMessage(address indexed from, uint8 indexed messageUpdates);

    // the message we're storing
    string message;
    // counting message updates;
    uint8 messageUpdates;

    constructor(string memory message_) public {
        message = message_;
        messageUpdates = 0;
    }

    function set_message(string memory message_) public {
        messageUpdates = messageUpdates + 1;
        emit SetMessage(msg.sender, messageUpdates);
        message = message_;
    }

    // return a string
    function get_message() public view returns (string memory) {
        return message;
    }
}
