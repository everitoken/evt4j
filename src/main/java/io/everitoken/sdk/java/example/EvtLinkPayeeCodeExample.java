package io.everitoken.sdk.java.example;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.EvtLink;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;

public class EvtLinkPayeeCodeExample {
    public static void main(String[] args) {
        NetParams netParams = new TestNetNetParams();
        EvtLink evtlink = new EvtLink(netParams);
        Address address = Address.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND");
        EvtLink.EveriLinkPayeeCodeParam payeeCodeParam = new EvtLink.EveriLinkPayeeCodeParam(address);

        String res = evtlink.getEvtLinkForPayeeCode(payeeCodeParam);
        System.out.println(res);
    }
}
