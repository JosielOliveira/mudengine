package com.jpinfo.mudengine.being.fixture;

import java.util.stream.Collectors;

import com.jpinfo.mudengine.being.model.MudBeing;
import com.jpinfo.mudengine.being.model.converter.MudBeingAttrConverter;
import com.jpinfo.mudengine.being.model.converter.MudBeingSkillConverter;

import br.com.six2six.fixturefactory.processor.Processor;

public class MudBeingProcessor implements Processor {

	@Override
	public void execute(Object result) {
		
		if (result instanceof MudBeing) {
			
			MudBeing dbBeing = (MudBeing)result;

			// Adjusting being attributes to resemble being class attributes
			dbBeing.setAttrs(
				dbBeing.getBeingClass().getAttrs().stream()
					.map(e -> MudBeingAttrConverter.convert(dbBeing.getCode(), e))
					.collect(Collectors.toSet())
					);
			
			// Adjusting being skills to resemble being class skills
			dbBeing.setSkills(
				dbBeing.getBeingClass().getSkills().stream()
					.map(e -> MudBeingSkillConverter.convert(dbBeing.getCode(), e))
					.collect(Collectors.toSet())
					);
		}

	}

}
