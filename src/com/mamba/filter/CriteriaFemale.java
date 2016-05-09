package com.mamba.filter;

import java.util.ArrayList;
import java.util.List;

public class CriteriaFemale implements Criteria {
	@Override
	public List<Person> meetCriteria(List<Person> persons) {
		List<Person> famalePersons = new ArrayList<Person>();
		for (Person person : persons)
			if (person.getGender().equalsIgnoreCase("FEMALE"))
				famalePersons.add(person);
		return famalePersons;

	}
}
