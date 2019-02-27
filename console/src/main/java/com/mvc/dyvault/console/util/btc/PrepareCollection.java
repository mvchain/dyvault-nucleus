package com.mvc.dyvault.console.util.btc;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;

import java.io.IOException;
import java.math.BigDecimal;

public class PrepareCollection extends BtcAction {

    private static String tetherAddress;
    private static String toAddress;

    public static void main(String[] args) throws BitcoindException, IOException, CommunicationException {
        parseArgs(args);
        prepareCollection(tetherAddress, toAddress, BigDecimal.valueOf(0.0001), null);
    }

    private static void parseArgs(String[] args) {
        tetherAddress = args[0];
        toAddress = args[1];
    }
}
