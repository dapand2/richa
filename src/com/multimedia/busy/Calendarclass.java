package com.multimedia.busy;

public class Calendarclass {
	//Class which is used to create a constructor of values that are the calendar id and the calendar name.
	public String name;
	public String id;

	public Calendarclass(String m_name, String m_id) {
		name = m_name;
		id = m_id;
	}

	@Override
	public String toString() {
		return name;
	}
}
