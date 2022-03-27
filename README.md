# DOCSIS-Groovy

This repository features segements of Groovy code developed by me for DOCSIS configurations. The full code is not posted because it contains vendor information and other items that should not be public. This should be viewed for demonstration purposes only.

Make-model provisioning is an enhanced feature in the DOCSIS provisioning world and those capabilities are implemented in the code shown here. The files contain embedded logic that allow the provisioning service to make decisions about which options to send to devices on the network. A device's make and model are referenced in the packets sent to the environment during discovery. The service then creates a configuration file dynamically for that specific device based on the information that was supplied.

This is achieved through Groovy's switch statements. The ***isCase()*** object is used within the switch statements to locate matches. The classifiers shown here rely heavily on regular expressions to cover the broad possibilities of vendor/make and model names. The naming conventions used by vendors vary so it was necessary to use regex most of the time.


The values from the discover packet option 43, sub-option 9 and 10 (model and make respectively) are populated through the following definitions:

```
def modelName = DHCP("vendor-encapsulated-options", 0, 9)
def vendorName = DHCP("vendor-encapsulated-options", 0, 10)
```

The first switch statement loop is opened in this manner:

```
switch (vendorName)
{
  case ~/\bA[Rr]\S.*/:
```

The device's vendorName is matched against the regex in the ***isCase()*** object. If there is a match any subsequent options are added until that switch statement is exited with the final curly bracket.

Nested switch statement are used here because some options are applied to all devices for a particular vendor while other options are only used for a smaller subset of models by that vendor (e.g. firmware). This switch statement calls the definition that includes the device's model name. If more matching is performed for that defintition then using a ***break*** statement will allow one to exit that part of the loop and perform a subsequent match. That is, more than one ***modelName*** can be called in the same switch statement.

```
switch (vendorName)
{
  case ~/\bA[Rr]\S.*/:
  configFile.add(TLV11("docsDevSwServerTransportProtocol.0", "2"))
  
  switch (modelName)
  {
    case ~/CM8.*/:
    configFile.add(TLV11("clabWIFIRadioEnable.1", "0"))
    break
    case ~/CM9.*/:
    configFile.add(TLV11("clabWIFIRadioEnable.1", "1"))
  }
}
```

Since the parent switch statement does not have a ***break*** statement that option will apply to all models of that vendor type. Subsquently, another switch statement will be opened to match model names and send options accordingly. Using "if-then" parlance, the code above says: if this vendor then get this option; if this vendor AND this model then get this option additionally.
