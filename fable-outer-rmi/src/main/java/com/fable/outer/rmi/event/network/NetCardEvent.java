package com.fable.outer.rmi.event.network;

import java.io.Serializable;

import com.fable.hamal.shuttle.communication.event.Event;
import com.fable.outer.rmi.type.CommonEventTypes;

/**
 * 
 * @author majy
 * 
 */
public class NetCardEvent {
	public static class Effiect extends Event {
		private static final long serialVersionUID = 1L;

		public Effiect(Serializable outCard) {
			super(CommonEventTypes.EFFICTNETCARD);
			setData(outCard);
		}
	}

	public static class ListAll extends Event {
		private static final long serialVersionUID = 1L;

		public ListAll() {
			super(CommonEventTypes.LISTNETCARD);
		}
	}

}
