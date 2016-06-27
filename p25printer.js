module.exports = {

    connect: function (macAddress, success, failure) {
        cordova.exec(success, failure, "P25Print", "connect", [macAddress]);
    },
    
    print: function (macAddress,printText, success, failure) {
        cordova.exec(success, failure, "P25Print", "print", [macAddress,printText]);
    }
}
