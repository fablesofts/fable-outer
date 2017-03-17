package com.fable.outer.rmi.event.handler.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.fable.dsp.common.dto.network.NetCardDto;
import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.hamal.shuttle.communication.event.EventHandler;
import com.fable.hamal.shuttle.communication.event.EventRegisterCenter;
import com.fable.hamal.shuttle.communication.event.EventType;
import com.fable.outer.rmi.type.CommonEventTypes;
/**
 * 
 * @author majy
 *
 */
@Service
public class NetworkCardEventHandler implements EventHandler {

	private static final Logger logger = org.slf4j.LoggerFactory
			.getLogger(NetworkCardEventHandler.class);

	@PostConstruct
	public void init() {
		EventRegisterCenter.regist(CommonEventTypes.LISTNETCARD, this);
		EventRegisterCenter.regist(CommonEventTypes.EFFICTNETCARD, this);
	}

	public Object handle(Event event) {
		EventType type = event.getType();
		Object handle = null;
		if (CommonEventTypes.LISTNETCARD.equals(type)) {
			try {
				handle = this.getNetCard();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		if (CommonEventTypes.EFFICTNETCARD.equals(type)) {
			try {
				handle = this.effictNetCard((Serializable) event.getData());
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return handle;
	}

	/**
	 * @author majy
	 * @throws Exception 
	 */
	public Boolean effictNetCard(Serializable param) throws Exception {
		boolean rt = false;
		String cmd = null;
		NetCardDto cardDto=(NetCardDto) param;
		String sysName = System.getProperty("os.name").toLowerCase();
		if (sysName.contains("windows")) {
			String name = cardDto.getName().replace("-", " ");
			cmd = "netsh interface ip set address \"" + name + "\" stat "
					+ cardDto.getIp() + " " + cardDto.getMask() + " "
					+ cardDto.getGateway() + " 1";

			Runtime.getRuntime().exec(cmd);

		} else if (sysName.contains("linux")) {
			File file = new File("/etc/sysconfig/network-scripts/ifcfg-"
					+ cardDto.getName());
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String temp = null;
			StringBuilder buffer = new StringBuilder();
			while ((temp = reader.readLine()) != null) {
				if (temp.contains("BOOTPROTO=")) {
					temp = "BOOTPROTO=\"static\"";
				}
				buffer.append(temp);
			}
			reader.close();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(buffer.toString());
			writer.flush();
			writer.close();
			cmd = ("ifconfig ") + cardDto.getName() + " " + cardDto.getIp()
					+ " netmask " + cardDto.getMask();
			Runtime.getRuntime().exec(cmd);
			rt=true;
		} else {
			throw new Exception("操作系统不匹配");
		}
		return rt;
	}

	/**
	 * @author majy
	 * @return
	 */
	public List<NetCardDto> getNetCard() throws IOException {
		List<NetCardDto> stack = new LinkedList<NetCardDto>();
		String sysName = System.getProperty("os.name").toLowerCase();
		if (sysName.contains("windows")) {
			Process process = Runtime.getRuntime().exec("ipconfig");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream(), Charset.forName("GBK")));
			String temp = null;
			Queue<String> queue = new LinkedList<String>();
			while ((temp = reader.readLine()) != null) {
				queue.add(temp);
				if (queue.size() > 5) {
					queue.poll();
				}
				if (temp.contains("IPv4")) {
					NetCardDto card = new NetCardDto();
					card.setIp(temp.split(":")[1].trim());
					card.setMask(reader.readLine().split(":")[1].trim());
					card.setGateway(reader.readLine().split(":")[1].trim());
					String[] nameStrs = queue.poll().split(":")[0].trim()
							.split(" ");
					card.setName(nameStrs[1] + "-" + nameStrs[2]);
					stack.add(card);
				}
			}
			queue.clear();
			reader.close();
		} else if (sysName.contains("linux")) {
			Process process = Runtime.getRuntime().exec("ifconfig");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				NetCardDto dto = new NetCardDto();
				if (temp.contains("mtu")||temp.contains("link")) {
					dto.setName(temp.split(":")[0].trim());
					if (!dto.getName().toLowerCase().equals("lo")) {
						temp = reader.readLine();
						if (temp.contains("inet") && !temp.contains("inet6")) {
							temp = temp.replace(" ", "").toLowerCase()
									.replace("inet", "");
							if (!temp.contains("netmask")) {
								dto.setIp(temp);
							} else {
								String[] subTemps = temp.split("netmask");
								dto.setIp(subTemps[0]);
								dto.setMask(subTemps[1].split("b")[0]);
							}
							stack.add(dto);
						}
					}
				}
			}
		}
		return stack;
	}

}
