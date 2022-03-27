/************
 * Defaults *
 ************/
 
def TLV11 = { name, value -> return TLV("11", [name, "TYPE", value]) }
def DHCP = discoveredData.&get
def getProperty = deviceProperties.&getProperty
def modelName = DHCP("vendor-encapsulated-options", 0, 9)
def vendorName = DHCP("vendor-encapsulated-options", 0, 10)

// Config File Start
configFile.add(option.createOptionValue("254",'1'))

// SNMP Access
new default_mta_snmp(binding).run()

/*********************
 * Switch Statements *
 *********************/

switch (vendorName)
{

   ///////////
  // ARRIS //
 ///////////

 case ~/\bA[Rr]\S.*/:

  switch (modelName)
  {
    case ~/T[MG][1-8].*/:

    //Credentials
    configFile.add(TLV11("sipCfgPortDisplayName.1", getProperty("SIP_Display_Line1", "ignore")))
    configFile.add(TLV11("sipCfgPortLogin.1", getProperty("SIP_Login_Line1", "ignore")))
    configFile.add(TLV11("sipCfgPortUserName.1", getProperty("SIP_UserName_Line1", "ignore")))
    configFile.add(TLV11("sipCfgPortPassword.1", getProperty("SIP_Password_Line1", "ignore")))

    //Other
    configFile.add(TLV11("arrisMtaCfgRTPDynPortStart.0", "49152"))
    configFile.add(TLV11("arrisMtaCfgRTPDynPortEnd.0", "65535"))
    configFile.add(TLV11("ifAdminStatus.9", "1"))
    configFile.add(TLV11("ifAdminStatus.10", "2"))
    configFile.add(TLV11("pktcMtaDevEnabled.0", "1"))
    configFile.add(TLV11("pktcNcsEndPntConfigMWD.9", "10"))
    configFile.add(TLV11("ppCfgMtaCallpFeatureSwitch.0", "16384"))
    configFile.add(TLV11("ppCfgMtaCountryTemplate.0", "1"))
    configFile.add(TLV11("sipCfgPacketizationRate.0", "20"))
    configFile.add(TLV11("sipCfgProvisionedCodecArray.0", "PCMU;PCMA"))
    configFile.add(TLV11("sipCfgRegistrarAdr.0", "test.net"))
    configFile.add(TLV11("sipCfgProxyAdr.0", "sip1.test.net"))
    configFile.add(TLV11("sipCfgProxyType.0", "1"))

    //Vendor Option Instance 1
    def digiMap_Option = TLV("43")
    digiMap_Option.add(TLV("43.8", "00-00-CA"))
    digiMap_Option.add(TLV(OptionSyntax.HEX,"43.69", "2939397C782E547C00"))
    configFile.add(digiMap_Option)

    //Vendor Option Instance 2
    def digiMap_Option_i2 = TLV("43")
    digiMap_Option_i2.add(TLV("43.8", "00-00-CA"))
    digiMap_Option_i2.add(TLV(OptionSyntax.HEX,"43.70", "2A36392C414C525400"))
    configFile.add(digiMap_Option_i2)

    break
  }

   ////////////
  // TG9452 //
 ////////////

  switch (modelName)
  {
    case "TG9452":

    //Credentials

    //configFile.add(TLV11("sipEndPntConfigUserId.9", "0000000000"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.2.3.1.1.9','STRING',getProperty("SIP_Login_Line1", "ignore")]));
    //configFile.add(TLV11("sipEndPntConfigUserName.9", "0000000000"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.2.3.1.2.9','STRING',getProperty("SIP_Display_Line1", "ignore")]));
    //configFile.add(TLV11("sipEndPntConfigUserAuthName.9", "0000000000"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.2.3.1.4.9','STRING',getProperty("SIP_UserName_Line1", "ignore")]));
    //configFile.add(TLV11("sipEndPntConfigUserPassword.9", "0000000000"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.2.3.1.3.9','STRING',getProperty("SIP_Password_Line1", "ignore")]));

    //Other
    configFile.add(TLV11("ifAdminStatus.9", "1"))
    configFile.add(TLV11("ifAdminStatus.10", "2"))
    configFile.add(TLV11("pktcMtaDevEnabled.0", "1"))

    //configFile.add(TLV11("sipDeviceConfigDNSSRVLookUpDisabled.0", "0"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.1.9.0','INTEGER','0']));
    //configFile.add(TLV11("sipEndPntConfigProxyId.9", "test.net"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.2.2.1.1.9','STRING','test.net']));
    //configFile.add(TLV11("sipEndPntConfigProxySigPort.9", "4061"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.2.2.1.2.9','GAUGE','4061']));
    //configFile.add(TLV11("sipEndPntConfigRegistrarId.9", "test.net"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.2.1.1.1.9','STRING','test.net']));
    //configFile.add(TLV11("sipEndPntConfigRegistrarSigPort.9", "4061"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.2.1.1.2.9','GAUGE','4061']));
    //configFile.add(TLV11("sipEndPntConfigOutboundProxyId.9", "63.209.193.22"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.2.4.1.1.9','STRING','10.10.10.12']));
    //configFile.add(TLV11("sipEndPntConfigOutboundProxySigPort.9", "4061"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.2.4.1.2.9','GAUGE','4061']));
    //configFile.add(TLV11("emtaEnableDQoSLite.0", "1"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.1.7.0','INTEGER','1']));
    //configFile.add(TLV11("sipEndPntConfigCodecType.9", "6"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.2.5.1.2.9','INTEGER','6']));
    //configFile.add(TLV11("sipEndPntConfigUserPacketPeriod.9", "20"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.2.3.1.8.9','INTEGER','20']));
    //configFile.add(TLV11("cmEmtaBase.20.0", "0"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.2.98.1.1.1.20.0','INTEGER','0']));
    //configFile.add(TLV11("emtaSignalingRtpBaseReceiveUdpPort.0", "49152"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.4.11.0','GAUGE','49152']));
    //configFile.add(TLV11("sipEndPntConfigUserDigitMap.9", "#xx|*xx.[t#]|[3469]11|0[t#]|00|[2-9]xxxxxx[t#]|1[2-9]xx[2-9]xxxxxx|[2-9]xx[2-9]xxxxxx|011[2-9]x.[t#]|9999"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.2.3.1.5.9','STRING','#xx|*xx.[t#]|[3469]11|0[t#]|00|[2-9]xxxxxx[t#]|1[2-9]xx[2-9]xxxxxx|[2-9]xx[2-9]xxxxxx|011[2-9]x.[t#]|9999']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.clidBlock", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.1','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.clidBlockOnce", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.2','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.clidUnblockOnce", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.3','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.anonymousCallReject", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.4','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.forwardingToNumber", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.5','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.forwardingPermanent", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.6','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.forwardingNoAnswer", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.7','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.forwardingBusy", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.8','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.autoRedial", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.9','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.lastCallReturn", "1"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.10','INTEGER','1']));
    //configFile.add(TLV11("sipCallFeatureEnableVSC.9.lastCallReturn", "*69"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.3.9.10','STRING','*69']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.callwaiting", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.11','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureAutoActivate.9.callwaiting", "1"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.7.9.11','INTEGER','1']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.callwaitingOnce", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.12','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureAutoActivate.9.callwaitingOnce", "1"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.7.9.12','INTEGER','1']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.callhold", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.13','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.threeway", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.14','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.callTransfer", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.15','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.pstnIntegration", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.16','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.distinctiveRing", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.17','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.completionOnBusy", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.18','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.datarecord", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.19','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.warmline", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.20','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.doNotDisturb", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.21','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.outgoingBarring", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.22','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.speedDialling", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.23','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.incomingHandling", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.24','INTEGER','2']));
    //configFile.add(TLV11("sipCallFeatureEnabled.9.networkPrivacy", "2"))
    configFile.add(option.createOptionValue(OptionSyntax.SNMP,"11",['.1.3.6.1.4.1.4413.2.2.2.1.6.8.3.1.1.2.9.25','INTEGER','2']));

    break
  }

  break

   ////////////
  // HITRON //
 ////////////

  case ~/Hitron.*/:

  //Credentials
  configFile.add(TLV11("htSipEndPntConfigUserAuthName.9", getProperty("SIP_UserName_Line1")))
  configFile.add(TLV11("htSipEndPntConfigUserId.9", getProperty("SIP_Login_Line1")))
  configFile.add(TLV11("htSipEndPntConfigUserName.9", getProperty("SIP_Display_Line1")))
  configFile.add(TLV11("htSipEndPntConfigUserPassword.9", getProperty("SIP_Passowrd_Line1")))

  //Other
  configFile.add(TLV11("htEmtaDevConfigCLIPDisplay.0", "0xe0"))
  configFile.add(TLV11("htSipCallFeatureAutoActivate.9.callWaiting", "1"))
  configFile.add(TLV11("htSipCallFeatureAutoActivate.9.doNotDisturb", "2"))
  configFile.add(TLV11("htSipCallFeatureAutoActivate.9.threeWay", "1"))
  configFile.add(TLV11("htSipCallFeatureDisableVSC.9.callWaiting", "#43"))
  configFile.add(TLV11("htSipCallFeatureDisableVSC.9.doNotDisturb", "*79"))
  configFile.add(TLV11("htSipCallFeatureDisableVSC.9.threeWay", "#91"))
  configFile.add(TLV11("htSipCallFeatureEnabled.9.callWaiting", "1"))
  configFile.add(TLV11("htSipCallFeatureEnabled.9.doNotDisturb", "1"))
  configFile.add(TLV11("htSipCallFeatureEnabled.9.threeWay", "1"))
  configFile.add(TLV11("htSipCallFeatureEnableVSC.9.callWaiting", "*43"))
  configFile.add(TLV11("htSipCallFeatureEnableVSC.9.doNotDisturb", "*78"))
  configFile.add(TLV11("htSipCallFeatureEnableVSC.9.threeWay", "#90"))
  configFile.add(TLV11("htSipCallFeatureLocal.9.callWaiting", "1"))
  configFile.add(TLV11("htSipCallFeatureLocal.9.doNotDisturb", "2"))
  configFile.add(TLV11("htSipCallFeatureLocal.9.threeWay", "1"))
  configFile.add(TLV11("htSipDeviceConfigDNSSRVLookUpDisabled.0", "1"))
  configFile.add(TLV11("htSipDeviceConfigeCountryCode.0", "27"))
  configFile.add(TLV11("htSipDeviceConfigHookFlashRelay.0", "1"))
  configFile.add(TLV11("htSipDeviceConfigLocalSignalingPort.0", "4061"))
  configFile.add(TLV11("htSipDeviceConfigRegistrationExpiry.0", "300"))
  configFile.add(TLV11("htSipDeviceConfigWarmLineTO.0", "10000"))
  configFile.add(TLV11("htSipEndPntConfigOutboundProxyId.9", "test.net"))
  configFile.add(TLV11("htSipEndPntConfigOutboundProxySigPort.9", "4061"))
  configFile.add(TLV11("htSipEndPntConfigProxyId.9", "test.net"))
  configFile.add(TLV11("htSipEndPntConfigProxySigPort.9", "4061"))
  configFile.add(TLV11("htSipEndPntConfigRegistrarId.9", "test.net"))
  configFile.add(TLV11("htSipEndPntConfigRegistrarRetryTimeout.9", "180"))
  configFile.add(TLV11("htSipEndPntConfigRegistrarSigPort.9", "4061"))
  configFile.add(TLV11("htSipEndPntConfigUserDigitMap.9", "#xx|*[1-9]X.T|[3469]11|0[t#]|00|[2-9]#|[2-9]xxxxxx[t#]|1[2-9]xx[2-9]xxxxxx|[2-9]xx[2-9]xxxxxx|011[2-9]x.[t#]|999|[2-9]x#|"))

  break
}

// Config File End
configFile.add(option.createOptionValue("254",'255'))