package com.jpinfo.mudengine.being.model.converter;

import com.jpinfo.mudengine.being.model.MudBeing;

import com.jpinfo.mudengine.being.model.MudBeingAttr;
import com.jpinfo.mudengine.being.model.MudBeingClass;
import com.jpinfo.mudengine.being.model.MudBeingClassAttr;
import com.jpinfo.mudengine.being.model.pk.MudBeingAttrPK;
import com.jpinfo.mudengine.common.being.Being;

public class MudBeingAttrConverter {

	private MudBeingAttrConverter() { }
	
	public static MudBeingAttr build(Long beingCode, String attrCode, Integer attrValue) {
		
		MudBeingAttr dbAttr = new MudBeingAttr();
		MudBeingAttrPK dbAttrPK = new MudBeingAttrPK();
		
		dbAttrPK.setCode(attrCode);
		dbAttrPK.setBeingCode(beingCode);
		
		dbAttr.setId(dbAttrPK);
		dbAttr.setValue(attrValue);
		
		return dbAttr;
	}
	
	public static MudBeingAttr convert(Long beingCode, MudBeingClassAttr classAttr) {
		
		MudBeingAttr dbAttr = new MudBeingAttr();
		MudBeingAttrPK dbAttrPK = new MudBeingAttrPK();
		
		dbAttrPK.setCode(classAttr.getId().getCode());
		dbAttrPK.setBeingCode(beingCode);
		
		dbAttr.setId(dbAttrPK);
		dbAttr.setValue(classAttr.getValue());
		
		return dbAttr;
	}
	
	
	
	public static MudBeing sync(MudBeing dbBeing, MudBeingClass previousClass, MudBeingClass nextClass) {

		if (previousClass!=null) {
		
			// Looking for attributes to remove
			dbBeing.getAttrs().removeIf(d -> {
				
				boolean existsInOldClass = previousClass.getAttrs().stream()
						.anyMatch(e -> d.getId().getCode().equals(e.getId().getCode()));
				
				boolean existsInNewClass = nextClass.getAttrs().stream()
						.anyMatch(e -> d.getId().getCode().equals(e.getId().getCode()));
				
				return existsInOldClass && ! existsInNewClass;
			});
		}
		
		// Looking for attributes to add/update
		nextClass.getAttrs().stream()
			.forEach(d -> {

				MudBeingAttr attr = 
					dbBeing.getAttrs().stream()
						.filter(e -> e.getId().getCode().equals(d.getId().getCode()))
						.findFirst()
						.orElse(MudBeingAttrConverter.convert(dbBeing.getCode(), d));

				// Update the attribute value
				attr.setValue(d.getValue());
				
				dbBeing.getAttrs().add(attr);
				
			});
		
		return dbBeing;
	}
	
	public static MudBeing sync(MudBeing dbBeing, Being requestBeing) {

		// Looking for attributes to remove
		dbBeing.getAttrs().removeIf(d -> 
			!requestBeing.getAttrs().containsKey(d.getId().getCode())
		);
		
		// Looking for attributes to add/update
		requestBeing.getAttrs().keySet().stream()
			.forEach(requestAttrCode -> {

				Integer requestAttrValue = requestBeing.getAttrs().get(requestAttrCode);
				
				MudBeingAttr attr = 
				dbBeing.getAttrs().stream()
					.filter(d -> d.getId().getCode().equals(requestAttrCode))
					.findFirst()
					.orElse(MudBeingAttrConverter.build(dbBeing.getCode(), requestAttrCode, requestAttrValue));
				
				
				// Update the attribute value
				attr.setValue(requestAttrValue);
				
				dbBeing.getAttrs().add(attr);
				
			});
		
		return dbBeing;
	}

}
