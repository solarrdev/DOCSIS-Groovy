/*************
 * Defaultss *
 *************/

def TLV = option.&createOptionValue
def TLV11 = { name, value -> return TLV("11", [name, "TYPE", value]) }
def DHCP = discoveredData.&get
def getProperty = deviceProperties.&getProperty

// Network Access Control
def networkAccess = TLV("3", "1")
configFile.add(networkAccess)

// Max CPE
def maxCPE = TLV("18", getProperty("maxcpe", "5"))
configFile.add(maxCPE)

/************
 * Includes *
 ************/

// SNMP Access
new default_snmp(binding).run()

// Define CVC_Groovy
new cvc_master(binding).run()

/*****************
 * Service Flows *
 *****************/

// Upstream Service Flow
def upServiceFlow = TLV("24")
upServiceFlow.add(TLV("24.1", "1"))
upServiceFlow.add(TLV("24.6", "7"))
upServiceFlow.add(TLV("24.7", "0"))
upServiceFlow.add(TLV("24.8", getProperty("upstream", "512000")))
upServiceFlow.add(TLV("24.9", getProperty("upstreamburst", "98304")))
upServiceFlow.add(TLV("24.10", "0"))
upServiceFlow.add(TLV("24.13", "0"))
upServiceFlow.add(TLV("24.14", getProperty("upconcatburst", "16384")))
upServiceFlow.add(TLV("24.15", "Best Effort"))
upServiceFlow.add(TLV("24.23", "0","0"))
configFile.add(upServiceFlow)

// Downstream Service Flow
def downServiceFlow = TLV("25")
downServiceFlow.add(TLV("25.1", "2"))
downServiceFlow.add(TLV("25.6", "7"))
downServiceFlow.add(TLV("25.8", getProperty("downstream", "2048000")))
downServiceFlow.add(TLV("25.9", getProperty("burstsize", "256000")))
downServiceFlow.add(TLV("25.13", "0"))
configFile.add(downServiceFlow)

/*******************
 * Upgrade Options *
 *******************/

def modelName = DHCP("vendor-encapsulated-options", 0, 9)
def vendorName = DHCP("vendor-encapsulated-options", 0, 10)
def softwareUpgradeFilename = null
def softwareTFTPServer = getProperty("tftpServer", "10.10.10.10")
def Certificate = null

/*********************
 * Switch Statements *
 *********************/

switch (vendorName)
{

  ///////////
 // ARRIS //
///////////

  case ~/\bA[Rr]\S.*/:
  softwareUpgradeFilename = getProperty("filename", "generic.img")
  Certificate = getProperty("CVC_Groovy", ArrisCVC)
  configFile.add(TLV11("arrisMtaDevProvMethodIndicator.0", "0"))

   ///////////////////////////
  // TG/DG34xx & SB/CM8200 //
 ///////////////////////////

  switch (modelName)
  {
    case ~/[TDCS][MBG](34|82)\d\d.*/:
    Certificate = getProperty("CVC_Groovy", ArrisCVC_New)

    switch (modelName)
    {
      case "TG3452A":   
      softwareUpgradeFilename = getProperty("filename", "AR01.02.056.04_102519_711.SIP.10.7.NA.simg")
      configFile.add(TLV11("docsDevSwAdminStatus.0", "2"))
      break
      case "DG3450A":
      softwareUpgradeFilename = getProperty("filename", "AR01.02.056.04_102519_711.NCS.10.7.NA.simg")
      configFile.add(TLV11("docsDevSwAdminStatus.0", "2"))
      break
      default:
      configFile.add(TLV11("docsDevSwAdminStatus.0", "3"))
    }
  }

   ////////////////////////////
  // CM/TG/DG16xx/24xx/32xx //
 ////////////////////////////

  switch (modelName)
  {
    case ~/[TDC][MG]([12]|32).*/:
    Certificate = getProperty("CVC_Groovy", ArrisCVC_New)

    switch (modelName)
    {
      case "DG1660A":
      Certificate = getProperty("CVC_Groovy", ArrisCVC)
      softwareUpgradeFilename = getProperty("filename", "TS0901103ASP1_032817_NA.16XX.GW.ATOM.img")
      configFile.add(TLV11("docsDevSwAdminStatus.0", "2"))
      break
      case "DG2470A":
      softwareUpgradeFilename = getProperty("filename", "TS0901103ASP1_032817_NA.24XX.GW.ATOM.img")
      configFile.add(TLV11("docsDevSwAdminStatus.0", "2"))
      break
      case "DG3260A":
      softwareUpgradeFilename = getProperty("filename", "TS0901103FB_032520_NA.24XX.GW.ATOM.img")
      configFile.add(TLV11("docsDevSwAdminStatus.0", "2"))
      break
      default:
      configFile.add(TLV11("docsDevSwAdminStatus.0", "3"))
    }
  }

  ////////////
 // HITRON //
////////////

  case ~/Hitron.*/:
  softwareUpgradeFilename = getProperty("filename", "generic.img")
  Certificate = getProperty("CVC_Groovy", HitronCVC)

   //////////
  // 458x //
 //////////

  switch (modelName)
  {
    case ~/.*458.*/:
    softwareUpgradeFilename = getProperty("filename", "CODA458X-BIOS-7.2.4.1.1b10-202103260925-D30.sbn")
    configFile.add(TLV11("docsDevSwServerTransportProtocol.0", "2"))

    def hitronRG_Option = TLV("43")
    hitronRG_Option.add(TLV("43.8", "00-05-CA"))
    hitronRG_Option.add(TLV(OptionSyntax.HEX,"43.242", "4348254E54454C"))
    hitronRG_Option.add(TLV(OptionSyntax.HEX,"43.243", "586975726F6E2C486974726F6E4D79496E7374616C6C"))
    configFile.add(hitronRG_Option)

    def eRouter = option.createOptionValue("202")

    def TR69ManagementServer = option.createOptionValue("202.2")
    TR69ManagementServer.add(option.createOptionValue("202.2.1", "1"))
    TR69ManagementServer.add(option.createOptionValue("202.2.2", "https://test.acs.net/"))
    TR69ManagementServer.add(option.createOptionValue("202.2.3", "Test"))
    TR69ManagementServer.add(option.createOptionValue("202.2.4", "password1"))
    eRouter.add(TR69ManagementServer)
    configFile.add(eRouter)

  }
  break

  /////////////
 // DEFAULT //
/////////////

default:
softwareUpgradeFilename = getProperty("filename", "generic.img")
Certificate = getProperty("CVC_Groovy", ArrisCVC)
configFile.add(TLV11("docsDevSwAdminStatus.0", "3"))
}

// Add TLVs
configFile.add(TLV("9", softwareUpgradeFilename))
configFile.add(TLV("21", softwareTFTPServer))
configFile.add(TLV("32", Certificate))