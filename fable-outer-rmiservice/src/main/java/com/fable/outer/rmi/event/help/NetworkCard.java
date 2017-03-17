package com.fable.outer.rmi.event.help;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkCard {
	
    /**
     * 自动获取所有网卡.
     * 
     * @return Map(返回网卡的集合，Key网卡名称，value数组[ip,mask,gateway])
     */
    public static Map getNetworkCard() {
        final Map<String, String[]> netMap = new HashMap<String, String[]>();
        String[] network = null;
        String address = "";
        BufferedReader br = null;
        final String os = System.getProperty("os.name");
        final List<String> listNet = new ArrayList<String>();
        if (os != null)
            if (os.startsWith(CommonConstants.OS_TYPE_WINDOWS)) {
                try {
                    final ProcessBuilder pb =
                        new ProcessBuilder("ipconfig", "/all");
                    final Process p = pb.start();
                    br =
                        new BufferedReader(new InputStreamReader(
                            p.getInputStream(), "GBK"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.indexOf("Ethernet adapter") != -1) {
                            final int index = line.indexOf("Ethernet adapter");
                            // 截取网卡名称
                            address = line.substring(16, line.length() - 1);
                            System.out.println(address.trim());
                        }
                        else if (line.indexOf("IP Address") != -1) {
                            network = new String[3];
                            network[0] =
                                line.substring(line.indexOf(":") + 1,
                                    line.length()).trim();
                        }
                        else if (line.indexOf("Subnet Mask") != -1)
                            network[1] =
                                line.substring(line.indexOf(":") + 1,
                                    line.length()).trim();
                        else if (line.indexOf("Default Gateway") != -1)
                            network[2] =
                                line.substring(line.indexOf(":") + 1,
                                    line.length()).trim();
                        if (network != null)
                            netMap.put(address, network);
                    }
                    br.close();

                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                String message;
                final StringBuffer sb = new StringBuffer("");
                try {
                    // 读取本机有多少块网卡
                    final Process ps =
                        Runtime.getRuntime().exec(
                            "sh /root/networkmodify.sh read");
                    br =
                        new BufferedReader(new InputStreamReader(
                            ps.getInputStream(), "GBK"));

                    while ((message = br.readLine()) != null) {
                        sb.append(message);
                        System.out.println("*******sb!=null**" + message +
                            "*****");

                        String[] cardmess = message.split(",");
                        String cardname = null;
                        if (cardmess.length > 0) {
                            network = new String[3];
                            cardname = cardmess[0].split(":")[0];
                            for (int i = 0; i < cardmess.length; i++) {
                                if (i == 0)
                                    continue;
                                if ("addr".equals(cardmess[i].split(":")[0]) &&
                                    cardmess[i].split(":").length > 1)
                                    network[0] = cardmess[i].split(":")[1];
                                else if ("Mask"
                                    .equals(cardmess[i].split(":")[0]) &&
                                    cardmess[i].split(":").length > 1)
                                    network[1] = cardmess[i].split(":")[1];
                                else if ("gateway".equals(cardmess[i]
                                    .split(":")[0]) &&
                                    cardmess[i].split(":").length > 1)
                                    network[2] = cardmess[i].split(":")[1];
                            }
                            netMap.put(cardname, network);
                        }
                    }
                }
                catch (final IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                finally {
                    try {
                        br.close();
                    }
                    catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        System.out.println("***********netMap count:" + netMap.size() +
            "************");
        return netMap;
    }
}
